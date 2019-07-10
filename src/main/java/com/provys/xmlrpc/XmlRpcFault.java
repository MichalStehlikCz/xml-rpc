package com.provys.xmlrpc;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class XmlRpcFault implements XmlRpcResponseBase {

    /** Name of faultCode element in response struct */
    private static final String CODE_ELEM = "faultCode";
    /** Name of faultString element in response struct */
    private static final String STRING_ELEM = "faultString";

    /** Fault code as defined in XML-RPC spec */
    private final int faultCode;
    @Nonnull
    private final String faultString;

    /**
     * Create new XML fault from supplied fault code and string
     *
     * @param faultCode is fault code returned by call
     * @param faultString is fault message returned by call
     */
    public XmlRpcFault(int faultCode, String faultString) {
        this.faultCode = faultCode;
        this.faultString = Objects.requireNonNull(faultString);
    }

    /**
     * Create new XML fault based on supplied Map, containing Integer value faultCode and String value faultString.
     * Throws exceptions if either of keys is missing or if their data-types do not match expectations
     *
     * @param faultStruct is map containing faultCode and faultString returned by call
     */
    public XmlRpcFault(Map<String, Object> faultStruct) {
        if (Objects.requireNonNull(faultStruct).size() != 2) {
            throw new IllegalArgumentException("Fault struct must contain exactly 2 elements");
        }
        var code = faultStruct.get(CODE_ELEM);
        if (code == null) {
            throw new IllegalArgumentException("Fault struct does not contain " + CODE_ELEM + " element");
        }
        if (!(code instanceof Integer)) {
            throw new IllegalArgumentException(CODE_ELEM + " element is not Integer");
        }
        this.faultCode = (Integer) code;
        var string = faultStruct.get(STRING_ELEM);
        if (string == null) {
            throw new IllegalArgumentException("Fault struct does not contain " + STRING_ELEM + " element");
        }
        if (!(string instanceof String)) {
            throw new IllegalArgumentException(CODE_ELEM + " element is not Integer");
        }
        this.faultString = (String) string;
    }

    /**
     * @return code of given fault
     */
    int getFaultCode() {
        return faultCode;
    }

    /**
     * @return message associated with given fault
     */
    @Nonnull
    String getFaultString() {
        return faultString;
    }

    /**
     * @return Map corresponding to given fault
     */
    @Nonnull
    Map<String, Object> getStruct() {
        var result = new HashMap<String, Object>(2);
        result.put(CODE_ELEM, faultCode);
        result.put(STRING_ELEM, faultString);
        return result;
    }

    /**
     * Return exception, corresponding to given Xml Rpc fault result
     */
    @Nonnull
    XmlRpcFaultException getException() {
        return new XmlRpcFaultException(faultCode, faultString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlRpcFault)) return false;

        XmlRpcFault that = (XmlRpcFault) o;

        if (getFaultCode() != that.getFaultCode()) return false;
        return getFaultString().equals(that.getFaultString());
    }

    @Override
    public int hashCode() {
        int result = getFaultCode();
        result = 31 * result + getFaultString().hashCode();
        return result;
    }

    @Override
    @Nonnull
    public String toString() {
        return "XmlRpcFault{" +
                "faultCode=" + faultCode +
                ", faultString='" + faultString + '\'' +
                '}';
    }
}
