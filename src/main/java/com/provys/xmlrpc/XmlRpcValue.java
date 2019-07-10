package com.provys.xmlrpc;

public class XmlRpcValue {

    /** value element */
    public static final String TAG = "value";

    /** i4 element (Integer) */
    public static final String I4 = "i4";
    /** int element (Integer) */
    public static final String INT = "int";
    /** boolean element (Boolean) */
    public static final String BOOLEAN = "boolean";
    /** string element (String) */
    public static final String STRING = "string";
    /** double element (Double) */
    public static final String DOUBLE = "double";
    /** date element (LocalDate) */
    public static final String DATE = "dateTime.iso8601";
    /** base64 element (Byte[]) */
    public static final String BASE64 = "base64";
    /** struct element (XmlRpcStruct - {@code Map<String, Object>}) */
    public static final String STRUCT = "struct";
    /** array element (XmlRpcArray - {@code List<Object>}) */
    public static final String ARRAY = "array";

    /** we do not want to create instances */
    private XmlRpcValue() {}
}
