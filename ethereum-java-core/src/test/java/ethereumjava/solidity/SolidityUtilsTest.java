package ethereumjava.solidity;


import ethereumjava.sha3.Sha3;
import org.json.JSONObject;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by gunicolas on 11/08/16.
 */
public class SolidityUtilsTest {

    @Test
    public void testToBigDecimal() throws Exception {
        BigDecimal bd = SolidityUtils.toBigDecimal("0xffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        bd = bd.scaleByPowerOfTen(-(bd.precision() - 1));
        assertEquals("1.157920892373162e+77", bd.toPlainString());
    }

    @Test
    public void testHexToBigDecimal() throws Exception {
        assertEquals("-35984.32684", SolidityUtils.hexToBigDecimal("-0x8c90.53abc947064ece9a2c67").toPlainString());
        assertEquals("35984.32684", SolidityUtils.hexToBigDecimal("0x8c90.53abc947064ece9a2c67").toPlainString());
    }

    @Test
    public void testBigDecimalToHexString() throws Exception {

        String testsEquals[] = new String[]{
            "47ac2.cf1224631",
            "478a",
            "-478a",
            "-478a.457b74ca",
            "8c90.53abc947064ece9a2c67",
            "-fff54.f54fe5f"
        };

        for (String test : testsEquals) {
            BigDecimal testBD = SolidityUtils.hexToBigDecimal(test);
            assertEquals(test, SolidityUtils.bigDecimalToHexString(testBD));
        }


    }

    @Test
    public void testFromWei() throws Exception {
        String actual = SolidityUtils.fromWei("-0x65ddf8.354aaa354", "ether").toPlainString();
        assertEquals(new BigDecimal("-0.00000000000667596021").toPlainString(), actual);
    }

    @Test
    public void testToWei() throws Exception {
        assertEquals("0", SolidityUtils.toWei("-0x65ddf8.3554", "noether").toPlainString());

        assertEquals("-6675960.20831298828125", SolidityUtils.toWei("-0x65ddf8.3554", "wei").toPlainString());

        assertEquals("-6675960208.31298828125", SolidityUtils.toWei("-0x65ddf8.3554", "kwei").toPlainString());
        assertEquals("-6675960208.31298828125", SolidityUtils.toWei("-0x65ddf8.3554", "babbage").toPlainString());
        assertEquals("-6675960208.31298828125", SolidityUtils.toWei("-0x65ddf8.3554", "femtoether").toPlainString());

        assertEquals("-6675960208312.98828125", SolidityUtils.toWei("-0x65ddf8.3554", "mwei").toPlainString());
        assertEquals("-6675960208312.98828125", SolidityUtils.toWei("-0x65ddf8.3554", "lovelace").toPlainString());
        assertEquals("-6675960208312.98828125", SolidityUtils.toWei("-0x65ddf8.3554", "picoether").toPlainString());

        assertEquals("-6675960208312988.28125", SolidityUtils.toWei("-0x65ddf8.3554", "gwei").toPlainString());
        assertEquals("-6675960208312988.28125", SolidityUtils.toWei("-0x65ddf8.3554", "shannon").toPlainString());
        assertEquals("-6675960208312988.28125", SolidityUtils.toWei("-0x65ddf8.3554", "nanoether").toPlainString());
        assertEquals("-6675960208312988.28125", SolidityUtils.toWei("-0x65ddf8.3554", "nano").toPlainString());

        assertEquals("-6675960208312988281.25", SolidityUtils.toWei("-0x65ddf8.3554", "szabo").toPlainString());
        assertEquals("-6675960208312988281.25", SolidityUtils.toWei("-0x65ddf8.3554", "microether").toPlainString());
        assertEquals("-6675960208312988281.25", SolidityUtils.toWei("-0x65ddf8.3554", "micro").toPlainString());

        assertEquals("-6675960208312988281250", SolidityUtils.toWei("-0x65ddf8.3554", "finney").toPlainString());
        assertEquals("-6675960208312988281250", SolidityUtils.toWei("-0x65ddf8.3554", "milliether").toPlainString());
        assertEquals("-6675960208312988281250", SolidityUtils.toWei("-0x65ddf8.3554", "milli").toPlainString());

        assertEquals("-6675960208312988281250000", SolidityUtils.toWei("-0x65ddf8.3554", "ether").toPlainString());

        assertEquals("-6675960208312988281250000000", SolidityUtils.toWei("-0x65ddf8.3554", "kether").toPlainString());
        assertEquals("-6675960208312988281250000000", SolidityUtils.toWei("-0x65ddf8.3554", "grand").toPlainString());

        assertEquals("-6675960208312988281250000000000", SolidityUtils.toWei("-0x65ddf8.3554", "mether").toPlainString());

        assertEquals("-6675960208312988281250000000000000", SolidityUtils.toWei("-0x65ddf8.3554", "gether").toPlainString());

        assertEquals("-6675960208312988281250000000000000000", SolidityUtils.toWei("-0x65ddf8.3554", "tether").toPlainString());

    }

    @Test
    public void testIsStrictAddress() throws Exception {
        assertTrue(SolidityUtils.isStrictAddress("0x5a8c31fb4173e1704002217bccd0cb423703a227"));
        assertFalse(SolidityUtils.isStrictAddress("0xaae545836"));
    }

    @Test
    public void testIsChecksumAddress() throws Exception {
        assertFalse(SolidityUtils.isChecksumAddress("0x1c8aff950685c2ed4bc3174f3472287b56d9517b9c948127319a09a7a36deac8"));
        assertFalse(SolidityUtils.isChecksumAddress("0xf19e7d10a295c3e86bc7a78c4286ea8f8066f232"));
        assertTrue(SolidityUtils.isChecksumAddress("0xF19e7D10a295C3E86Bc7a78c4286EA8f8066F232"));
    }

    @Test
    public void testSha3() throws Exception {
        String sha3 = Sha3.hash("hello");
        assertEquals("1c8aff950685c2ed4bc3174f3472287b56d9517b9c948127319a09a7a36deac8", sha3);
    }

    @Test
    public void testToChecksumAddress() throws Exception {
        String checksumAddress = SolidityUtils.toChecksumAddress("0xf19e7d10a295c3e86bc7a78c4286ea8f8066f232");
        assertEquals("0xF19e7D10a295C3E86Bc7a78c4286EA8f8066F232", checksumAddress);
    }

    @Test
    public void testToHexBoolean() throws Exception {
        assertEquals("0x1", SolidityUtils.toHex(true));
        assertEquals("0x0", SolidityUtils.toHex(false));
        assertEquals("0x1", SolidityUtils.toHex(Boolean.valueOf(true)));
        assertEquals("0x0", SolidityUtils.toHex(Boolean.valueOf(false)));
    }

    @Test
    public void testToHexJSON() throws Exception {
        JSONObject json = new JSONObject("{a:\"b\",c:\"d\"}");
        assertEquals("0x7b2261223a2262222c2263223a2264227d", SolidityUtils.toHex(json));
    }

    @Test
    public void testToHexAsciiString() throws Exception {
        assertEquals("0x68656c6c6f20776f726c64", SolidityUtils.toHex("hello world"));
    }

    @Test
    public void testIsStringNumber() throws Exception {
        assertTrue(SolidityUtils.isStringNumber("+35135468"));
        assertTrue(SolidityUtils.isStringNumber("-35135468"));
        assertTrue(SolidityUtils.isStringNumber("35135468"));

        assertTrue(SolidityUtils.isStringNumber("+35135468.56468"));
        assertTrue(SolidityUtils.isStringNumber("-35135468.654"));
        assertTrue(SolidityUtils.isStringNumber("35135468.63546"));

        assertTrue(SolidityUtils.isStringNumber("+.56468"));
        assertTrue(SolidityUtils.isStringNumber("+56468."));
        assertTrue(SolidityUtils.isStringNumber("-35135468."));
        assertTrue(SolidityUtils.isStringNumber("-.654"));
    }
}
