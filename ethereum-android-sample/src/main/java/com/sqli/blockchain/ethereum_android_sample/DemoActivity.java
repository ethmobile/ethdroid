package com.sqli.blockchain.ethereum_android_sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.sqli.blockchain.android_geth.EthereumService;

import ethereumjava.EthereumJava;
import me.relex.circleindicator.CircleIndicator;
import me.wangyuwei.loadingview.LoadingView;

/**
 */

public class DemoActivity extends FragmentActivity implements EthereumService.EthereumServiceInterface {

    ViewPager viewPager;
    CircleIndicator circleIndicator;

    LoadingView loadingView;

    SampleApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.loading);

        loadingView = (LoadingView) findViewById(R.id.loading_view);
        loadingView.start();

        application = (SampleApplication) getApplication();
        application.registerGethReady(this);
    }

    public EthereumJava getEthereumJava() {
        return application.ethereumjava;
    }

    @Override
    public void onEthereumServiceReady() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.demo);

                loadingView.stop();

                viewPager = (ViewPager) findViewById(R.id.viewpager);
                circleIndicator = (CircleIndicator) findViewById(R.id.indicator);

                viewPager.setAdapter(new SimplePagerAdapter(getSupportFragmentManager()));
                circleIndicator.setViewPager(viewPager);
                viewPager.setCurrentItem(0);
            }
        });

    }
}
