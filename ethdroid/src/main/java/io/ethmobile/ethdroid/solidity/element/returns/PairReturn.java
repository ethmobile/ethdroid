package io.ethmobile.ethdroid.solidity.element.returns;


import io.ethmobile.ethdroid.solidity.types.SType;

/**
 * Created by gunicolas on 21/03/17.
 */

public class PairReturn<T1 extends SType, T2 extends SType> extends SingleReturn<T1> {

    T2 element2;

    public PairReturn(T1 element1, T2 element2) {
        super(element1);
        this.element2 = element2;
    }

    public T2 getElement2() {
        return element2;
    }
}
