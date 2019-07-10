package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcException;
import com.provys.xmlrpc.XmlRpcResponseBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;

public class XmlRpcResponseReader {

    private static final Logger LOG = LogManager.getLogger(XmlRpcResponseReader.class.getName());
    private static final String ROOT_TAG = "methodResponse";

    @Nonnull
    private final ResponseParser responseParser;
    @Nonnull
    private final FaultParser faultParser;

    /**
     * Create new Xml-Rpc response reader, uses default response and fault parser implementation
     */
    public XmlRpcResponseReader() {
        this.responseParser = new ResponseParser();
        this.faultParser = new FaultParser();
    }

    @Nonnull
    private XmlRpcResponseBase parse(XMLStreamReader reader) throws XMLStreamException {
        // read root element
        reader.nextTag();
        if (!reader.getLocalName().equals(ROOT_TAG)) {
            throw new XmlRpcException("Root elements in XML-RPC response data source should be " + ROOT_TAG +
                    ", not " + reader.getLocalName());
        }
        // read content
        XmlRpcResponseBase response;
        if (reader.nextTag() != XMLStreamConstants.START_ELEMENT) {
            throw new XmlRpcException("Empty XML-RPC response");
        }
        switch (reader.getLocalName()) {
            case ResponseParser.TAG:
                response = responseParser.parse(reader);
                break;
            case FaultParser.TAG:
                response = faultParser.parse(reader);
                break;
            default:
                throw new XmlRpcException("Elements " + ResponseParser.TAG + " or " + FaultParser.TAG +
                        " expected in XML-RPC response, not " + reader.getLocalName());
        }
        if (reader.nextTag() != XMLStreamConstants.END_ELEMENT) {
            throw new XmlRpcException("End element expected in XML-RPC response");
        }
        return response;
    }

    public XmlRpcResponseBase read(InputStream reportBodyStream) {
        XmlRpcResponseBase response;
        XMLStreamReader reader = null;
        try {
            reader = XMLInputFactory.newInstance().createXMLStreamReader(reportBodyStream);
            response = parse(reader);
        } catch (XMLStreamException e) {
            throw new XmlRpcException("Error reading response", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    LOG.warn("Error closing XML reader for response");
                }
            }
        }
        return response;
    }
}
