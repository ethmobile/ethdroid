package com.sqli.blockchain.ethereum_android_sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.sqli.blockchain.ethereum_android_sample.R;

import ethereumjava.module.objects.NodeInfo;
import rx.Subscriber;

/**
 * Created by root on 14/11/16.
 */

public class NodeInfoFragment extends SampleFragment implements View.OnClickListener {

    ActionProcessButton nodeInfoButton;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nodeinfo,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nodeInfoButton = (ActionProcessButton) view.findViewById(R.id.nodeinfo_button);
        nodeInfoButton.setOnClickListener(this);

        textView = (TextView) view.findViewById(R.id.nodeinfo_content);
        textView.setText("");
    }

    @Override
    public void onClick(View v) {
        if( v == nodeInfoButton ){
            ethereumJava.admin.getNodeInfo()
                    .subscribe(new Subscriber<NodeInfo>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("GETH",e.getMessage());
                            onNodeInfoError();
                        }

                        @Override
                        public void onNext(NodeInfo nodeInfo) {
                            if( nodeInfo != null ) {
                                onNodeInfoSuccess(nodeInfo);
                            }
                        }
                    });
            nodeInfoButton.setMode(ActionProcessButton.Mode.ENDLESS);
        }
    }

    private void onNodeInfoError(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nodeInfoButton.setProgress(-1);
            }
        });
    }

    private void onNodeInfoSuccess(final NodeInfo nodeInfo){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nodeInfoButton.setProgress(100);
                textView.setText(nodeInfo.enode);
            }
        });
    }
}
