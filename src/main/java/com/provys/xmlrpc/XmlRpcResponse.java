package com.provys.xmlrpc;

import javax.annotation.Nonnull;

/**
 * Class represents successful response to XmlRpc request
 */
public class XmlRpcResponse implements XmlRpcResponseBase {

    @Nonnull
    private final Object value;

    public XmlRpcResponse(Object value) {
        this.value = value;
    }

    @Nonnull
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlRpcResponse)) return false;

        XmlRpcResponse that = (XmlRpcResponse) o;

        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public String toString() {
        return "XmlRpcResponse{" +
                "value=" + value +
                '}';
    }
}
