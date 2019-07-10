package com.provys.xmlrpc.parsers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class TypeParserSimpleTest {

    private static class TestTypeParserSimple extends TypeParserSimple<String> {

        @Nonnull
        @Override
        public String getElement() {
            return "test";
        }

        @Override
        String convertValue(String value) {
            return value;
        }
    }

    @SuppressWarnings("unused")
    @Nonnull
    static Stream<Object[]> parseValueTest() {
        return Stream.of(
                new Object[]{"<value><test>Test string</test></value>", "Test string"}
                , new Object[]{"<value> <test>Test string</test> </value>", "Test string"}
                , new Object[]{"<value> <test></test> </value>", ""}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseValueTest(String xml, String result) {
        var parser = new TestTypeParserSimple();
        try (var stringReader = new StringReader(xml)) {
            var reader = XMLInputFactory.newInstance().createXMLStreamReader(stringReader);
            reader.next();
            reader.nextTag();
            assertThat(parser.parse(reader)).isEqualTo(result);
            assertThat(reader.isEndElement()).isTrue();
            assertThat(reader.getLocalName()).isEqualTo("test");
        } catch (XMLStreamException e) {
            throw new RuntimeException("Error reading XML stream", e);
        }
    }
}