package io.ethmobile.ethdroid.solidity.coder.decoder;

import java.util.Arrays;

import io.ethmobile.ethdroid.Utils;
import io.ethmobile.ethdroid.solidity.coder.SCoder;
import io.ethmobile.ethdroid.solidity.types.SArray;
import io.ethmobile.ethdroid.solidity.types.SType;

/**
 * Created by gunicolas on 10/03/17.
 */

public class SArrayDecoder {

    public static SArray decodeArray(String toDecode, Class<? extends SType> nestedType,
        int[] arraySize) {

        int arraySizeLength = arraySize.length;

        int numberOfElements = arraySize[arraySizeLength - 1];
        SType[] ret = new SType[numberOfElements];

        int offset = 0;

        int[] arraySizeRecursive = Arrays.copyOf(arraySize, arraySizeLength - 1);
        int length = arraySizeLength == 1 ? SType.ENCODED_SIZE : Utils.multiplyByArrayValues(
            SType.ENCODED_SIZE, arraySizeRecursive);

        for (int i = 0; i < numberOfElements; i++) {
            String stypeString = toDecode.substring(offset, offset + length);
            ret[i] = arraySizeLength == 1 ? SCoder.decodeParam(stypeString, nestedType)
                : decodeArray(stypeString, nestedType, arraySizeRecursive);
            offset += length;
        }

        return SArray.fromArray(ret);
    }

}
