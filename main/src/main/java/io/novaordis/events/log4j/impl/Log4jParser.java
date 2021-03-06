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
import io.novaordis.events.log4j.pattern.Log4jPatternLayout;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.query.Query;
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

    private Log4jPatternLayout patternLayout;

    private List<Event> fullyParsedEvents;

    private Log4jEventImpl currentEvent;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Log4jParser() {

        this(null);
    }

    public Log4jParser(Log4jPatternLayout patternLayout) {

        fullyParsedEvents = new ArrayList<>();

        setPatternLayout(patternLayout);
    }

    // ParserBase implementation ---------------------------------------------------------------------------------------

    @Override
    protected List<Event> parse(long lineNumber, String line, Query query) throws ParsingException {

        if (patternLayout == null) {

            applyHeuristics(lineNumber, line);
        }
        else {

            //
            // there is a pattern layout in place, use it to parse the line
            //

            //
            // in most cases, lines are structured log event renderings so attempt preemptive parsing
            //

            try {

                Log4jEventImpl event = patternLayout.parse(lineNumber, line);

                //
                // parsing succeeded, we got another log event, move the current one, if any, to the fully parsed
                // event queue, and roll with this one
                //

                if (currentEvent != null) {

                    fullyParsedEvents.add(currentEvent);
                }

                currentEvent = event;

            }
            catch(Log4jPatternLayoutException e) {

                if (currentEvent == null) {

                    //
                    // bubble the exception up
                    //

                    throw new ParsingException(lineNumber, e);
                }

                //
                // attempt to pass it to the current event, it may be an exception stack trace or multi-line message
                // line
                //

                currentEvent.appendLine(line);
            }

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

    /**
     * The pattern layout can be injected into the parser instance after it is constructed, as long as it is done
     * before the instance is used to parse content.
     *
     * @param patternLayout null is acceptable, it means "reset".
     */
    public void setPatternLayout(Log4jPatternLayout patternLayout) {

        this.patternLayout = patternLayout;

        log.debug(this + " installed a log4j pattern layout: " + patternLayout);
    }

    /**
     * May return null, and in this case, the parser will have to use heuristics.
     */
    public Log4jPatternLayout getPatternLayout() {

        return patternLayout;
    }

    @Override
    public String toString() {

        if (patternLayout == null) {

            return "Heuristic Log4jParser[" + Integer.toHexString(System.identityHashCode(this)) + "]";
        }

        String patternLiteral = patternLayout.getLiteral();

        return "Log4jParser[" + patternLiteral + "]";
    }


    // Package protected -----------------------------------------------------------------------------------------------

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

                currentEvent.appendRawLine(line);
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
