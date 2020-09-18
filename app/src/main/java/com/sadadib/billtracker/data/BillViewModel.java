package com.sadadib.billtracker.data;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class BillViewModel extends ViewModel implements BillRepository.OnRepoCompleteListener {
    private static final int PAGE_LOAD_LIMIT = 3;
    private static final int PAGE_START = 0;
    private static final int NO_INDEX = -1;
    private static final boolean LOADING = true;
    private static final String TAG = "VM";

    private BillRepository mBillRepo;
    private MutableLiveData<List<BillData>> billData;

    private boolean firstLoad = LOADING;
    private boolean loadingStatus = !LOADING;
    private int loaderIndex = NO_INDEX;
    private int curPage = PAGE_START;

    private void init() {
        mBillRepo = new BillRepository(this);
        billData = mBillRepo.addFirstFromFireStore();
    }

    private void updateBillData(List<BillData> list) {
        billData.postValue(list);
    }

    public LiveData<List<BillData>> getBillData() {
        if (billData == null) {
            init();
        }
        return billData;
    }

    public int getLoaderIndex() {
        return loaderIndex;
    }

    public boolean getFirstLoad() {
        return firstLoad;
    }

    public void setFirstLoad() {
        firstLoad = !firstLoad;
    }

    public void addData() {
        if (curPage++ >= PAGE_LOAD_LIMIT) {
            return;
        }

        addLoader();
        mBillRepo.loadMoreBills();
    }

    public boolean isLoading() {
        return loadingStatus;
    }

    private void addLoader() {
        List<BillData> newList = billData.getValue();
        if (newList == null)
            return;

        newList.add(null);

        loaderIndex = newList.size() - 1;
        loadingStatus = LOADING;

        updateBillData(newList);
    }

    private void removeLoader() {
        List<BillData> newList = billData.getValue();
        if (newList == null)
            return;

        if (loaderIndex != newList.size() - 1)
            return;

        //Remove loader
        newList.remove(loaderIndex);

        //Set loading status to not loading
        loadingStatus = !LOADING;

        //Update BillData list with no loader list
        updateBillData(newList);

        //Set loader index to -1
        loaderIndex = NO_INDEX;
    }

    @Override
    public void onFirstLoad(List<BillData> data) {

        List<BillData> newList = billData.getValue();
        if (newList == null && newList.size() < 1) {
            return;
        }

        for (int i = 0; i < data.size(); i++) {
            if (i < 1)
                newList.set(i, data.get(i));
            else
                newList.add(data.get(i));
        }

        updateBillData(newList);

    }

    //TODO: Set query parameters and allow user to request different data
    @Override
    public void onLoadMore(List<BillData> data) {
        List<BillData> newList = billData.getValue();
        if (newList == null)
            return;

        //Remove current loader and add new data
        removeLoader();
        newList.addAll(data);

        //End loading status and update list
        loadingStatus = !LOADING;
        updateBillData(newList);
    }

    @Override
    public void onError() {

    }
}

