package io.ethmobile.ethdroid.solidity.types;

/**
 * Created by gunicolas on 08/08/16.
 */
public abstract class SType<T> {

    public static int ENCODED_SIZE = 64;
    T value;

    public SType(T value) {
        this.value = value;
    }

    public static Class<? extends SType> getClazz() {
        return SType.class;
    }

    public final T get() {
        return value;
    }

    public abstract String asString();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SType)) return false;
        SType toCompare = (SType) o;
        return get().equals(toCompare.get());
    }
}
