package com.provys.xmlrpc.serializers;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.*;

class TypeSerializerSimpleTest {

    private static class TestTypeSerializerSimple extends TypeSerializerSimple<String> {

        @Override
        protected String convertValue(String object) {
            return object;
        }

        @Override
        protected String getElement() {
            return "test";
        }

        @Override
        public Class<String> getBaseClass() {
            return String.class;
        }
    }

    @Test
    void serializeValueTest() {
        try (StringWriter stringWriter = new StringWriter()) {
            var writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
            var testSerializer = new TestTypeSerializerSimple();
            testSerializer.serializeValue(writer, "test value");
            writer.flush();
            assertThat(stringWriter.toString()).isEqualTo("test value");
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Writing XML failed", e);
        }
    }
}