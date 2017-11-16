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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/15/17
 */
public class FormatModifierTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NullLiteral() throws Exception {

        try {

            new FormatModifier(null);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null literal"));
        }
    }

    @Test
    public void constructor_and_render_Literal() throws Exception {

        String s = "5";

        FormatModifier m = new FormatModifier(s);

        assertEquals("5", m.getLiteral());

        assertEquals(5, m.getMinimumFieldWidth().intValue());
        assertTrue(m.isRightJustified());
        assertNull(m.getMaximumFieldWidth());
        assertTrue(m.isTruncateFromFront());


        assertEquals("     ", m.render(""));
        assertEquals("    a", m.render("a"));
        assertEquals("   aa", m.render("aa"));
        assertEquals("aaaaa", m.render("aaaaa"));
        assertEquals("aaaaaa", m.render("aaaaaa"));
    }

    @Test
    public void constructor_and_render_Literal2() throws Exception {

        String s = "-5";

        FormatModifier m = new FormatModifier(s);

        assertEquals("-5", m.getLiteral());

        assertEquals(5, m.getMinimumFieldWidth().intValue());
        assertFalse(m.isRightJustified());
        assertNull(m.getMaximumFieldWidth());
        assertTrue(m.isTruncateFromFront());

        assertEquals("     ", m.render(""));
        assertEquals("a    ", m.render("a"));
        assertEquals("aa   ", m.render("aa"));
        assertEquals("aaaaa", m.render("aaaaa"));
        assertEquals("aaaaaa", m.render("aaaaaa"));
    }

    @Test
    public void constructor_and_render_Literal3() throws Exception {

        String s = ".3";

        FormatModifier m = new FormatModifier(s);

        assertEquals(".3", m.getLiteral());

        assertNull(m.getMinimumFieldWidth());
        assertTrue(m.isRightJustified());
        assertEquals(3, m.getMaximumFieldWidth().intValue());
        assertTrue(m.isTruncateFromFront());

        assertEquals("", m.render(""));
        assertEquals("A", m.render("A"));
        assertEquals("AB", m.render("AB"));
        assertEquals("ABC", m.render("ABC"));
        assertEquals("BCD", m.render("ABCD"));
    }

    @Test
    public void constructor_and_render_Literal3_1() throws Exception {

        // empty space is acceptable
        String s = "   .30";

        FormatModifier m = new FormatModifier(s);

        assertEquals("   .30", m.getLiteral());

        assertNull(m.getMinimumFieldWidth());
        assertTrue(m.isRightJustified());
        assertEquals(30, m.getMaximumFieldWidth().intValue());
        assertTrue(m.isTruncateFromFront());
    }

    @Test
    public void constructor_and_render_Literal4() throws Exception {

        String s = "2.3";

        FormatModifier m = new FormatModifier(s);

        assertEquals("2.3", m.getLiteral());

        assertEquals(2, m.getMinimumFieldWidth().intValue());
        assertTrue(m.isRightJustified());
        assertEquals(3, m.getMaximumFieldWidth().intValue());
        assertTrue(m.isTruncateFromFront());

        assertEquals("  ", m.render(""));
        assertEquals(" A", m.render("A"));
        assertEquals("AB", m.render("AB"));
        assertEquals("ABC", m.render("ABC"));
        assertEquals("BCD", m.render("ABCD"));
    }

    @Test
    public void constructor_and_render_Literal5() throws Exception {

        String s = "-2.3";

        FormatModifier m = new FormatModifier(s);

        assertEquals("-2.3", m.getLiteral());

        assertEquals(2, m.getMinimumFieldWidth().intValue());
        assertFalse(m.isRightJustified());
        assertEquals(3, m.getMaximumFieldWidth().intValue());
        assertTrue(m.isTruncateFromFront());

        assertEquals("  ", m.render(""));
        assertEquals("A ", m.render("A"));
        assertEquals("AB", m.render("AB"));
        assertEquals("ABC", m.render("ABC"));
        assertEquals("BCD", m.render("ABCD"));
    }

    @Test
    public void constructor_and_render_Literal6() throws Exception {

        String s = "-2.-3";

        FormatModifier m = new FormatModifier(s);

        assertEquals("-2.-3", m.getLiteral());

        assertEquals(2, m.getMinimumFieldWidth().intValue());
        assertFalse(m.isRightJustified());
        assertEquals(3, m.getMaximumFieldWidth().intValue());
        assertFalse(m.isTruncateFromFront());

        assertEquals("  ", m.render(""));
        assertEquals("A ", m.render("A"));
        assertEquals("AB", m.render("AB"));
        assertEquals("ABC", m.render("ABC"));
        assertEquals("ABC", m.render("ABCD"));
    }

    @Test
    public void constructor_and_render_Literal7() throws Exception {

        String s = "-blah";

        try {

            new FormatModifier(s);
            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid minimum field width specification:"));
            assertTrue(msg.contains("blah"));
            assertTrue(e.getCause() instanceof NumberFormatException);
        }
    }

    @Test
    public void render_Null() throws Exception {

        FormatModifier m = new FormatModifier("1");

        try {

            m.render(null);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null argument"));
        }
    }

    // render() --------------------------------------------------------------------------------------------------------
    
    @Test
    public void render() throws Exception {}

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
