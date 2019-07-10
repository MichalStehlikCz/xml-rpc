package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.TestValueObject1;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.*;

class TypeSerializerObjectTest {

    @Test
    void getBaseClassTest() {
        assertThat(new TypeSerializerObject().getBaseClass()).isEqualTo(Object.class);
    }

    @Test
    void getElementTest() {
        assertThat(new TypeSerializerObject().getElement()).isEqualTo("struct");
    }

    @Test
    void serializeValueTest() {
        try (StringWriter stringWriter = new StringWriter()) {
            var writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
            var testSerializer = new TypeSerializerObject();
            testSerializer.serializeValue(writer, new TestValueObject1(10, "string value"));
            writer.flush();
            assertThat(stringWriter.toString()).contains(
                    "<member><name>property1</name><value><i4>10</i4></value></member>",
                    "<member><name>property2</name><value><string>string value</string></value></member>");
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Writing XML failed", e);
        }
    }
}