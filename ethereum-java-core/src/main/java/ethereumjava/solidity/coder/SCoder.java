package ethereumjava.solidity.coder;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.solidity.SolidityUtils;
import ethereumjava.solidity.coder.encoder.SEncoder;
import ethereumjava.solidity.types.SType;

/**
 * Created by gunicolas on 08/09/16.
 */
public abstract class SCoder {

    public static String encodeParams(Object[] parameters) {

        int dynamicOffset = 0;

        String encodedParameters = "";
        for (Object parameter : parameters) {

            Class paramClass = parameter.getClass();
            Class<? extends SEncoder> encoder = SCoderMapper.getEncoderForClass(paramClass);

            if (encoder == null) {
                throw new EthereumJavaException("No encoder found for this class : " + paramClass.getSimpleName());
            }
            try {
                String encodedParam = encoder.newInstance().encode(parameter);

                encodedParameters += encodedParam;

                String typeName = SolidityUtils.extractSolidityTypeName(parameter);
                int staticPartLength = SType.staticPartLength(typeName);
                int roundedStaticPartLength = (int) (Math.floor((staticPartLength + 31) / 32) * 32);
                dynamicOffset += roundedStaticPartLength;

            } catch (Exception e) {
                throw new EthereumJavaException(e);
            }
        }


        return encodedParameters;
    }

}
