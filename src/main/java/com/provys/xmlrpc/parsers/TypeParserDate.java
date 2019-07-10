package com.provys.xmlrpc.parsers;

import com.provys.xmlrpc.XmlRpcValue;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.*;

import static java.time.temporal.ChronoField.*;

/**
 * Parser used to parse date value.
 * By default, parses date values in format defined in Xml-Rpc standard, e.g. YYYYMMDDTHH:MI:SS, but fallback support
 * for format YYYY-MM-DDTHH:MM:ss+HHMM is added as it is used for dates in dokuWiki
 */
class TypeParserDate extends TypeParserSimple<LocalDateTime> {

    private static final DateTimeFormatter XMLRPC_DATE_PARSER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 4,SignStyle.EXCEEDS_PAD)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('T')
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .optionalEnd()
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT)
            .withChronology(IsoChronology.INSTANCE);

    private static final DateTimeFormatter DOKUWIKI_DATE_PARSER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 4,SignStyle.EXCEEDS_PAD)
            .appendLiteral('-')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('T')
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .optionalEnd()
            .optionalStart()
            .appendOffset("+HHMM", "Z")
            .optionalEnd()
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT)
            .withChronology(IsoChronology.INSTANCE);

    @Nonnull
    @Override
    public String getElement() {
        return XmlRpcValue.DATE;
    }

    @Override
    LocalDateTime convertValue(String value) {
        try {
            return LocalDateTime.from(XMLRPC_DATE_PARSER.parse(value));
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.from(DOKUWIKI_DATE_PARSER.parse(value));
            } catch (DateTimeParseException e2) {
                throw e;
            }
        }
    }
}
