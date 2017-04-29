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
import io.novaordis.events.api.parser.*;
import io.novaordis.events.api.parser.ParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private Log4jEvent currentEvent;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Log4jParser() {

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

        return flush();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    List<Event> matchPattern(long lineNumber, String line) throws ParsingException {

        throw new RuntimeException("matchPattern() NOT YET IMPLEMENTED");
    }

    void applyHeuristics(long lineNumber, String line) throws ParsingException {

        Timestamp t = Timestamp.find(line);

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

            currentEvent = Log4jEvent.build(lineNumber, t, line.substring(t.getIndexOfNextCharInLine()));
        }
    }

    /**
     * Replaces the fully parsed event list with a new one, and return fully parsed event list.
     */
    List<Event> flush() {

        if (fullyParsedEvents.isEmpty()) {

            return Collections.emptyList();
        }

        List<Event> result = fullyParsedEvents;

        fullyParsedEvents = new ArrayList<>();

        return result;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
