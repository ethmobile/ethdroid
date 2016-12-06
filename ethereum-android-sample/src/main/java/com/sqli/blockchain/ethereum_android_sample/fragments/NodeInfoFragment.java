package com.sqli.blockchain.ethereum_android_sample.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 */

public class NodeInfoFragment extends SampleFragment implements View.OnClickListener {

    ActionProcessButton nodeInfoButton;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nodeinfo, container, false);
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
        if (v == nodeInfoButton) {
            ethereumJava.admin.getNodeInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNodeInfoSuccess(), onNodeInfoError());
            nodeInfoButton.setMode(ActionProcessButton.Mode.ENDLESS);
        }
    }

    @NonNull
    private Action1<Throwable> onNodeInfoError() {
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                nodeInfoButton.setProgress(-1);
            }
        };
    }

    @NonNull
    private Action1<NodeInfo> onNodeInfoSuccess() {
        return new Action1<NodeInfo>() {
            @Override
            public void call(NodeInfo nodeInfo) {
                nodeInfoButton.setProgress(100);
                textView.setText(nodeInfo.enode);
            }
        };
    }
}
