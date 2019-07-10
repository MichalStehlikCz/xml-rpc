package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcValue;

import javax.annotation.Nonnull;

/**
 * Parser used to parse i4 integer value
 */
class TypeParserI4 extends TypeParserSimple<Integer> {

    @Nonnull
    @Override
    public String getElement() {
        return XmlRpcValue.I4;
    }

    @Override
    Integer convertValue(String value) {
        return Integer.parseInt(value);
    }
}
