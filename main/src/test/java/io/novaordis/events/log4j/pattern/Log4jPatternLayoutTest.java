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

import io.novaordis.events.log4j.pattern.convspec.Date;
import io.novaordis.events.log4j.pattern.convspec.Level;
import io.novaordis.events.log4j.pattern.convspec.LineSeparator;
import io.novaordis.events.log4j.pattern.convspec.Logger;
import io.novaordis.events.log4j.pattern.convspec.ThreadName;

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

        assertEquals(11, pl.getPatternComponentCount());

        Iterator<ConversionPatternComponent> i = pl.getPatternComponentIterator();

        assertTrue(i.hasNext());

        Date d = (Date)i.next();

        assertEquals("%d{HH:mm:ss,SSS}", d.getLiteral());
        assertEquals("HH:mm:ss,SSS", d.getSimpleDateFormat().toPattern());
        assertEquals(Date.CONVERSION_CHARACTER, d.getConversionCharacter().charValue());

        assertTrue(i.hasNext());

        LiteralText l = (LiteralText)i.next();

        assertEquals(" ", l.getLiteral());

        assertTrue(i.hasNext());

        Level p = (Level)i.next();

        assertEquals("%-5p", p.getLiteral());
        FormatModifier m = p.getFormatModifier();
        assertEquals("-5", m.getLiteral());
        assertEquals(Level.CONVERSION_CHARACTER, p.getConversionCharacter().charValue());

        assertTrue(i.hasNext());

        LiteralText l2 = (LiteralText)i.next();

        assertEquals(" [", l2.getLiteral());

        assertTrue(i.hasNext());

        Logger c = (Logger)i.next();

        assertEquals("%c", c.getLiteral());
        assertNull(c.getFormatModifier());
        assertEquals(Logger.CONVERSION_CHARACTER, c.getConversionCharacter().charValue());

        assertTrue(i.hasNext());

        LiteralText l3 = (LiteralText)i.next();

        assertEquals("] (", l3.getLiteral());

        assertTrue(i.hasNext());

        ThreadName t = (ThreadName)i.next();

        assertEquals("%t", t.getLiteral());
        assertNull(t.getFormatModifier());
        assertEquals(ThreadName.CONVERSION_CHARACTER, t.getConversionCharacter().charValue());

        assertTrue(i.hasNext());

        LiteralText l4 = (LiteralText)i.next();

        assertEquals(") ", l4.getLiteral());

        assertTrue(i.hasNext());

        fail("return here");

//        UnknownPatternElement u = (UnknownPatternElement)i.next();
//
//        assertEquals("%s", u.getLiteral());
//        assertEquals('s', u.getConversionCharacter().charValue());
//
//        assertTrue(i.hasNext());
//
//        UnknownPatternElement u2 = (UnknownPatternElement)i.next();
//
//        assertEquals("%E", u2.getLiteral());
//        assertEquals('E', u2.getConversionCharacter().charValue());
//
//        assertTrue(i.hasNext());

        LineSeparator n = (LineSeparator)i.next();
        assertNull(n.getFormatModifier());
        assertEquals(LineSeparator.CONVERSION_CHARACTER, n.getConversionCharacter().charValue());

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
