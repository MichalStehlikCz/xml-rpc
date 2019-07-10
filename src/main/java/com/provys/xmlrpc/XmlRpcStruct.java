package com.provys.xmlrpc;

import java.util.HashMap;
import java.util.Map;

public class XmlRpcStruct extends HashMap<String, Object> {

    public static final String MEMBER_TAG = "member";
    public static final String NAME_TAG = "name";
    public static final String VALUE_TAG = XmlRpcValue.TAG;

    public XmlRpcStruct() {
        super();
    }

    public XmlRpcStruct(Map<String, Object> map) {
        super(map);
    }
}
