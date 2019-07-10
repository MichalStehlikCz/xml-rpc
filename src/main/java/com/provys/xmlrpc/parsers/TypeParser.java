package com.provys.xmlrpc.parsers;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Interface for parser used to parse value of value element
 */
interface TypeParser {

    @Nonnull
    String getElement();

    /**
     *  Parse value of specific type from stream. Initial position in stream is on opening element (indicating type),
     *  end position is on corresponding end element
     *
     * @param reader is xml stream reader containing data
     * @throws XMLStreamException if there are any problems reading data
     */
    Object parse(XMLStreamReader reader) throws XMLStreamException;
}
