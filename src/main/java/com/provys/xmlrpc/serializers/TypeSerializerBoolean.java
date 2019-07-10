package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.XmlRpcValue;

public class TypeSerializerBoolean extends TypeSerializerSimple<Boolean> {

    @Override
    public Class<Boolean> getBaseClass() {
        return Boolean.class;
    }

    @Override
    protected String getElement() {
        return XmlRpcValue.BOOLEAN;
    }

    @Override
    protected String convertValue(Boolean object) {
        return object ? "1" : "0";
    }
}
