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
    public void constructor_and_apply_Literal() throws Exception {

        String s = "5";

        FormatModifier m = new FormatModifier(s);

        assertEquals("5", m.getLiteral());

        assertEquals(5, m.getMinimumFieldWidth().intValue());
        assertTrue(m.isRightJustified());
        assertNull(m.getMaximumFieldWidth());
        assertTrue(m.isTruncateFromFront());


        assertEquals("     ", m.apply(""));
        assertEquals("    a", m.apply("a"));
        assertEquals("   aa", m.apply("aa"));
        assertEquals("aaaaa", m.apply("aaaaa"));
        assertEquals("aaaaaa", m.apply("aaaaaa"));
    }

    @Test
    public void constructor_and_apply_Literal2() throws Exception {

        String s = "-5";

        FormatModifier m = new FormatModifier(s);

        assertEquals("-5", m.getLiteral());

        assertEquals(5, m.getMinimumFieldWidth().intValue());
        assertFalse(m.isRightJustified());
        assertNull(m.getMaximumFieldWidth());
        assertTrue(m.isTruncateFromFront());

        assertEquals("     ", m.apply(""));
        assertEquals("a    ", m.apply("a"));
        assertEquals("aa   ", m.apply("aa"));
        assertEquals("aaaaa", m.apply("aaaaa"));
        assertEquals("aaaaaa", m.apply("aaaaaa"));
    }

    @Test
    public void constructor_and_apply_Literal3() throws Exception {

        String s = ".3";

        FormatModifier m = new FormatModifier(s);

        assertEquals(".3", m.getLiteral());

        assertNull(m.getMinimumFieldWidth());
        assertTrue(m.isRightJustified());
        assertEquals(3, m.getMaximumFieldWidth().intValue());
        assertTrue(m.isTruncateFromFront());

        assertEquals("", m.apply(""));
        assertEquals("A", m.apply("A"));
        assertEquals("AB", m.apply("AB"));
        assertEquals("ABC", m.apply("ABC"));
        assertEquals("BCD", m.apply("ABCD"));
    }

    @Test
    public void constructor_and_apply_Literal3_1() throws Exception {

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
    public void constructor_and_apply_Literal4() throws Exception {

        String s = "2.3";

        FormatModifier m = new FormatModifier(s);

        assertEquals("2.3", m.getLiteral());

        assertEquals(2, m.getMinimumFieldWidth().intValue());
        assertTrue(m.isRightJustified());
        assertEquals(3, m.getMaximumFieldWidth().intValue());
        assertTrue(m.isTruncateFromFront());

        assertEquals("  ", m.apply(""));
        assertEquals(" A", m.apply("A"));
        assertEquals("AB", m.apply("AB"));
        assertEquals("ABC", m.apply("ABC"));
        assertEquals("BCD", m.apply("ABCD"));
    }

    @Test
    public void constructor_and_apply_Literal5() throws Exception {

        String s = "-2.3";

        FormatModifier m = new FormatModifier(s);

        assertEquals("-2.3", m.getLiteral());

        assertEquals(2, m.getMinimumFieldWidth().intValue());
        assertFalse(m.isRightJustified());
        assertEquals(3, m.getMaximumFieldWidth().intValue());
        assertTrue(m.isTruncateFromFront());

        assertEquals("  ", m.apply(""));
        assertEquals("A ", m.apply("A"));
        assertEquals("AB", m.apply("AB"));
        assertEquals("ABC", m.apply("ABC"));
        assertEquals("BCD", m.apply("ABCD"));
    }

    @Test
    public void constructor_and_apply_Literal6() throws Exception {

        String s = "-2.-3";

        FormatModifier m = new FormatModifier(s);

        assertEquals("-2.-3", m.getLiteral());

        assertEquals(2, m.getMinimumFieldWidth().intValue());
        assertFalse(m.isRightJustified());
        assertEquals(3, m.getMaximumFieldWidth().intValue());
        assertFalse(m.isTruncateFromFront());

        assertEquals("  ", m.apply(""));
        assertEquals("A ", m.apply("A"));
        assertEquals("AB", m.apply("AB"));
        assertEquals("ABC", m.apply("ABC"));
        assertEquals("ABC", m.apply("ABCD"));
    }

    @Test
    public void constructor_and_apply_Literal7() throws Exception {

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
    public void apply_Null() throws Exception {

        FormatModifier m = new FormatModifier("1");

        try {

            m.apply(null);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null argument"));
        }
    }

    // unapply() -------------------------------------------------------------------------------------------------------

    @Test
    public void unapply_ArgumentShorterThanMinimumFieldWidth() throws Exception {

        FormatModifier m = new FormatModifier("3");

        String s = "AB";

        try {

            m.unapply(s, 0);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("string argument shorter than minimum field width"));
        }
    }

    @Test
    public void unapply_FillRight() throws Exception {

        FormatModifier m = new FormatModifier("3");

        String s = m.apply("A");
        assertEquals("  A", s);

        ProcessedString ps = m.unapply(s, 0);
        String s2 = ps.getProcessedString();
        assertEquals("A", s2);
        assertEquals(0, ps.from());
        assertEquals(3, ps.to());
    }

    @Test
    public void unapply_Fill_Left() throws Exception {

        FormatModifier m = new FormatModifier("-3");

        String s = m.apply("A");
        assertEquals("A  ", s);

        ProcessedString ps = m.unapply(s, 0);
        String s2 = ps.getProcessedString();
        assertEquals("A", s2);
        assertEquals(0, ps.from());
        assertEquals(3, ps.to());
    }

    @Test
    public void unapply_Truncate_FromFront() throws Exception {

        FormatModifier m = new FormatModifier(".3");

        String s = m.apply("ABCD");
        assertEquals("BCD", s);

        ProcessedString ps = m.unapply(s, 0);
        String s2 = ps.getProcessedString();
        assertEquals("BCD", s2);
        assertEquals(0, ps.from());
        assertEquals(3, ps.to());
    }

    @Test
    public void unapply_Truncate_FromFront_StringLongerThanMax() throws Exception {

        FormatModifier m = new FormatModifier(".3");

        String s = "ABCD";

        ProcessedString ps = m.unapply(s, 0);
        String s2 = ps.getProcessedString();
        assertEquals("ABC", s2);
        assertEquals(0, ps.from());
        assertEquals(3, ps.to());
    }

    @Test
    public void unapply_Truncate_FromBack() throws Exception {

        FormatModifier m = new FormatModifier(".-3");

        String s = m.apply("ABCD");
        assertEquals("ABC", s);

        ProcessedString ps = m.unapply(s, 0);
        String s2 = ps.getProcessedString();
        assertEquals("ABC", s2);
        assertEquals(0, ps.from());
        assertEquals(3, ps.to());
    }

    @Test
    public void unapply_Truncate_FromBack_StringLongerThanMax() throws Exception {

        FormatModifier m = new FormatModifier(".-3");

        String s = "ABCD";

        ProcessedString ps = m.unapply(s, 0);
        String s2 = ps.getProcessedString();
        assertEquals("ABC", s2);
        assertEquals(0, ps.from());
        assertEquals(3, ps.to());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
