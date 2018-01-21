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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import io.novaordis.events.api.event.EndOfStreamEvent;
import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.events.log4j.pattern.Log4jPatternLayout;
import io.novaordis.utilities.Files;
import io.novaordis.utilities.logging.log4j.Log4jLevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public class Log4jParserTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        Log4jParser p = new Log4jParser();

        assertNull(p.getPatternLayout());
    }

    // setPatternLayout()/getPatternLayout() ---------------------------------------------------------------------------

    @Test
    public void setAndGetPatterLayout() throws Exception {

        Log4jParser p = new Log4jParser();

        Log4jPatternLayout l = new Log4jPatternLayout("%c");

        p.setPatternLayout(l);

        Log4jPatternLayout l2 = p.getPatternLayout();

        assertEquals(l, l2);
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse_Log4jPatternLayout() throws Exception {

        String line = "09:01:55,011 INFO  [org.jboss.modules] (main) JBoss Modules version 1.3.8.Final-redhat-1";

        Log4jParser p = new Log4jParser();

        Log4jPatternLayout layout = new Log4jPatternLayout("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n");

        p.setPatternLayout(layout);

        List<Event> events = p.parse(7, line, null);
        assertTrue(events.isEmpty());

        List<Event> events2 = p.close();
        assertEquals(2, events2.size());

        assertTrue(events2.get(1) instanceof EndOfStreamEvent);

        Log4jEvent e = (Log4jEvent)events2.get(0);

        Long time = e.getTime();
        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("09:01:55,011").getTime(), time.longValue());

        Log4jLevel level = e.getLevel();
        assertEquals(Log4jLevel.INFO, level);

        String logger = e.getLogger();
        assertEquals("org.jboss.modules", logger);

        String threadName = e.getThreadName();
        assertEquals("main", threadName);

        String message = e.getMessage();
        assertEquals("JBoss Modules version 1.3.8.Final-redhat-1", message);

        long lineNumber = e.getLineNumber();
        assertEquals(7, lineNumber);

        String raw = e.getRawRepresentation();
        assertEquals(line, raw);
    }

    @Test
    public void parse_Log4jPatternLayout_Exception() throws Exception {

        String[] content  = new String[] {

                "20:00:00,000 ERROR [io.novaordis.A] (thread-1) red: a.b.C: blue",
                "\texception line 1",
                "",
                "22:22:22,222 INFO  [io.novaordis.B] (thread-2) green"
        };

        Log4jPatternLayout patternLayout = new Log4jPatternLayout("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n");

        Log4jParser p = new Log4jParser();

        p.setPatternLayout(patternLayout);

        List<Event> events = p.parse(1, content[0], null);

        assertTrue(events.isEmpty());

        List<Event> events2 = p.parse(2, content[1], null);

        assertTrue(events2.isEmpty());

        List<Event> events3 = p.parse(3, content[2], null);

        assertTrue(events3.isEmpty());

        List<Event> events4 = p.parse(4, content[3], null);

        assertEquals(1, events4.size());

        Log4jEventImpl e = (Log4jEventImpl)events4.get(0);

        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("20:00:00,000").getTime(), e.getTime().longValue());
        assertEquals(Log4jLevel.ERROR, e.getLevel());
        assertEquals("io.novaordis.A", e.getLogger());
        assertEquals("thread-1", e.getThreadName());
        assertEquals("red", e.getMessage());

        assertEquals(": a.b.C: blue\n\texception line 1\n", e.getExceptionRendering());

        assertEquals(Log4jEventImpl.EXCEPTION_APPEND_MODE, e.getAppendMode());

        List<Event> events5 = p.close();

        assertEquals(2, events5.size());

        assertTrue(events5.get(1) instanceof EndOfStreamEvent);

        Log4jEvent e2 = (Log4jEvent) events5.get(0);

        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("22:22:22,222").getTime(), e2.getTime().longValue());
        assertEquals(Log4jLevel.INFO, e2.getLevel());
        assertEquals("io.novaordis.B", e2.getLogger());
        assertEquals("thread-2", e2.getThreadName());
        assertEquals("green", e2.getMessage());

        assertNull(e2.getExceptionRendering());

        assertEquals(Log4jEventImpl.EXCEPTION_APPEND_MODE, e.getAppendMode());
    }

    @Test
    public void parse_Log4jPatternLayout_MultiLineComment() throws Exception {

        String[] content  = new String[] {

                "20:00:00,000 ERROR [a.B] (thread-C) multi-line message follows:",
                "\tA",
                "",
                "\tB",
                "21:11:11,111 INFO  [d.E] (thread-F) green",
                "22:22:22,222 WARN  [g.H] (thread-I) blue",
        };

        Log4jPatternLayout patternLayout = new Log4jPatternLayout("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n");

        Log4jParser p = new Log4jParser();

        p.setPatternLayout(patternLayout);

        List<Event> events = p.parse(1, content[0], null);
        assertTrue(events.isEmpty());

        List<Event> events2 = p.parse(2, content[1], null);
        assertTrue(events2.isEmpty());

        List<Event> events3 = p.parse(3, content[2], null);
        assertTrue(events3.isEmpty());

        List<Event> events4 = p.parse(4, content[3], null);
        assertTrue(events4.isEmpty());

        List<Event> events5 = p.parse(5, content[4], null);

        assertEquals(1, events5.size());

        Log4jEventImpl e = (Log4jEventImpl)events5.get(0);

        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("20:00:00,000").getTime(), e.getTime().longValue());
        assertEquals(Log4jLevel.ERROR, e.getLevel());
        assertEquals("a.B", e.getLogger());
        assertEquals("thread-C", e.getThreadName());
        assertEquals("multi-line message follows:\n\tA\n\n\tB", e.getMessage());
        assertNull(e.getExceptionRendering());
        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());

        List<Event> events6 = p.parse(6, content[5], null);

        assertEquals(1, events6.size());

        Log4jEvent e2 = (Log4jEvent)events6.get(0);

        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("21:11:11,111").getTime(), e2.getTime().longValue());
        assertEquals(Log4jLevel.INFO, e2.getLevel());
        assertEquals("d.E", e2.getLogger());
        assertEquals("thread-F", e2.getThreadName());
        assertEquals("green", e2.getMessage());
        assertNull(e2.getExceptionRendering());
        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());

        List<Event> events7 = p.close();

        assertEquals(2, events7.size());

        assertTrue(events7.get(1) instanceof EndOfStreamEvent);

        Log4jEvent e3 = (Log4jEvent)events7.get(0);

        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("22:22:22,222").getTime(), e3.getTime().longValue());
        assertEquals(Log4jLevel.WARN, e3.getLevel());
        assertEquals("g.H", e3.getLogger());
        assertEquals("thread-I", e3.getThreadName());
        assertEquals("blue", e3.getMessage());
        assertNull(e3.getExceptionRendering());
        assertEquals(Log4jEventImpl.MESSAGE_APPEND_MODE, e.getAppendMode());
    }

    // applyHeuristics() -----------------------------------------------------------------------------------------------

    @Test
    public void applyHeuristics_Sequence() throws Exception {

        Log4jParser p = new Log4jParser();

        Log4jEvent currentEvent = p.getCurrentEvent();
        List<Event> fullyParsedEvents = p.getFullyParsedEvents();

        assertNull(currentEvent);
        assertTrue(fullyParsedEvents.isEmpty());

        p.applyHeuristics(7L, "00:00:00,000 INFO [io.novaordis] (Some Thread) test");

        currentEvent = p.getCurrentEvent();
        fullyParsedEvents = p.getFullyParsedEvents();

        assertEquals(7L, currentEvent.getLineNumber().longValue());
        long expected = new SimpleDateFormat("HH:mm:ss,SSS").parse("00:00:00,000").getTime();
        assertEquals(expected, currentEvent.getTime().longValue());
        assertTrue(fullyParsedEvents.isEmpty());

        p.applyHeuristics(8L, "00:00:00,001 INFO [io.novaordis] (Some Thread) test");

        currentEvent = p.getCurrentEvent();
        fullyParsedEvents = p.getFullyParsedEvents();

        assertEquals(8L, currentEvent.getLineNumber().longValue());
        expected = new SimpleDateFormat("HH:mm:ss,SSS").parse("00:00:00,001").getTime();
        assertEquals(expected, currentEvent.getTime().longValue());

        assertEquals(1, fullyParsedEvents.size());
        expected = new SimpleDateFormat("HH:mm:ss,SSS").parse("00:00:00,000").getTime();
        assertEquals(expected, ((TimedEvent) fullyParsedEvents.get(0)).getTime().longValue());

        p.applyHeuristics(9L, "00:00:00,002 INFO [io.novaordis] (Some Thread) test");

        currentEvent = p.getCurrentEvent();
        fullyParsedEvents = p.getFullyParsedEvents();

        assertEquals(9L, currentEvent.getLineNumber().longValue());
        expected = new SimpleDateFormat("HH:mm:ss,SSS").parse("00:00:00,002").getTime();
        assertEquals(expected, currentEvent.getTime().longValue());

        assertEquals(2, fullyParsedEvents.size());
        expected = new SimpleDateFormat("HH:mm:ss,SSS").parse("00:00:00,000").getTime();
        assertEquals(expected, ((TimedEvent)fullyParsedEvents.get(0)).getTime().longValue());
        expected = new SimpleDateFormat("HH:mm:ss,SSS").parse("00:00:00,001").getTime();
        assertEquals(expected, ((TimedEvent)fullyParsedEvents.get(1)).getTime().longValue());
    }

    // close() ---------------------------------------------------------------------------------------------------------

    @Test
    public void close_NoEvents() throws Exception {

        Log4jParser p = new Log4jParser();

        List<Event> afterParse = p.parse(1, "this is not a matching event", null);
        assertTrue(afterParse.isEmpty());

        List<Event> afterClose = p.close();

        assertEquals(1, afterClose.size());
        assertTrue(afterClose.get(0) instanceof EndOfStreamEvent);
    }

    @Test
    public void close_OneEvent() throws Exception {

        Log4jParser p = new Log4jParser();

        List<Event> afterParse = p.parse(1, "01/01/17 01:01:01,001 INFO [io.novaordis] (main) something", null);
        assertTrue(afterParse.isEmpty());

        List<Event> afterClose = p.close();

        assertEquals(2, afterClose.size());
        Log4jEvent e = (Log4jEvent)afterClose.get(0);
        assertEquals("something", e.getMessage());
        Event e2 = afterClose.get(1);
        assertTrue(e2 instanceof EndOfStreamEvent);

    }

    @Test
    public void close_TwoEvents() throws Exception {

        Log4jParser p = new Log4jParser();

        List<Event> afterParse = p.parse(1, "01/01/17 01:01:01,001 INFO [io.novaordis] (main) blue", null);
        assertTrue(afterParse.isEmpty());

        List<Event> afterParse2 = p.parse(2, "01/01/17 01:01:02,002 INFO [io.novaordis] (main) red", null);

        assertEquals(1, afterParse2.size());
        Log4jEvent e = (Log4jEvent)afterParse2.get(0);
        assertEquals("blue", e.getMessage());

        List<Event> afterClose = p.close();

        assertEquals(2, afterClose.size());
        Log4jEvent e2 = (Log4jEvent)afterClose.get(0);
        assertEquals("red", e2.getMessage());
        Event e3 = afterClose.get(1);
        assertTrue(e3 instanceof EndOfStreamEvent);
    }

    // production ------------------------------------------------------------------------------------------------------

    @Test
    public void productionLine001() throws Exception {

        String line = getProductionLine("production-line-001.txt");

        Log4jParser p = new Log4jParser();

        p.parse(line, null);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private String getProductionLine(String fileName) throws Exception {

        File file = new File(System.getProperty("basedir"), "src/test/resources/data/" + fileName);
        assertTrue(file.isFile());
        return Files.read(file);
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
