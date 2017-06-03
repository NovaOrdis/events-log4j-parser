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

package io.novaordis.events.log4j;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.parser.ParsingException;
import io.novaordis.events.log4j.impl.Log4jParser;
import io.novaordis.utilities.UserErrorException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
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

            Configuration c = new Configuration(args);

            List<Event> events = new ArrayList<>();

            Log4jParser parser = new Log4jParser();

            BufferedReader br = null;

            try {

                br = new BufferedReader(new FileReader(c.getFile()));

                String line;

                while ((line = br.readLine()) != null) {

                    events.addAll(parser.parse(line));
                }

                events.addAll(parser.close());

            }
            finally {

                if (br != null) {

                    br.close();
                }
            }

            for (Event e : events) {

                if (c.getQuery() != null && !c.getQuery().selects(e)) {

                    continue;
                }

                System.out.println(e);
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

    // Inner classes ---------------------------------------------------------------------------------------------------

}
