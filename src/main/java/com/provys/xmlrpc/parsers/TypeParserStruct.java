package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcException;
import com.provys.xmlrpc.XmlRpcStruct;
import com.provys.xmlrpc.XmlRpcValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Parser used to parse Xml-Rpc struct value
 */
class TypeParserStruct extends TypeParserBase<XmlRpcStruct> {


    private final ValueParser valueParser;

    TypeParserStruct() {
        this(ValueParser.getDefault());
    }

    TypeParserStruct(ValueParser valueParser) {
        this.valueParser = valueParser;
    }

    @Nonnull
    @Override
    public String getElement() {
        return XmlRpcValue.STRUCT;
    }

    /**
     * Builder for single entry of resulting struct (map)
     */
    private static class Member {
        private String name;
        private boolean nameSet = false;
        private Object value;
        private boolean valueSet = false;

        void setName(String name) {
            if (nameSet) {
                throw new XmlRpcException("Parse Struct member - duplicate value element");
            }
            if (name.isEmpty()) {
                throw new XmlRpcException("Parse Struct member - name cannot be empty");
            }
            this.name = name;
            nameSet = true;
        }

        @Nonnull
        String getName() {
            if (name == null) {
                throw new XmlRpcException("Parse Struct member - name is not specified");
            }
            return name;
        }

        void setValue(@Nullable Object value) {
            if (valueSet) {
                throw new XmlRpcException("Parse Struct member - duplicate value element");
            }
            this.value = value;
            valueSet = true;
        }

        @Nonnull
        Object getValue() {
            if (value == null) {
                throw new XmlRpcException("Parse Struct member - value is not specified");
            }
            return value;
        }
    }

    /**
     * Parse single member element.
     *
     * @param reader is xml reader reading source data; initial position is on member start element, closing position on
     *              corresponding end element
     * @return parsed member value
     * @throws XMLStreamException if any problem  is encountered reading source stream
     */
    @Nonnull
    private Member parseMember(XMLStreamReader reader) throws XMLStreamException {
        var member = new Member();
        if (!reader.isStartElement()) {
            throw new XmlRpcException("Parse Struct - member element expected");
        }
        if (!reader.getLocalName().equals(XmlRpcStruct.MEMBER_TAG)) {
            throw new XmlRpcException("Parse Struct - " + XmlRpcStruct.MEMBER_TAG + " expected, not " +
                    reader.getLocalName());
        }
        while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
            switch (reader.getLocalName()) {
                case XmlRpcStruct.NAME_TAG:
                    member.setName(reader.getElementText());
                    break;
                case XmlRpcStruct.VALUE_TAG:
                    member.setValue(valueParser.parse(reader));
                    break;
                default:
                    throw new XmlRpcException("Parse struct - unrecognized element " + reader.getLocalName() +
                            " in member description");
            }
        }
        if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
            throw new XmlRpcException("Parse struct - expected end of member element, not " + reader.getEventType());
        }
        return member;
    }

    @Override
    protected XmlRpcStruct parseValue(XMLStreamReader reader) throws XMLStreamException {
        XmlRpcStruct result = new XmlRpcStruct();
        while (reader.nextTag() == XMLStreamConstants.START_ELEMENT) {
            var member = parseMember(reader);
            result.put(member.getName(), member.getValue());
        }
        return result;
    }
}
