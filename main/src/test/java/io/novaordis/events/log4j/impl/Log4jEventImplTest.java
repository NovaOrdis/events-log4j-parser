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

import org.junit.Test;

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

    @Override
    protected Log4jEventImpl getLog4jEventToTest() throws Exception {

        return new Log4jEventImpl();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}