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

package io.novaordis.events.log4j;

import io.novaordis.events.log4j.impl.TimestampMatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public class LogProcessing {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final SimpleDateFormat OUT = new SimpleDateFormat("HH:mm:ss,SSS");

    // Static ----------------------------------------------------------------------------------------------------------

    public static void out(Object o) {

        System.out.println(o == null ? "null" : o.toString());
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d) .*");
    private static final DateFormat IN = new SimpleDateFormat("HH:mm:ss,SSS");

    private static TimestampMatcher findTimestamp(String line) throws Exception {

        Matcher m = DATE_PATTERN.matcher(line);

        if (!m.find()) {

            return null;
        }

        String timestampAsString = m.group(1);
        Date timestamp = IN.parse(timestampAsString);
        int nextCharInLine = m.end(1) + 1;
        return new TimestampMatcher(timestamp.getTime(), null, nextCharInLine);
    }

    private static void lineWithTimestamp(Context context, long lineNumber, TimestampMatcher t, String restOfTheLine)
            throws Exception {

        if (restOfTheLine.startsWith("ERROR")) {

            errorLine(context, lineNumber, t, restOfTheLine.substring("ERROR".length() + 1));
        }
    }

    private static void lineWithoutTimestamp(Context context, long lineNumber, String restOfTheLine) {
    }

    private static void errorLine(Context context, long lineNumber, TimestampMatcher t, String restOfTheLine)
            throws Exception {

        Error error = new Error(t.getTime(), lineNumber);

        //
        // logger
        //

        if (!restOfTheLine.startsWith("[")) {

            throw new ParsingException("line " + lineNumber + " does not contain [logger...");
        }

        int i = restOfTheLine.indexOf(']');

        String logger = restOfTheLine.substring(1, i);
        error.setLogger(logger);
        restOfTheLine = restOfTheLine.substring(i + 1);

        //
        // thread
        //

        i = restOfTheLine.indexOf('(');

        if (i == -1) {

            throw new ParsingException("line " + lineNumber + " does not contain (thread-name...");
        }

        //
        // walk accounting for embedded parantheses
        //

        int nestingLevel = 0;
        int j;

        for(j = i + 1; j < restOfTheLine.length(); j ++) {

            if (restOfTheLine.charAt(j) == '(') {

                nestingLevel ++;
            }
            else if (restOfTheLine.charAt(j) == ')') {

                if (nestingLevel == 0) {

                    //
                    // end of the thread name
                    //

                    break;
                }

                nestingLevel --;
            }
        }

        if (j == restOfTheLine.length()) {

            throw new ParsingException("line " + lineNumber + ": unbalanced thread name parantheses");
        }

        error.setThreadName(restOfTheLine.substring(i + 1, j));

        restOfTheLine = restOfTheLine.substring(j + 1).trim();

        error.setComment(restOfTheLine);

        context.process(error);
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
