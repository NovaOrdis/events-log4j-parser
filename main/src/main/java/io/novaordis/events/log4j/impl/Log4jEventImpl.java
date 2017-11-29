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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.novaordis.events.api.event.GenericTimedEvent;
import io.novaordis.events.api.event.StringProperty;
import io.novaordis.utilities.logging.log4j.Log4jLevel;

/**
 * Log4jEventImpl instances is capable of parsing multi-line exception renderings, when introduced by %E.
 *
 * https://kb.novaordis.com/index.php/Log4j_Pattern_Layout#.27E.27_WildFly_Exception
 *
 * Once the '%E' conversion character is identified, the instance enters a "exception collection" mode.
 *
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public class Log4jEventImpl extends GenericTimedEvent implements Log4jEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String LEVEL_PROPERTY_NAME = "level";
    public static final String LOGGER_PROPERTY_NAME = "logger";
    public static final String THREAD_NAME_PROPERTY_NAME = "thread";
    public static final String MESSAGE_PROPERTY_NAME = "msg";
    public static final String EXCEPTION_PROPERTY_NAME = "exception";

    private static final DateFormat TO_STRING_DATE_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm:ss,SSS");

    public static final byte NO_APPEND_MODE = 0;
    public static final byte EXCEPTION_APPEND_MODE = 1;
    public static final byte MESSAGE_APPEND_MODE = 2;

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private byte appendMode;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Log4jEventImpl() {

        this(0L, 0L, null, null, null, null, null);
    }

    /**
     * @param rawLine the unparsed raw line, the way it appears in the log. Necessary to keep a raw representation
     *                of the event.
     */
    public Log4jEventImpl(long lineNumber, long timestamp, Log4jLevel level, String category, String threadName,
                          String message, String rawLine) {

        super(timestamp);

        setLineNumber(lineNumber);
        setLevel(level);
        setLogger(category);
        setThreadName(threadName);
        setMessage(message);
        appendRawLine(rawLine);

        //
        // not in append mode
        //
        this.appendMode = NO_APPEND_MODE;
    }

    // Log4jEvent implementation ---------------------------------------------------------------------------------------

    /**
     * May return null.
     *
     * @exception IllegalStateException if the internal storage for property cannot be converted to Log4jLevel
     */
    @Override
    public Log4jLevel getLevel() {

        StringProperty sp = getStringProperty(LEVEL_PROPERTY_NAME);

        if (sp == null) {

            return null;
        }

        String s = sp.getString();

        if (s == null) {

            return null;
        }

        Log4jLevel level =  Log4jLevel.fromLiteral(s);

        if (level == null) {

            throw new IllegalStateException("invalid '" + LEVEL_PROPERTY_NAME + "' value: \"" + s + "\"");
        }

        return level;
    }

    @Override
    public String getLogger() {

        StringProperty sp = getStringProperty(LOGGER_PROPERTY_NAME);

        if (sp == null) {

            return null;
        }

        return sp.getString();
    }

    @Override
    public String getThreadName() {

        StringProperty sp = getStringProperty(THREAD_NAME_PROPERTY_NAME);

        if (sp == null) {

            return null;
        }

        return sp.getString();
    }

    @Override
    public String getMessage() {

        StringProperty sp = getStringProperty(MESSAGE_PROPERTY_NAME);

        if (sp == null) {

            return null;
        }

        return sp.getString();
    }

    @Override
    public String getExceptionRendering() {

        StringProperty sp = getStringProperty(EXCEPTION_PROPERTY_NAME);

        if (sp == null) {

            return null;
        }

        return sp.getString();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public void setLevel(Log4jLevel level) {

        setStringProperty(LEVEL_PROPERTY_NAME, level == null ? null : level.toLiteral());
    }

    public void setLogger(String s) {

        setStringProperty(LOGGER_PROPERTY_NAME, s);
    }

    public void setThreadName(String s) {

        setStringProperty(THREAD_NAME_PROPERTY_NAME, s);
    }

    public void setMessage(String s) {

        setStringProperty(MESSAGE_PROPERTY_NAME, s);
    }

    /**
     * @return the append mode (EXCEPTION_APPEND_MODE, MESSAGE_APPEND_MODE). Anything else means "not in append mode",
     * and appendLine() invocation will throw exception.
     */
    public byte getAppendMode() {

        return appendMode;
    }

    /**
     * Set the append mode to NO_APPEND_MODE|EXCEPTION_APPEND_MODE|MESSAGE_APPEND_MODE.
     */
    public void setAppendMode(byte m) {

        if (NO_APPEND_MODE != m && EXCEPTION_APPEND_MODE != m && MESSAGE_APPEND_MODE != m) {

            throw new IllegalArgumentException("invalid append mode: " + m);
        }

        this.appendMode = m;
    }

    /**
     * Use it to append lines following a formatted log event rendering line. These lines belong either to a
     * multi-line message or to an exception stack trace. The Log4jEventImpl instance will interpret the semantics
     * of the line being appended relative to the value of the appendMode flag. Possible append modes:
     * EXCEPTION_APPEND_MODE, MESSAGE_APPEND_MODE. The method updates the raw representation internally.
     *
     * It is imperative that the whole log line is added in a single appendLine() invocation, otherwise the line number
     * counting will fail.
     *
     * @param line may be an empty line, but never null.
     *
     * @exception IllegalStateException if the instance is not in append mode.
     */
    public void appendLine(String line) {

        if (line == null) {

            throw new IllegalArgumentException("null line");
        }

        String propertyName;

        if (MESSAGE_APPEND_MODE == appendMode) {

            propertyName = MESSAGE_PROPERTY_NAME;
        }
        else if (EXCEPTION_APPEND_MODE == appendMode) {

            propertyName = EXCEPTION_PROPERTY_NAME;
        }
        else {

            throw new IllegalStateException("not in append mode");
        }

        StringProperty p = getStringProperty(propertyName);

        if (p == null) {

            p = new StringProperty(propertyName);

            setProperty(p);
        }

        String s = p.getString();

        if (s == null) {

            s = line;
        }
        else {

            s += "\n";
            s += line;
        }

        p.setValue(s);

        appendRawLine(line);
    }

    @Override
    public String toString() {

        String s = "";

        s += getLineNumber() + ", ";

        s += TO_STRING_DATE_FORMAT.format(getTime()) + ", ";

        s += getLevel() + ", ";

        s += getLogger() + ", ";

        s += getMessage();

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
