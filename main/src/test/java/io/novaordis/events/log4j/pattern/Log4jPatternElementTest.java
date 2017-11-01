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
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public abstract class Log4jPatternElementTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    /**
     * Test that must be provided by subclasses and must insure that add() invoked after an add() that returned
     * AddResult.LAST throws Log4jPatternLayoutException.
     */
    @Test
    public abstract void addAfterLast() throws Exception;

    /**
     * Test that must be provided by subclasses and must insure that add() invoked after an add() that returned
     * AddResult.NOT_ACCEPTED throws Log4jPatternLayoutException.
     */
    @Test
    public abstract void addAfterNotAccepted() throws Exception;

    @Test
    public void getFormatModifierLiteral() throws Exception {

        //
        // by default, the elements have no format modifier literal
        //

        Log4jPatternElement e = getLog4jPatternElementToTest();

        String m = e.getFormatModifierLiteral();

        assertNull(m);
    }

    // getLiteral() ----------------------------------------------------------------------------------------------------

    @Test
    public void getLiteral() throws Exception {

        Log4jPatternElement e = getLog4jPatternElementToTest();
        assertNull(e.getFormatModifierLiteral());

        String expected = "%" + e.getIdentifier();
        String l = e.getLiteral();

        assertEquals(expected, l);
    }

    @Test
    public void getLiteral_WithFormatModifier() throws Exception {

        Log4jPatternElement e = getLog4jPatternElementToTest();

        ((Log4jPatternElementBase)e).setFormatModifierLiteral("-5");

        String expected = "%-5" + e.getIdentifier();
        String l = e.getLiteral();

        assertEquals(expected, l);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Log4jPatternElement getLog4jPatternElementToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
