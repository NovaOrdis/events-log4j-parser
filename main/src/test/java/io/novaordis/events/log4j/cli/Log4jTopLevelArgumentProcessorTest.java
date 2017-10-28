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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.novaordis.events.cli.Configuration;
import io.novaordis.events.cli.ConfigurationImpl;
import io.novaordis.utilities.UserErrorException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/28/17
 */
public class Log4jTopLevelArgumentProcessorTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        new Log4jTopLevelArgumentProcessor();
    }

    // process() -------------------------------------------------------------------------------------------------------

    @Test
    public void process_ArgumentsThatDoNotMeanAnythingToProcessor() throws Exception {

        Log4jTopLevelArgumentProcessor p = new Log4jTopLevelArgumentProcessor();

        List<String> args = Arrays.asList("A", "B", "C");

        ConfigurationImpl c = new ConfigurationImpl(new String[0], null);

        p.process(args, c);

        assertEquals(3, args.size());
        assertEquals("A", args.get(0));
        assertEquals("B", args.get(1));
        assertEquals("C", args.get(2));

        assertNull(c.getApplicationSpecificConfiguration());
    }

    @Test
    public void process_FormatShortOption() throws Exception {

        Log4jTopLevelArgumentProcessor p = new Log4jTopLevelArgumentProcessor();

        List<String> args = new ArrayList<>(
                Arrays.asList("A", "B", "-f", "%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n", "C"));

        ConfigurationImpl c = new ConfigurationImpl(new String[0], null);

        p.process(args, c);

        assertEquals(3, args.size());
        assertEquals("A", args.get(0));
        assertEquals("B", args.get(1));
        assertEquals("C", args.get(2));

        Log4jConfiguration log4jc = (Log4jConfiguration)c.getApplicationSpecificConfiguration();
        assertNotNull(log4jc);
        assertEquals("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n", log4jc.getPatternLayoutString());
    }

    @Test
    public void process_FormatShortOption_MissingPatternLayout() throws Exception {

        Log4jTopLevelArgumentProcessor p = new Log4jTopLevelArgumentProcessor();

        List<String> args = new ArrayList<>(Arrays.asList("-f"));

        ConfigurationImpl c = new ConfigurationImpl(new String[0], null);

        try {

            p.process(args, c);
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("log4j pattern layout specification implied by -f is missing"));
        }
    }

    @Test
    public void process_FormatShortOption_MissingPatternLayout_2() throws Exception {

        Log4jTopLevelArgumentProcessor p = new Log4jTopLevelArgumentProcessor();

        List<String> args = new ArrayList<>(Arrays.asList("-f", "describe"));

        ConfigurationImpl c = new ConfigurationImpl(new String[0], null);

        try {

            p.process(args, c);
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing log4j pattern layout specification"));
        }
    }

    @Test
    public void process_FormatLongOption_MissingPatternLayout() throws Exception {

        Log4jTopLevelArgumentProcessor p = new Log4jTopLevelArgumentProcessor();

        List<String> args = new ArrayList<>(Arrays.asList("--format="));

        ConfigurationImpl c = new ConfigurationImpl(new String[0], null);

        try {

            p.process(args, c);
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("log4j pattern layout specification that should follow --format= is missing"));
        }
    }

    @Test
    public void process_FormatLongOption_InvalidPatternLayout() throws Exception {

        Log4jTopLevelArgumentProcessor p = new Log4jTopLevelArgumentProcessor();

        List<String> args = new ArrayList<>(Arrays.asList("--format=blah"));

        ConfigurationImpl c = new ConfigurationImpl(new String[0], null);

        try {

            p.process(args, c);
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid log4j pattern layout specification"));
        }
    }

    @Test
    public void process_FormatLongOption() throws Exception {

        Log4jTopLevelArgumentProcessor p = new Log4jTopLevelArgumentProcessor();

        List<String> args = new ArrayList<>(
                Arrays.asList("A", "B", "--format=%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n", "C"));

        ConfigurationImpl c = new ConfigurationImpl(new String[0], null);

        p.process(args, c);

        assertEquals(3, args.size());
        assertEquals("A", args.get(0));
        assertEquals("B", args.get(1));
        assertEquals("C", args.get(2));

        Log4jConfiguration log4jc = (Log4jConfiguration)c.getApplicationSpecificConfiguration();
        assertNotNull(log4jc);
        assertEquals("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n", log4jc.getPatternLayoutString());
    }

    @Test
    public void process_FormatLongOption_SimpleQuotes() throws Exception {

        fail("return here");

    }

    // processLog4jPatternLayoutLiteral() ------------------------------------------------------------------------------

    @Test
    public void processLog4jPatternLayoutLiteral_NullArgs() throws Exception {

        Log4jTopLevelArgumentProcessor p = new Log4jTopLevelArgumentProcessor();

        try {

            p.processLog4jPatternLayoutLiteral(null, new ConfigurationImpl(new String[0], null));
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null log4j pattern layout literal"));
        }
    }

    @Test
    public void processLog4jPatternLayoutLiteral_NullArgs2() throws Exception {

        Log4jTopLevelArgumentProcessor p = new Log4jTopLevelArgumentProcessor();

        try {

            p.processLog4jPatternLayoutLiteral("d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n", null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null configuration"));
        }
    }

    @Test
    public void processLog4jPatternLayoutLiteral() throws Exception {

        Log4jTopLevelArgumentProcessor p = new Log4jTopLevelArgumentProcessor();
        Configuration c = new ConfigurationImpl(new String[0], null);

        p.processLog4jPatternLayoutLiteral("d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n", c);

        Log4jConfiguration ac = (Log4jConfiguration)c.getApplicationSpecificConfiguration();
        assertEquals("d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n", ac.getPatternLayoutString());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
