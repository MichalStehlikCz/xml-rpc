package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcValue;

import javax.annotation.Nonnull;
import java.util.Base64;

/**
 * Parser used to parse base64 encoded value
 */
class TypeParserBase64 extends TypeParserSimple<byte[]> {

    @Nonnull
    @Override
    public String getElement() {
        return XmlRpcValue.BASE64;
    }

    @Override
    byte[] convertValue(String value) {
        return Base64.getDecoder().decode(value);
    }
}
