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

package io.novaordis.events.log4j.pattern.convspec;

import org.junit.Test;

import io.novaordis.events.log4j.impl.Log4jEventImpl;
import io.novaordis.events.log4j.pattern.AddResult;
import io.novaordis.events.log4j.pattern.FormatModifier;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.log4j.pattern.ProcessedString;
import io.novaordis.events.log4j.pattern.RenderedLogEvent;
import io.novaordis.utilities.logging.log4j.Log4jLevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class LevelTest extends ConversionSpecifierTest {

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

        Level e = getConversionSpecifierToTest(null);

        AddResult r = e.add(' ');
        assertEquals(AddResult.NOT_ACCEPTED, r);

        try {

            e.add(' ');

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains("attempt to add more characters to a closed conversion pattern component"));
        }
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        Level e = new Level("p");

        assertEquals(Level.CONVERSION_CHARACTER, e.getConversionCharacter().charValue());
        assertNull(e.getFormatModifier());
        assertEquals("%p", e.getLiteral());
    }

    @Test
    public void constructor_WithFormatModifier() throws Exception {

        Level e = new Level("-5p");

        assertEquals(Level.CONVERSION_CHARACTER, e.getConversionCharacter().charValue());
        FormatModifier m = e.getFormatModifier();
        assertEquals("-5", m.getLiteral());
        assertEquals("%-5p", e.getLiteral());
    }

    @Test
    public void constructor_WrongIdentifier() throws Exception {

        try {

            new Level("c");

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("missing expected conversion character '" + Level.CONVERSION_CHARACTER + "'"));
        }
    }

    // add() -----------------------------------------------------------------------------------------------------------

    @Test
    public void add() throws Exception {

        Level e = getConversionSpecifierToTest(null);

        AddResult r = e.add(' ');
        assertEquals(AddResult.NOT_ACCEPTED, r);

        try {

            e.add(' ');

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains("attempt to add more characters to a closed conversion pattern component"));
        }
    }

    @Test
    public void getLiteral() throws Exception {

        Level e = getConversionSpecifierToTest(null);

        assertEquals("%p", e.getLiteral());
    }

    @Test
    public void getLiteral_FormatModifier() throws Exception {

        Level e = getConversionSpecifierToTest(null);

        e.setFormatModifier(new FormatModifier("-5"));

        assertEquals("%-5p", e.getLiteral());
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse() throws Exception {

        String line = "INFO BLAHBLAH";

        int from = 0;

        Level pe = new Level("-5p");

        RenderedLogEvent p = pe.parseLogContent(line, from, null);

        Log4jLevel l = (Log4jLevel)p.get();

        assertEquals(Log4jLevel.INFO, l);

        assertEquals(0, p.from());
        assertEquals(5, p.to());
        assertEquals("INFO ", line.substring(p.from(), p.to()));
    }

    @Test
    public void parse_NoSuchLevel() throws Exception {

        String line = "something BLAHBLAH";

        Level pe = new Level("-5p");

        try {

            pe.parseLogContent(line, 0, null);

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("not a valid log4j level:"));
            assertTrue(msg.contains("something BLAHBLAH"));
        }
    }

    // parseLiteralAfterFormatModifierWasUnapplied() -------------------------------------------------------------------

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied_Invalid() throws Exception {

        Level l = new Level();

        assertNull(l.getFormatModifier());

        ProcessedString ps = new ProcessedString(0, "blah");

        try {

            l.parseLiteralAfterFormatModifierWasUnapplied(ps);
            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("not a valid log4j level:"));
            assertTrue(msg.contains("blah"));
        }
    }

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied_INFO() throws Exception {

        Level l = new Level();

        assertNull(l.getFormatModifier());

        ProcessedString ps = new ProcessedString(2, "INFO");

        RenderedLogEvent e = l.parseLiteralAfterFormatModifierWasUnapplied(ps);

        assertEquals(2, e.from());
        assertEquals(6, e.to());
        assertEquals(Log4jLevel.INFO, e.get());
    }

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied_TRACE() throws Exception {

        Level l = new Level();

        assertNull(l.getFormatModifier());

        ProcessedString ps = new ProcessedString(2, "TRACE");

        RenderedLogEvent e = l.parseLiteralAfterFormatModifierWasUnapplied(ps);

        assertEquals(2, e.from());
        assertEquals(7, e.to());
        assertEquals(Log4jLevel.TRACE, e.get());
    }

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied_DEBUG() throws Exception {

        Level l = new Level();

        assertNull(l.getFormatModifier());

        ProcessedString ps = new ProcessedString(2, "DEBUG");

        RenderedLogEvent e = l.parseLiteralAfterFormatModifierWasUnapplied(ps);

        assertEquals(2, e.from());
        assertEquals(7, e.to());
        assertEquals(Log4jLevel.DEBUG, e.get());
    }

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied_WARN() throws Exception {

        Level l = new Level();

        assertNull(l.getFormatModifier());

        ProcessedString ps = new ProcessedString(2, "WARN");

        RenderedLogEvent e = l.parseLiteralAfterFormatModifierWasUnapplied(ps);

        assertEquals(2, e.from());
        assertEquals(6, e.to());
        assertEquals(Log4jLevel.WARN, e.get());
    }

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied_ERROR() throws Exception {

        Level l = new Level();

        assertNull(l.getFormatModifier());

        ProcessedString ps = new ProcessedString(2, "ERROR");

        RenderedLogEvent e = l.parseLiteralAfterFormatModifierWasUnapplied(ps);

        assertEquals(2, e.from());
        assertEquals(7, e.to());
        assertEquals(Log4jLevel.ERROR, e.get());
    }

    // injectIntoLog4jEvent() ------------------------------------------------------------------------------------------

    @Test
    public void injectIntoLog4jEvent() throws Exception {

        Level cs = new Level();

        Log4jEventImpl e = new Log4jEventImpl();

        cs.injectIntoLog4jEvent(e, Log4jLevel.INFO);

        Log4jLevel result = e.getLevel();

        assertEquals(Log4jLevel.INFO, result);
    }

    @Test
    public void injectIntoLog4jEvent_InvalidType() throws Exception {

        Level cs = new Level();

        Log4jEventImpl e = new Log4jEventImpl();

        try {

            cs.injectIntoLog4jEvent(e, "INFO");

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains("invalid value type"));
            assertTrue(msg.contains("String"));
            assertTrue(msg.contains("expected Log4jLevel"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected String getMatchingLogContent() throws Exception {

        return "INFO";
    }

    @Override
    protected Level getConversionSpecifierToTest(FormatModifier m) throws Exception {

        Level cs = new Level();
        cs.setFormatModifier(m);
        return cs;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
