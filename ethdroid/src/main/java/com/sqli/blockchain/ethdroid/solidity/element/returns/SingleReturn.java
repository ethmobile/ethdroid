package com.sqli.blockchain.ethdroid.solidity.element.returns;


import com.sqli.blockchain.ethdroid.solidity.types.SType;

/**
 * Created by gunicolas on 21/03/17.
 */

public class SingleReturn<T extends SType> {
    T element1;

    public SingleReturn(T element) {
        this.element1 = element;
    }

    public T getElement1() {
        return element1;
    }
}
