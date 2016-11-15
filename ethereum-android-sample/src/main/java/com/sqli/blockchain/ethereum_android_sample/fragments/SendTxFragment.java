package com.sqli.blockchain.ethereum_android_sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.sqli.blockchain.ethereum_android_sample.R;

import java.math.BigDecimal;

import ethereumjava.module.objects.Hash;
import ethereumjava.module.objects.TransactionRequest;
import ethereumjava.solidity.SolidityUtils;

/**
 * Created by root on 14/11/16.
 */

public class SendTxFragment extends SampleFragment implements View.OnClickListener {

    final static String FROM_DEFAULT = "0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b";
    final static String TO_DEFAULT = "0xc385233b188811c9f355d4caec14df86d6248235";
    final static String VALUE_DEFAULT = "1";

    EditText fromTextview;
    EditText toTextview;
    EditText valueTextview;

    ActionProcessButton sendTxButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sendtx,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        savedInstanceState.getString("ACCOUNT");

        sendTxButton = (ActionProcessButton) view.findViewById(R.id.sendtx_button);
        sendTxButton.setOnClickListener(this);

        fromTextview = (EditText) view.findViewById(R.id.tx_from);

        toTextview = (EditText) view.findViewById(R.id.tx_to);

        valueTextview = (EditText) view.findViewById(R.id.tx_value);

    }

    @Override
    public void onClick(View v) {
        if( v == sendTxButton ){

            // GET previously created account

            String from = fromTextview.getText().toString();
            String to = toTextview.getText().toString();
            String value = valueTextview.getText().toString();

            if( from == null || from.length() <= 0 ) value = FROM_DEFAULT;
            if( to == null || to.length() <= 0 ) value = TO_DEFAULT;
            if( value == null || value.length() <= 0 ) value = VALUE_DEFAULT;


            BigDecimal amount = SolidityUtils.toWei(value, "ether");
            String amountHex = SolidityUtils.toHex(amount);
            TransactionRequest tx = new TransactionRequest(from,to,amountHex,"message");

            Hash txHash = ethereumJava.eth.sendTransaction(tx);


        }
    }
}