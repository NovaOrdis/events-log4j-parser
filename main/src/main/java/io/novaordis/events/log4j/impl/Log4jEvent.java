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

import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.events.api.parser.ParsingException;
import io.novaordis.utilities.logging.log4j.Log4jLevel;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public interface Log4jEvent extends TimedEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    static Log4jEvent build(long lineNumber, TimestampMatcher t, String restOfTheLine) throws ParsingException {

        //
        // look for org.apache.log4j.Level values, as represented in Log4jLevel
        //

        int i;

        //
        // skip the leading spaces
        //

        for(i = 0; i < restOfTheLine.length(); i ++) {

            if (restOfTheLine.charAt(i) != ' ') {

                break;
            }
        }

        Log4jLevel level = null;

        for(Log4jLevel l: Log4jLevel.values()) {

            String literal = l.toLiteral();

            if (restOfTheLine.startsWith(literal, i)) {

                level = l;
                i += literal.length();
                break;
            }
        }

        if (level == null) {

            throw new ParsingException("no log level found after timestamp", lineNumber);
        }

        //
        // skip the spaces
        //

        for(; i < restOfTheLine.length(); i ++) {

            if (restOfTheLine.charAt(i) != ' ') {

                break;
            }
        }

        //
        // category
        //

        ParsingResult category = Parsers.find(restOfTheLine, i, '[', ']', lineNumber);

        if (category == null) {

            throw new ParsingException(
                    "no log category found, expecting [...]", lineNumber, t.getIndexOfNextCharInLine() + i);
        }

        i = category.getNext();

        //
        // skip the spaces
        //

        for(; i < restOfTheLine.length(); i ++) {

            if (restOfTheLine.charAt(i) != ' ') {

                break;
            }
        }

        //
        // thread name, look for nested parantheses
        //

        ParsingResult threadName = Parsers.find(restOfTheLine, i, '(', ')', lineNumber);

        if (threadName == null) {

            throw new ParsingException("no thread name found after the log category, expecting (...)", lineNumber);
        }

        i = threadName.getNext();

        //
        // skip the spaces
        //

        for(; i < restOfTheLine.length(); i ++) {

            if (restOfTheLine.charAt(i) != ' ') {

                break;
            }
        }

        String message = null;

        if (i < restOfTheLine.length() - 1) {

            message = restOfTheLine.substring(i);
        }

        return new Log4jEventImpl(lineNumber, t.getTime(), level, category.getValue(), threadName.getValue(), message);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    Log4jLevel getLogLevel();

    void setLogLevel(Log4jLevel level);

    String getLogCategory();

    void setLogCategory(String s);

    String getThreadName();

    void setThreadName(String s);

    String getMessage();

    void setMessage(String s);

    /**
     * For multi-line log events (stack traces).
     */
    void append(String line);

}
