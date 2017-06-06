package io.ethmobile.ethdroid.solidity.element.returns;

import io.ethmobile.ethdroid.solidity.types.SType;

/**
 * Created by gunicolas on 22/03/17.
 */

public class TripleReturn<T1 extends SType, T2 extends SType, T3 extends SType> extends
    PairReturn<T1, T2> {

    T3 element3;

    public TripleReturn(T1 element1, T2 element2, T3 element3) {
        super(element1, element2);
        this.element3 = element3;
    }

    public T3 getElement3() {
        return element3;
    }
}
