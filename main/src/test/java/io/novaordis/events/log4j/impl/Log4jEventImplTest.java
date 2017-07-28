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
public class Log4jEventImplTest extends Log4jEventTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void invalidLogLevelStorage() throws Exception {

        Log4jEventImpl e = getLog4jEventToTest();

        String s = "something that cannot be a log level";

        e.setStringProperty(Log4jEventImpl.LOG_LEVEL_PROPERTY_NAME, s);

        try {

            e.getLogLevel();
            fail("should have failed");
        }
        catch(IllegalStateException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.equals("invalid '" + Log4jEventImpl.LOG_LEVEL_PROPERTY_NAME + "' value: \"" + s + "\""));
        }
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // log level -------------------------------------------------------------------------------------------------------

    @Test
    public void logLevel_Mutation() throws Exception {

        Log4jEventImpl e = getLog4jEventToTest();

        e.setLogLevel(Log4jLevel.ERROR);

        assertEquals(Log4jLevel.ERROR, e.getLogLevel());

        e.setLogLevel(null);

        assertNull(e.getLogLevel());
    }

    // log category ----------------------------------------------------------------------------------------------------

    @Test
    public void logCategory_Mutation() throws Exception {

        Log4jEventImpl e = getLog4jEventToTest();

        e.setLogCategory("io.novaordis");

        assertEquals("io.novaordis", e.getLogCategory());

        e.setLogCategory(null);

        assertNull(e.getLogCategory());
    }

    // thread name -----------------------------------------------------------------------------------------------------

    @Test
    public void threadName_Mutation() throws Exception {

        Log4jEventImpl e = getLog4jEventToTest();

        e.setThreadName("some thread name");

        assertEquals("some thread name", e.getThreadName());

        e.setThreadName(null);

        assertNull(e.getThreadName());
    }

    // message ---------------------------------------------------------------------------------------------------------

    @Test
    public void message_Mutation() throws Exception {

        Log4jEventImpl e = getLog4jEventToTest();

        e.setMessage("some message");

        assertEquals("some message", e.getMessage());

        e.setMessage(null);

        assertNull(e.getMessage());
    }

    // raw representation/append() -------------------------------------------------------------------------------------

    @Test
    public void rawRepresentation_append() throws Exception {

        Log4jEventImpl e = getLog4jEventToTest();

        e.append("this is the first line");

        String s = e.getRawRepresentation();

        assertEquals("this is the first line", s);

        e.append("this is the second line");

        String s2 = e.getRawRepresentation();

        assertEquals("this is the first line\nthis is the second line", s2);

    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Log4jEventImpl getLog4jEventToTest() throws Exception {

        return new Log4jEventImpl();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
