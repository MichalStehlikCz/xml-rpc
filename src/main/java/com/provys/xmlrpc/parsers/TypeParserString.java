package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcValue;

import javax.annotation.Nonnull;

/**
 * Parser used to parse string value
 */
class TypeParserString extends TypeParserSimple<String> {

    @Nonnull
    @Override
    public String getElement() {
        return XmlRpcValue.STRING;
    }

    @Override
    String convertValue(String value) {
        return value;
    }
}
