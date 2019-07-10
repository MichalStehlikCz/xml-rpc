package com.provys.xmlrpc.parsers;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Common ancestor for parsers that parse element text and convert it to return type.
 *
 * @param <T> is return (value) type of parser
 */
abstract class TypeParserSimple<T> extends TypeParserBase<T> {

    /**
     * Convert supplied value to type returned by parser.
     *
     * @param value is element value
     * @return value converted to required return type
     */
    abstract T convertValue(String value);

    @Override
    protected T parseValue(XMLStreamReader reader) throws XMLStreamException {
        return convertValue(reader.getElementText());
    }
}
