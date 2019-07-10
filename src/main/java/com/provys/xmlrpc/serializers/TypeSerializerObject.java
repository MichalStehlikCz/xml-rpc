package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.XmlRpcException;
import com.provys.xmlrpc.XmlRpcStruct;
import com.provys.xmlrpc.XmlRpcValue;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * Serializer uses reflection to retrieve all supplied object's properties, and if they are public or have public
 * getter, they are serialized into struct. It would be probably easy to add support for some annotations, but... until
 * it is needed, we will stick with this simple version.
 * It might not work on Android as java.beans package is missing there...
 */
class TypeSerializerObject extends TypeSerializerBase<Object> {

    /**
     * Value serializer used to serialize array elements
     */
    private final ValueSerializer valueSerializer;

    TypeSerializerObject() {
        this.valueSerializer = ValueSerializer.getDefault();
    }

    TypeSerializerObject(ValueSerializer valueSerializer) {
        this.valueSerializer = Objects.requireNonNull(valueSerializer);
    }

    @Override
    public Class<Object> getBaseClass() {
        return Object.class;
    }

    @Override
    protected String getElement() {
        return XmlRpcValue.STRUCT;
    }

    @Override
    protected void serializeValue(XMLStreamWriter writer, Object object) throws XMLStreamException {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass(), java.lang.Object.class);
            for (var descriptor : beanInfo.getPropertyDescriptors()) {
                if ((descriptor.getReadMethod() != null) &&
                        Modifier.isPublic(descriptor.getReadMethod().getModifiers())) {
                    Object value = descriptor.getReadMethod().invoke(object);
                    if (value != null) {
                        writer.writeStartElement(XmlRpcStruct.MEMBER_TAG);
                        writer.writeStartElement(XmlRpcStruct.NAME_TAG);
                        writer.writeCharacters(descriptor.getName());
                        writer.writeEndElement();
                        valueSerializer.serialize(writer, value);
                        writer.writeEndElement();
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw e;
        } catch(java.lang.Exception e) {
            throw new XmlRpcException("Object serializer exception", e);
        }
    }
}
