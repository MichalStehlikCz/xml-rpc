package com.provys.xmlrpc.serializers;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.*;

class TypeSerializerObjectArrayTest {

    @Test
    void getBaseClassTest() {
        assertThat(new TypeSerializerObjectArray().getBaseClass()).isEqualTo(Object[].class);
    }

    @Test
    void getElementTest() {
        assertThat(new TypeSerializerObjectArray().getElement()).isEqualTo("array");
    }

    @Test
    void serializeValueTest() {
        try (StringWriter stringWriter = new StringWriter()) {
            var writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
            var testSerializer = new TypeSerializerObjectArray();
            testSerializer.serializeValue(writer, new Object[]{"test value", 5});
            writer.flush();
            assertThat(stringWriter.toString()).isEqualTo("<data><value><string>test value</string></value><value><i4>5</i4></value></data>");
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Writing XML failed", e);
        }
    }
}