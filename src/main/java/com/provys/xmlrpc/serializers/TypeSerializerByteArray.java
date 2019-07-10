package com.provys.xmlrpc.serializers;

import java.util.Base64;

/**
 * Serialize byte[] as base64 value
 */
class TypeSerializerByteArray extends TypeSerializerSimple<byte[]> {

    @Override
    public Class<byte[]> getBaseClass() {
        return byte[].class;
    }

    @Override
    protected String getElement() {
        return "base64";
    }

    @Override
    protected String convertValue(byte[] object) {
        return Base64.getEncoder().encodeToString(object);
    }
}
