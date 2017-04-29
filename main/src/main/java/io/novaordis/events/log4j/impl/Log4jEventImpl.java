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

import io.novaordis.events.api.event.GenericTimedEvent;
import io.novaordis.events.api.event.StringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public class Log4jEventImpl extends GenericTimedEvent implements Log4jEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String LOG_LEVEL_PROPERTY_NAME = "log-level";
    public static final String LOG_CATEGORY_PROPERTY_NAME = "log-category";
    public static final String THREAD_PROPERTY_NAME = "thread";
    public static final String MESSAGE_PROPERTY_NAME = "message";

    private static final DateFormat TO_STRING_DATE_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm:ss,SSS");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    Log4jEventImpl() {

        this(0L, 0L, null, null, null, null);
    }

    public Log4jEventImpl(
            long lineNumber, long timestamp, Log4jLevel level, String category, String threadName, String message) {

        super(timestamp);
        setLineNumber(lineNumber);
        setLogLevel(level);
        setLogCategory(category);
        setThreadName(threadName);
        setMessage(message);
    }

    // Log4jEvent implementation ---------------------------------------------------------------------------------------

    /**
     * May return null.
     *
     * @exception IllegalStateException if the internal storage for property cannot be converted to Log4jLevel
     */
    @Override
    public Log4jLevel getLogLevel() {

        StringProperty sp = getStringProperty(LOG_LEVEL_PROPERTY_NAME);

        if (sp == null) {

            return null;
        }

        String s = sp.getString();

        if (s == null) {

            return null;
        }

        Log4jLevel level =  Log4jLevel.fromLiteral(s);

        if (level == null) {

            throw new IllegalStateException("invalid '" + LOG_LEVEL_PROPERTY_NAME + "' value: \"" + s + "\"");
        }

        return level;
    }

    @Override
    public void setLogLevel(Log4jLevel level) {

        setStringProperty(LOG_LEVEL_PROPERTY_NAME, level == null ? null : level.toLiteral());
    }

    @Override
    public String getLogCategory() {

        StringProperty sp = getStringProperty(LOG_CATEGORY_PROPERTY_NAME);

        if (sp == null) {

            return null;
        }

        return sp.getString();
    }

    @Override
    public void setLogCategory(String s) {

        setStringProperty(LOG_CATEGORY_PROPERTY_NAME, s);
    }

    @Override
    public String getThreadName() {

        StringProperty sp = getStringProperty(THREAD_PROPERTY_NAME);

        if (sp == null) {

            return null;
        }

        return sp.getString();
    }

    @Override
    public void setThreadName(String s) {

        setStringProperty(THREAD_PROPERTY_NAME, s);
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
    public void setMessage(String s) {

        setStringProperty(MESSAGE_PROPERTY_NAME, s);
    }

    @Override
    public void append(String line) {

        //
        // temporarily ignoring extra lines
        //
    }

    @Override
    public String toString() {


        String s = "";

        s += getLineNumber() + ": ";

        s += TO_STRING_DATE_FORMAT.format(getTime());

        return s;


    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
