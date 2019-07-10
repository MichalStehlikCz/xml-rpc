package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.XmlRpcValue;

class TypeSerializerDouble extends TypeSerializerSimple<Double> {

    @Override
    public Class<Double> getBaseClass() {
        return Double.class;
    }

    @Override
    protected String getElement() {
        return XmlRpcValue.DOUBLE;
    }

    @Override
    protected String convertValue(Double object) {
        return object.toString();
    }
}
