package io.ethmobile.ethdroid.solidity.coder;

import java.util.HashMap;
import java.util.Map;

import io.ethmobile.ethdroid.solidity.coder.decoder.SBoolDecoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.SDecoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.sintdecoder.SInt128Decoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.sintdecoder.SInt16Decoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.sintdecoder.SInt256Decoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.sintdecoder.SInt32Decoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.sintdecoder.SInt64Decoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.sintdecoder.SInt8Decoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.suintdecoder.SUInt128Decoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.suintdecoder.SUInt16Decoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.suintdecoder.SUInt256Decoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.suintdecoder.SUInt32Decoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.suintdecoder.SUInt64Decoder;
import io.ethmobile.ethdroid.solidity.coder.decoder.suintdecoder.SUInt8Decoder;
import io.ethmobile.ethdroid.solidity.coder.encoder.SArrayEncoder;
import io.ethmobile.ethdroid.solidity.coder.encoder.SBoolEncoder;
import io.ethmobile.ethdroid.solidity.coder.encoder.SBytesEncoder;
import io.ethmobile.ethdroid.solidity.coder.encoder.SEncoder;
import io.ethmobile.ethdroid.solidity.coder.encoder.SIntEncoder;
import io.ethmobile.ethdroid.solidity.coder.encoder.SUIntEncoder;
import io.ethmobile.ethdroid.solidity.types.SArray;
import io.ethmobile.ethdroid.solidity.types.SBool;
import io.ethmobile.ethdroid.solidity.types.SBytes;
import io.ethmobile.ethdroid.solidity.types.SInt;
import io.ethmobile.ethdroid.solidity.types.SUInt;

/**
 * Mapping between Solidity types and coders (Encoders/Decoders).
 * Created by gunicolas on 08/09/16.
 */
public abstract class SCoderMapper {

    private static final Map<Class, Class<? extends SEncoder>> encoderMapping =
        new HashMap<Class, Class<? extends SEncoder>>() {{
            put(SInt.class, SIntEncoder.class);
            put(SUInt.class,
                SUIntEncoder.class); //TODO SUIntEncoder == SIntEncoder --> Refactor to use the
            // same encoder
            put(SBytes.class, SBytesEncoder.class);
            put(SBool.class, SBoolEncoder.class);
            put(SArray.class, SArrayEncoder.class);
        }};


    private static final Map<Class, Class<? extends SDecoder>> decoderMapping =
        new HashMap<Class, Class<? extends SDecoder>>() {{
            put(SInt.SInt8.class, SInt8Decoder.class);
            put(SInt.SInt16.class, SInt16Decoder.class);
            put(SInt.SInt32.class, SInt32Decoder.class);
            put(SInt.SInt64.class, SInt64Decoder.class);
            put(SInt.SInt128.class, SInt128Decoder.class);
            put(SInt.SInt256.class, SInt256Decoder.class);
            put(SUInt.SUInt8.class, SUInt8Decoder.class);
            put(SUInt.SUInt16.class, SUInt16Decoder.class);
            put(SUInt.SUInt32.class, SUInt32Decoder.class);
            put(SUInt.SUInt64.class, SUInt64Decoder.class);
            put(SUInt.SUInt128.class, SUInt128Decoder.class);
            put(SUInt.SUInt256.class, SUInt256Decoder.class);
            put(SBool.class, SBoolDecoder.class);
        }};


    /**
     * Returns solidity encoder for the given class or null if there is no encoder specified.
     * Look for an encoder of super class if none is found while there is no superclass.
     *
     * @param clazz the class looking for an encoder
     * @return an encoder for the given class
     */
    public static Class<? extends SEncoder> getEncoderForClass(Class clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends SEncoder> ret = encoderMapping.get(clazz);
        if (ret != null) {
            return ret;
        }
        return getEncoderForClass(clazz.getSuperclass());
    }

    /**
     * Returns solidity decoder for the given class or null if there is no decoder specified.
     * Look for a decoder of super class if none is found while there is no superclass.
     *
     * @param clazz the class looking for an decoder
     * @return a decoder for the given class
     */
    public static Class<? extends SDecoder> getDecoderForClass(Class clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends SDecoder> ret = decoderMapping.get(clazz);
        if (ret != null) {
            return ret;
        }
        return getDecoderForClass(clazz.getSuperclass());
    }
}
