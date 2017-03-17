package ethereumjava.solidity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.module.Eth;
import ethereumjava.sha3.Sha3;
import ethereumjava.solidity.types.SArray;

/**
 * Abstract class which designate Solidity contract elements like Function or Events
 * Created by gunicolas on 13/10/16.
 */
abstract class SolidityElement {

    String address;
    Method method;
    String fullName;
    Eth eth;

    Map<Type,SArray.Size> parameters;
    Map<Type,SArray.Size> returns;


    SolidityElement(String address, Method method, Eth eth) {
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
     * @return signature of the given element.
     */
    private String transformToFullName() {
        StringBuilder sbStr = new StringBuilder();

        sbStr.append(method.getName());

        sbStr.append('(');

        for (Type parameter : parameters.keySet()) {

            Class clazz;
            SArray.Size arraySize = parameters.get(parameter);

            if (arraySize != null) { // It's an array
                clazz = SArray.getNestedType(parameter);
            } else { // It's a simple type
                clazz = (Class) parameter;
            }
            String arrayStr = SArray.sizeToString(arraySize);

            sbStr.append(clazz.getSimpleName().substring(1).toLowerCase());
            sbStr.append(arrayStr);
            sbStr.append(",");
        }
        if( parameters.size() > 0 ){
            sbStr.setLength(sbStr.length() - 1);
        }
        sbStr.append(')');

        return sbStr.toString();
    }

    protected abstract Map<Type,SArray.Size> getParametersType();

    protected Map<Type,SArray.Size> getReturnsType(){
        Type[] returnTypes = extractReturnTypesFromElement();
        Map<Type,SArray.Size> ret = new IdentityHashMap<>();

        ReturnParameters annotations = method.getAnnotation(ReturnParameters.class);
        SArray.Size[] arraySizeAnnotations = annotations == null ? new SArray.Size[]{} : annotations.value();

        for (int i = 0; i < returnTypes.length; i++) {
            SArray.Size arraySizeAnnotation = arraySizeAnnotations.length > i ? arraySizeAnnotations[i] : null;
            ret.put(returnTypes[i], arraySizeAnnotation);
        }
        return ret;
    }

    /**
     * Extract return types from a solidity element (function or event)
     * Ex: SolidityFunction<SUInt.SUInt256> return [SUInt.SUInt256]
     * @return the return type of a solidity element (function or event).
     */
    Type[] extractReturnTypesFromElement(){
        Type returnType = method.getGenericReturnType();
        if( returnType instanceof ParameterizedType){
            return ((ParameterizedType) returnType).getActualTypeArguments();
        } else{ // It's a Class (no generic parameters, so no returns)
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
