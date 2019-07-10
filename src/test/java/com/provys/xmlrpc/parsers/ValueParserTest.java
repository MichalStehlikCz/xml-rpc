package com.provys.xmlrpc.parsers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class ValueParserTest {

    private static final Object value1 = "value1";

    @SuppressWarnings("unused")
    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"<document><value><test></test></value></document>", value1}
                , new Object[]{"<document><value>testvalue</value></document>", "testvalue"}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String xml, @Nullable Object result) {
        var parser = new ValueParser(Arrays.asList(new TypeParser[]{
                new TestTypeParser("test", "value1"),
                new TestTypeParser("test2", 2)}));
        try (var stringReader = new StringReader(xml)) {
            var reader = XMLInputFactory.newInstance().createXMLStreamReader(stringReader);
            // navigate through document element to value element
            reader.next();
            reader.nextTag();
            assertThat(parser.parse(reader)).isEqualTo(result);
            assertThat(reader.isEndElement()).isTrue();
            assertThat(reader.getLocalName()).isEqualTo("value");
        } catch (XMLStreamException e) {
            throw new RuntimeException("Error reading XML stream", e);
        }
    }
}