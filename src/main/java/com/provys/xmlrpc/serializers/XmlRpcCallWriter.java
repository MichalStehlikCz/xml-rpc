package com.provys.xmlrpc.serializers;

import com.provys.xmlrpc.XmlRpcException;

import javax.annotation.Nullable;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class XmlRpcCallWriter {

    private static final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

    private final ValueSerializer valueSerializer;
    private final Charset charset;

    public XmlRpcCallWriter() {
        this(StandardCharsets.UTF_8);
    }

    public XmlRpcCallWriter(Charset charset) {
        this.valueSerializer = ValueSerializer.getDefault();
        this.charset = Objects.requireNonNull(charset);
    }

    public void write(OutputStream outputStream, String methodName, @Nullable List<Object> params) {
        if (methodName.isBlank()) {
            throw new XmlRpcException("Method name cannot be blank");
        }
        try {
            XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(outputStream, charset.name());
            writer.writeProcessingInstruction("xml", "version=\"1.0\"");
            writer.writeStartElement("methodCall");
            writer.writeStartElement("methodName");
            writer.writeCharacters(methodName);
            writer.writeEndElement();
            if ((params != null) && (!params.isEmpty())) {
                writer.writeStartElement("params");
                for (var param : params) {
                    writer.writeStartElement("param");
                    valueSerializer.serialize(writer, param);
                    writer.writeEndElement();
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.flush();
        } catch (XMLStreamException e) {
            throw new XmlRpcException("Error writing Xml-Rpc call", e);
        }
    }
}
