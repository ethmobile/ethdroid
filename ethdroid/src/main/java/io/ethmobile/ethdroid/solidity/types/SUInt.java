package io.ethmobile.ethdroid.solidity.types;


import java.math.BigInteger;
import java.util.regex.Pattern;

import io.ethmobile.ethdroid.exception.EthDroidException;

/**
 * Created by gunicolas on 16/08/16.
 */
public abstract class SUInt<T> extends SType<T> {

    private SUInt(T value) {
        super(value);
    }

    public static SUInt8 fromShort(short from) {
        if (from < 0) {
            throw new EthDroidException("illegal argument. " + from + " is negative");
        } else if (from >= (Math.pow(2, 8))) {
            throw new EthDroidException("illegal argument. " + from + " bigger than 2^8");
        }
        return new SUInt8(from);
    }

    public static SUInt16 fromInteger(int from) {
        if (from < 0) {
            throw new EthDroidException("illegal argument. " + from + " is negative");
        } else if (from >= (Math.pow(2, 16))) {
            throw new EthDroidException("illegal argument. " + from + " bigger than 2^16");
        }
        return new SUInt16(from);
    }

    public static SUInt32 fromLong(long from) {
        if (from < 0) {
            throw new EthDroidException("illegal argument. " + from + " is negative");
        } else if (from >= (Math.pow(2, 32))) {
            throw new EthDroidException("illegal argument. " + from + " bigger than 2^32");
        }
        return new SUInt32(from);
    }

    public static SUInt64 fromBigInteger64(BigInteger from) {
        if (from.compareTo(BigInteger.ZERO) < 0) {
            throw new EthDroidException("illegal argument. " + from.toString() + " is negative");
        } else if (from.compareTo(new BigInteger("2").pow(64)) >= 0) {
            throw new EthDroidException("illegal argument. " + from + " bigger than 2^64");
        }
        return new SUInt64(from);
    }

    public static SUInt128 fromBigInteger128(BigInteger from) {
        if (from.compareTo(BigInteger.ZERO) < 0) {
            throw new EthDroidException("illegal argument. " + from.toString() + " is negative");
        } else if (from.compareTo(new BigInteger("2").pow(128)) >= 0) {
            throw new EthDroidException("illegal argument. " + from + " bigger than 2^128");
        }
        return new SUInt128(from);
    }

    public static SUInt256 fromBigInteger256(BigInteger from) {
        if (from.compareTo(BigInteger.ZERO) < 0) {
            throw new EthDroidException("illegal argument. " + from.toString() + " is negative");
        } else if (from.compareTo(new BigInteger("2").pow(256)) >= 0) {
            throw new EthDroidException("illegal argument. " + from + " bigger than 2^256");
        }
        return new SUInt256(from);
    }

    @Override
    public String asString() {
        return value.toString();
    }

    public static class SUInt8 extends SUInt<Short> {
        private SUInt8(Short value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^uint8(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }

    public static class SUInt16 extends SUInt<Integer> {
        private SUInt16(Integer value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^uint16(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }

    public static class SUInt32 extends SUInt<Long> {
        public SUInt32(Long value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^uint32(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }

    public static class SUInt64 extends SUInt<BigInteger> {

        public SUInt64(BigInteger value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^uint64(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }

    public static class SUInt128 extends SUInt<BigInteger> {
        public SUInt128(BigInteger value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^uint128(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }

    public static class SUInt256 extends SUInt<BigInteger> {
        public SUInt256(BigInteger value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^uint256(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }

}
