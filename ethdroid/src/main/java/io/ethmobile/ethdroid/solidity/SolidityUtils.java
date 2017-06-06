package io.ethmobile.ethdroid.solidity;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.regex.Pattern;

import io.ethmobile.ethdroid.sha3.Sha3;


/**
 * Created by gunicolas on 08/08/16.
 */
public abstract class SolidityUtils {

    public static final HashMap<String, BigDecimal> UNIT = new HashMap<String, BigDecimal>() {{
        put("noether", new BigDecimal(0));
        put("wei", new BigDecimal(1));
        put("kwei", new BigDecimal("1000"));
        put("babbage", new BigDecimal("1000"));
        put("femtoether", new BigDecimal("1000"));
        put("mwei", new BigDecimal("1000000"));
        put("lovelace", new BigDecimal("1000000"));
        put("picoether", new BigDecimal("1000000"));
        put("gwei", new BigDecimal("1000000000"));
        put("shannon", new BigDecimal("1000000000"));
        put("nanoether", new BigDecimal("1000000000"));
        put("nano", new BigDecimal("1000000000"));
        put("szabo", new BigDecimal("1000000000000"));
        put("microether", new BigDecimal("1000000000000"));
        put("micro", new BigDecimal("1000000000000"));
        put("finney", new BigDecimal("1000000000000000"));
        put("milliether", new BigDecimal("1000000000000000"));
        put("milli", new BigDecimal("1000000000000000"));
        put("ether", new BigDecimal("1000000000000000000"));
        put("kether", new BigDecimal("1000000000000000000000"));
        put("grand", new BigDecimal("1000000000000000000000"));
        put("mether", new BigDecimal("1000000000000000000000000"));
        put("gether", new BigDecimal("1000000000000000000000000000"));
        put("tether", new BigDecimal("1000000000000000000000000000000"));
    }};


    public static String padLeft(String toPad, int nbChars, char sign) {
        return new String(new char[nbChars - toPad.length()]).replace('\0', sign) + toPad;
    }

    public static String padLeftWithZeros(String toPad, int nbChars) {
        return padLeft(toPad, nbChars, '0');
    }

    public static String padRight(String toPad, int nbChars, char sign) {
        return toPad + new String(new char[nbChars - toPad.length()]).replace('\0', sign);
    }

    public static String padRightWithZeros(String toPad, int nbChars) {
        return padRight(toPad, nbChars, '0');
    }

    public static String removeHexIdentifier(String hex) {
        int index = hex.indexOf("0x");
        if (index == 0) {
            return hex.substring(index + 2);
        }
        return hex;
    }

    public static byte hexStringToByte(String data) {
        return (byte) ((Character.digit(data.charAt(0), 16) << 4)
            + Character.digit(data.charAt(1), 16));
    }

    public static byte[] hexStringToBytes(String bytes) {
        String hexNoId = removeHexIdentifier(bytes);

        int length = hexNoId.length() / 2;

        byte byteArray[] = new byte[length];

        for (int i = 0; i < hexNoId.length() - 1; i += 2) {
            //grab the hex in pairs
            String output = hexNoId.substring(i, (i + 2));
            //convert the decimal to character
            byteArray[i / 2] = hexStringToByte(output);
        }
        return byteArray;
    }

    public static String hexToUtf8(String hex) {
        byte bytes[] = hexStringToBytes(hex);
        return new String(bytes, Charset.forName("UTF-8"));
    }

    public static String hexToAscii(String hex) {
        StringBuilder sb = new StringBuilder();

        String hexNoId = removeHexIdentifier(hex);

        for (int i = 0; i < hexNoId.length() - 1; i += 2) {
            //grab the hex in pairs
            String output = hexNoId.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);
        }

        return sb.toString();


    }

    public static String utf8ToHex(String utf8) {
        byte bytes[] = utf8.getBytes(Charset.forName("UTF-8"));
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return "0x" + sb.toString();
    }

    public static String asciiToHex(String ascii) {
        byte bytes[] = ascii.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return "0x" + sb.toString();
    }

    public static String extractDisplayName(String name) {
        int index = name.indexOf('(');
        if (index != -1) {
            return name.substring(0, index);
        } else {
            return name;
        }
    }

    public static String extractTypeName(String name) {
        int index = name.indexOf('(');
        if (index != -1) {
            return name.substring(index + 1, name.length() - 1 - (index + 1)).replace(" ", "");
        } else {
            return "";
        }
    }

    public static String extractSolidityTypeName(Object argument) {
        return argument.getClass().getSimpleName().substring(1).toLowerCase();
    }

    /* Format "-6564.1654" to BigDecimal object */
    public static BigDecimal stringNumberToBigDecimal(String number) {
        if (!isStringNumber(number)) return null;
        int floatIndex = number.indexOf('.');
        if (floatIndex != -1) {
            // Add Exponent Indicator
            number = number.replace(".", "");
            number += "E" + (floatIndex - number.length());
        }
        return new BigDecimal(number);
    }

    /*
    * Get BigDecimal from hexadecimal string
    * based on DAICONRAD solution : https://github.com/daiconrad/bighex
    */
    public static BigDecimal hexToBigDecimal(String hex) throws IllegalArgumentException {
        // handle leading sign
        BigDecimal sign = null;
        if (hex.startsWith("-")) {
            hex = hex.substring(1);
            sign = new BigDecimal(-1);
        } else if (hex.startsWith("+")) {
            hex = hex.substring(1);
        }

        // constant must start with 0x or 0X
        if (!(hex.startsWith("0x") || hex.startsWith("0X"))) {
            throw new IllegalArgumentException(
                "not a hexadecimal floating point constant");
        }
        hex = hex.substring(2);

        // find the hexadecimal point, if any
        int hexadecimalPoint = hex.indexOf(".");
        int hexadecimalPlaces = 0;
        if (hexadecimalPoint >= 0) {
            hexadecimalPlaces = hex.length() - 1 - hexadecimalPoint;
            hex = hex.replace(".", "");
        }

        // reduce the exponent by 4 for every hexadecimal place
        int binaryExponent = -(hexadecimalPlaces * 4);
        boolean positive = true;
        if (binaryExponent < 0) {
            binaryExponent = -binaryExponent;
            positive = false;
        }

        BigDecimal base = new BigDecimal(new BigInteger(hex, 16));
        BigDecimal factor = new BigDecimal(BigInteger.valueOf(2).pow(binaryExponent));
        BigDecimal value = positive ? base.multiply(factor) : base.divide(factor);
        if (sign != null) {
            value = value.multiply(sign);
        }

        //return formatDecimalNumber(value);
        return value;
    }

    /* Format String number or hexadecimal to BigDecimal object */
    public static BigDecimal toBigDecimal(String value) {
        if (value.contains("0x")) {
            return hexToBigDecimal(value);
        } else {
            return stringNumberToBigDecimal(value);
        }
    }

    public static String toDecimal(String value) {
        return toBigDecimal(value).toPlainString();
    }

    public static String bigDecimalToHexString(String value) {
        BigDecimal decimal = toBigDecimal(value);
        return bigDecimalToHexString(decimal);
    }

    /*
    * Get Hexadecimal string from bigDecimal String (no 0x identifier)
    * By M.PLOUHINEC
     */
    public static String bigDecimalToHexString(BigDecimal value) {

        // Déclaration de variables locales
        BigDecimal factor;
        BigDecimal base;
        StringBuilder l_result = new StringBuilder();
        int mod4;
        int scale;
        // gestion du scale
        mod4 = value.scale() % 4;

        if (mod4 == 0) {
            scale = value.scale();
        } else {
            scale = value.scale() + (4 - mod4);
        }

        factor = new BigDecimal(BigInteger.valueOf(2).pow(scale));

        if (value.signum() >= 0) {
            base = value.multiply(factor);
        } else {
            base = value.multiply(factor).multiply(BigDecimal.valueOf(-1));
        }

        BigInteger bi = base.toBigInteger();
        String sTemp = bi.toString(16);

        // Gestion du signe
        if (value.signum() == -1) {
            l_result.append("-");
            sTemp.replace("-", "");
        }

        // Gestion de la partie entière
        int nbDecimales = scale / 4;

        l_result.append(sTemp.substring(0, sTemp.length() - nbDecimales));

        // Gestion de la partie décimale
        if (nbDecimales > 0) {
            l_result.append(".");
            l_result.append(sTemp.substring(sTemp.length() - nbDecimales, sTemp.length()));
        }

        return l_result.toString();
    }

    /* format to hexadecimal the given objet. If the object is null or "empty", it returns null */
    public static String toHex(Object obj) throws IllegalArgumentException {
        if (obj == null) {
            return null;
        }
        if (isBoolean(obj)) {
            boolean b = (boolean) obj;
            if (b) {
                return "0x1";
            } else {
                return "0x0";
            }
        } else if (isBigDecimal(obj)) {
            BigDecimal bd = (BigDecimal) obj;
            return "0x" + bigDecimalToHexString(bd);
        } else if (isJsonObject(obj)) {
            JsonObject json = (JsonObject) obj;
            return utf8ToHex(json.toString());
        } else if (isString(obj)) {
            String s = (String) obj;
            if (s.length() == 0) {
                return null;
            }
            if (s.contains("0x")) {
                return s;
            } else if (isStringNumber(s)) {
                return "0x" + bigDecimalToHexString(s);
            } else {
                return asciiToHex(s);
            }
        }
        throw new IllegalArgumentException(obj.toString() + " is a wrong type");
    }

    public static BigDecimal getValueOfUnit(String unit) {
        return UNIT.get(unit.toLowerCase());
    }

    public static BigDecimal fromWei(String number, String unit) throws IllegalArgumentException {
        BigDecimal numberBD = toBigDecimal(number);
        if (numberBD != null) {
            BigDecimal unitBD = getValueOfUnit(unit);
            if (unitBD != null) {
                return numberBD.divide(unitBD);
            }
            throw new IllegalArgumentException(unit + " is not a valid unit");
        }
        throw new IllegalArgumentException(number + " is not a valid number");
    }

    public static BigDecimal toWei(String number, String unit) throws IllegalArgumentException {
        BigDecimal numberBD = toBigDecimal(number);
        if (numberBD != null) {
            BigDecimal unitBD = getValueOfUnit(unit);
            if (unitBD != null) {
                return numberBD.multiply(unitBD);
            }
            throw new IllegalArgumentException(unit + " is not a valid unit");
        }

        throw new IllegalArgumentException(number + " is not a valid number");
    }

    public static BigDecimal toTwosComplement(String value) {
        BigDecimal valueBD = toBigDecimal(value);
        if (valueBD.signum() == -1) {
            BigInteger complement = new BigInteger(
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", 16);
            return valueBD.add(new BigDecimal(complement)).add(new BigDecimal(1));
        }
        return valueBD;
    }

    public static boolean isStrictAddress(String value) {
        Pattern p = Pattern.compile("^0x[0-9a-f]{40}$", Pattern.CASE_INSENSITIVE);
        return p.matcher(value).matches();
    }

    public static boolean isAddress(String value) {
        Pattern requirementsPattern = Pattern.compile("^(0x)?[0-9a-f]{40}$",
            Pattern.CASE_INSENSITIVE);
        boolean requirements = requirementsPattern.matcher(value).matches();
        if (!requirements) {
            return false; //
        }
        Pattern allCaps = Pattern.compile("^(0x)?[0-9A-F]{40}$");
        boolean isallCaps = allCaps.matcher(value).matches();
        Pattern allLowers = Pattern.compile("^(0x)?[0-9a-f]{40}$");
        boolean isallLowers = allLowers.matcher(value).matches();
        if (isallCaps || isallLowers) {
            return true;
        }
        return isChecksumAddress(value);
    }

    public static boolean isChecksumAddress(String value) {
        if (value == null) return false;
        value = value.replace("0x", "");
        String hash = Sha3.hash(value.toLowerCase());
        for (int i = 0; i < 40; i++) {
            int hashI = Integer.parseInt(String.valueOf(hash.charAt(i)), 16);
            String valueI = String.valueOf(value.charAt(i));

            if ((hashI > 7 && valueI.toUpperCase() != valueI) ||
                (hashI <= 7 && valueI.toLowerCase() != valueI)) {
                return false;
            }
        }
        return true;
    }

    public static String toChecksumAddress(String address) {
        if (address == null) return "";
        address = address.toLowerCase().replace("0x", "");
        String hash = Sha3.hash(address);
        String checkSumAddress = "0x";
        for (int i = 0; i < address.length(); i++) {
            int hashI = Integer.parseInt(String.valueOf(hash.charAt(i)), 16);
            String addressI = String.valueOf(address.charAt(i));
            if (hashI > 7) {
                checkSumAddress += addressI.toUpperCase();
            } else {
                checkSumAddress += addressI;
            }
        }
        return checkSumAddress;
    }

    public static String toAddress(String value) {
        if (isStrictAddress(value)) {
            return value;
        }
        Pattern p = Pattern.compile("^[0-9a-f]{40}$");
        boolean isAddress = p.matcher(value).matches();
        if (isAddress) {
            return "0x" + value;
        }
        return "0x";//TODO + padLeft(toHex(address).substr(2), 40);
    }

    public static boolean isBigDecimal(Object obj) {
        return obj instanceof BigDecimal;
    }

    public static boolean isString(Object obj) {
        return obj instanceof String;
    }

    public static boolean isBoolean(Object obj) {
        return obj instanceof Boolean;
    }

    public static boolean isJsonArray(Object obj) {
        return obj instanceof JsonArray;
    }

    public static boolean isJsonObject(Object obj) {
        return obj instanceof JsonObject;
    }

    public static boolean isJsonString(String value) {
        try {
            new JsonPrimitive(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isStringNumber(String value) {
        Pattern integer = Pattern.compile("^(-|\\+)?[0-9]+$");
        Pattern decimal = Pattern.compile("^(-|\\+)?[0-9]*\\.[0-9]+$");
        boolean isInteger = integer.matcher(value).matches();
        boolean isDecimal = decimal.matcher(value).matches();
        return isInteger || isDecimal;
    }

    public static boolean isHexNegative(String value) {
        BigDecimal valueBD = hexToBigDecimal(value.substring(0, 1));
        String bigInteger = valueBD.toBigIntegerExact().toString(2);
        return bigInteger.substring(0, 1).compareTo("1") == 0;
    }


}
