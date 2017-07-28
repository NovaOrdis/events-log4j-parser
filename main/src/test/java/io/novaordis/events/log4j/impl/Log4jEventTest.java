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

import io.novaordis.events.api.parser.ParsingException;
import io.novaordis.utilities.logging.log4j.Log4jLevel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public abstract class Log4jEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // build() ---------------------------------------------------------------------------------------------------------

    @Test
    public void build_INFO_LeadingSpaces() throws Exception {

        String s = "   INFO [org.jboss.modules] (main) JBoss Modules version 1.3.7.Final-redhat-1";

        TimestampMatcher t = new TimestampMatcher(7L, null, 10);

        Log4jEvent e = Log4jEvent.build(8L, t, 0, s);

        assertEquals(8L, e.getLineNumber().longValue());
        assertEquals(7L, e.getTime().longValue());
        assertEquals(Log4jLevel.INFO, e.getLogLevel());
        assertEquals("org.jboss.modules", e.getLogCategory());
        assertEquals("main", e.getThreadName());
        assertEquals("JBoss Modules version 1.3.7.Final-redhat-1", e.getMessage());
        assertEquals(s, e.getRawRepresentation());
    }

    @Test
    public void build_INFO() throws Exception {

        String s = "INFO [org.jboss.modules] (main) JBoss Modules version 1.3.7.Final-redhat-1";

        TimestampMatcher t = new TimestampMatcher(7L, null, 10);

        Log4jEvent e = Log4jEvent.build(8L, t, 0, s);

        assertEquals(8L, e.getLineNumber().longValue());
        assertEquals(7L, e.getTime().longValue());
        assertEquals(Log4jLevel.INFO, e.getLogLevel());
        assertEquals("org.jboss.modules", e.getLogCategory());
        assertEquals("main", e.getThreadName());
        assertEquals("JBoss Modules version 1.3.7.Final-redhat-1", e.getMessage());
        assertEquals(s, e.getRawRepresentation());
    }

    @Test
    public void build_WARN() throws Exception {

        String s = "WARN [org.jboss.modules] (main) JBoss Modules version 1.3.7.Final-redhat-1";

        TimestampMatcher t = new TimestampMatcher(7L, null, 10);

        Log4jEvent e = Log4jEvent.build(8L, t, 0, s);

        assertEquals(8L, e.getLineNumber().longValue());
        assertEquals(7L, e.getTime().longValue());
        assertEquals(Log4jLevel.WARN, e.getLogLevel());
        assertEquals("org.jboss.modules", e.getLogCategory());
        assertEquals("main", e.getThreadName());
        assertEquals("JBoss Modules version 1.3.7.Final-redhat-1", e.getMessage());
        assertEquals(s, e.getRawRepresentation());
    }

    @Test
    public void build_ERROR() throws Exception {

        String s = "ERROR [org.jboss.modules] (main) JBoss Modules version 1.3.7.Final-redhat-1";

        TimestampMatcher t = new TimestampMatcher(7L, null, 10);
        Log4jEvent e = Log4jEvent.build(8L, t, 0, s);

        assertEquals(8L, e.getLineNumber().longValue());
        assertEquals(7L, e.getTime().longValue());
        assertEquals(Log4jLevel.ERROR, e.getLogLevel());
        assertEquals("org.jboss.modules", e.getLogCategory());
        assertEquals("main", e.getThreadName());
        assertEquals("JBoss Modules version 1.3.7.Final-redhat-1", e.getMessage());
        assertEquals(s, e.getRawRepresentation());
    }

    @Test
    public void build_MissingLogLevel() throws Exception {

        String s = "something [org.jboss.modules] (main) JBoss Modules version 1.3.7.Final-redhat-1";

        TimestampMatcher t = new TimestampMatcher(7L, null, -1);

        try {

            Log4jEvent.build(8L, t, 0, s);
            fail("should have thrown exception");
        }
        catch(ParsingException e) {

            String msg = e.getMessage();
            assertEquals(8L, e.getLineNumber().longValue());
            assertTrue(msg.equals("no log level found after timestamp"));
        }
    }

    @Test
    public void build_MissingCategory() throws Exception {

        String s = "ERROR something";

        int position = 10;

        TimestampMatcher t = new TimestampMatcher(7L, null, position);

        try {

            Log4jEvent.build(8L, t, 0, s);
            fail("should have thrown exception");
        }
        catch(ParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.matches("no.*category.*$"));

            assertEquals(8L, e.getLineNumber().longValue());
            assertEquals(position + "ERROR ".length(), e.getPositionInLine().intValue());
        }
    }

    @Test
    public void categoryWithNestedBrackets() throws Exception {

        String s = "ERROR [org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/mp/train/consist/composite].[javax.ws.rs.core.Application]] (blah) blah2";

        TimestampMatcher t = new TimestampMatcher(7L, null, 10);
        Log4jEvent e = Log4jEvent.build(8L, t, 0, s);

        assertEquals(8L, e.getLineNumber().longValue());
        assertEquals(7L, e.getTime().longValue());
        assertEquals(Log4jLevel.ERROR, e.getLogLevel());
        assertEquals("org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/mp/train/consist/composite].[javax.ws.rs.core.Application]", e.getLogCategory());
        assertEquals("blah", e.getThreadName());
        assertEquals("blah2", e.getMessage());
        assertEquals(s, e.getRawRepresentation());
    }

    @Test
    public void build_RecursiveParanthesesInThreadName() throws Exception {

        String s = "ERROR [io.test] (something (some between parantheses (and one more level) and more)) something";

        TimestampMatcher t = new TimestampMatcher(7L, null, -1);

        Log4jEvent e = Log4jEvent.build(8L, t, 0, s);

        String tn = e.getThreadName();

        assertEquals("something (some between parantheses (and one more level) and more)", tn);
        assertEquals("something", e.getMessage());
        assertEquals(s, e.getRawRepresentation());
    }

    @Test
    public void build_UnbalancedParanthesesInThreadName() throws Exception {

        String s = "ERROR [io.test] (unbalanced (something) something else";

        TimestampMatcher t = new TimestampMatcher(7L, null, -1);

        try {

            Log4jEvent.build(8L, t, 0, s);
            fail("should have thrown exception");
        }
        catch(ParsingException e) {

            String msg = e.getMessage();
            assertEquals(8L, e.getLineNumber().longValue());
            assertTrue(msg.contains("unbalanced"));
            assertTrue(msg.contains("("));
            assertTrue(msg.contains(")"));
        }
    }

    // log level -------------------------------------------------------------------------------------------------------

    @Test
    public void logLevel() throws Exception {

        Log4jEvent e = getLog4jEventToTest();

        assertNull(e.getLogLevel());
    }

    // log category ----------------------------------------------------------------------------------------------------

    @Test
    public void logCategory() throws Exception {

        Log4jEvent e = getLog4jEventToTest();

        assertNull(e.getLogCategory());
    }

    // thread name -----------------------------------------------------------------------------------------------------

    @Test
    public void threadName() throws Exception {

        Log4jEvent e = getLog4jEventToTest();

        assertNull(e.getThreadName());
    }

    // message ---------------------------------------------------------------------------------------------------------

    @Test
    public void message() throws Exception {

        Log4jEvent e = getLog4jEventToTest();

        assertNull(e.getMessage());
    }

    // getRawRepresentation --------------------------------------------------------------------------------------------

    @Test
    public void getRawRepresentation() throws Exception {

        Log4jEvent e = getLog4jEventToTest();

        assertNull(e.getRawRepresentation());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Log4jEvent getLog4jEventToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
