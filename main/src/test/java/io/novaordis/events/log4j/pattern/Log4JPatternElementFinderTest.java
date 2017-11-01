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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class Log4JPatternElementFinderTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void lookup_InvalidFrom() throws Exception {

        Log4jPatternElementFinder f = new Log4jPatternElementFinder();

        try {

            f.lookup("blah", 4, new ElementHolder());
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("'from' beyond the edge"));
        }
    }

    @Test
    public void lookup_InvalidFrom2() throws Exception {

        Log4jPatternElementFinder f = new Log4jPatternElementFinder();

        try {

            f.lookup("blah", 5, new ElementHolder());
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("'from' beyond the edge"));
        }
    }

    @Test
    public void lookup_Date() throws Exception {

        Log4jPatternElementFinder f = new Log4jPatternElementFinder();
        
        ElementHolder holder = new ElementHolder();

        int i = f.lookup("%" + DatePatternElement.IDENTIFIER, 1, holder);
        
        assertEquals(1, i);
        
        DatePatternElement e = (DatePatternElement)holder.get();

        assertNotNull(e);
    }

    @Test
    public void lookup_Level() throws Exception {

        Log4jPatternElementFinder f = new Log4jPatternElementFinder();

        ElementHolder holder = new ElementHolder();

        int i = f.lookup("%" + LevelPatternElement.IDENTIFIER, 1, holder);

        assertEquals(1, i);

        LevelPatternElement e = (LevelPatternElement)holder.get();

        assertNotNull(e);
    }

    @Test
    public void lookup_LineSeparator() throws Exception {

        Log4jPatternElementFinder f = new Log4jPatternElementFinder();

        ElementHolder holder = new ElementHolder();

        int i = f.lookup("%" + LineSeparatorPatternElement.IDENTIFIER, 1, holder);

        assertEquals(1, i);

        LineSeparatorPatternElement e = (LineSeparatorPatternElement)holder.get();

        assertNotNull(e);
    }

    @Test
    public void lookup_Logger() throws Exception {

        Log4jPatternElementFinder f = new Log4jPatternElementFinder();

        ElementHolder holder = new ElementHolder();

        int i = f.lookup("%" + LoggerPatternElement.IDENTIFIER, 1, holder);

        assertEquals(1, i);

        LoggerPatternElement e = (LoggerPatternElement)holder.get();

        assertNotNull(e);
    }

    @Test
    public void lookup_ThreadName() throws Exception {

        Log4jPatternElementFinder f = new Log4jPatternElementFinder();

        ElementHolder holder = new ElementHolder();

        int i = f.lookup("%" + ThreadNamePatternElement.IDENTIFIER, 1, holder);

        assertEquals(1, i);

        ThreadNamePatternElement e = (ThreadNamePatternElement)holder.get();

        assertNotNull(e);
    }

    @Test
    public void lookup_FormatModifierLiteral_LeftPad() throws Exception {

        Log4jPatternElementFinder f = new Log4jPatternElementFinder();

        ElementHolder holder = new ElementHolder();

        int i = f.lookup("%10p   ", 1, holder);

        assertEquals(3, i);

        LevelPatternElement e = (LevelPatternElement)holder.get();

        assertNotNull(e);

        assertEquals("10", e.getFormatModifierLiteral());
    }

    @Test
    public void lookup_FormatModifierLiteral_RightPad() throws Exception {

        Log4jPatternElementFinder f = new Log4jPatternElementFinder();

        ElementHolder holder = new ElementHolder();

        int i = f.lookup("%-5p   ", 1, holder);

        assertEquals(3, i);

        LevelPatternElement e = (LevelPatternElement)holder.get();

        assertNotNull(e);

        assertEquals("-5", e.getFormatModifierLiteral());

        String s = e.toString();

        assertEquals("%-5p", s);
    }

    @Test
    public void lookup_FormatModifierLiteral_Truncate() throws Exception {

        Log4jPatternElementFinder f = new Log4jPatternElementFinder();

        ElementHolder holder = new ElementHolder();

        int i = f.lookup("%.30p   ", 1, holder);

        assertEquals(4, i);

        LevelPatternElement e = (LevelPatternElement)holder.get();

        assertNotNull(e);

        assertEquals(".30", e.getFormatModifierLiteral());
    }

    @Test
    public void lookup_Unknown() throws Exception {

        Log4jPatternElementFinder f = new Log4jPatternElementFinder();

        ElementHolder holder = new ElementHolder();

        int i = f.lookup("%s   ", 1, holder);

        assertEquals(1, i);

        UnknownPatternElement e = (UnknownPatternElement)holder.get();

        assertNotNull(e);

        assertNull(e.getFormatModifierLiteral());

        assertEquals('s', e.getIdentifier().charValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
