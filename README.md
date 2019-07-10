# XML-RPC Client
XML-RPC client library implemented to support communication with DokuWiki. Other libraries I found either were very old and no longer maintained (like redstone) or brought dependencies I didn't like, this is why I implemented this basic library.

It only supports basic XML-RPC over HTTP with XmlRpcClient class, either without authentication, login stored in cookies or using basic authentication. Only basic data types defined in XML-RPC specification are supported at the moment, even though it should be easy to extend this support by implementing new parsers implementing TypeParser interface or serializers, implementing TypeSerializer interface.
