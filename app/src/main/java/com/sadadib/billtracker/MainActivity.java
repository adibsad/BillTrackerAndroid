package com.sadadib.billtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sadadib.billtracker.data.BillViewModel;

public class MainActivity extends AppCompatActivity
        implements BillsListFragment.OnFragmentInteractionListener {
    private BillViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new ViewModelProvider(this).get(BillViewModel.class);


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
