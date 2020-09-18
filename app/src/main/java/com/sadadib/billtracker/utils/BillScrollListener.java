package com.sadadib.billtracker.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sadadib.billtracker.data.BillViewModel;

public class BillScrollListener extends RecyclerView.OnScrollListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BillViewModel model;

    public BillScrollListener(RecyclerView rv, BillViewModel model) {
        this.recyclerView = rv;
        this.layoutManager = rv.getLayoutManager();
        this.model = model;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

            if (!model.isLoading()) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreItems();
                }
            }
        }
    }

    private void loadMoreItems() {
        recyclerView.post(new Runnable() {
            public void run() {
                if (!model.isLoading()) {
                    model.addData();
                }
            }
        });
    }
}
