/*
 * Copyright (c) 2017 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.events.log4j.pattern.convspec;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.novaordis.events.log4j.impl.Log4jEventImpl;
import io.novaordis.events.log4j.pattern.AddResult;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.log4j.pattern.ProcessedString;
import io.novaordis.events.log4j.pattern.RenderedLogEvent;
import io.novaordis.utilities.time.TimestampImpl;

/**
 * Outputs the date of the logging event ('d{pattern}').
 *
 * The type of the corresponding parsed object instance is java.lang.Date.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class Date extends ConversionSpecifierBase {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final char CONVERSION_CHARACTER = 'd';

    public static final String DEFAULT = "DEFAULT";
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";

    public static final String ISO8601 = "ISO8601";
    public static final String ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss,SSS";

    public static final String ISO8601_BASIC = "ISO8601_BASIC";
    public static final String ISO8601_BASIC_PATTERN = "yyyyMMdd'T'HHmmss,SSS";

    public static final String ABSOLUTE = "ABSOLUTE";
    public static final String ABSOLUTE_PATTERN = "HH:mm:ss,SSS";

    public static final String DATE = "DATE";
    public static final String DATE_PATTERN = "dd MMM yyyy HH:mm:ss,SSS";

    public static final String COMPACT = "COMPACT";
    public static final String COMPACT_PATTERN = "yyyyMMddHHmmssSSS";

    public static final String UNIX = "UNIX";

    public static final String UNIX_MILLIS = "UNIX_MILLIS";

    private static final short NEW = 0;
    private static final short READING_DATE_PATTERN = 1;
    private static final short CLOSED = 2;

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private short state;
    private String datePatternLiteral;
    private SimpleDateFormat dateFormat;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Date() {

        this.state = NEW;
    }

    /**
     * @param pattern, without the pattern element marker ('%'). Example: "d{HH:mm:ss,SSS}"
     */
    protected Date(String pattern) throws Log4jPatternLayoutException {

        super(pattern);
    }

    // ConversionSpecifier implementation ------------------------------------------------------------------------------

    @Override
    public char getConversionCharacter() {

        return CONVERSION_CHARACTER;
    }

    @Override
    public String getLiteral() {

        String s = super.getLiteral();

        if (datePatternLiteral != null) {

            s += '{' + datePatternLiteral + '}';
        }

        return s;
    }

    @Override
    public AddResult add(char c) throws Log4jPatternLayoutException {

        if (state == NEW) {

            if (c != '{') {

                //
                // the default format, no explicit date format pattern
                //

                this.state = CLOSED;
                processDatePattern();
                return AddResult.NOT_ACCEPTED;
            }

            state = READING_DATE_PATTERN;
            return AddResult.ACCEPTED;
        }
        else if (state == READING_DATE_PATTERN) {

            if (c == '}') {

                state = CLOSED;
                processDatePattern();
                return AddResult.LAST;
            }
            else {

                if (datePatternLiteral == null) {

                    datePatternLiteral = "" + c;
                }
                else {

                    datePatternLiteral += c;
                }
                return AddResult.ACCEPTED;
            }
        }
        else if (state == CLOSED) {

            throw new Log4jPatternLayoutException("attempt to add more characters to a closed date pattern element");
        }
        else {

            throw new IllegalStateException("unknown state " + state);
        }
    }

    @Override
    public RenderedLogEvent parseLiteralAfterFormatModifierWasUnapplied(ProcessedString ps)
            throws Log4jPatternLayoutException {

        int length = dateFormat.toPattern().length();

        String s2 = ps.getProcessedString();

        int s2Length = s2.length();

        if (s2Length == 0) {

            throw new Log4jPatternLayoutException("empty string when expecting a time stamp");
        }
        else if (s2Length < length) {

            throw new Log4jPatternLayoutException(
                    "date rendering string is shorted that what would have been expected given the pattern " +
                            getLiteral());
        }
        else if (s2Length > length) {

            throw new Log4jPatternLayoutException(
                    "date rendering string is longer that what would have been expected given the pattern " +
                            getLiteral());
        }

        java.util.Date d;

        try {

            d =  dateFormat.parse(s2);
        }
        catch(ParseException e) {

            throw new Log4jPatternLayoutException("date \"" + s2 + "\" does not match pattern " + getLiteral());
        }

        return new RenderedLogEvent(d, ps.from(), ps.to());
    }

    @Override
    protected void injectIntoEvent(Log4jEventImpl e, Object value) {

        //
        // null handled by superclass method
        //

        if (!(value instanceof java.util.Date)) {

            throw new IllegalArgumentException(
                    "invalid value type " + value.getClass().getSimpleName() + ", expected java.util.Date");
        }

        e.setTimestamp(new TimestampImpl(((java.util.Date)value).getTime()));
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public SimpleDateFormat getSimpleDateFormat() {

        return dateFormat;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void processDatePattern() throws Log4jPatternLayoutException {

        //
        // no date pattern literal means DEFAULT
        //

        String dpl = datePatternLiteral == null ? DEFAULT_PATTERN : datePatternLiteral;

        if (DEFAULT.equals(dpl)) {

            dpl = DEFAULT_PATTERN;
        }
        else if (ISO8601.equals(dpl)) {

            dpl = ISO8601_PATTERN;
        }
        else if (ISO8601_BASIC.equals(dpl)) {

            dpl = ISO8601_BASIC_PATTERN;
        }
        else if (ABSOLUTE.equals(dpl)) {

            dpl = ABSOLUTE_PATTERN;
        }
        else if (DATE.equals(dpl)) {

            dpl = DATE_PATTERN;
        }
        else if (COMPACT.equals(dpl)) {

            dpl = COMPACT_PATTERN;
        }
        else if (UNIX.equals(dpl)) {

            throw new RuntimeException("Support for UNIX pattern NOT YET IMPLEMENTED");
        }
        else if (UNIX_MILLIS.equals(dpl)) {

            throw new RuntimeException("Support for UNIX_MILLIS pattern NOT YET IMPLEMENTED");
        }

        try {

            this.dateFormat = new SimpleDateFormat(dpl);
        }
        catch(Exception e) {

            throw new Log4jPatternLayoutException("invalid date format \"" + dpl + "\"", e);
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
