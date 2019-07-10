package com.provys.xmlrpc.serializers;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

interface TypeSerializer<T> {

    /**
     * Return base class this object supports. It can be used as fallback for more types, but this class is used to sort
     * serializers from more specific to less specific
     */
    Class<T> getBaseClass();

    /**
     * Identifies if this serializer supports serialization of objects given type
     *
     * @param object is object to be serialized
     * @return true if serializer can be used on given object, false otherwise
     */
    boolean canSerialize(Object object);

    /**
     * Serialize supplied value to stream.
     *
     * @param writer is xml stream writer object will be written to
     * @param object is object to be written to stream
     */
    void serialize(XMLStreamWriter writer, T object) throws XMLStreamException;
}
