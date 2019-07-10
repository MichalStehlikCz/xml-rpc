package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.XmlRpcValue;

import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.ArrayList;
import java.util.List;

class ValueSerializer {

    private static final ValueSerializer DEFAULT = new ValueSerializer();

    /**
     * Return default value serializer, supporting basic data types
     *
     * @return default serializer based on XML-RPC standard
     */
    static ValueSerializer getDefault() {
        return DEFAULT;
    }

    private final List<TypeSerializer<?>> typeSerializers;

    /**
     * Add new serializer. We try to put it before any existing serializer, that is capable of serializing its base
     * class
     *
     * @param typeSerializers is list of serializers; new one is added to this list
     * @param serializer is serializer to be added to list
     */
    private static void addSerializer(List<TypeSerializer<?>> typeSerializers, TypeSerializer serializer) {
        Class<?> baseClass = serializer.getBaseClass();
        for (int i = 0; i < typeSerializers.size(); i++) {
            if (typeSerializers.get(i).getBaseClass().isAssignableFrom(baseClass)) {
                typeSerializers.add(i, serializer);
                return;
            }
        }
        typeSerializers.add(serializer);
    }

    /**
     * Create default value serializer, supporting basic data types
     */
    private ValueSerializer() {
        typeSerializers = new ArrayList<>();
        var serializers = new TypeSerializer[]{new TypeSerializerInteger(), new TypeSerializerString(),
                new TypeSerializerBoolean(), new TypeSerializerDouble(), new TypeSerializerLocalDateTime(),
                new TypeSerializerByteArray(), new TypeSerializerCollection(this),
                new TypeSerializerMap(this), new TypeSerializerObjectArray(this),
                new TypeSerializerObject(this)};
        for (var serializer : serializers) {
            addSerializer(this.typeSerializers, serializer);
        }
    }

    /**
     * Create new value serializer utilizing specified list of type serializers.
     *
     * @param typeSerializers is list of type serializers to be used by this value serializer
     */
    ValueSerializer(List<TypeSerializer> typeSerializers) {
        this.typeSerializers = new ArrayList<>(typeSerializers.size());
        for (var typeSerializer : typeSerializers) {
            addSerializer(this.typeSerializers, typeSerializer);
        }
    }

    /**
     * Check if serializer supports values of given type and if so, serialize object.
     *
     * @param writer is writer where object should be serialized
     * @param typeSerializer is serializer that should try to serialize object
     * @param object is object to be serialized
     * @param <T> is type supported by serializer
     * @return if serialization has been successful
     * @throws XMLStreamException if there is any problem writing to XML stream
     */
    private <T> boolean serializeValue(XMLStreamWriter writer, TypeSerializer<T> typeSerializer, Object object)
            throws XMLStreamException {
        if (typeSerializer.canSerialize(object)) {
            typeSerializer.serialize(writer, typeSerializer.getBaseClass().cast(object));
            return true;
        }
        return false;
    }

    /**
     * Find serializer and serialize supplied value to given stream
     *
     * @param writer is writer serialized value should be written to
     * @param object is object to be serialized
     * @throws XMLStreamException if there is any problem writing to XML stream
     */
    private void serializeValue(XMLStreamWriter writer, Object object) throws XMLStreamException {
        for (var typeSerializer : typeSerializers) {
            if (serializeValue(writer, typeSerializer, object)) {
                return;
            }
        }
    }

    /**
     * Find serializer and serialize supplied object to given stream
     *
     * @param writer is writer serialized value should be written to
     * @param object is object to be serialized, potentially null
     * @throws XMLStreamException if there is any problem writing to XML stream
     */
    void serialize(XMLStreamWriter writer, @Nullable Object object) throws XMLStreamException {
        writer.writeStartElement(XmlRpcValue.TAG);
        if (object != null) {
            serializeValue(writer, object);
        }
        writer.writeEndElement();
    }
}
