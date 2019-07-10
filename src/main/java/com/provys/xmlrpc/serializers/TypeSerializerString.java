package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.XmlRpcValue;

/**
 * Serializer for String objects
 */
class TypeSerializerString extends TypeSerializerSimple<String> {

    @Override
    public Class<String> getBaseClass() {
        return String.class;
    }

    @Override
    protected String getElement() {
        return XmlRpcValue.STRING;
    }

    @Override
    protected String convertValue(String object) {
        return object;
    }
}
