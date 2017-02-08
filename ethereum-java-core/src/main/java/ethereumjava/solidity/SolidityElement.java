package ethereumjava.solidity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.module.Eth;
import ethereumjava.sha3.Sha3;
import ethereumjava.solidity.types.SArray;

import static ethereumjava.Utils.arrayContainsAnnotation;

/**
 * Abstract class which designate Solidity contract elements like Function or Events
 * Created by gunicolas on 13/10/16.
 */
abstract class SolidityElement {

    String address;
    Method method;
    String fullName;
    Eth eth;

    SolidityElement(String address, Method method, Eth eth) {
        this.address = address;
        this.method = method;
        this.fullName = transformToFullName();
        this.eth = eth;
    }

    private String transformToFullName() {
        StringBuilder sbStr = new StringBuilder();
        int i = 0;
        Class[] parameters = getParametersTypes();
        for (Class c : parameters) {
            String name;
            if( c.isAssignableFrom(SArray.class)){
                Annotation[] annotations = method.getParameterAnnotations()[i];
                SArray.Type annotation = arrayContainsAnnotation(annotations,SArray.Type.class);
                if( annotation != null ) {
                    name = annotation.value();
                }else{
                    throw new EthereumJavaException("Error in definition of "+method.getName()+". Missing SArray type annotation.");
                }
            } else{
                name = c.getSimpleName().substring(1).toLowerCase();
            }
            sbStr.append(name);
            if (i < parameters.length - 1) {
                sbStr.append(",");
            }
            i++;
        }
        return method.getName() + '(' + sbStr.toString() + ')';
    }

    protected abstract Class[] getParametersTypes();


    protected String signature() {
        return Sha3.hash(this.fullName);
    }
}
