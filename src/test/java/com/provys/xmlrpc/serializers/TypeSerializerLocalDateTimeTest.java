package com.provys.xmlrpc.serializers;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class TypeSerializerLocalDateTimeTest {

    @Test
    void getBaseClassTest() {
        assertThat(new TypeSerializerLocalDateTime().getBaseClass()).isEqualTo(LocalDateTime.class);
    }

    @Test
    void getElementTest() {
        assertThat(new TypeSerializerLocalDateTime().getElement()).isEqualTo("dateTime.iso8601");
    }

    @Test
    void convertValueTest() {
        try (StringWriter stringWriter = new StringWriter()) {
            var writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
            var testSerializer = new TypeSerializerLocalDateTime();
            testSerializer.serializeValue(writer, LocalDateTime.of(1998, 7, 17, 14, 8, 55));
            writer.flush();
            assertThat(stringWriter.toString()).isEqualTo("19980717T14:08:55");
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Writing XML failed", e);
        }
    }
}