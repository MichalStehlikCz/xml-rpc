package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.XmlRpcArray;
import com.provys.xmlrpc.XmlRpcValue;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Collection;
import java.util.Objects;

public class TypeSerializerCollection extends TypeSerializerBase<Collection> {

    /**
     * Value serializer used to serialize array elements
     */
    private final ValueSerializer valueSerializer;

    TypeSerializerCollection() {
        this.valueSerializer = ValueSerializer.getDefault();
    }

    TypeSerializerCollection(ValueSerializer valueSerializer) {
        this.valueSerializer = Objects.requireNonNull(valueSerializer);
    }

    @Override
    public Class<Collection> getBaseClass() {
        return Collection.class;
    }

    @Override
    protected String getElement() {
        return XmlRpcValue.ARRAY;
    }

    @Override
    protected void serializeValue(XMLStreamWriter writer, Collection objects) throws XMLStreamException {
        writer.writeStartElement(XmlRpcArray.DATA_TAG);
        for (Object object : objects) {
            valueSerializer.serialize(writer, object);
        }
        writer.writeEndElement();
    }
}
