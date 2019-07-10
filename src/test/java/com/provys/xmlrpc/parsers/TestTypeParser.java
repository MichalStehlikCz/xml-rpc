package com.provys.xmlrpc.parsers;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple test parser - parses takes tag name and return value as parameter, parses empty tag element and returns value
 */
class TestTypeParser implements TypeParser {

    private final String tag;
    private final Object returnValue;

    TestTypeParser(String tag, Object returnValue) {
        this.tag = Objects.requireNonNull(tag);
        this.returnValue = Objects.requireNonNull(returnValue);
    }

    @Nonnull
    @Override
    public String getElement() {
        return tag;
    }

    @Override
    public Object parse(XMLStreamReader reader) throws XMLStreamException {
        assertThat(reader.isStartElement()).isTrue();
        assertThat(reader.getLocalName()).isEqualTo(tag);
        assertThat(reader.next()).isEqualTo(XMLStreamConstants.END_ELEMENT);
        assertThat(reader.getLocalName()).isEqualTo(tag);
        return returnValue;
    }
}
