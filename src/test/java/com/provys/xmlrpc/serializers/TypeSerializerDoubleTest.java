package com.provys.xmlrpc.serializers;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.*;

class TypeSerializerDoubleTest {

    @Test
    void getBaseClassTest() {
        assertThat(new TypeSerializerDouble().getBaseClass()).isEqualTo(Double.class);
    }

    @Test
    void getElementTest() {
        assertThat(new TypeSerializerDouble().getElement()).isEqualTo("double");
    }

    @Test
    void convertValueTest() {
        try (StringWriter stringWriter = new StringWriter()) {
            var writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
            var testSerializer = new TypeSerializerDouble();
            testSerializer.serializeValue(writer, -12.214);
            writer.flush();
            assertThat(stringWriter.toString()).isEqualTo("-12.214");
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Writing XML failed", e);
        }
    }
}