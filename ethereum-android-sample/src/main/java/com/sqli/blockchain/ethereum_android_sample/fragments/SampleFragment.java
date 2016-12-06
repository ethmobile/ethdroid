package com.sqli.blockchain.ethereum_android_sample.fragments;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.sqli.blockchain.ethereum_android_sample.DemoActivity;

import ethereumjava.EthereumJava;

/**
 */

public class SampleFragment extends Fragment {

    EthereumJava ethereumJava;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ethereumJava = ((DemoActivity) getActivity()).getEthereumJava();
    }
}
