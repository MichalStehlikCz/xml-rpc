package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcValue;

import javax.annotation.Nonnull;

/**
 * Parser used to parse int integer value
 */
class TypeParserInt extends TypeParserSimple<Integer> {

    @Nonnull
    @Override
    public String getElement() {
        return XmlRpcValue.INT;
    }

    @Override
    Integer convertValue(String value) {
        return Integer.parseInt(value);
    }
}
