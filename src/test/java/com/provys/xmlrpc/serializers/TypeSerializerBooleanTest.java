package com.provys.xmlrpc.serializers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class TypeSerializerBooleanTest {

    @Test
    void getBaseClassTest() {
        assertThat(new TypeSerializerBoolean().getBaseClass()).isEqualTo(Boolean.class);
    }

    @Test
    void getElementTest() {
        assertThat(new TypeSerializerBoolean().getElement()).isEqualTo("boolean");
    }

    @SuppressWarnings("unused")
    @Nonnull
    static Stream<Object[]> convertValueTest() {
        return Stream.of(
                new Object[]{true, "1"}
                , new Object[]{false, "0"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void convertValueTest(boolean value, String result) {
        try (StringWriter stringWriter = new StringWriter()) {
            var writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
            var testSerializer = new TypeSerializerBoolean();
            testSerializer.serializeValue(writer, value);
            writer.flush();
            assertThat(stringWriter.toString()).isEqualTo(result);
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException("Writing XML failed", e);
        }
    }
}