package io.ethmobile.ethdroid.model;

import java.math.BigDecimal;

/**
 * Created by gunicolas on 29/05/17.
 */

public class Balance {

    private static final BigDecimal KWEI = BigDecimal.TEN.pow(3);
    private static final BigDecimal PICOETHER = BigDecimal.TEN.pow(6);
    private static final BigDecimal NANOETHER = BigDecimal.TEN.pow(9);
    private static final BigDecimal MICROETHER = BigDecimal.TEN.pow(12);
    private static final BigDecimal MILLIETHER = BigDecimal.TEN.pow(15);
    private static final BigDecimal ETHER = BigDecimal.TEN.pow(18);
    private static final BigDecimal KETHER = BigDecimal.TEN.pow(21);
    private static final BigDecimal METHER = BigDecimal.TEN.pow(24);
    private static final BigDecimal GETHER = BigDecimal.TEN.pow(27);
    private static final BigDecimal TETHER = BigDecimal.TEN.pow(30);

    private BigDecimal value;

    private Balance(long value) {
        this.value = BigDecimal.valueOf(value);
    }

    /**
     * Build a balance object with the given value (in wei === 10^18 ether)
     *
     * @param value Value ti use in wei (1 wei === 10^18 ether)
     * @return balance built from value
     */
    public static Balance of(long value) {
        return new Balance(value);
    }

    public long inWei() {
        return this.value.longValue();
    }

    public double inKWei() {
        return this.value.divide(KWEI).doubleValue();
    }

    public double inPicoEther() {
        return this.value.divide(PICOETHER).doubleValue();
    }

    public double inNanoEther() {
        return this.value.divide(NANOETHER).doubleValue();
    }

    public double inMicroEther() {
        return this.value.divide(MICROETHER).doubleValue();
    }

    public double inMilliEther() {
        return this.value.divide(MILLIETHER).doubleValue();
    }

    public double inEther() {
        return this.value.divide(ETHER).doubleValue();
    }

    public double inKEther() {
        return this.value.divide(KETHER).doubleValue();
    }

    public double inMEther() {
        return this.value.divide(METHER).doubleValue();
    }

    public double inGEther() {
        return this.value.divide(GETHER).doubleValue();
    }

    public double inTEther() {
        return this.value.divide(TETHER).doubleValue();
    }

    public String string() {
        return this.value.toString();
    }

}
