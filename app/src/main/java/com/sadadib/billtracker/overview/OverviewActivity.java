package com.sadadib.billtracker.overview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sadadib.billtracker.BillsListFragment;
import com.sadadib.billtracker.R;
import com.sadadib.billtracker.data.BillData;
import com.sadadib.billtracker.data.BillViewModel;

public class OverviewActivity extends AppCompatActivity {
    private static final String TAG = "OV Activity";
    private BillViewModel model;
    private BillData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Intent intent = getIntent();
        data = (BillData) intent.getParcelableExtra(BillsListFragment.CURRENT_BILL);

        if (data != null) {
            Log.d(TAG, data.toString());

        }


    }
}
