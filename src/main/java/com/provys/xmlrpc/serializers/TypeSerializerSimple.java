package com.provys.xmlrpc.serializers;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Common ancestor for serializers that write element (corresponding to XML-RPC type) with text value representing
 * supplied object
 *
 * @param <T> is object type this serializer supports
 */
abstract class TypeSerializerSimple<T> extends TypeSerializerBase<T> {

    /**
     * Convert supplied object to string that will be written to XML stream as characters
     *
     * @param object to be written to stream
     * @return object converted to its XML-RPC string representation
     */
    protected abstract String convertValue(T object);

    @Override
    protected void serializeValue(XMLStreamWriter writer, T object) throws XMLStreamException {
        writer.writeCharacters(convertValue(object));
    }

}
