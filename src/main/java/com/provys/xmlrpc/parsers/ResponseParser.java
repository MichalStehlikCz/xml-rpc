package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcException;
import com.provys.xmlrpc.XmlRpcResponse;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

class ResponseParser {

    static final String TAG = "params";
    private static final String PARAM_TAG = "param";

    private final ValueParser valueParser;

    ResponseParser() {
        this.valueParser = ValueParser.getDefault();
    }

    @SuppressWarnings("Duplicates")
    @Nonnull
    private XmlRpcResponse parseParam(XMLStreamReader reader) throws XMLStreamException {
        if (!reader.isStartElement()) {
            throw new XmlRpcException("Parse " + PARAM_TAG + " - element expected");
        }
        if (!reader.getLocalName().equals(PARAM_TAG)) {
            throw new XmlRpcException("Parse " + PARAM_TAG + " - incorrect element name " + reader.getLocalName());
        }
        reader.nextTag();
        var result = new XmlRpcResponse(valueParser.parse(reader));
        if (reader.nextTag() != XMLStreamConstants.END_ELEMENT) {
            throw new XmlRpcException("Parse " + PARAM_TAG + " - end element expected");
        }
        if (!reader.getLocalName().equals(PARAM_TAG)) {
            throw new XmlRpcException("Parse " + PARAM_TAG + " - incorrect end element name " +
                    reader.getLocalName());
        }
        return result;
    }

    @SuppressWarnings("Duplicates")
    @Nonnull
    XmlRpcResponse parse(XMLStreamReader reader) throws XMLStreamException {
        if (!reader.isStartElement()) {
            throw new XmlRpcException("Parse " + TAG + " - element expected");
        }
        if (!reader.getLocalName().equals(TAG)) {
            throw new XmlRpcException("Parse " + TAG + " - incorrect element name " + reader.getLocalName());
        }
        reader.nextTag();
        var result = parseParam(reader);
        if (reader.nextTag() != XMLStreamConstants.END_ELEMENT) {
            throw new XmlRpcException("Parse " + TAG + " - end element expected");
        }
        if (!reader.getLocalName().equals(TAG)) {
            throw new XmlRpcException("Parse " + TAG + " - incorrect end element name " +
                    reader.getLocalName());
        }
        return result;
    }
}
