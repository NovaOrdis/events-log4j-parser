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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.parser.ParserBase;
import io.novaordis.utilities.parsing.ParsingException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public class Log4jParser extends ParserBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(Log4jParser.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Log4jPattern pattern;

    private List<Event> fullyParsedEvents;

    private Log4jEventImpl currentEvent;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Log4jParser() {

        pattern = null;
        fullyParsedEvents = new ArrayList<>();
    }

    // ParserBase implementation ---------------------------------------------------------------------------------------

    @Override
    protected List<Event> parse(long lineNumber, String line) throws ParsingException {

        if (pattern != null) {

            matchPattern(lineNumber, line);
        }
        else {

            applyHeuristics(lineNumber, line);
        }

        return flush();

    }

    @Override
    protected List<Event> close(long lineNumber) throws ParsingException {

        List<Event> result = flush();

        if (currentEvent == null) {

            return result;
        }

        if (result.isEmpty()) {

            return Arrays.asList(currentEvent);
        }

        result.add(currentEvent);

        return result;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    List<Event> matchPattern(long lineNumber, String line) throws ParsingException {

        throw new RuntimeException("matchPattern(" + lineNumber + ", " + line + ") NOT YET IMPLEMENTED");
    }

    void applyHeuristics(long lineNumber, String line) throws ParsingException {

        TimestampMatcher t = TimestampMatcher.find(lineNumber, line);

        if (t == null) {

            //
            // no timestamp found on line, it either belongs to last event started on an previous line, or it should be
            // discarded
            //

            if (currentEvent == null) {

                log.warn("discarding line " + lineNumber + ", no timestamp and no previous event");
            }
            else {

                currentEvent.append(line);
            }
        }
        else {


            //
            // we found a timestamp at the beginning of the line, this is a new event
            //

            if (currentEvent != null) {

                //
                // save the current event, we're done with it
                //

                fullyParsedEvents.add(currentEvent);
            }

            currentEvent = Log4jEvent.build(lineNumber, t, t.getIndexOfNextCharInLine(), line);
        }
    }

    /**
     * Returns the actual fully parsed events list instance, if it contains accumulated events, and replaces it with
     * a new one.
     */
    List<Event> flush() {

        if (fullyParsedEvents.isEmpty()) {

            return Collections.emptyList();
        }

        List<Event> result = fullyParsedEvents;

        fullyParsedEvents = new ArrayList<>();

        return result;
    }

    /**
     * May return null.
     */
    Log4jEvent getCurrentEvent() {

        return currentEvent;
    }

    /**
     * @return the underlying storage
     */
    List<Event> getFullyParsedEvents() {

        return fullyParsedEvents;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
