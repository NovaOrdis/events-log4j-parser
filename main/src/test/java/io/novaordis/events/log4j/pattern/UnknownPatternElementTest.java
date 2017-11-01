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

package io.novaordis.events.log4j.pattern;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class UnknownPatternElementTest extends Log4jPatternElementTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void addAfterLast() throws Exception {

        //
        // noop
        //
    }

    @Test
    @Override
    public void addAfterNotAccepted() throws Exception {

        UnknownPatternElement e = getLog4jPatternElementToTest();

        AddResult r = e.add(' ');
        assertEquals(AddResult.NOT_ACCEPTED, r);

        try {

            e.add(' ');

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains("attempt to add more characters to a closed element"));
        }
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected UnknownPatternElement getLog4jPatternElementToTest() throws Exception {

        return new UnknownPatternElement('x');
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
