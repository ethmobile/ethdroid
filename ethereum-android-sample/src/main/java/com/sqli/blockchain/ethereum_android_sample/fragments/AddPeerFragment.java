package com.sqli.blockchain.ethereum_android_sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.sqli.blockchain.ethereum_android_sample.R;

/**
 * Created by root on 14/11/16.
 */

public class AddPeerFragment extends SampleFragment implements View.OnClickListener {

    final String ENODE = "enode://a4de274d3a159e10c2c9a68c326511236381b84c9ec52e72ad732eb0b2b1a2277938f78593cdbe734e6002bf23114d434a085d260514ab336d4acdc312db671b@[::]:30303";

    ActionProcessButton addPeerButton;
    EditText addPeerId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.addpeer,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addPeerButton = (ActionProcessButton) view.findViewById(R.id.addpeer_button);
        addPeerButton.setMode(ActionProcessButton.Mode.ENDLESS);
        addPeerButton.setOnClickListener(this);

        addPeerId = (EditText) view.findViewById(R.id.addpeer_id);
        addPeerId.setText(ENODE);

    }

    @Override
    public void onClick(View v) {
        if( v == addPeerButton ){
            boolean peerAdded = ethereumJava.admin.addPeer(ENODE);
            if( peerAdded ){
                addPeerButton.setProgress(100);
            } else{
                addPeerButton.setProgress(-1);
            }
        }
    }
}
