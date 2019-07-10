package com.provys.xmlrpc.serializers;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

abstract class TypeSerializerBase<T> implements TypeSerializer<T> {

    @Override
    public boolean canSerialize(Object object) {
        return getBaseClass().isInstance(object);
    }

    /**
     * @return element tag enclosing value of this type
     */
    abstract protected String getElement();

    /**
     * Serialize supplied value
     *
     * @param writer is xml stream writer object will be written to
     * @param object is object to be written to stream
     * @throws XMLStreamException if there is any problem writing to stream
     */
    abstract protected void serializeValue(XMLStreamWriter writer, T object) throws XMLStreamException;

    @Override
    public void serialize(XMLStreamWriter writer, T object) throws XMLStreamException {
        writer.writeStartElement(getElement());
        serializeValue(writer, getBaseClass().cast(object));
        writer.writeEndElement();
    }
}
