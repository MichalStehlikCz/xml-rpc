package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.XmlRpcValue;

/**
 * Serializer writing Integer values as i4
 */
class TypeSerializerInteger extends TypeSerializerSimple<Integer> {

    @Override
    public Class<Integer> getBaseClass() {
        return Integer.class;
    }

    @Override
    protected String getElement() {
        return XmlRpcValue.I4;
    }

    @Override
    protected String convertValue(Integer object) {
        return object.toString();
    }
}
