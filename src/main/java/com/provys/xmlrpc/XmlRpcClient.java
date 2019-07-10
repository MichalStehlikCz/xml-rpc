package com.provys.xmlrpc;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import com.provys.xmlrpc.parsers.XmlRpcResponseReader;
import com.provys.xmlrpc.serializers.XmlRpcCallWriter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An XmlRpcClient represents a connection to an Xml-Rpc enabled server
 */
@SuppressWarnings("WeakerAccess")
public class XmlRpcClient {

    /** The server URL. */
    @Nonnull
    private final URL url;
    /** Username for http basic authentication */
    @Nullable
    private final String userName;
    /** Password for http basic authentication */
    @Nullable
    private final String password;
    /** Cookie manager that will store cookies from this session */
    @Nonnull
    private final CookieManager cookieManager = new CookieManager();
    /** HTTP request properties, or null if none have been set by the application. */
    @Nonnull
    private final Map<String, String> requestProperties = new HashMap<>();
    /** HTTP header fields returned by the server in the latest response. */
    @Nonnull
    private final Map<String, String> headerFields = new HashMap<>();

    /** Output stream to which XML-RPC messages are serialized. */
    @Nullable
    private OutputStream outputBuffer;

    /** Indicates whether or not we shall stream the message directly or build them locally? */
    private final boolean streamMessages;

    /** Character set used in communication */
    @Nonnull
    private final Charset charset;

    /** The serializer used to serialize arguments. */
    @Nonnull
    private final XmlRpcCallWriter callWriter;

    /** Reader used to parse response */
    @Nonnull
    private final XmlRpcResponseReader responseReader = new XmlRpcResponseReader();

    /**
     * Creates a new client with the ability to send XML-RPC messages to the the server at the given URL.
     *
     * @param url the URL at which the XML-RPC service is located
     * @param streamMessages Indicates whether or not to stream messages directly or if the messages should be completed
     *                      locally before being sent all at once. Streaming is not directly supported by XML-RPC, since
     *                      the Content-Length header is not included in the HTTP post. If the other end is not relying
     *                      on Content-Length, streaming the message directly is much more efficient.
     * @throws MalformedURLException thrown when supplied String is not valid URL
     */
    public XmlRpcClient(String url, boolean streamMessages) throws MalformedURLException {
        this(new URL(url), streamMessages);
    }

    /**
     * Creates a new client with the ability to send XML-RPC messages to the the server at the given URL. Communication
     * will use UTF-8 charset.
     *
     * @param url the URL at which the XML-RPC service is located
     * @param streamMessages Indicates whether or not to stream messages directly or if the messages should be completed
     *                      locally before being sent all at once. Streaming is not directly supported by XML-RPC, since
     *                      the Content-Length header is not included in the HTTP post. If the other end is not relying
     *                      on Content-Length, streaming the message directly is much more efficient.
     */
    public XmlRpcClient(URL url, boolean streamMessages) {
        this(url, streamMessages, StandardCharsets.UTF_8);
    }

    /**
     * Creates a new client with the ability to send XML-RPC messages to the the server at the given URL.
     *
     * @param url the URL at which the XML-RPC service is located
     * @param streamMessages Indicates whether or not to stream messages directly or if the messages should be completed
     *                      locally before being sent all at once. Streaming is not directly supported by XML-RPC, since
     *                      the Content-Length header is not included in the HTTP post. If the other end is not relying
     *                      on Content-Length, streaming the message directly is much more efficient.
     * @param charset is charset to be used in Xml-Rpc message
     */
    public XmlRpcClient(URL url, boolean streamMessages, Charset charset) {
        this(url, null, null, streamMessages, charset);
    }

    /**
     * Creates a new client with the ability to send XML-RPC messages to the the server at the given URL.
     *
     * @param url the URL at which the XML-RPC service is located
     * @param userName is username to be used for login (using basic authentication)
     * @param password is password used for login
     * @param streamMessages Indicates whether or not to stream messages directly or if the messages should be completed
     *                      locally before being sent all at once. Streaming is not directly supported by XML-RPC, since
     *                      the Content-Length header is not included in the HTTP post. If the other end is not relying
     *                      on Content-Length, streaming the message directly is much more efficient.
     * @param charset is charset to be used in Xml-Rpc message
     */
    public XmlRpcClient(URL url, @Nullable String userName, @Nullable String password, boolean streamMessages,
                        Charset charset) {
        this.url = Objects.requireNonNull(url);
        this.userName = userName;
        this.password = password;
        if ((password == null) && (userName != null)) {
            throw new IllegalArgumentException("Password must be specified when userName is provided");
        }
        if ((password != null) && (userName == null)) {
            throw new IllegalArgumentException("Username must be specified when password is provided");
        }
        this.streamMessages = streamMessages;
        this.charset = Objects.requireNonNull(charset);
        this.callWriter = new XmlRpcCallWriter(charset);
    }

    /**
     *  Sets the HTTP request properties that the client will use for the next invocation, and any invocations that
     *  follow until setRequestProperties() is invoked again. Null is accepted and means that no special HTTP request
     *  properties will be used in any future XML-RPC invocations using this XmlRpcClient instance.
     *
     *  @param requestProperties The HTTP request properties to use for future invocations
     *                           made using this XmlRpcClient instance. These will replace
     *                           any previous properties set using this method or the
     *                           setRequestProperty() method.
     */
    public void setRequestProperties(@Nullable Map<String, String> requestProperties) {
        this.requestProperties.clear();
        if (requestProperties != null) {
            this.requestProperties.putAll(requestProperties);
        }
    }
    
    /**
     *  Sets a single HTTP request property to be used in future invocations. Valid until next invocation of
     *  {@code setRequestProperties}
     *
     *  @param name Name of the property to set
     *  @param value The value of the property
     */

    public void setRequestProperty(String name, String value) {
        requestProperties.put(name, value);
    }

    /**
     *  Opens a connection to the URL associated with the client instance. Any HTTP request properties set using
     *  setRequestProperties() are recorded with the internal HttpURLConnection and are used in the HTTP request.
     *
     *  @throws IOException If a connection could not be opened. The exception is propagated out of any unsuccessful
     *  calls made into the internal java.net.HttpURLConnection.
     */
    @Nonnull
    private HttpURLConnection openConnection() throws IOException {
        var connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (userName != null) {
            connection.addRequestProperty("Authorization",
                    "Basic " + Base64.getEncoder().encodeToString((userName + ":" + password).getBytes()));
        }
        connection.setRequestMethod("POST");
        connection.setRequestProperty(
                "Content-Type", "text/xml; charset=" + charset.name());
        // set cookies
        if (!cookieManager.getCookieStore().getCookies().isEmpty()) {
            // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
            connection.setRequestProperty("Cookie",
                    cookieManager.getCookieStore().getCookies().stream()
                            .map(HttpCookie::toString)
                            .collect(Collectors.joining(";")));
        }
        for (var requestProperty : requestProperties.entrySet()) {
            connection.setRequestProperty(
                    requestProperty.getKey(),
                    requestProperty.getValue()
            );
        }
        return connection;
    }

    private void saveCookies(HttpURLConnection connection) {
        Map<String, List<String>> headerFieldLists = connection.getHeaderFields();
        List<String> cookiesHeader = headerFieldLists.get("Set-Cookie");
        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
            }
        }
    }

    private void saveHeaderFields(HttpURLConnection connection) {
        headerFields.clear();
        int fieldNumber = 1;
        String headerFieldKey;
        while ((headerFieldKey = connection.getHeaderFieldKey(fieldNumber)) != null) {
            headerFields.put(headerFieldKey, connection.getHeaderField(fieldNumber++));
        }
    }

    /**
     *  Handles the response returned by the XML-RPC server. If the server responds with a
     *  "non-200"-HTTP response or if the XML payload is unparseable, this is interpreted
     *  as an error in communication and will result in an XmlRpcException.<p>
     *
     *  If the user does not want the socket to be kept alive or if the server does not
     *  support keep-alive, the socket is closed.
     *
     *  @throws IOException If a socket error occurrs, or if the XML returned is unparseable.
     *                      This exception is currently also thrown if a HTTP response other
     *                      than "200 OK" is received.
     */
    private Object handleResponse(HttpURLConnection connection) throws IOException {
        XmlRpcResponseBase response;
        try (var inputStream = connection.getInputStream()) {
            response = responseReader.read(inputStream);
        }
        saveCookies(connection);
        saveHeaderFields(connection);
        if (response instanceof XmlRpcResponse) {
            return ((XmlRpcResponse) response).getValue();
        } else if (response instanceof XmlRpcFault) {
            throw ((XmlRpcFault) response).getException();
        }
        throw new XmlRpcException("Internal error: unsupported response type " + response.getClass().getName());
    }

    /**
     * Invoke remote method and evaluate result when message streaming is allowed (e.g. message is written directly to
     * connections output stream, even though it means content length cannot be in header)
     *
     *  @param method The name of the method to call.
     *  @param arguments The arguments to be used in the call.
     *  @return The object returned from the Xml-Rpc invocation
     * @throws IOException if any problems occur during sending request or reading the response.
     */
    private Object invokeStreamed(HttpURLConnection connection, String method, @Nullable List<Object> arguments)
            throws IOException {
        Object response;
        try (var outputStream = new BufferedOutputStream(connection.getOutputStream())) {
            callWriter.write(outputStream, method, arguments);
            outputStream.flush();
            response = handleResponse(connection);
        }
        return response;
    }

    /**
     * Invoke remote method and evaluate result when message streaming is not allowed and message length is properly
     * written in request header
     *
     *  @param method The name of the method to call.
     *  @param arguments The arguments to be used in the call.
     *  @return The object returned from the Xml-Rpc invocation
     * @throws IOException if any problems occur during sending request or reading the response.
     */
    private Object invokeBuffered(HttpURLConnection connection, String method, @Nullable List<Object> arguments)
            throws IOException {
        if (outputBuffer == null) {
            outputBuffer = new ByteArrayOutputStream(2048);
        }
        Object response;
        callWriter.write(outputBuffer, method, arguments);
        var byteArray = ((ByteArrayOutputStream) outputBuffer).toByteArray();
        connection.setRequestProperty("Content-Length", String.valueOf(byteArray.length));
        try (var output = new BufferedOutputStream(connection.getOutputStream())) {
            output.write(byteArray);
            output.flush();
        }
        ((ByteArrayOutputStream) outputBuffer).reset();
        response = handleResponse(connection);
        return response;
    }

    /**
     *  Invokes a method on the terminating XML-RPC end point. The supplied method name and
     *  argument collection is used to encode the call into an XML-RPC compatible message.
     *
     *  @param method The name of the method to call.
     *  @param arguments The arguments to be used in the call.
     *  @return The object returned from the Xml-Rpc invocation
     *  @throws XmlRpcException One or more of the supplied arguments are unserializable. That is,
     *                          the built-in serializer connot parse it or find a custom serializer
     *                          that can. There may also be problems with the socket communication.
     * @throws XmlRpcFaultException Error occurred in the method call.
     */
    public synchronized Object invoke(String method, @Nullable List<Object> arguments) {
        Object response;
        HttpURLConnection connection = null;
        try {
            connection = openConnection();
            if (streamMessages) {
                response = invokeStreamed(connection, method, arguments);
            } else {
                response = invokeBuffered(connection, method, arguments);
            }
        } catch (IOException e) {
            throw new XmlRpcException("Network error", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }

    /**
     *  Invokes a method on the terminating XML-RPC end point. The supplied method name and argument vector is used to
     *  encode the call into XML-RPC.
     *
     *  @param method The name of the method to call.
     *  @param arguments The arguments to encode in the call.
     *  @return The object returned from the terminating XML-RPC end point.
     *  @throws XmlRpcException One or more of the supplied arguments are unserializable. That is, the built-in
     *  serializer cannot parse it or find a custom serializer that can. There may also be problems with the socket
     *  communication.
     */
    public synchronized Object invoke(String method, @Nullable Object... arguments) {
        return invoke(method, (arguments==null) ? Collections.emptyList() : Arrays.asList(arguments));
    }

    /**
     *  Returns the HTTP header fields from the latest server invocation.
     *  These are the fields set by the HTTP server hosting the XML-RPC service.
     * 
     *  @return The HTTP header fields from the latest server invocation. Note that
     *          the XmlRpcClient instance retains ownership of this map and the map
     *          contents is replaced on the next request. If there is a need to
     *          keep the fields between requests the map returned should be cloned.
     */

    public Map<String, String> getResponseHeaderFields() {
        return Collections.unmodifiableMap(headerFields);
    }
}
