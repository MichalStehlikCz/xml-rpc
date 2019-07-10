package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.XmlRpcArray;
import com.provys.xmlrpc.XmlRpcValue;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Objects;

class TypeSerializerObjectArray extends TypeSerializerBase<Object[]> {

    /**
     * Value serializer used to serialize array elements
     */
    private final ValueSerializer valueSerializer;

    TypeSerializerObjectArray() {
        this.valueSerializer = ValueSerializer.getDefault();
    }

    TypeSerializerObjectArray(ValueSerializer valueSerializer) {
        this.valueSerializer = Objects.requireNonNull(valueSerializer);
    }

    @Override
    public Class<Object[]> getBaseClass() {
        return Object[].class;
    }

    @Override
    protected String getElement() {
        return XmlRpcValue.ARRAY;
    }

    @Override
    protected void serializeValue(XMLStreamWriter writer, Object[] objects) throws XMLStreamException {
        writer.writeStartElement(XmlRpcArray.DATA_TAG);
        for (Object object : objects) {
            valueSerializer.serialize(writer, object);
        }
        writer.writeEndElement();
    }
}
