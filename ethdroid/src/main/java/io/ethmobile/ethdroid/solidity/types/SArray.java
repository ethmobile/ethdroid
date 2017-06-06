package io.ethmobile.ethdroid.solidity.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import io.ethmobile.ethdroid.exception.EthDroidException;


/**
 * Created by gunicolas on 07/02/17.
 */

public class SArray<T extends SType> extends SType<T[]> {

    private int fixedSize;

    private SArray(T[] value, int _fixedSize) {
        super(value);
        fixedSize = _fixedSize;
    }

    public static <T extends SType> SArray<T> fromArray(T[] array) {
        return new SArray<>(array, array.length);
    }

    public static <T extends SType> SArray<T> fromList(List<T> list) {

        T[] ret = (T[]) Array.newInstance(T.getClazz(), list.size());
        for (int i = 0; i < list.size(); i++) {
            ret[i] = list.get(i);
        }

        return new SArray<>(ret, -1);
    }

    public static Class<? extends SType> getNestedType(Type type) {
        if (type instanceof ParameterizedType) {
            return SArray.getNestedType(((ParameterizedType) type).getActualTypeArguments()[0]);
        } else if (type instanceof Class) {
            return (Class<? extends SType>) type;
        } else {
            throw new EthDroidException("type not handled (" + type.toString() + ")");
        }
    }

    /**
     * Converts given size array to its representative String
     * Ex:
     * {@literal
     * {3,3} --> [3][3]
     * {0,3,0} --> [][3][]
     * {} --> ""
     * null --> ""
     * }
     *
     * @param size size of an array
     * @return string representation of the given size
     */
    public static String sizeToString(Size size) {
        if (size == null) return "";
        StringBuilder ret = new StringBuilder();
        for (int i : size.value()) {
            ret.append("[");
            ret.append(i > 0 ? i : "");
            ret.append("]");
        }
        return ret.toString();
    }

    @Override
    public String asString() {
        return null;
    }

    public boolean isDynamic() {
        return fixedSize == -1;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SArray)) return false;
        SArray array = (SArray) o;
        SType[] arrayValues = (SType[]) array.get();
        int index = 0;
        for (T item : value) {
            if (!item.equals(arrayValues[index])) return false;
            index++;
        }
        return true;
    }

    @Target({ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Size {
        /**
         * Contains an array of sizes of the annotated SArray.
         * fixe sized array must have a value greater than 0.
         * {@literal
         * (ex: int[3] -> {3})
         * }
         * dynamic sized array must have a value of 0
         * {@literal
         * (ex: int[] -> {0})
         * }
         * single value must have a no values
         * {@literal
         * (ex: int -> {})
         * }
         *
         * @return an array of sizes of the annotated SArray
         */
        int[] value() default {};
    }
}
