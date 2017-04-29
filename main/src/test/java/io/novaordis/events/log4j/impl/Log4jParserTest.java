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
import org.junit.Test;

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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
