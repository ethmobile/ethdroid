package io.ethmobile.ethdroid.model;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by gunicolas on 29/05/17.
 */
@RunWith(AndroidJUnit4.class)
public class BalanceTest {

    Balance balance;

    @Before
    public void setUp() throws Exception {
        balance = Balance.of(3);
    }

    @Test
    public void inWei() throws Exception {
        Assert.assertEquals(3, balance.inWei());
    }

    @Test
    public void inKWei() throws Exception {
        Assert.assertEquals(0.003, balance.inKWei(), 0);
    }

    @Test
    public void inPicoEther() throws Exception {
        Assert.assertEquals(0.000003, balance.inPicoEther(), 0);
    }

    @Test
    public void inNanoEther() throws Exception {
        Assert.assertEquals(0.000000003, balance.inNanoEther(), 0);
    }

    @Test
    public void inMicroEther() throws Exception {
        Assert.assertEquals(0.000000000003, balance.inMicroEther(), 0);
    }

    @Test
    public void inMilliEther() throws Exception {
        Assert.assertEquals(0.000000000000003, balance.inMilliEther(), 0);
    }

    @Test
    public void inEther() throws Exception {
        Assert.assertEquals(0.000000000000000003, balance.inEther(), 0);
    }

    @Test
    public void inKEther() throws Exception {
        Assert.assertEquals(0.000000000000000000003, balance.inKEther(), 0);
    }

    @Test
    public void inMEther() throws Exception {
        Assert.assertEquals(0.000000000000000000000003, balance.inMEther(), 0);
    }

    @Test
    public void inGEther() throws Exception {
        Assert.assertEquals(0.000000000000000000000000003, balance.inGEther(), 0);
    }

    @Test
    public void inTEther() throws Exception {
        Assert.assertEquals(0.000000000000000000000000000003, balance.inTEther(), 0);
    }

    @Test
    public void asString() throws Exception {
        Assert.assertEquals("3", balance.string());
    }


}
