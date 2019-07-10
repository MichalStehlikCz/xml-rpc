package com.provys.xmlrpc.serializers;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.*;

class TypeSerializerStringTest {

    @Test
    void getBaseClassTest() {
        assertThat(new TypeSerializerString().getBaseClass()).isEqualTo(String.class);
    }

    @Test
    void getElementTest() {
        assertThat(new TypeSerializerString().getElement()).isEqualTo("string");
    }

    @Test
    void convertValueTest() {
        try (StringWriter stringWriter = new StringWriter()) {
            var writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
            var testSerializer = new TypeSerializerString();
            testSerializer.serializeValue(writer, "test value");
            writer.flush();
            assertThat(stringWriter.toString()).isEqualTo("test value");
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Writing XML failed", e);
        }
    }
}