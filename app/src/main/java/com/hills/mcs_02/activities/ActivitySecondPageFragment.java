package com.hills.mcs_02.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hills.mcs_02.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ActivitySecondPageFragment extends Fragment {

    public ActivitySecondPageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity_2rd_page, container, false);
    }
}
