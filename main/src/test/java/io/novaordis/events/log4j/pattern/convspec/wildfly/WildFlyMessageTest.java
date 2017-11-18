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

package io.novaordis.events.log4j.pattern.convspec.wildfly;

import org.junit.Test;

import io.novaordis.events.log4j.impl.Log4jEventImpl;
import io.novaordis.events.log4j.pattern.AddResult;
import io.novaordis.events.log4j.pattern.FormatModifier;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.log4j.pattern.RenderedLogEvent;
import io.novaordis.events.log4j.pattern.convspec.ConversionSpecifierTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class WildFlyMessageTest extends ConversionSpecifierTest {

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

        WildFlyMessage s = getConversionSpecifierToTest(null);

        AddResult r = s.add(' ');
        assertEquals(AddResult.NOT_ACCEPTED, r);

        try {

            s.add(' ');

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

        WildFlyMessage s = new WildFlyMessage("s");

        assertEquals(WildFlyMessage.CONVERSION_CHARACTER, s.getConversionCharacter().charValue());
        assertNull(s.getFormatModifier());
        assertEquals("%s", s.getLiteral());
    }

    @Test
    public void constructor_WithFormatModifier() throws Exception {

        WildFlyMessage s = new WildFlyMessage("-5s");

        assertEquals(WildFlyMessage.CONVERSION_CHARACTER, s.getConversionCharacter().charValue());
        FormatModifier m = s.getFormatModifier();
        assertEquals("-5", m.getLiteral());
        assertEquals("%-5s", s.getLiteral());
    }

    @Test
    public void constructor_WrongIdentifier() throws Exception {

        try {

            new WildFlyMessage("c");

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains(
                    "missing expected conversion character '" + WildFlyMessage.CONVERSION_CHARACTER + "'"));
        }
    }

    // getLiteral() ----------------------------------------------------------------------------------------------------

    @Test
    public void getLiteral_TypeSpecific() throws Exception {

        WildFlyMessage s = getConversionSpecifierToTest(null);

        assertEquals("%s", s.getLiteral());
    }

    @Test
    public void getLiteral_TypeSpecific_FormatModifier() throws Exception {

        FormatModifier m = new FormatModifier("-5");
        WildFlyMessage s = getConversionSpecifierToTest(m);

        assertEquals("%-5s", s.getLiteral());
    }

    // parseLogContent() -----------------------------------------------------------------------------------------------

    @Test
    public void parseLogContent_TypeSpecific() throws Exception {

        String line = "this is some message";

        int from = 0;

        WildFlyMessage pe = new WildFlyMessage();

        RenderedLogEvent p = pe.parseLogContent(line, from, null);

        String s = (String)p.get();

        assertEquals(0, p.from());
        assertEquals(20, p.to());
        assertEquals("this is some message", s);
    }

    @Test
    public void parseLogContent_PatternMismatch() throws Exception {

        //
        // we cannot really mismatch a message, as anything is valid as a message - noop
        //
    }

    // injectIntoEvent() ------------------------------------------------------------------------------------------

    @Test
    public void injectIntoLog4jEvent() throws Exception {

        WildFlyMessage cs = new WildFlyMessage();

        Log4jEventImpl e = new Log4jEventImpl();

        cs.injectIntoEvent(e, "something");

        assertEquals("something", e.getMessage());
        assertEquals("something", e.getStringProperty(Log4jEventImpl.MESSAGE_PROPERTY_NAME).getString());
    }

    @Test
    public void injectIntoEvent_InvalidType() throws Exception {

        WildFlyMessage cs = new WildFlyMessage();

        Log4jEventImpl e = new Log4jEventImpl();

        try {

            cs.injectIntoEvent(e, new Integer(3));

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains("invalid value type"));
            assertTrue(msg.contains("Integer"));
            assertTrue(msg.contains("expected String"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected String getMatchingLogContent() throws Exception {

        return "this is a synthetic message";
    }

    @Override
    protected WildFlyMessage getConversionSpecifierToTest(FormatModifier m) throws Exception {

        WildFlyMessage cs = new WildFlyMessage();
        cs.setFormatModifier(m);
        return cs;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
