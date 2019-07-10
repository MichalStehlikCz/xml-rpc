package com.provys.xmlrpc.serializers;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class TypeSerializerMapTest {

    @Test
    void getBaseClassTest() {
        assertThat(new TypeSerializerMap().getBaseClass()).isEqualTo(Map.class);
    }

    @Test
    void getElementTest() {
        assertThat(new TypeSerializerMap().getElement()).isEqualTo("struct");
    }

    @Test
    void serializeValueTest() {
        try (StringWriter stringWriter = new StringWriter()) {
            var writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
            var testSerializer = new TypeSerializerMap();
            testSerializer.serializeValue(writer, Map.of("member1", "value1", "member2", 5));
            writer.flush();
            assertThat(stringWriter.toString()).contains(
                    "<member><name>member1</name><value><string>value1</string></value></member>",
                    "<member><name>member2</name><value><i4>5</i4></value></member>");
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Writing XML failed", e);
        }
    }
}