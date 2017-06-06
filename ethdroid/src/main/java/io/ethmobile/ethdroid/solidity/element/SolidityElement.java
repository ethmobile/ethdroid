package io.ethmobile.ethdroid.solidity.element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import io.ethmobile.ethdroid.EthDroid;
import io.ethmobile.ethdroid.sha3.Sha3;
import io.ethmobile.ethdroid.solidity.types.SArray;

/**
 * Abstract class which designate Solidity contract elements like Function or Events
 * Created by gunicolas on 13/10/16.
 */
public abstract class SolidityElement {

    protected String address;
    protected Method method;
    protected String fullName;
    protected EthDroid eth;

    protected List<AbstractMap.SimpleEntry<Type, SArray.Size>> parameters;
    protected List<AbstractMap.SimpleEntry<Type, SArray.Size>> returns;


    protected SolidityElement(String address, Method method, EthDroid eth) {
        this.address = address;
        this.method = method;
        this.eth = eth;

        this.returns = getReturnsType();
        this.parameters = getParametersType();

        this.fullName = transformToFullName();
    }

    /**
     * Get the signature of the given element.
     * Ex: bar(type1,type2,...)
     *
     * @return signature of the given element.
     */
    private String transformToFullName() {
        StringBuilder sbStr = new StringBuilder();

        sbStr.append(method.getName());

        sbStr.append('(');

        for (AbstractMap.SimpleEntry<Type, SArray.Size> parameter : parameters) {

            Class clazz;
            Type parameterType = parameter.getKey();
            SArray.Size parameterSize = parameter.getValue();

            if (parameterSize != null) { // It's an array
                clazz = SArray.getNestedType(parameterType);
            } else { // It's a simple type
                clazz = (Class) parameterType;
            }
            String arrayStr = SArray.sizeToString(parameterSize);

            sbStr.append(clazz.getSimpleName().substring(1).toLowerCase());
            sbStr.append(arrayStr);
            sbStr.append(",");
        }
        if (parameters.size() > 0) {
            sbStr.setLength(sbStr.length() - 1);
        }
        sbStr.append(')');

        return sbStr.toString();
    }

    protected abstract List<AbstractMap.SimpleEntry<Type, SArray.Size>> getParametersType();

    protected List<AbstractMap.SimpleEntry<Type, SArray.Size>> getReturnsType() {
        Type[] returnTypes = extractReturnTypesFromElement();
        List<AbstractMap.SimpleEntry<Type, SArray.Size>> ret = new ArrayList<>();

        ReturnParameters annotations = method.getAnnotation(ReturnParameters.class);
        SArray.Size[] arraySizeAnnotations =
            annotations == null ? new SArray.Size[]{} : annotations.value();

        for (int i = 0; i < returnTypes.length; i++) {
            SArray.Size size = null;
            if (arraySizeAnnotations.length > i) {
                size = arraySizeAnnotations[i];
                if (size.value().length == 0) {
                    size = null;
                }
            }
            ret.add(new AbstractMap.SimpleEntry<>(returnTypes[i], size));
        }
        return ret;
    }

    /**
     * Extract return types from a solidity element (function or event)
     * Ex: SolidityFunction<SUInt.SUInt256> return [SUInt.SUInt256]
     *
     * @return the return type of a solidity element (function or event).
     */
    Type[] extractReturnTypesFromElement() {
        Type returnType = method.getGenericReturnType();
        if (returnType instanceof ParameterizedType) {
            return ((ParameterizedType) returnType).getActualTypeArguments();
        } else { // It's a Class (no generic parameters, so no returns)
            return new Type[]{};
        }
    }

    protected String signature() {
        return Sha3.hash(this.fullName);
    }


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ReturnParameters {
        SArray.Size[] value() default {};
    }


}
