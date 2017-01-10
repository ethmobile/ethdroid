package ethereumjava.solidity;

import java.lang.reflect.Method;

import ethereumjava.module.Eth;
import ethereumjava.sha3.Sha3;

/**
 * Abstract class which designate Solidity contract elements like Function or Events
 * Created by gunicolas on 13/10/16.
 */
public abstract class SolidityElement {

    String address;
    Method method;
    String fullName;
    Eth eth;

    public SolidityElement(String address, Method method, Eth eth) {
        this.address = address;
        this.method = method;
        this.fullName = transformToFullName();
        this.eth = eth;
    }

    protected String transformToFullName() {
        StringBuilder sbStr = new StringBuilder();
        int i = 0;
        Class[] parameters = getParametersTypes();
        for (Class c : parameters) {
            sbStr.append(c.getSimpleName().substring(1).toLowerCase());
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
