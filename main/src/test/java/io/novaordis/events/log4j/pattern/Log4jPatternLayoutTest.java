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

import java.util.Iterator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/28/17
 */
public class Log4jPatternLayoutTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NullLiteral() throws Exception {

        try {

            new Log4jPatternLayout(null);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null log4j pattern layout literal"));
        }
    }

    @Test
    public void constructor_TrailingPatternElementMarker() throws Exception {

        try {

            new Log4jPatternLayout("something%");

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains(
                    "'" + Log4jPatternLayout.PATTERN_ELEMENT_MARKER + "' not followed by any pattern element"));
        }
    }

    @Test
    public void constructor() throws Exception {

        Log4jPatternLayout pl = new Log4jPatternLayout("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n");

        assertEquals("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n", pl.getLiteral());

        Iterator<Log4jPatternElement> i = pl.getPatternElementIterator();

        assertTrue(i.hasNext());

        DatePatternElement d = (DatePatternElement)i.next();

        assertEquals("%d{HH:mm:ss,SSS}", d.getLiteral());
        assertEquals("HH:mm:ss,SSS", d.getSimpleDateFormat().toPattern());
        assertEquals(DatePatternElement.IDENTIFIER, d.getIdentifier().charValue());

        assertTrue(i.hasNext());

        LiteralPatternElement l = (LiteralPatternElement)i.next();

        assertEquals(" ", l.getLiteral());

        assertTrue(i.hasNext());

        LevelPatternElement p = (LevelPatternElement)i.next();

        assertEquals("%-5p", p.getLiteral());
        assertEquals("-5", p.getFormatModifierLiteral());
        assertEquals(LevelPatternElement.IDENTIFIER, p.getIdentifier().charValue());

        assertTrue(i.hasNext());

        LiteralPatternElement l2 = (LiteralPatternElement)i.next();

        assertEquals(" [", l2.getLiteral());

        assertTrue(i.hasNext());

        LoggerPatternElement c = (LoggerPatternElement)i.next();

        assertEquals("%c", c.getLiteral());
        assertNull(c.getFormatModifierLiteral());
        assertEquals(LoggerPatternElement.IDENTIFIER, c.getIdentifier().charValue());

        assertTrue(i.hasNext());

        LiteralPatternElement l3 = (LiteralPatternElement)i.next();

        assertEquals("] (", l3.getLiteral());

        assertTrue(i.hasNext());

        ThreadNamePatternElement t = (ThreadNamePatternElement)i.next();

        assertEquals("%t", t.getLiteral());
        assertNull(t.getFormatModifierLiteral());
        assertEquals(ThreadNamePatternElement.IDENTIFIER, t.getIdentifier().charValue());

        assertTrue(i.hasNext());

        LiteralPatternElement l4 = (LiteralPatternElement)i.next();

        assertEquals(") ", l4.getLiteral());

        assertTrue(i.hasNext());

        UnknownPatternElement u = (UnknownPatternElement)i.next();

        assertEquals("%s", u.getLiteral());
        assertEquals('s', u.getIdentifier().charValue());

        assertTrue(i.hasNext());

        UnknownPatternElement u2 = (UnknownPatternElement)i.next();

        assertEquals("%E", u2.getLiteral());
        assertEquals('E', u2.getIdentifier().charValue());

        assertTrue(i.hasNext());

        LineSeparatorPatternElement n = (LineSeparatorPatternElement)i.next();
        assertNull(n.getFormatModifierLiteral());
        assertEquals(LineSeparatorPatternElement.IDENTIFIER, n.getIdentifier().charValue());

        assertFalse(i.hasNext());
    }

    @Test
    public void constructor_InvalidDateFormat() throws Exception {

        try {

            new Log4jPatternLayout("%d{blah}");

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid date format"));
            assertTrue(msg.contains("blah"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
