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

package io.novaordis.events.log4j.cli;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.parser.ParsingException;
import io.novaordis.events.cli.Configuration;
import io.novaordis.events.cli.ConfigurationImpl;
import io.novaordis.events.log4j.impl.Log4jEvent;
import io.novaordis.events.log4j.impl.Log4jParser;
import io.novaordis.events.processing.Procedure;
import io.novaordis.events.processing.count.Count;
import io.novaordis.events.processing.exclude.Exclude;
import io.novaordis.events.query.NullQuery;
import io.novaordis.events.query.Query;
import io.novaordis.utilities.UserErrorException;
import io.novaordis.utilities.help.InLineHelp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public class Main {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {

        try {

            Configuration c = new ConfigurationImpl(args);

            if (c.isHelp()) {

                help();
                System.exit(0);
            }

            List<Event> events = new ArrayList<>();

            Log4jParser parser = new Log4jParser();

            Procedure procedure = c.getProcedure();

            InputStream is = c.getInputStream();

            BufferedReader br = null;

            try {

                br = new BufferedReader(new InputStreamReader(is));

                String line;

                while ((line = br.readLine()) != null) {

                    //
                    // apply the query here to avoid accumulation in memory or processing of irrelevant events
                    //

                    List<Event> es = parser.parse(line);

                    if (procedure != null && procedure instanceof Exclude) {

                        //
                        // for 'exclude', process the events before applying the query.
                        //

                        procedure.process(es);
                    }
                    else {

                        //
                        // in all other cases, apply the query and then possibly apply the procedure
                        //

                        es = applyQuery(es, c.getQuery());

                        if (!es.isEmpty()) {

                            if (procedure != null) {

                                procedure.process(es);
                            }
                            else {

                                //
                                // accumulate in memory
                                //
                                events.addAll(es);
                            }
                        }
                    }
                }

                List<Event> es = parser.close();

                if (procedure != null && procedure instanceof Exclude) {

                    //
                    // for 'exclude', process the events before applying the query.
                    //

                    procedure.process(es);
                }
                else {

                    //
                    // in all other cases, apply the query and then possibly apply the procedure
                    //

                    es = applyQuery(es, c.getQuery());

                    if (procedure != null) {

                        procedure.process(es);
                    }
                    else {

                        //
                        // accumulate in memory
                        //
                        events.addAll(es);
                    }
                }
            }
            finally {

                if (br != null) {

                    br.close();
                }
            }

            if (procedure == null) {

                //noinspection Convert2streamapi
                for (Event e : events) {

                    //
                    // events are already filtered, we don't need to filter them again
                    //

                    if (e instanceof Log4jEvent) {

                        Log4jEvent log4je = (Log4jEvent) e;
                        System.out.println(log4je.getRawRepresentation());
                    } else {

                        System.out.println(e);
                    }
                }
            }
            else {

                if (procedure instanceof Count) {

                    System.out.println(((Count)procedure).getCount());
                }
            }
        }
        catch(UserErrorException e) {

            System.err.println("[error]: " + e.getMessage());
        }
        catch(ParsingException e) {

            System.err.println("[error] line " + e.getLineNumber() + ": " + e.getMessage());
        }
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    /**
     * @param q may be null
     */
    private static List<Event> applyQuery(List<Event> events, Query q) {

        if (q instanceof NullQuery) {

            return events;
        }

        List<Event> result = null;

        for (Event e : events) {

            if (q != null && !q.selects(e)) {

                continue;
            }

            if (result == null) {

                result = new ArrayList<>();
            }

            result.add(e);
        }

        if (result == null) {

            return Collections.emptyList();
        }
        else {

            return result;
        }
    }

    private static void help() throws UserErrorException {

        System.out.println(InLineHelp.get());
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}