package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcException;
import com.provys.xmlrpc.XmlRpcValue;

import javax.annotation.Nonnull;

/**
 * Parser used to parse boolean value
 */
class TypeParserBoolean extends TypeParserSimple<Boolean> {

    @Nonnull
    @Override
    public String getElement() {
        return XmlRpcValue.BOOLEAN;
    }

    @Override
    Boolean convertValue(String value) {
        try {
            switch (Integer.parseInt(value)) {
                case 0:
                    return false;
                case 1:
                    return true;
                default:
                    throw new XmlRpcException("Parse Boolean - invalid value " + value);
            }
        } catch (NumberFormatException e) {
            throw new XmlRpcException("Parse Boolean - invalid value " + value, e);
        }
    }
}
