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

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.TimedEvent;
import io.novaordis.utilities.Files;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

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

        List<Event> afterParse = p.parse(1, "this is not a matching event");
        assertTrue(afterParse.isEmpty());

        List<Event> afterClose = p.close();
        assertTrue(afterClose.isEmpty());
    }

    @Test
    public void close_OneEvent() throws Exception {

        Log4jParser p = new Log4jParser();

        List<Event> afterParse = p.parse(1, "01/01/17 01:01:01,001 INFO [io.novaordis] (main) something");
        assertTrue(afterParse.isEmpty());

        List<Event> afterClose = p.close();

        assertEquals(1, afterClose.size());
        Log4jEvent e = (Log4jEvent)afterClose.get(0);
        assertEquals("something", e.getMessage());
    }

    @Test
    public void close_TwoEvents() throws Exception {

        Log4jParser p = new Log4jParser();

        List<Event> afterParse = p.parse(1, "01/01/17 01:01:01,001 INFO [io.novaordis] (main) blue");
        assertTrue(afterParse.isEmpty());

        List<Event> afterParse2 = p.parse(2, "01/01/17 01:01:02,002 INFO [io.novaordis] (main) red");

        assertEquals(1, afterParse2.size());
        Log4jEvent e = (Log4jEvent)afterParse2.get(0);
        assertEquals("blue", e.getMessage());

         List<Event> afterClose = p.close();

        assertEquals(1, afterClose.size());
        Log4jEvent e2 = (Log4jEvent)afterClose.get(0);
        assertEquals("red", e2.getMessage());
    }

    // production ------------------------------------------------------------------------------------------------------

    @Test
    public void productionLine001() throws Exception {

        String line = getProductionLine("production-line-001.txt");

        Log4jParser p = new Log4jParser();

        p.parse(line);
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
