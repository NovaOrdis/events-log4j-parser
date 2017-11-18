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

package io.novaordis.events.log4j.pattern;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.junit.Test;

import io.novaordis.events.log4j.impl.Log4jEvent;
import io.novaordis.events.log4j.impl.Log4jEventImpl;
import io.novaordis.events.log4j.pattern.convspec.Date;
import io.novaordis.events.log4j.pattern.convspec.Level;
import io.novaordis.events.log4j.pattern.convspec.LineSeparator;
import io.novaordis.events.log4j.pattern.convspec.Logger;
import io.novaordis.events.log4j.pattern.convspec.ThreadName;
import io.novaordis.events.log4j.pattern.convspec.wildfly.WildFlyException;
import io.novaordis.events.log4j.pattern.convspec.wildfly.WildFlyMessage;
import io.novaordis.utilities.logging.log4j.Log4jLevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/28/17
 */
public class Log4jPatternLayoutTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NullLiteral() throws Exception {

        try {

            new Log4jPatternLayout(null);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null log4j pattern layout literal"));
        }
    }

    @Test
    public void constructor_TrailingPatternElementMarker() throws Exception {

        try {

            new Log4jPatternLayout("something%");

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains(
                    "'" + Log4jPatternLayout.CONVERSION_SPECIFIER_MARKER + "' not followed by any pattern element"));
        }
    }

    @Test
    public void constructor_InvalidDateFormat() throws Exception {

        try {

            new Log4jPatternLayout("%d{blah}");

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid date format"));
            assertTrue(msg.contains("blah"));
        }
    }

    @Test
    public void constructor_ThereShouldBeAtLeastOneConversionSpecifier() throws Exception {

        try {

            new Log4jPatternLayout("blah");

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("the log4j pattern layout must contain at least on conversion specifier (%...)"));
            assertTrue(msg.contains("blah"));
        }
    }

    @Test
    public void constructor() throws Exception {

        Log4jPatternLayout pl = new Log4jPatternLayout("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n");

        assertEquals("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n", pl.getLiteral());

        assertEquals(11, pl.getPatternComponentCount());

        Iterator<ConversionPatternComponent> i = pl.getPatternComponentIterator();

        assertTrue(i.hasNext());

        Date d = (Date)i.next();

        assertEquals("%d{HH:mm:ss,SSS}", d.getLiteral());
        assertEquals("HH:mm:ss,SSS", d.getSimpleDateFormat().toPattern());
        assertEquals(Date.CONVERSION_CHARACTER, d.getConversionCharacter().charValue());

        assertTrue(i.hasNext());

        LiteralText l = (LiteralText)i.next();

        assertEquals(" ", l.getLiteral());

        assertTrue(i.hasNext());

        Level p = (Level)i.next();

        assertEquals("%-5p", p.getLiteral());
        FormatModifier m = p.getFormatModifier();
        assertEquals("-5", m.getLiteral());
        assertEquals(Level.CONVERSION_CHARACTER, p.getConversionCharacter().charValue());

        assertTrue(i.hasNext());

        LiteralText l2 = (LiteralText)i.next();

        assertEquals(" [", l2.getLiteral());

        assertTrue(i.hasNext());

        Logger c = (Logger)i.next();

        assertEquals("%c", c.getLiteral());
        assertNull(c.getFormatModifier());
        assertEquals(Logger.CONVERSION_CHARACTER, c.getConversionCharacter().charValue());

        assertTrue(i.hasNext());

        LiteralText l3 = (LiteralText)i.next();

        assertEquals("] (", l3.getLiteral());

        assertTrue(i.hasNext());

        ThreadName t = (ThreadName)i.next();

        assertEquals("%t", t.getLiteral());
        assertNull(t.getFormatModifier());
        assertEquals(ThreadName.CONVERSION_CHARACTER, t.getConversionCharacter().charValue());

        assertTrue(i.hasNext());

        LiteralText l4 = (LiteralText)i.next();

        assertEquals(") ", l4.getLiteral());

        assertTrue(i.hasNext());

        WildFlyMessage u = (WildFlyMessage)i.next();

        assertEquals("%s", u.getLiteral());
        assertEquals('s', u.getConversionCharacter().charValue());

        assertTrue(i.hasNext());

        WildFlyException u2 = (WildFlyException)i.next();

        assertEquals("%E", u2.getLiteral());
        assertEquals('E', u2.getConversionCharacter().charValue());

        assertTrue(i.hasNext());

        LineSeparator n = (LineSeparator)i.next();
        assertNull(n.getFormatModifier());
        assertEquals(LineSeparator.CONVERSION_CHARACTER, n.getConversionCharacter().charValue());

        assertFalse(i.hasNext());
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse() throws Exception {

        String patternLayoutSpecification = "%m%n";

        Log4jPatternLayout patternLayout = new Log4jPatternLayout(patternLayoutSpecification);

        String logLine = "some message";

        Log4jEventImpl e = patternLayout.parse(7, logLine);

        String message = e.getMessage();

        assertEquals("some message", message);

        assertEquals(7L, e.getLineNumber().longValue());

        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());
    }

    @Test
    public void parse_ExceptionRendered_NoLineSeparator() throws Exception {

        String patternLayoutSpecification = "%s%E";

        Log4jPatternLayout patternLayout = new Log4jPatternLayout(patternLayoutSpecification);

        String logLine = "message: com.microsoft.sqlserver.jdbc.SQLServerException: something else";

        Log4jEventImpl e = patternLayout.parse(8, logLine);

        String message = e.getMessage();

        assertEquals("message", message);

        String exception = e.getExceptionRendering();

        assertEquals(": com.microsoft.sqlserver.jdbc.SQLServerException: something else", exception);

        assertEquals(8L, e.getLineNumber().longValue());

        assertEquals(Log4jEventImpl.EXCEPTION_APPEND_MODE, e.getAppendMode());
    }

    @Test
    public void parse_ExceptionRendered_LineSeparator() throws Exception {

        String patternLayoutSpecification = "%s%E%n";

        Log4jPatternLayout patternLayout = new Log4jPatternLayout(patternLayoutSpecification);

        String logLine = "message: com.microsoft.sqlserver.jdbc.SQLServerException: something else";

        Log4jEventImpl e = patternLayout.parse(9L, logLine);

        String message = e.getMessage();

        assertEquals("message", message);

        String exception = e.getExceptionRendering();

        assertEquals(": com.microsoft.sqlserver.jdbc.SQLServerException: something else", exception);

        assertEquals(9L, e.getLineNumber().longValue());

        assertEquals(Log4jEventImpl.EXCEPTION_APPEND_MODE, e.getAppendMode());
    }

    @Test
    public void parse_ExceptionNotRendered_NoLineSeparator() throws Exception {

        String patternLayoutSpecification = "%s%E";

        Log4jPatternLayout patternLayout = new Log4jPatternLayout(patternLayoutSpecification);

        String logLine = "something: blah";

        Log4jEventImpl e = patternLayout.parse(10L, logLine);

        String message = e.getMessage();

        assertEquals("something: blah", message);

        String exception = e.getExceptionRendering();

        assertNull(exception);

        assertEquals(10L, e.getLineNumber().longValue());

        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());
    }

    @Test
    public void parse_ExceptionNotRendered_LineSeparator() throws Exception {

        String patternLayoutSpecification = "%s%E%n";

        Log4jPatternLayout patternLayout = new Log4jPatternLayout(patternLayoutSpecification);

        String logLine = "something: blah";

        Log4jEventImpl e = patternLayout.parse(7L, logLine);

        String message = e.getMessage();

        assertEquals("something: blah", message);

        String exception = e.getExceptionRendering();

        assertNull(exception);

        assertEquals(7L, e.getLineNumber().longValue());

        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());
    }

    @Test
    public void parse3() throws Exception {

        String patternLayoutSpecification = "%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n";

        Log4jPatternLayout patternLayout = new Log4jPatternLayout(patternLayoutSpecification);

        String logLine = "09:01:56,538 INFO  [org.xnio] (MSC service thread 1-3) XNIO Version 3.0.16.GA-redhat-1";

        Log4jEventImpl e = patternLayout.parse(7L, logLine);

        long time = e.getTime();

        assertEquals(time, new SimpleDateFormat("HH:mm:ss,SSS").parse("09:01:56,538").getTime());

        String message = e.getMessage();

        assertEquals("XNIO Version 3.0.16.GA-redhat-1", message);

        String exception = e.getExceptionRendering();

        assertNull(exception);

        assertEquals(7L, e.getLineNumber().longValue());

        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());
    }

    @Test
    public void parse4() throws Exception {

        String line = "09:01:55,011 INFO  [org.jboss.modules] (main) JBoss Modules version 1.3.8.Final-redhat-1";

        Log4jPatternLayout patternLayout = new Log4jPatternLayout("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n");

        int elementCount = patternLayout.getPatternComponentCount();

        assertEquals(11, elementCount);

        Log4jEventImpl e = patternLayout.parse(7L, line);

        Long time = e.getTime();
        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("09:01:55,011").getTime(), time.longValue());

        assertEquals(Log4jLevel.INFO, e.getLevel());

        assertEquals("org.jboss.modules", e.getLogger());

        assertEquals("main", e.getThreadName());

        assertEquals("JBoss Modules version 1.3.8.Final-redhat-1", e.getMessage());

        assertEquals(7L, e.getLineNumber().longValue());

        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());
    }

    @Test
    public void parse_Exception() throws Exception {

        String[] content = new String[] {

                "18:14:06,481 ERROR [io.novaordis.playground.jee.servlet.simplest.ServletExample] (http-127.0.0.1:8080-1) a message that accompanies an exception: java.lang.Exception: SYNTHETIC",
                "\tat io.novaordis.playground.jee.servlet.simplest.ServletExample.doGet(ServletExample.java:69) [classes:]",
                "",
                "18:14:06,482 INFO  [io.novaordis.playground.jee.servlet.simplest.ServletExample2] (http-127.0.0.1:8080-2) getHostName()"
        };

        String patternLayoutString = "%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n";

        Log4jPatternLayout patternLayout = new Log4jPatternLayout(patternLayoutString);

        Log4jEventImpl e = patternLayout.parse(1, content[0]);

        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("18:14:06,481").getTime(), e.getTime().longValue());
        assertEquals(Log4jLevel.ERROR, e.getLevel());
        assertEquals("io.novaordis.playground.jee.servlet.simplest.ServletExample", e.getLogger());
        assertEquals("http-127.0.0.1:8080-1", e.getThreadName());
        assertEquals("a message that accompanies an exception", e.getMessage());

        assertEquals(": java.lang.Exception: SYNTHETIC", e.getExceptionRendering());

        assertEquals(Log4jEventImpl.EXCEPTION_APPEND_MODE, e.getAppendMode());

        //
        // the Log4jPatternLayout instance must fail when asked to parse an exception stack trace line, the upper
        // layer must add that line to the current Log4jEvent instance.
        //

        try {

            patternLayout.parse(2, content[1]);

            fail("should have thrown exceptions");
        }
        catch(Log4jPatternLayoutException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains(
                    "date rendering string is shorted that what would have been expected given the pattern"));
            assertTrue(msg.contains("HH:mm:ss,SSS"));
        }

        //
        // instead the line should be added directly
        //

        e.appendLine(content[1]);

        assertEquals(
                ": java.lang.Exception: SYNTHETIC\n" +
                        "\tat io.novaordis.playground.jee.servlet.simplest.ServletExample.doGet(ServletExample.java:69) [classes:]",
                e.getExceptionRendering());

        assertEquals(Log4jEventImpl.EXCEPTION_APPEND_MODE, e.getAppendMode());

        //
        // the Log4jPatternLayout instance must fail when asked to parse an exception stack trace line, the upper
        // layer must add that line to the current Log4jEvent instance.
        //

        try {

            patternLayout.parse(3, content[2]);

            fail("should have thrown exceptions");
        }
        catch(Log4jPatternLayoutException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains("empty string when expecting a time stamp"));
        }

        //
        // instead the line should be added directly
        //

        //
        // empty line turns off exception collection mode
        //

        e.appendLine(content[2]);

        assertEquals(
                ": java.lang.Exception: SYNTHETIC\n" +
                        "\tat io.novaordis.playground.jee.servlet.simplest.ServletExample.doGet(ServletExample.java:69) [classes:]\n",
                e.getExceptionRendering());

        assertEquals(Log4jEventImpl.EXCEPTION_APPEND_MODE, e.getAppendMode());

        Log4jEvent e2 = patternLayout.parse(4, content[3]);

        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("18:14:06,482").getTime(), e2.getTime().longValue());
        assertEquals(Log4jLevel.INFO, e2.getLevel());
        assertEquals("io.novaordis.playground.jee.servlet.simplest.ServletExample2", e2.getLogger());
        assertEquals("http-127.0.0.1:8080-2", e2.getThreadName());
        assertEquals("getHostName()", e2.getMessage());

        assertNull(e2.getExceptionRendering());

        assertEquals(Log4jEventImpl.EXCEPTION_APPEND_MODE, e.getAppendMode());
    }

    @Test
    public void parse_MultiLineMessage() throws Exception {

        String[] content  = new String[] {

                "20:00:00,000 ERROR [a.B] (thread-C) multi-line message follows:",
                "\tA",
                "",
                "\tB",
                "22:22:22,222 INFO  [d.E] (thread-F) green"
        };

        Log4jPatternLayout patternLayout = new Log4jPatternLayout("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n");

        Log4jEventImpl e = patternLayout.parse(1, content[0]);

        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("20:00:00,000").getTime(), e.getTime().longValue());
        assertEquals(Log4jLevel.ERROR, e.getLevel());
        assertEquals("a.B", e.getLogger());
        assertEquals("thread-C", e.getThreadName());
        assertEquals("multi-line message follows:", e.getMessage());
        assertNull(e.getExceptionRendering());
        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());

        //
        // the Log4jPatternLayout instance must fail when asked to parse unformatted line, the upper layer must add
        // that line to the current Log4jEvent instance.
        //

        try {

            patternLayout.parse(2, content[1]);

            fail("should have thrown exceptions");
        }
        catch(Log4jPatternLayoutException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains(
                    "date rendering string is shorted that what would have been expected given the pattern"));
            assertTrue(msg.contains("HH:mm:ss,SSS"));
        }

        //
        // instead the line should be added directly
        //

        e.appendLine(content[1]);

        assertEquals("multi-line message follows:\n\tA", e.getMessage());
        assertNull(e.getExceptionRendering());
        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());

        //
        // the Log4jPatternLayout instance must fail when asked to parse unformatted line, the upper layer must add
        // that line to the current Log4jEvent instance.
        //

        try {

            patternLayout.parse(3, content[2]);

            fail("should have thrown exceptions");
        }
        catch(Log4jPatternLayoutException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains("empty string when expecting a time stamp"));
        }

        //
        // instead the line should be added directly
        //

        e.appendLine(content[2]);

        assertEquals("multi-line message follows:\n\tA\n", e.getMessage());
        assertNull(e.getExceptionRendering());
        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());


        //
        // the Log4jPatternLayout instance must fail when asked to parse unformatted line, the upper layer must add
        // that line to the current Log4jEvent instance.
        //

        try {

            patternLayout.parse(4, content[3]);

            fail("should have thrown exceptions");
        }
        catch(Log4jPatternLayoutException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains(
                    "date rendering string is shorted that what would have been expected given the pattern"));
            assertTrue(msg.contains("HH:mm:ss,SSS"));
        }

        //
        // instead the line should be added directly
        //

        e.appendLine(content[3]);

        assertEquals("multi-line message follows:\n\tA\n\n\tB", e.getMessage());
        assertNull(e.getExceptionRendering());
        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());


        Log4jEvent e2 = patternLayout.parse(5, content[4]);

        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("22:22:22,222").getTime(), e2.getTime().longValue());
        assertEquals(Log4jLevel.INFO, e2.getLevel());
        assertEquals("d.E", e2.getLogger());
        assertEquals("thread-F", e2.getThreadName());
        assertEquals("green", e2.getMessage());

        assertNull(e2.getExceptionRendering());

        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
