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

import io.novaordis.utilities.logging.log4j.Log4jLevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class LevelPatternElementTest extends Log4jPatternElementTest {

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

        LevelPatternElement e = getLog4jPatternElementToTest();

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

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        LevelPatternElement e = new LevelPatternElement("p");

        assertEquals(LevelPatternElement.IDENTIFIER, e.getIdentifier().charValue());
        assertNull(e.getFormatModifierLiteral());
        assertEquals("%p", e.getLiteral());
    }

    @Test
    public void constructor_WithFormatModifier() throws Exception {

        LevelPatternElement e = new LevelPatternElement("-5p");

        assertEquals(LevelPatternElement.IDENTIFIER, e.getIdentifier().charValue());
        assertEquals("-5", e.getFormatModifierLiteral());
        assertEquals("%-5p", e.getLiteral());
    }

    @Test
    public void constructor_WrongIdentifier() throws Exception {

        try {

            new LevelPatternElement("c");

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("identifier '" + LevelPatternElement.IDENTIFIER + "' not encountered"));
        }
    }

    // add() -----------------------------------------------------------------------------------------------------------

    @Test
    public void add() throws Exception {

        LevelPatternElement e = getLog4jPatternElementToTest();

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

    @Test
    public void getLiteral() throws Exception {

        LevelPatternElement e = getLog4jPatternElementToTest();

        assertEquals("%p", e.getLiteral());
    }

    @Test
    public void getLiteral_FormatModifier() throws Exception {

        LevelPatternElement e = getLog4jPatternElementToTest();

        e.setFormatModifierLiteral("-5");

        assertEquals("%-5p", e.getLiteral());
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse() throws Exception {

        String line = "INFO BLAHBLAH";

        int from = 0;

        LevelPatternElement pe = new LevelPatternElement("-5p");

        ParsedElement p = pe.parse(line, from, null);

        Log4jLevel l = (Log4jLevel)p.get();

        assertEquals(Log4jLevel.INFO, l);

        assertEquals(0, p.from());
        assertEquals(5, p.to());
        assertEquals("INFO ", p.getLiteral());
    }

    @Test
    public void parse_PatternMismatch() throws Exception {

        fail("return here");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected LevelPatternElement getLog4jPatternElementToTest() throws Exception {

        return new LevelPatternElement();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
