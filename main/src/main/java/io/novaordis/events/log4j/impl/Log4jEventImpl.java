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

import io.novaordis.events.api.event.Event;
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
    public static final String THREAD_NAME_PROPERTY_NAME = "thread-name";
    public static final String MESSAGE_PROPERTY_NAME = "message";
    public static final String EXCEPTION_PROPERTY_NAME = "exception";

    private static final DateFormat TO_STRING_DATE_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm:ss,SSS");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

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
        append(rawLine);
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
     * Append the given line to the raw representation of the event. It works with all lines, including the first line.
     */
    public void append(String line) {

        StringProperty p = getStringProperty(Event.RAW_PROPERTY_NAME);

        String rawRepresentation;

        if (p == null) {

            rawRepresentation = line;
        }
        else {

            rawRepresentation = p.getString() + "\n" + line;
        }

        setStringProperty(Event.RAW_PROPERTY_NAME, rawRepresentation);
    }

    public void appendToException(String line) {

        StringProperty p = getStringProperty(EXCEPTION_PROPERTY_NAME);

        if (p == null) {

            p = new StringProperty(EXCEPTION_PROPERTY_NAME);
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
