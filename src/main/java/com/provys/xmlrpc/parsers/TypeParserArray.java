package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcArray;
import com.provys.xmlrpc.XmlRpcException;
import com.provys.xmlrpc.XmlRpcValue;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Parser used to retrieve Xml-Rpc array value from xml stream.
 */
class TypeParserArray extends TypeParserBase<XmlRpcArray> {

    private final ValueParser valueParser;

    TypeParserArray() {
        this(ValueParser.getDefault());
    }

    TypeParserArray(ValueParser valueParser) {
        this.valueParser = valueParser;
    }

    @Nonnull
    @Override
    public String getElement() {
        return XmlRpcValue.ARRAY;
    }

    /**
     * Parse content of data element.
     *
     * @param reader is reader containing source data
     * @return parsed Xml-Rpc array
     * @throws XMLStreamException in case there is problem reading source data
     */
    private XmlRpcArray parseData(XMLStreamReader reader) throws XMLStreamException {
        XmlRpcArray result = new XmlRpcArray();
        while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
            result.add(valueParser.parse(reader));
        }
        return result;
    }

    @Override
    protected XmlRpcArray parseValue(XMLStreamReader reader) throws XMLStreamException {
        if (reader.nextTag() != XMLStreamConstants.START_ELEMENT) {
            throw new XmlRpcException("Parse array - " + XmlRpcArray.DATA_TAG + " element expected");
        }
        if (!reader.getLocalName().equals(XmlRpcArray.DATA_TAG)) {
            throw new XmlRpcException("Parse array - " + XmlRpcArray.DATA_TAG + " element expected, not " +
                    reader.getLocalName());
        }
        var result = parseData(reader);
        if (!reader.isEndElement()) {
            throw new XmlRpcException("Parse array - end of " + XmlRpcArray.DATA_TAG + " element expected");
        }
        if (!reader.getLocalName().equals(XmlRpcArray.DATA_TAG)) {
            throw new XmlRpcException("Parse array - " + XmlRpcArray.DATA_TAG + " end element expected, not " +
                    reader.getLocalName());
        }
        reader.nextTag();
        return result;
    }
}
