package ethereumjava;

import ethereumjava.exception.EthereumJavaException;
import ethereumjava.module.annotation.EthereumMethod;
import ethereumjava.module.annotation.GenericTypeIndex;
import rx.Observable;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by gunicolas on 22/08/16.
 */

/**
 * Utility methods
 */
abstract class Utils {

    /**
     * Check given method to extract it's return type or parametized type with the exact generic type
     * (must be defined in parameters and method annotated with GenericTypeIndex ).
     * The return type can be the generic of an Observable.
     * <p>
     * Example :
     * <ul>
     * <li>Observable<String> --> String</li>
     * <li>Block<Hash> --> Block<Hash></li>
     * <li>Observable<Block<Transaction>> --> Block<Transaction></li>
     * </ul>
     */
    @SuppressWarnings("unchecked")
    static Type extractReturnType(Method m, Object[] args) {

        Type returnType = null;
        if (m.getReturnType().isAssignableFrom(Observable.class)) { // Case of Observable<Object>
            ParameterizedType returnParameterizedType = (ParameterizedType) m.getGenericReturnType();
            returnType = returnParameterizedType.getActualTypeArguments()[0]; // Extract Object type
            if (returnType instanceof ParameterizedType) { // If Object is Object<?>
                returnType = ((ParameterizedType) returnType).getRawType(); // Extract Object class type
            }
        } else {
            returnType = m.getReturnType();
        }

        GenericTypeIndex annotation = m.getAnnotation(GenericTypeIndex.class);
        if (annotation != null) {
            int index = annotation.value();
            if (index < 0 || index >= args.length) {
                throw new EthereumJavaException("GenericTypeIndex value out of bounds");
            }
            Class genericType = (Class) args[index];
            returnType = getType((Class<?>) returnType, genericType); //TODO Dangerous cast -- check why getType can't take "Type" parameters
        }
        return returnType;
    }

    static Type getType(final Class<?> rawClass, final Class<?> parameter) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{parameter};
            }

            @Override
            public Type getRawType() {
                return rawClass;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    static String formatArgsToString(Object[] args) {

        if (args == null) {
            return "[]";
        }

        StringBuilder stringBuilder = new StringBuilder("[");

        for (Object arg : args) {
            stringBuilder.append(formatArgToString(arg));
            stringBuilder.append(",");
        }
        int lastIndexOf = stringBuilder.lastIndexOf(",");
        stringBuilder.replace(lastIndexOf, lastIndexOf + 1, "]");

        return stringBuilder.toString();

    }

    static String formatArgToString(Object arg) {
        if (arg == null) {
            return "\"\"";
        }
        if (arg instanceof String) {
            return "\"" + arg + "\"";
        } else if (arg instanceof BigInteger) {
            return "\"0x" + ((BigInteger) arg).toString(16) + "\"";
        }
        return String.valueOf(arg);
    }

    static String extractMethodName(Method method) {
        EthereumMethod annotation = method.getAnnotation(EthereumMethod.class);
        if (annotation != null) {
            return annotation.name();
        } else {
            String methodName = method.getName();
            int getIdx = methodName.indexOf("get");
            if (getIdx == -1) {
                return methodName;
            } else {
                Class module = method.getDeclaringClass();
                methodName = methodName.substring(3, methodName.length()); // Remove get keyword
                char charArray[] = methodName.toCharArray();              //------------------
                charArray[0] = Character.toLowerCase(charArray[0]);       // respect method name format
                methodName = new String(charArray);                       //------------------
                for (Method m : module.getMethods()) {
                    if (m.getName().equals(methodName)
                        && Arrays.equals(m.getParameterTypes(), method.getParameterTypes())) {
                        return extractMethodName(m);
                    }
                }
            }
        }
        throw new EthereumJavaException("InvocationHandler error: No Ethereum method Found for " + method.getName());
    }
}
