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

import java.util.StringTokenizer;

import org.junit.Test;

import io.novaordis.events.log4j.pattern.Log4jPatternLayout;
import io.novaordis.utilities.logging.log4j.Log4jLevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public class Log4jEventImplTest extends Log4jEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void invalidLogLevelStorage() throws Exception {

        Log4jEventImpl e = getLog4jEventToTest();

        String s = "something that cannot be a log level";

        e.setStringProperty(Log4jEventImpl.LEVEL_PROPERTY_NAME, s);

        try {

            e.getLevel();
            fail("should have failed");
        }
        catch(IllegalStateException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.equals("invalid '" + Log4jEventImpl.LEVEL_PROPERTY_NAME + "' value: \"" + s + "\""));
        }
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // log level -------------------------------------------------------------------------------------------------------

    @Test
    public void logLevel_Mutation() throws Exception {

        Log4jEventImpl e = getLog4jEventToTest();

        e.setLevel(Log4jLevel.ERROR);

        assertEquals(Log4jLevel.ERROR, e.getLevel());

        e.setLevel(null);

        assertNull(e.getLevel());
    }

    // log category ----------------------------------------------------------------------------------------------------

    @Test
    public void logCategory_Mutation() throws Exception {

        Log4jEventImpl e = getLog4jEventToTest();

        e.setLogger("io.novaordis");

        assertEquals("io.novaordis", e.getLogger());

        e.setLogger(null);

        assertNull(e.getLogger());
    }

    // thread name -----------------------------------------------------------------------------------------------------

    @Test
    public void threadName_Mutation() throws Exception {

        Log4jEventImpl e = getLog4jEventToTest();

        e.setThreadName("some thread name");

        assertEquals("some thread name", e.getThreadName());

        e.setThreadName(null);

        assertNull(e.getThreadName());
    }

    // message ---------------------------------------------------------------------------------------------------------

    @Test
    public void message_Mutation() throws Exception {

        Log4jEventImpl e = getLog4jEventToTest();

        e.setMessage("some message");

        assertEquals("some message", e.getMessage());

        e.setMessage(null);

        assertNull(e.getMessage());
    }

    // raw representation/append() -------------------------------------------------------------------------------------

    @Test
    public void rawRepresentation() throws Exception {

        String line = "20:00:00 multi-line message\n sub-line-1\nsub-line-2";

        Log4jParser p = new Log4jParser(new Log4jPatternLayout("%d{HH:mm:ss} %m%n"));

        int i = 1;

        for(StringTokenizer st = new StringTokenizer(line, "\n"); st.hasMoreTokens(); i ++) {

            p.parse(i, st.nextToken());
        }

        Log4jEventImpl e = (Log4jEventImpl)p.close().get(0);

        assertEquals("multi-line message\n sub-line-1\nsub-line-2", e.getMessage());

        String raw = e.getRawRepresentation();

        assertEquals(line, raw);
    }

    // append mode -----------------------------------------------------------------------------------------------------

    @Test
    public void defaultAppendMode() throws Exception {

        Log4jEventImpl le = new Log4jEventImpl();

        assertEquals(Log4jEventImpl.NO_APPEND_MODE, le.getAppendMode());
    }

    @Test
    public void setAppendMode_NO_APPEND_MODE() throws Exception {

        Log4jEventImpl le = new Log4jEventImpl();

        le.setAppendMode(Log4jEventImpl.NO_APPEND_MODE);

        assertEquals(Log4jEventImpl.NO_APPEND_MODE, le.getAppendMode());
    }

    @Test
    public void setAppendMode_EXCEPTION_APPEND_MODE() throws Exception {

        Log4jEventImpl le = new Log4jEventImpl();

        le.setAppendMode(Log4jEventImpl.EXCEPTION_APPEND_MODE);

        assertEquals(Log4jEventImpl.EXCEPTION_APPEND_MODE, le.getAppendMode());
    }

    @Test
    public void setAppendMode_MESSAGE_APPEND_MODE() throws Exception {

        Log4jEventImpl le = new Log4jEventImpl();

        le.setAppendMode(Log4jEventImpl.MESSAGE_APPEND_MODE);

        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, le.getAppendMode());
    }

    @Test
    public void setAppendMode_Invalid() throws Exception {

        Log4jEventImpl le = new Log4jEventImpl();

        try {

            le.setAppendMode((byte) 5);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid append mode"));
            assertTrue(msg.contains("5"));
        }
    }

    // appendLine() ----------------------------------------------------------------------------------------------------

    @Test
    public void appendLine_Null() throws Exception {

        Log4jEventImpl le = new Log4jEventImpl();

        le.setAppendMode(Log4jEventImpl.EXCEPTION_APPEND_MODE);

        try {

            le.appendLine(null);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null line"));
        }
    }

    @Test
    public void appendLine_NotInAppendMode() throws Exception {

        Log4jEventImpl le = new Log4jEventImpl();

        assertEquals(Log4jEventImpl.NO_APPEND_MODE, le.getAppendMode());

        try {

            le.appendLine("something");

            fail("should have thrown exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("not in append mode"));
        }
    }

    @Test
    public void appendLine_ExceptionMode_NoRaw() throws Exception {

        Log4jEventImpl le = new Log4jEventImpl();

        le.setAppendMode(Log4jEventImpl.EXCEPTION_APPEND_MODE);

        assertNull(le.getExceptionRendering());
        assertNull(le.getMessage());
        assertNull(le.getRawRepresentation());

        le.appendLine("line 1");

        assertEquals("line 1", le.getExceptionRendering());
        assertEquals("line 1", le.getRawRepresentation());
        assertNull(le.getMessage());

        le.appendLine("line 2");

        assertEquals("line 1\nline 2", le.getExceptionRendering());
        assertEquals("line 1\nline 2", le.getRawRepresentation());
        assertNull(le.getMessage());

        le.appendLine(" ");

        assertEquals("line 1\nline 2\n ", le.getExceptionRendering());
        assertEquals("line 1\nline 2\n ", le.getRawRepresentation());
        assertNull(le.getMessage());

        le.appendLine("\t");

        assertEquals("line 1\nline 2\n \n\t", le.getExceptionRendering());
        assertEquals("line 1\nline 2\n \n\t", le.getRawRepresentation());
        assertNull(le.getMessage());

        le.appendLine("");

        assertEquals("line 1\nline 2\n \n\t\n", le.getExceptionRendering());
        assertEquals("line 1\nline 2\n \n\t\n", le.getRawRepresentation());
        assertNull(le.getMessage());

        le.appendLine("line 3");

        assertEquals("line 1\nline 2\n \n\t\n\nline 3", le.getExceptionRendering());
        assertEquals("line 1\nline 2\n \n\t\n\nline 3", le.getRawRepresentation());
        assertNull(le.getMessage());
    }

    @Test
    public void appendLine_ExceptionMode_WithRaw() throws Exception {

        Log4jEventImpl le = new Log4jEventImpl();

        le.setAppendMode(Log4jEventImpl.EXCEPTION_APPEND_MODE);

        le.appendRawLine("first line");

        assertNull(le.getExceptionRendering());
        assertNull(le.getMessage());
        assertEquals("first line", le.getRawRepresentation());

        le.appendLine("line 1");

        assertEquals("line 1", le.getExceptionRendering());
        assertNull(le.getMessage());
        assertEquals("first line\nline 1", le.getRawRepresentation());

        le.appendLine("line 2");

        assertEquals("line 1\nline 2", le.getExceptionRendering());
        assertNull(le.getMessage());
        assertEquals("first line\nline 1\nline 2", le.getRawRepresentation());

        le.appendLine(" ");

        assertEquals("line 1\nline 2\n ", le.getExceptionRendering());
        assertNull(le.getMessage());
        assertEquals("first line\nline 1\nline 2\n ", le.getRawRepresentation());

        le.appendLine("\t");

        assertEquals("line 1\nline 2\n \n\t", le.getExceptionRendering());
        assertNull(le.getMessage());
        assertEquals("first line\nline 1\nline 2\n \n\t", le.getRawRepresentation());

        le.appendLine("");

        assertEquals("line 1\nline 2\n \n\t\n", le.getExceptionRendering());
        assertNull(le.getMessage());
        assertEquals("first line\nline 1\nline 2\n \n\t\n", le.getRawRepresentation());

        le.appendLine("line 3");

        assertEquals("line 1\nline 2\n \n\t\n\nline 3", le.getExceptionRendering());
        assertNull(le.getMessage());
        assertEquals("first line\nline 1\nline 2\n \n\t\n\nline 3", le.getRawRepresentation());
    }

    @Test
    public void appendLine_MessageMode_NoRaw() throws Exception {

        Log4jEventImpl le = new Log4jEventImpl();

        le.setAppendMode(Log4jEventImpl.MESSAGE_APPEND_MODE);

        assertNull(le.getExceptionRendering());
        assertNull(le.getMessage());
        assertNull(le.getRawRepresentation());

        le.appendLine("line 1");

        assertEquals("line 1", le.getMessage());
        assertEquals("line 1", le.getRawRepresentation());
        assertNull(le.getExceptionRendering());

        le.appendLine("line 2");

        assertEquals("line 1\nline 2", le.getMessage());
        assertEquals("line 1\nline 2", le.getRawRepresentation());
        assertNull(le.getExceptionRendering());

        le.appendLine(" ");

        assertEquals("line 1\nline 2\n ", le.getMessage());
        assertEquals("line 1\nline 2\n ", le.getRawRepresentation());
        assertNull(le.getExceptionRendering());

        le.appendLine("\t");

        assertEquals("line 1\nline 2\n \n\t", le.getMessage());
        assertEquals("line 1\nline 2\n \n\t", le.getRawRepresentation());
        assertNull(le.getExceptionRendering());

        le.appendLine("");

        assertEquals("line 1\nline 2\n \n\t\n", le.getMessage());
        assertEquals("line 1\nline 2\n \n\t\n", le.getRawRepresentation());
        assertNull(le.getExceptionRendering());

        le.appendLine("line 3");

        assertEquals("line 1\nline 2\n \n\t\n\nline 3", le.getMessage());
        assertEquals("line 1\nline 2\n \n\t\n\nline 3", le.getRawRepresentation());
        assertNull(le.getExceptionRendering());
    }

    @Test
    public void appendLine_MessageMode_WithRaw() throws Exception {

        Log4jEventImpl le = new Log4jEventImpl();

        le.appendRawLine("first line");

        le.setAppendMode(Log4jEventImpl.MESSAGE_APPEND_MODE);

        assertNull(le.getExceptionRendering());
        assertNull(le.getMessage());
        assertEquals("first line", le.getRawRepresentation());

        le.appendLine("line 1");

        assertEquals("line 1", le.getMessage());
        assertNull(le.getExceptionRendering());
        assertEquals("first line\nline 1", le.getRawRepresentation());

        le.appendLine("line 2");

        assertEquals("line 1\nline 2", le.getMessage());
        assertNull(le.getExceptionRendering());
        assertEquals("first line\nline 1\nline 2", le.getRawRepresentation());

        le.appendLine(" ");

        assertEquals("line 1\nline 2\n ", le.getMessage());
        assertNull(le.getExceptionRendering());
        assertEquals("first line\nline 1\nline 2\n ", le.getRawRepresentation());

        le.appendLine("\t");

        assertEquals("line 1\nline 2\n \n\t", le.getMessage());
        assertNull(le.getExceptionRendering());
        assertEquals("first line\nline 1\nline 2\n \n\t", le.getRawRepresentation());

        le.appendLine("");

        assertEquals("line 1\nline 2\n \n\t\n", le.getMessage());
        assertNull(le.getExceptionRendering());
        assertEquals("first line\nline 1\nline 2\n \n\t\n", le.getRawRepresentation());

        le.appendLine("line 3");

        assertEquals("line 1\nline 2\n \n\t\n\nline 3", le.getMessage());
        assertNull(le.getExceptionRendering());
        assertEquals("first line\nline 1\nline 2\n \n\t\n\nline 3", le.getRawRepresentation());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Log4jEventImpl getLog4jEventToTest() throws Exception {

        return new Log4jEventImpl();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
