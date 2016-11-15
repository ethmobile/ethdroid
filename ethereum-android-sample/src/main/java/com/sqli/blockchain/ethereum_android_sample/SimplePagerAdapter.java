package com.sqli.blockchain.ethereum_android_sample;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sqli.blockchain.ethereum_android_sample.fragments.AddPeerFragment;
import com.sqli.blockchain.ethereum_android_sample.fragments.ContractFragment;
import com.sqli.blockchain.ethereum_android_sample.fragments.CreateAccountFragment;
import com.sqli.blockchain.ethereum_android_sample.fragments.NodeInfoFragment;
import com.sqli.blockchain.ethereum_android_sample.fragments.PeersFragment;
import com.sqli.blockchain.ethereum_android_sample.fragments.SendTxFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 14/11/16.
 */

public class SimplePagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public SimplePagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new NodeInfoFragment());
        fragments.add(new AddPeerFragment());
        fragments.add(new PeersFragment());
        fragments.add(new CreateAccountFragment());
        //fragments.add(new SendTxFragment());
        fragments.add(new ContractFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}
