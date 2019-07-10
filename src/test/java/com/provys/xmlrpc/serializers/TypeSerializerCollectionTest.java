package com.provys.xmlrpc.serializers;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

class TypeSerializerCollectionTest {

    @Test
    void getBaseClassTest() {
        assertThat(new TypeSerializerCollection().getBaseClass()).isEqualTo(Collection.class);
    }

    @Test
    void getElementTest() {
        assertThat(new TypeSerializerCollection().getElement()).isEqualTo("array");
    }

    @Test
    void serializeValueTest() {
        try (StringWriter stringWriter = new StringWriter()) {
            var writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
            var testSerializer = new TypeSerializerCollection();
            testSerializer.serializeValue(writer, Arrays.asList("test value", 5));
            writer.flush();
            assertThat(stringWriter.toString()).isEqualTo("<data><value><string>test value</string></value><value><i4>5</i4></value></data>");
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Writing XML failed", e);
        }
    }
}