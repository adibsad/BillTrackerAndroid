package com.sadadib.billtracker;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sadadib.billtracker.data.BillData;
import com.sadadib.billtracker.utils.BillFormat;

import java.util.List;


public class BillsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BillData> mDataSet;
    private final int LOADING = 0;
    private final int ITEM = 1;
    private Resources res;

    private OnBillClickListener mListener;

    public interface OnBillClickListener {
        void onBillClick(int position);
    }

    public void setBillItemListener(OnBillClickListener listener) { this.mListener = listener; }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView billId;
        public TextView titleView;
        public TextView sponsorView;
        public TextView dateView;
        public TextView introducedAt;
        public TextView billStatus;
        public ImageView imageView;

        public BillViewHolder(View v, OnBillClickListener listener) {
            super(v);
            billId = (TextView) v.findViewById(R.id.bill_item_id);
            titleView = (TextView) v.findViewById(R.id.bill_item_title);
            sponsorView = (TextView) v.findViewById(R.id.bill_item_sponsor);
            introducedAt = (TextView) v.findViewById(R.id.bill_introduced_at);
            billStatus = (TextView) v.findViewById(R.id.bill_status_text);
            dateView = (TextView) v.findViewById(R.id.bill_status_at);
            imageView = (ImageView) v.findViewById(R.id.bill_item_image);

            if (listener != null) {
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = getAdapterPosition();

                            if (position != RecyclerView.NO_POSITION)
                                listener.onBillClick(position);
                        }
                    });

            }

        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.bill_loading_bar);
        }
    }

    public BillsListAdapter(List<BillData> data) {
        mDataSet = data;
    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        res = parent.getResources();
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case ITEM:
                View itemView = inflater.inflate(R.layout.bill_row, parent, false);
                viewHolder = new BillViewHolder(itemView, mListener);
                break;
            case LOADING:
                View loadingView = inflater.inflate(R.layout.bill_row_loading, parent, false);
                viewHolder = new LoadingViewHolder(loadingView);
                break;
        }


        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position) == null ? LOADING : ITEM;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                BillData data = mDataSet.get(position);
                BillViewHolder itemHolder = (BillViewHolder) holder;

                itemHolder.imageView.setImageResource(R.drawable.bill_example_image);
                itemHolder.billId.setText(res.getString(R.string.bill_text, BillFormat.formatBillId(data.getBillId())));
                itemHolder.introducedAt.setText(res.getString(R.string.bill_text, BillFormat.formatDate(data.getIntroducedAt())));
                itemHolder.billStatus.setText(res.getString(R.string.bill_text, "Last Action"));
                itemHolder.titleView.setText(res.getString(R.string.bill_text, data.getDisplayTitle()));
                itemHolder.sponsorView.setText(res.getString(R.string.bill_text, BillFormat.formatSponsor(data.getSponsorInfo(), data.getParty())));
                itemHolder.dateView.setText(res.getString(R.string.bill_text, BillFormat.formatDate(data.getStatusAt())));
                break;

            case LOADING:
                break;

        }

    }

    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public void addLoader() {
        mDataSet.add(null);
        notifyItemChanged(mDataSet.size() - 1);
    }
}
