package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcValue;

import javax.annotation.Nonnull;

/**
 * Parser used to parse double value
 */
class TypeParserDouble extends TypeParserSimple<Double> {

    @Nonnull
    @Override
    public String getElement() {
        return XmlRpcValue.DOUBLE;
    }

    @Override
    Double convertValue(String value) {
        return Double.parseDouble(value);
    }
}
