package com.sadadib.billtracker.data;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BillRepository {
    private static final String CONGRESS = "116";
    private static final String META_PATH = "meta";
    private static final String TAG = "Repo";

    public static final int NUM_BILLS_ADDED = 5;
    public static final int NUM_INIT_BILLS = 10;

    private static CollectionReference documentRef;
    private static CollectionReference metaRef;
    private QueryDocumentSnapshot lastVisible;
    private OnRepoCompleteListener mListener;

    private ArrayList<BillData> billData = new ArrayList<>();

    public BillRepository(OnRepoCompleteListener mListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        metaRef = db.collection(META_PATH);
        documentRef = db.collection(CONGRESS);

        this.mListener = mListener;

    }

    public MutableLiveData<List<BillData>> addFirstFromFireStore() {

        documentRef.whereEqualTo("top_subject", "Crime and law enforcement")
                .orderBy("status_at", Query.Direction.DESCENDING)
                .limit(NUM_INIT_BILLS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //billData.clear();
                        if (task.isSuccessful()) {
                            List<BillData> dataList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                BillData data = new BillData(document.getData());
                                Log.d(TAG, data.toString());
                                dataList.add(data);
                                lastVisible = document;
                            }

                            mListener.onFirstLoad(dataList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return initBillData();
    }

    public void loadMoreBills() {
        if (lastVisible == null) {
            return;
        }

        documentRef.whereEqualTo("top_subject", "Crime and law enforcement")
                .orderBy("status_at", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(NUM_BILLS_ADDED)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            List<BillData> dataList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                BillData data = new BillData(document.getData());
                                Log.d(TAG, data.toString());
                                dataList.add(data);
                                lastVisible = document;
                            }

                            mListener.onLoadMore(dataList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private MutableLiveData<List<BillData>> initBillData() {
        MutableLiveData<List<BillData>> data = new MutableLiveData<>();
        billData.add(null);
        data.setValue(billData);
        return data;
    }

    /*

    public List<BillData> addFirstBillData() {
        return populateBillData(20);
    }

    public List<BillData> addBillData() {
        return populateBillData(5);
    }

    private List<BillData> populateBillData(int numBills) {
        billData.clear();
        for (int i = 0; i < numBills; i++) {
            billData.add(new BillData("H.R. 120: Example Bill", "Sen. Spon Sor [D - NY5]", "Jan 4, 2019"));
        }

        return billData;
    }
    */

    public interface OnRepoCompleteListener {
        void onFirstLoad(List<BillData> data);
        void onLoadMore(List<BillData> data);
        void onError();
    }
}
