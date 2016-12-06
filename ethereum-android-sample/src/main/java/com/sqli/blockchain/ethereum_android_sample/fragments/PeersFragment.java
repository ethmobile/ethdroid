package com.sqli.blockchain.ethereum_android_sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.sqli.blockchain.ethereum_android_sample.R;

import java.util.List;

import ethereumjava.module.objects.Peer;

/**
 */

public class PeersFragment extends SampleFragment implements View.OnClickListener {

    final static String DEFAULT_TEXTVIEW = "No Peers";

    ActionProcessButton peersButton;
    TextView peersTextview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.peers, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        peersButton = (ActionProcessButton) view.findViewById(R.id.peers_button);
        peersButton.setOnClickListener(this);

        peersTextview = (TextView) view.findViewById(R.id.peers_textview);
        peersTextview.setText(DEFAULT_TEXTVIEW);

    }

    @Override
    public void onClick(View v) {
        if (v == peersButton) {
            List<Peer> peers = ethereumJava.admin.peers();
            String textViewText;
            if (peers == null || peers.size() <= 0) {
                textViewText = DEFAULT_TEXTVIEW;
            } else {
                textViewText = peers.toString();
            }
            peersTextview.setText(textViewText);

        }
    }
}
