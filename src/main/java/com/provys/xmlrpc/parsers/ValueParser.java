package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcException;
import com.provys.xmlrpc.XmlRpcValue;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Parser for value element.
 */
class ValueParser {

    private static final ValueParser DEFAULT = new ValueParser();

    /**
     * Return default value parser, supporting data types defined in specification (i4, int, boolean, string, double,
     * date, base64, array and struct)
     * @return default parser based on XML-RPC standard
     */
    static ValueParser getDefault() {
        return DEFAULT;
    }

    private final Map<String, TypeParser> typeParsers;

    /**
     * Create default value parser, supporting data types defined in specification (i4, int, boolean, string, double,
     * date, base64, array and struct)
     */
    private ValueParser() {
        typeParsers = new HashMap<>();
        var parsers = new TypeParser[]{new TypeParserI4(), new TypeParserInt(), new TypeParserBoolean(), new TypeParserString(),
                new TypeParserDouble(), new TypeParserDate(), new TypeParserBase64(), new TypeParserArray(this),
                new TypeParserStruct(this)};
        for (var parser : parsers) {
            this.typeParsers.put(parser.getElement(), parser);
        }
    }

    /**
     * Create new value parser utilizing specified list of type parsers.
     *
     * @param typeParsers is list of type parsers to be used by this value parser
     */
    ValueParser(List<TypeParser> typeParsers) {
        this.typeParsers = typeParsers.stream()
                .collect(Collectors.toMap(TypeParser::getElement, Function.identity()));
    }

    /**
     * Parse string value (that was not enclosed in element). Unfortunatelly we have to navigate stream to next event
     * after {@code <value>} to determine if it is text element or there is some structure inside and thus we cannot use
     * getElementText provided directly by XMLStreamReader and have to implement it here instead.
     *
     * @param reader is XML stream reader containing source data
     * @return string value read from stream
     * @throws XMLStreamException if any exception happens during reading the data
     */
    private String parseStringValue(XMLStreamReader reader) throws XMLStreamException {
        int eventType = reader.getEventType();
        StringBuilder content = new StringBuilder();
        while (eventType != XMLStreamConstants.END_ELEMENT) {
            if (eventType == XMLStreamConstants.CHARACTERS
                    || eventType == XMLStreamConstants.CDATA
                    || eventType == XMLStreamConstants.SPACE
                    || eventType == XMLStreamConstants.ENTITY_REFERENCE) {
                content.append(reader.getText());
            } else //noinspection StatementWithEmptyBody
                if (eventType == XMLStreamConstants.PROCESSING_INSTRUCTION
                    || eventType == XMLStreamConstants.COMMENT) {
                // skipping

            } else if (eventType == XMLStreamConstants.END_DOCUMENT) {
                throw new XMLStreamException("unexpected end of document when reading element text content",
                        reader.getLocation());
            } else if (eventType == XMLStreamConstants.START_ELEMENT) {
                throw new XMLStreamException(
                        "element text content may not contain START_ELEMENT", reader.getLocation());
            } else {
                throw new XMLStreamException(
                        "Unexpected event type " + eventType, reader.getLocation());
            }
            eventType = reader.next();
        }
        return content.toString().trim();
    }

    /**
     * Parse content of the value element; expects either element that defines type of value to be parsed or text.
     *
     * @param reader is reader containing source xml data. Starting position is on opening value element, end position
     *               is on end of value element
     * @return parsed value
     * @throws XMLStreamException if problem is encountered reading source data
     */
    private Object parseValue(XMLStreamReader reader) throws XMLStreamException {
        reader.next();
        // we want to skip whitespace and find either element or start of string value
        while (reader.isWhiteSpace()) {
            reader.next();
        }
        if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
            var parser = typeParsers.get(reader.getLocalName());
            if (parser == null) {
                throw new XmlRpcException("Unrecognized value type " + reader.getLocalName());
            }
            var result = parser.parse(reader);
            reader.nextTag();
            return result;
        }
        return parseStringValue(reader);
    }

    /**
     * Parse value from reader
     *
     * @param reader with source xml data. Initial position is on opening element, ending position is on end of value
     *               element
     * @return parsed value
     * @throws XMLStreamException if any problem is encountered reading content of XML stream
     */
    Object parse(XMLStreamReader reader) throws XMLStreamException {
        if (!reader.isStartElement()) {
            throw new XmlRpcException("Parse " + XmlRpcValue.TAG + " - element expected");
        }
        if (!reader.getLocalName().equals(XmlRpcValue.TAG)) {
            throw new XmlRpcException("Parse " + XmlRpcValue.TAG + " - incorrect element name " + reader.getLocalName());
        }
        Object result = parseValue(reader);
        if (!reader.isEndElement()) {
            throw new XmlRpcException("Parse " + XmlRpcValue.TAG + " - end element expected");
        }
        if (!reader.getLocalName().equals(XmlRpcValue.TAG)) {
            throw new XmlRpcException("Parse " + XmlRpcValue.TAG + " - incorrect end element " + reader.getLocalName());
        }
        return result;
    }
}
