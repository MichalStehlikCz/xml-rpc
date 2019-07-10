package com.provys.xmlrpc.parsers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class TypeParserArrayTest {

    private static final Object value1 = "value1";
    private static final Object value2 = 1;

    @Test
    void getElementTest() {
        assertThat(new TypeParserArray().getElement()).isEqualTo("array");
    }

    @SuppressWarnings("unused")
    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"<document><array><data><value><test></test></value><value><test2></test2></value></data></array></document>",
                        new Object[]{value1, value2}}
                , new Object[]{"<document><array><data></data></array></document>",
                        new Object[]{}}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String xml, Object[] result) {
        var valueParser = new ValueParser(Arrays.asList(new TypeParser[]{
                new TestTypeParser("test", value1),
                new TestTypeParser("test2", value2)}));
        var parser = new TypeParserArray(valueParser);
        try (var stringReader = new StringReader(xml)) {
            var reader = XMLInputFactory.newInstance().createXMLStreamReader(stringReader);
            reader.next();
            reader.nextTag();
            assertThat(parser.parse(reader)).containsExactly(result);
            assertThat(reader.isEndElement()).isTrue();
            assertThat(reader.getLocalName()).isEqualTo("array");
        } catch (XMLStreamException e) {
            throw new RuntimeException("Error reading XML stream", e);
        }
    }
}