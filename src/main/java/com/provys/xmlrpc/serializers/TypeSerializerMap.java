package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.XmlRpcStruct;
import com.provys.xmlrpc.XmlRpcValue;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Map;
import java.util.Objects;

class TypeSerializerMap extends TypeSerializerBase<Map> {

    /**
     * Value serializer used to serialize array elements
     */
    private final ValueSerializer valueSerializer;

    TypeSerializerMap() {
        this.valueSerializer = ValueSerializer.getDefault();
    }

    TypeSerializerMap(ValueSerializer valueSerializer) {
        this.valueSerializer = Objects.requireNonNull(valueSerializer);
    }

    @Override
    public Class<Map> getBaseClass() {
        return Map.class;
    }

    @Override
    protected String getElement() {
        return XmlRpcValue.STRUCT;
    }

    @Override
    @SuppressWarnings("squid:S2864") // we cannot use iteration on entrySet because we use map with no type parameters,
                                     // which erases type of resulting set...
    protected void serializeValue(XMLStreamWriter writer, Map map) throws XMLStreamException {
        for (Object key : map.keySet()) {
            writer.writeStartElement(XmlRpcStruct.MEMBER_TAG);
            writer.writeStartElement(XmlRpcStruct.NAME_TAG);
            writer.writeCharacters(key.toString());
            writer.writeEndElement();
            valueSerializer.serialize(writer, map.get(key));
            writer.writeEndElement();
        }
    }
}
