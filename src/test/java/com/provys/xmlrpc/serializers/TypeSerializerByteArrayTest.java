package com.provys.xmlrpc.serializers;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.*;

class TypeSerializerByteArrayTest {

    @Test
    void getBaseClassTest() {
        assertThat(new TypeSerializerByteArray().getBaseClass()).isEqualTo(byte[].class);
    }

    @Test
    void getElementTest() {
        assertThat(new TypeSerializerByteArray().getElement()).isEqualTo("base64");
    }

    @Test
    void convertValueTest() {
        try (StringWriter stringWriter = new StringWriter()) {
            var writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
            var testSerializer = new TypeSerializerByteArray();
            testSerializer.serializeValue(writer, "you can't read this!".getBytes());
            writer.flush();
            assertThat(stringWriter.toString()).isEqualTo("eW91IGNhbid0IHJlYWQgdGhpcyE=");
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Writing XML failed", e);
        }
    }
}