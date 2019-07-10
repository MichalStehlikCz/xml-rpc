package com.provys.xmlrpc.serializers;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.*;

class TypeSerializerIntegerTest {

    @Test
    void getBaseClassTest() {
        assertThat(new TypeSerializerInteger().getBaseClass()).isEqualTo(Integer.class);
    }

    @Test
    void getElementTest() {
        assertThat(new TypeSerializerInteger().getElement()).isEqualTo("i4");
    }

    @Test
    void convertValueTest() {
        try (StringWriter stringWriter = new StringWriter()) {
            var writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
            var testSerializer = new TypeSerializerInteger();
            testSerializer.serializeValue(writer, 4);
            writer.flush();
            assertThat(stringWriter.toString()).isEqualTo("4");
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Writing XML failed", e);
        }
    }
}