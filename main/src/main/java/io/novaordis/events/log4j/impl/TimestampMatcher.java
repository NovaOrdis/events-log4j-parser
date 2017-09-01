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

package io.novaordis.events.log4j.impl;

import io.novaordis.events.api.parser.ParsingException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public class TimestampMatcher {

    // Constants -------------------------------------------------------------------------------------------------------

    //
    // IMPORTANT: if a new timestamp pattern is added, the corresponding date format in DATE_FORMATS
    //
    public static final Pattern[] KNOWN_TIMESTAMP_PATTERNS = {

            Pattern.compile("(^[0-2]\\d:[0-5]\\d:[0-5]\\d,\\d\\d\\d)( +).*"),
            Pattern.compile("(^[0-1]\\d/[0-3]\\d/\\d\\d [0-2]\\d:[0-5]\\d:[0-5]\\d,\\d\\d\\d)( +).*"),
    };

    //
    // IMPORTANT: if a new date format is added, the corresponding timestamp pattern in KNOWN_TIMESTAMP_PATTERNS
    //
    public static final DateFormat[] DATE_FORMATS = {

            new SimpleDateFormat("HH:mm:ss,SSS"),
            new SimpleDateFormat("MM/dd/yy HH:mm:ss,SSS"),
    };

    static {

        //
        // sanity check
        //

        if (KNOWN_TIMESTAMP_PATTERNS.length != DATE_FORMATS.length) {

            throw new IllegalArgumentException(
                    "the KNOWN_TIMESTAMP_PATTERNS number is different from DATE_FORMATS, which means " +
                            TimestampMatcher.class.getName() + " is incorrectly coded");
        }
    }

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Matches the line against know timestamp patterns and return a Timestamp instance if the line *begins* with
     * a known pattern.
     */
    public static TimestampMatcher find(long lineNumber, String line) throws ParsingException {

        //
        // discard invalid leading characters
        //

        int offset;

        for(offset = 0; offset < line.length(); offset++) {

            // if it's control (non-printable)
            if (line.charAt(offset) < 32) {

                continue;
            }

            if (offset <= line.length() - 3 &&
                    line.charAt(offset) == '[' &&
                    line.charAt(offset + 1) == '0' &&
                    line.charAt(offset + 2) == 'm') {

                offset += 2;
                continue;
            }

            break;
        }

        if (offset != 0) {

            line = line.substring(offset);
        }

        //
        // attempt to match known timestamp patterns
        //

        int dateFormatIndex = -1;

        for(Pattern p: KNOWN_TIMESTAMP_PATTERNS) {

            dateFormatIndex++;

            Matcher m = p.matcher(line);

            if (m.find()) {

                //
                // pattern found, parse it and extract the date
                //

                String s = m.group(1);

                Date d;

                try {

                    d = DATE_FORMATS[dateFormatIndex].parse(s);
                }
                catch(ParseException pe) {

                    throw new ParsingException(lineNumber, "invalid timestamp \"" + s + "\"", pe);
                }

                return new TimestampMatcher(d.getTime(), DATE_FORMATS[dateFormatIndex], offset + m.end(2));

            }
        }

        //
        // no know timestamp pattern matched
        //

        return null;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private long time;
    private int indexOfNextCharInLine;
    private DateFormat dateFormat;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param df the DateFormat of the time stamp as matched in the log
     */
    public TimestampMatcher(long timestamp, DateFormat df, int indexOfNextCharInLine) {

        this.time = timestamp;
        this.indexOfNextCharInLine = indexOfNextCharInLine;
        this.dateFormat = df;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public long getTime() {

        return time;
    }

    public int getIndexOfNextCharInLine() {

        return  indexOfNextCharInLine;
    }

    @Override
    public String toString() {

        String s = "";

        if (dateFormat == null) {

            s += time;
        }
        else {

            s += dateFormat.format(time);
        }

        s += ", " + indexOfNextCharInLine;

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
