package com.sadadib.billtracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sadadib.billtracker.data.BillData;
import com.sadadib.billtracker.data.BillRepository;
import com.sadadib.billtracker.data.BillViewModel;
import com.sadadib.billtracker.overview.OverviewActivity;
import com.sadadib.billtracker.utils.BillScrollListener;

import java.util.List;

public class BillsListFragment extends Fragment {
    private RecyclerView recyclerView;
    private BillsListAdapter mAdapter;
    private View view;
    private RecyclerView.LayoutManager layoutManager;
    private BillViewModel model;

    private static final String TAG = "BillListFragment";
    public static final String CURRENT_BILL = "Current Bill";

    private OnFragmentInteractionListener mListener;

    public BillsListFragment() {
        // Required empty public constructor
    }

    public static BillsListFragment newInstance(String param1, String param2) {
        BillsListFragment fragment = new BillsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bill_list, container, false);
        model = new ViewModelProvider( requireActivity() ).get(BillViewModel.class);

        initBillDataObserver();
        buildRecyclerView();
        initScrollListener();

        return view;
    }

    private void initScrollListener() {
        BillScrollListener scrollListener = new BillScrollListener(recyclerView, model);
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void initBillDataObserver() {
        model.getBillData().observe(getViewLifecycleOwner(), new Observer<List<BillData>>() {
            @Override
            public void onChanged(List<BillData> billData) {
                int billDataSize = billData.size() - 1;

                if (model.getFirstLoad()) {
                    if (billData.get(billDataSize) != null) {
                        mAdapter.notifyDataSetChanged();
                        model.setFirstLoad();
                    }
                    return;
                }

                if (model.isLoading()) {
                    mAdapter.notifyItemInserted(model.getLoaderIndex());
                    Log.d(TAG, "Added Loader: " + String.valueOf(billDataSize));
                } else {

                    if (model.getLoaderIndex() != -1) {
                        //Log.d(TAG, "Removing Loader:  " + String.valueOf(billDataSize));
                        mAdapter.notifyItemRemoved(model.getLoaderIndex());
                    }

                    Log.d(TAG, "Added more bills:  " + String.valueOf(billDataSize + 1));

                    mAdapter.notifyItemRangeChanged(billDataSize - BillRepository.NUM_BILLS_ADDED, billDataSize);
                }

            }

        });
    }

    private void buildRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.bill_recycler);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BillsListAdapter( model.getBillData().getValue() );
        recyclerView.setAdapter(mAdapter);

        // set item click listener
        mAdapter.setBillItemListener((int position) -> {
            Intent intent = new Intent(getContext(), OverviewActivity.class);
            intent.putExtra(CURRENT_BILL, model.getBillData().getValue().get(position));

            startActivity(intent);
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
