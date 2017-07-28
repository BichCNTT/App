package com.example.ominext.mediaplayerapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class LyricFragment extends Fragment {

    public LyricFragment() {
        // Required empty public constructor
    }


    public static LyricFragment newInstance() {
        LyricFragment fragment = new LyricFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_lyric, container, false);
    }
}
