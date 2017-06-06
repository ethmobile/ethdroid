package io.ethmobile.ethdroid.solidity.coder;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.List;

import io.ethmobile.ethdroid.Utils;
import io.ethmobile.ethdroid.exception.EthDroidException;
import io.ethmobile.ethdroid.solidity.coder.decoder.SArrayDecoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.SDecoder;
import io.ethmobile.ethdroid.solidity.coder.encoder.SEncoder;
import io.ethmobile.ethdroid.solidity.types.SArray;
import io.ethmobile.ethdroid.solidity.types.SType;

/**
 * Created by gunicolas on 08/09/16.
 */
public abstract class SCoder {

    public static String encodeParams(Object[] parameters) {

        String encodedParameters = "";
        for (Object parameter : parameters) {
            encodedParameters += encodeParam(parameter);
        }

        return encodedParameters;
    }

    public static String encodeParam(Object parameter) throws EthDroidException {
        Class paramClass = parameter.getClass();
        Class<? extends SEncoder> encoder = SCoderMapper.getEncoderForClass(paramClass);

        if (encoder == null) {
            throw new EthDroidException(
                "No encoder found for this class : " + paramClass.getSimpleName());
        }
        try {
            return encoder.newInstance().encode(parameter);
        } catch (Exception e) {
            throw new EthDroidException(e);
        }
    }

    public static SType[] decodeParams(String encodedData,
        List<AbstractMap.SimpleEntry<Type, SArray.Size>> returnTypes) {

        int numberParams = returnTypes.size();
        SType[] ret = new SType[numberParams];
        int offset = 0;

        for (int i = 0; i < numberParams; i++) {
            int length;
            SType param;
            AbstractMap.SimpleEntry<Type, SArray.Size> returnType = returnTypes.get(i);
            Type t = returnType.getKey();
            SArray.Size size = returnType.getValue();
            if (size == null) {
                length = SType.ENCODED_SIZE;
                param = decodeParam(encodedData.substring(offset, offset + length),
                    (Class<SType>) t);
            } else {
                // TODO handle dynamic arrays
                length = Utils.multiplyByArrayValues(SType.ENCODED_SIZE, size.value());
                param = SArrayDecoder.decodeArray(encodedData.substring(offset, offset + length),
                    SArray.getNestedType(t), size.value());
            }
            offset += length;
            ret[i] = param;
        }

        return ret;
    }

    public static <T extends SType> T decodeParam(String encodedParam, Class<T> parameterClass) {
        Class<? extends SDecoder> decoder = SCoderMapper.getDecoderForClass(parameterClass);

        if (decoder == null) {
            throw new EthDroidException(
                "No encoder found for this class : " + parameterClass.getSimpleName());
        }

        try {
            return (T) decoder.newInstance().decode(encodedParam);
        } catch (Exception e) {
            throw new EthDroidException(e);
        }
    }
}
