package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Ancestor, supporting parsers that identify value type by enclosing element and parse content of that element to
 * return value
 * @param <T> is type of object returned by parser
 */
abstract class TypeParserBase<T> implements TypeParser {

    /**
     * Read value from supplied reader.
     *
     * @param reader is reader, currently positioned on element specifying type; method has to move position to end
     *              element corresponding to this
     * @return effective value
     * @throws XMLStreamException if there is any problem retrieving data from stream
     */
    protected abstract T parseValue(XMLStreamReader reader) throws XMLStreamException;

    @Override
    public T parse(XMLStreamReader reader) throws XMLStreamException {
        if (!reader.isStartElement()) {
            throw new XmlRpcException("Parse " + getElement() + " - element expected");
        }
        if (!reader.getLocalName().equals(getElement())) {
            throw new XmlRpcException("Parse " + getElement() + " - incorrect element name " + reader.getLocalName());
        }
        T result = parseValue(reader);
        if (!reader.isEndElement()) {
            throw new XmlRpcException("Parse " + getElement() + " - end element expected");
        }
        if (!reader.getLocalName().equals(getElement())) {
            throw new XmlRpcException("Parse " + getElement() + " - incorrect end element name " +
                    reader.getLocalName());
        }
        return result;
    }
}
