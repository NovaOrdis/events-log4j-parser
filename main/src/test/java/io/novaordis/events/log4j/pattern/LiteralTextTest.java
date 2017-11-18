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

import io.novaordis.events.log4j.impl.Log4jEventImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class LiteralTextTest extends ConversionPatternComponentTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void defaultLiteral() throws Exception {

        LiteralText lt = getConversionPatternComponentToTest();

        String s = getMatchingLogContent();
        assertEquals(s, lt.getLiteral());
    }

    @Test
    public void add() throws Exception {

        LiteralText lt = new LiteralText();

        assertNull(lt.getLiteral());

        AddResult result = lt.add('b');
        assertEquals(AddResult.ACCEPTED, result);

        assertEquals("b", lt.getLiteral());

        AddResult result2 = lt.add('l');
        assertEquals(AddResult.ACCEPTED, result2);

        assertEquals("bl", lt.getLiteral());

        AddResult result3 = lt.add('a');
        assertEquals(AddResult.ACCEPTED, result3);

        assertEquals("bla", lt.getLiteral());

        AddResult result4 = lt.add('h');
        assertEquals(AddResult.ACCEPTED, result4);

        assertEquals("blah", lt.getLiteral());

        AddResult result5 = lt.add(' ');
        assertEquals(AddResult.ACCEPTED, result5);

        assertEquals("blah ", lt.getLiteral());

        AddResult result6 = lt.add('[');
        assertEquals(AddResult.ACCEPTED, result6);

        assertEquals("blah [", lt.getLiteral());
    }

    @Test
    public void add_ConversionSpecifierMarker() throws Exception {

        LiteralText lt = getConversionPatternComponentToTest();

        AddResult result = lt.add(' ');
        assertEquals(AddResult.ACCEPTED, result);

        AddResult result2 = lt.add(Log4jPatternLayout.CONVERSION_SPECIFIER_MARKER);
        assertEquals(AddResult.NOT_ACCEPTED, result2);

        try {

            lt.add('x');

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("attempt to add more characters to a closed conversion pattern component"));
        }
    }

    // getLiteral() ----------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void getLiteral() throws Exception {

        LiteralText e = new LiteralText();

        e.add('b');
        e.add('l');
        e.add('a');
        e.add('h');

        String l = e.getLiteral();

        assertEquals("blah", l);
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse() throws Exception {

        String line = "something that does not matter something else blah";

        int from = 31;

        LiteralText pe = new LiteralText("something else");

        RenderedLogEvent p = pe.parseLogContent(line, from, null);

        String s = (String)p.get();

        assertEquals("something else", s);

        assertEquals(31, p.from());
        assertEquals(45, p.to());
    }

    @Test
    public void parse_LiteralsDoNotMatch() throws Exception {

        String line = "a blah";

        int from = 2;

        LiteralText pe = new LiteralText("blih");

        try {

            pe.parseLogContent(line, from, null);

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertEquals("pattern element's literal \"blih\" does not match the parsed string literal \"blah\"", msg);
        }
    }

    // findNext() ------------------------------------------------------------------------------------------------------

    @Test
    public void findNext() throws Exception {

        LiteralText lt = new LiteralText(" ");

        Integer i = lt.findNext(" something something else", 1);

        assertEquals(10, i.intValue());
    }

    @Test
    public void findNext_NotFound() throws Exception {

        LiteralText lt = new LiteralText("@@@");

        Integer i = lt.findNext("something something else", 0);

        assertNull(i);
    }

    // injectIntoEvent() ------------------------------------------------------------------------------------------

    @Test
    public void injectIntoLog4jEvent() throws Exception {

        //
        // we're a noop
        //

        Log4jEventImpl e = new Log4jEventImpl();

        int propertyCount = e.getProperties().size();

        LiteralText lt = new LiteralText("something");

        lt.injectIntoEvent(e, "something else");

        int propertyCount2 = e.getProperties().size();

        assertEquals(propertyCount, propertyCount2);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected LiteralText getConversionPatternComponentToTest() throws Exception {

        return new LiteralText(") something (");
    }

    @Override
    protected String getMatchingLogContent() throws Exception {

        return ") something (";
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
