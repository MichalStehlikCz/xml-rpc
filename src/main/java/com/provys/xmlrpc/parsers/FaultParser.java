package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcException;
import com.provys.xmlrpc.XmlRpcFault;
import com.provys.xmlrpc.XmlRpcStruct;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

class FaultParser {

    static final String TAG = "fault";

    private final ValueParser valueParser;

    FaultParser() {
        this.valueParser = ValueParser.getDefault();
    }

    XmlRpcFault parse(XMLStreamReader reader) throws XMLStreamException {
        if (!reader.isStartElement()) {
            throw new XmlRpcException("Invalid position for parsing fault response - current position not element");
        }
        if (!reader.getLocalName().equals(TAG)) {
            throw new XmlRpcException("Invalid position for parsing fault response - current position not element" +
                    TAG);
        }
        if (reader.nextTag() != XMLStreamConstants.START_ELEMENT) {
            throw new XmlRpcException("Missing value in fault element");
        }
        var value = valueParser.parse(reader);
        if (!(value instanceof XmlRpcStruct)) {
            throw new XmlRpcException("Struct expected in fault value, " + value.getClass().getSimpleName() + " found");
        }
        return new XmlRpcFault((XmlRpcStruct) value);
    }
}
