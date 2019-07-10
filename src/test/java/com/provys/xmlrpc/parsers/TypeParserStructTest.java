package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcStruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class TypeParserStructTest {

    private static final Object value1 = "value1";
    private static final Object value2 = 1;

    @Test
    void getElementTest() {
        assertThat(new TypeParserStruct().getElement()).isEqualTo("struct");
    }

    @SuppressWarnings("unused")
    @Nonnull
    static Stream<Object[]> parseValueTest() {
        return Stream.of(
                new Object[]{"<struct><member><name>member1</name><value><test></test></value></member>" +
                        "<member><value><test2></test2></value><name>member2</name></member></struct>",
                        new XmlRpcStruct(Map.of("member1", value1, "member2", value2))}
                , new Object[]{"<struct></struct>", new XmlRpcStruct()}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseValueTest(String xml, XmlRpcStruct result) {
        var valueParser = new ValueParser(Arrays.asList(new TypeParser[]{
                new TestTypeParser("test", value1),
                new TestTypeParser("test2", value2)}));
        var parser = new TypeParserStruct(valueParser);
        try (var stringReader = new StringReader(xml)) {
            var reader = XMLInputFactory.newInstance().createXMLStreamReader(stringReader);
            reader.next();
            assertThat(parser.parseValue(reader)).isEqualTo(result);
            assertThat(reader.isEndElement()).isTrue();
            assertThat(reader.getLocalName()).isEqualTo("struct");
        } catch (XMLStreamException e) {
            throw new RuntimeException("Error reading XML stream", e);
        }
    }
}