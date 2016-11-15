package com.sqli.blockchain.ethereum_android_sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.sqli.blockchain.ethereum_android_sample.R;

/**
 * Created by root on 14/11/16.
 */

public class CreateAccountFragment extends SampleFragment implements View.OnClickListener {

    final static String DEFAULT_PASSWORD = "passwd";

    ActionProcessButton createAccountButton;
    TextView createAccountTextview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.createaccount,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createAccountButton = (ActionProcessButton) view.findViewById(R.id.createaccount_button);
        createAccountButton.setOnClickListener(this);

        createAccountTextview = (TextView) view.findViewById(R.id.createaccount_password);
    }


    @Override
    public void onClick(View v) {
        if( v == createAccountButton ){
            String accountId = ethereumJava.personal.newAccount(DEFAULT_PASSWORD);
            createAccountTextview.setText(accountId);
        }
    }
}
