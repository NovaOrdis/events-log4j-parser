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

import io.novaordis.events.log4j.pattern.ConversionPatternComponentHolder;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.log4j.pattern.convspec.wildfly.WildFlyException;
import io.novaordis.events.log4j.pattern.convspec.wildfly.WildFlyMessage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class ConversionSpecifierFinderTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void lookup_InvalidFrom() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        try {

            f.lookup("blah", 4, new ConversionPatternComponentHolder());
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("'from' beyond the edge"));
        }
    }

    @Test
    public void lookup_InvalidFrom2() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        try {

            f.lookup("blah", 5, new ConversionPatternComponentHolder());
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("'from' beyond the edge"));
        }
    }

    @Test
    public void lookup_Date() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        int i = f.lookup("%" + Date.CONVERSION_CHARACTER, 1, holder);
        
        assertEquals(1, i);
        
        Date cs = (Date)holder.getInstance();

        assertNotNull(cs);
    }

    @Test
    public void lookup_Level() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        int i = f.lookup("%" + Level.CONVERSION_CHARACTER, 1, holder);

        assertEquals(1, i);

        Level cs = (Level)holder.getInstance();

        assertNotNull(cs);
    }

    @Test
    public void lookup_LineSeparator() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        int i = f.lookup("%" + LineSeparator.CONVERSION_CHARACTER, 1, holder);

        assertEquals(1, i);

        LineSeparator cs = (LineSeparator)holder.getInstance();

        assertNotNull(cs);
    }

    @Test
    public void lookup_Logger() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        int i = f.lookup("%" + Logger.CONVERSION_CHARACTER, 1, holder);

        assertEquals(1, i);

        Logger cs = (Logger)holder.getInstance();

        assertNotNull(cs);
    }

    @Test
    public void lookup_ThreadName() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        int i = f.lookup("%" + ThreadName.CONVERSION_CHARACTER, 1, holder);

        assertEquals(1, i);

        ThreadName cs = (ThreadName)holder.getInstance();

        assertNotNull(cs);
    }

    @Test
    public void lookup_Message() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        int i = f.lookup("%" + Message.CONVERSION_CHARACTER, 1, holder);

        assertEquals(1, i);

        Message cs = (Message)holder.getInstance();

        assertNotNull(cs);
    }

    @Test
    public void lookup_WildFlyMessage() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        int i = f.lookup("%" + WildFlyMessage.CONVERSION_CHARACTER, 1, holder);

        assertEquals(1, i);

        WildFlyMessage cs = (WildFlyMessage)holder.getInstance();

        assertNotNull(cs);
    }

    @Test
    public void lookup_WildFlyException() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        int i = f.lookup("%" + WildFlyException.CONVERSION_CHARACTER, 1, holder);

        assertEquals(1, i);

        WildFlyException cs = (WildFlyException)holder.getInstance();

        assertNotNull(cs);
    }

    @Test
    public void lookup_FormatModifierLiteral_LeftPad() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        int i = f.lookup("%10p   ", 1, holder);

        assertEquals(3, i);

        Level e = (Level)holder.getInstance();

        assertNotNull(e);

        assertEquals("10", e.getFormatModifier().getLiteral());
    }

    @Test
    public void lookup_FormatModifierLiteral_RightPad() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        int i = f.lookup("%-5p   ", 1, holder);

        assertEquals(3, i);

        Level e = (Level)holder.getInstance();

        assertNotNull(e);

        assertEquals("-5", e.getFormatModifier().getLiteral());

        String s = e.toString();

        assertEquals("%-5p", s);
    }

    @Test
    public void lookup_FormatModifierLiteral_Truncate() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        int i = f.lookup("%.30p   ", 1, holder);

        assertEquals(4, i);

        Level e = (Level)holder.getInstance();

        assertNotNull(e);

        assertEquals(".30", e.getFormatModifier().getLiteral());
    }

    @Test
    public void lookup_UnknownConversionCharacter() throws Exception {

        ConversionSpecifierFinder f = new ConversionSpecifierFinder();

        ConversionPatternComponentHolder holder = new ConversionPatternComponentHolder();

        try {

            f.lookup("%y   ", 1, holder);

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("unknown conversion character:"));
            assertTrue(msg.contains("'y'"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
