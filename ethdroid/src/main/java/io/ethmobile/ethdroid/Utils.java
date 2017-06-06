package io.ethmobile.ethdroid;

import org.ethereum.geth.Hash;
import org.ethereum.geth.Transactions;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;

/**
 * Created by gunicolas on 22/08/16.
 */

/**
 * Utility methods
 */
public abstract class Utils {

/*    */

    /**
     * Check given method to extract it's return type or parametized type with the exact generic
     * type
     * (must be defined in parameters and method annotated with GenericTypeIndex ).
     * The return type can be the generic of an Observable.
     * <p>
     * Example :
     * <ul>
     * <li>Observable<String> --> String</li>
     * <li>Block<Hash> --> Block<Hash></li>
     * <li>Observable<Block<Transaction>> --> Block<Transaction></li>
     * </ul>
     *//*
    @SuppressWarnings("unchecked")
    static Type extractReturnType(Method m, Object[] args) {

        Type returnType = null;
        if (m.getReturnType().isAssignableFrom(Observable.class)) { // Case of Observable<Object>
            ParameterizedType returnParameterizedType = (ParameterizedType) m
            .getGenericReturnType();
            returnType = returnParameterizedType.getActualTypeArguments()[0]; // Extract Object type
            if (returnType instanceof ParameterizedType) { // If Object is Object<?>
                returnType = ((ParameterizedType) returnType).getRawType(); // Extract Object
                class type
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
            returnType = getType((Class<?>) returnType, genericType); //TODO Dangerous cast --
            check why getType can't take "Type" parameters
        }
        return returnType;
    }*/
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

/*    static String extractMethodName(Method method) {
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
                charArray[0] = Character.toLowerCase(charArray[0]);       // respect method name
                format
                methodName = new String(charArray);                       //------------------
                for (Method m : module.getMethods()) {
                    if (m.getName().equals(methodName)
                        && Arrays.equals(m.getParameterTypes(), method.getParameterTypes())) {
                        return extractMethodName(m);
                    }
                }
            }
        }
        throw new EthereumJavaException("InvocationHandler error: No Ethereum method Found for "
        + method.getName());
    }*/

    /**
     * Returns first occurence of annotationType in annotationArray or null if doesn't exist
     *
     * @param annotationArray the annotation array to test
     * @param annotationType  the annotation type to find
     * @param <T>             the annotation type
     * @return the first occurence of annotationType in annotationArray or null if doesn't exist
     */
    public static <T extends Annotation> T arrayContainsAnnotation(Annotation[] annotationArray,
        Class<T> annotationType) {
        for (Annotation annotation : annotationArray) {
            if (annotationType.isInstance(annotation)) {
                return (T) annotation;
            }
        }
        return null;
    }

    /**
     * Multiply the given value by values contained in the array
     * {@literal
     * Ex: (3,[1,2,3]) -> 3*1*2*3=18
     * }
     *
     * @param a     the base value
     * @param array an array of values to multiply by
     * @return the multiplication of the given value by values contained in the array
     */
    public static int multiplyByArrayValues(int a, int[] array) {
        int ret = a;
        for (int value : array) {
            ret *= value;
        }
        return ret;
    }

    /**
     * Test if the first parameter is a class or a subclass of the second parameter.
     *
     * @param toTest the (sub)class to test
     * @param clazz  the reference class
     * @return true if the first parameter is a class or a subclass of the second parameter.
     */
    public static boolean isClassOrSubclass(Class toTest, Class clazz) {
        try {
            toTest.asSubclass(clazz);
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public static void deleteDirIfExists(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDirIfExists(f);
            }
        }
        file.delete();
    }

    public static boolean transactionListContains(Transactions transactions, Hash txHash)
        throws Exception {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getHash().getHex().compareTo(txHash.getHex()) == 0) return true;
        }
        return false;
    }


}
