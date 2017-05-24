package io.ethmobile.ethdroid.solidity.types;


import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * Created by gunicolas on 4/08/16.
 */
public abstract class SInt<T> extends SType<T> {

    private SInt(T value) {
        super(value);
    }

    public static SInt8 fromByte(byte from) {
        return new SInt8(from);
    }

    public static SInt16 fromShort(short from) {
        return new SInt16(from);
    }

    public static SInt32 fromInteger(int from) {
        return new SInt32(from);
    }

    public static SInt64 fromLong(long from) {
        return new SInt64(from);
    }

    public static SInt128 fromBigInteger128(BigInteger from) {
        return new SInt128(from);
    }

    public static SInt256 fromBigInteger256(BigInteger from) {
        return new SInt256(from);
    }

    @Override
    public String asString() {
        return value.toString();
    }

    public static final class SInt8<Byte> extends SInt {
        private SInt8(Byte value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^int8(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }

    public static final class SInt16<Short> extends SInt {
        private SInt16(Short value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^int16(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }

    public static final class SInt32<Integer> extends SInt {
        private SInt32(Integer value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^int32(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }

    public static final class SInt64<Long> extends SInt {
        private SInt64(Long value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^int64(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }

    public static final class SInt128<BigInteger> extends SInt {
        private SInt128(BigInteger value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^int128(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }

    public static final class SInt256<BigInteger> extends SInt {
        private SInt256(BigInteger value) {
            super(value);
        }

        public static boolean isType(String name) {
            return Pattern.compile("^int(256)?(\\[([0-9])*\\])*$").matcher(name).matches();
        }
    }
}
