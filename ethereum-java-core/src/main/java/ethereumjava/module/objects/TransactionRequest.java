package ethereumjava.module.objects;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;

import ethereumjava.solidity.SolidityUtils;

/**
 * Created by gunicolas on 30/08/16.
 */
public class TransactionRequest {

    String fromHex;
    String toHex;
    String gasHex;

    String valueHex;
    String dataHex;

    public TransactionRequest(String to) {
        this(null, to, null, null, null);
    }

    public TransactionRequest(String fromHex, String toHex) {
        this(fromHex, toHex, null, null);
    }

    public TransactionRequest(String from, String to, String value, String data) {
        this(from, to, new BigInteger("90000"), value, data);
    }

    public TransactionRequest(String from, String to, BigInteger gas, String value, String data) throws IllegalArgumentException {
        this.fromHex = SolidityUtils.toHex(from);
        this.toHex = SolidityUtils.toHex(to);
        if (gas == null) this.gasHex = null;
        else this.gasHex = SolidityUtils.toHex(new BigDecimal(gas));
        this.valueHex = SolidityUtils.toHex(value);
        this.dataHex = SolidityUtils.toHex(data);
    }

    public void setDataHex(String dataHex) {
        this.dataHex = dataHex;
    }

    public void setToHex(String toHex) {
        this.toHex = toHex;
    }

    public void setGas(BigInteger gas) {
        this.gasHex = SolidityUtils.toHex(new BigDecimal(gas));
    }

    public void setValueHex(BigInteger valueHex) {
        this.valueHex = SolidityUtils.toHex(new BigDecimal(valueHex));
    }

    @Override
    public String toString() {

        JSONObject jsonObject = new JSONObject();

        if (fromHex != null) jsonObject.put("from", fromHex);
        if (toHex != null) jsonObject.put("to", toHex);
        if (gasHex != null) jsonObject.put("gas", gasHex);
        if (valueHex != null) jsonObject.put("value", valueHex);
        if (dataHex != null) jsonObject.put("data", dataHex);

        return jsonObject.toString();

    }
}
