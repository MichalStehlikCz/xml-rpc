package com.provys.xmlrpc;

import java.util.Objects;

/**
 *  Exception thrown by the XML-RPC library in case of a fault response. The exception is thrown only if the call was
 *  successfully made but the response contained a fault message. If a call could not be made due to a local problem (if
 *  an argument could not be serialized or if there was a network problem) an XmlRpcException is thrown instead.
 */

@SuppressWarnings("WeakerAccess")
public class XmlRpcFaultException extends RuntimeException {

    /** Serial version UID. */
    private static final long serialVersionUID = 3257566200450856503L;

    /** The exception error code. See XML-RPC specification. */
    private final int errorCode;

    /**
     *  Creates a new exception with the supplied message and error code.
     *  The message and error code values are those returned from the remote
     *  XML-RPC service.
     *
     *  @param message The exception message.
     */

    XmlRpcFaultException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }


    /**
     * Returns the error code reported by the remote XML-RPC service.
     *
     * @return the error code reported by the XML-RPC service.
     */
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlRpcFaultException)) return false;
        XmlRpcFaultException that = (XmlRpcFaultException) o;
        return (getErrorCode() == that.getErrorCode()) &&
                (Objects.equals(getMessage(), that.getMessage())) &&
                (Objects.equals(getCause(), that.getCause()));
    }

    @Override
    public int hashCode() {
        return getErrorCode()*31 + getMessage().hashCode();
    }

    @Override
    public String toString() {
        return "XmlRpcFaultException{" +
                "errorCode=" + errorCode +
                "} " + super.toString();
    }
}
