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
    public void add() throws Exception {

        LiteralText e = getConversionPatternComponentToTest();

        fail("return here");

//        assertNull(e.getLiteral());
//
//        AddResult result = e.add('b');
//        assertEquals(AddResult.ACCEPTED, result);
//
//        assertEquals("b", e.getLiteral());
//
//        AddResult result2 = e.add('l');
//        assertEquals(AddResult.ACCEPTED, result2);
//
//        assertEquals("bl", e.getLiteral());
//
//        AddResult result3 = e.add('a');
//        assertEquals(AddResult.ACCEPTED, result3);
//
//        assertEquals("bla", e.getLiteral());
//
//        AddResult result4 = e.add('h');
//        assertEquals(AddResult.ACCEPTED, result4);
//
//        assertEquals("blah", e.getLiteral());
//
//        AddResult result5 = e.add(' ');
//        assertEquals(AddResult.ACCEPTED, result5);
//
//        assertEquals("blah ", e.getLiteral());
//
//        AddResult result6 = e.add('[');
//        assertEquals(AddResult.ACCEPTED, result6);
//
//        assertEquals("blah [", e.getLiteral());
    }

    // getLiteral() ----------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void getLiteral() throws Exception {

        LiteralText e = new LiteralText();

//        e.add('b');
//        e.add('l');
//        e.add('a');
//        e.add('h');
//
//        assertNull(e.getFormatModifier());
//
//        String l = e.getLiteral();
//
//        assertEquals("blah", l);

        fail("return here");
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
        assertEquals("something else", p.getLiteral());
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
