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
import io.novaordis.events.log4j.pattern.ProcessedString;
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
public class WildFlyExceptionTest extends ConversionSpecifierTest {

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

        WildFlyException s = getConversionSpecifierToTest(null);

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

    @Test
    @Override
    public void parseLogContent_NextConversionPatternComponentIsException() throws Exception {

        // This is overridden by WildFlyExceptionTest with a noop, because we don't support the case of two
        // successive "%E%E"
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        WildFlyException s = new WildFlyException("E");

        assertEquals(WildFlyException.CONVERSION_CHARACTER, s.getConversionCharacter().charValue());
        assertNull(s.getFormatModifier());
        assertEquals("%E", s.getLiteral());
    }

    @Test
    public void constructor_WithFormatModifier() throws Exception {

        WildFlyException s = new WildFlyException("-5E");

        assertEquals(WildFlyException.CONVERSION_CHARACTER, s.getConversionCharacter().charValue());
        FormatModifier m = s.getFormatModifier();
        assertEquals("-5", m.getLiteral());
        assertEquals("%-5E", s.getLiteral());
    }

    @Test
    public void constructor_WrongIdentifier() throws Exception {

        try {

            new WildFlyException("c");

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains(
                    "missing expected conversion character '" + WildFlyException.CONVERSION_CHARACTER + "'"));
        }
    }

    // getLiteral() ----------------------------------------------------------------------------------------------------

    @Test
    public void getLiteral_TypeSpecific() throws Exception {

        WildFlyException s = getConversionSpecifierToTest(null);

        assertEquals("%E", s.getLiteral());
    }

    @Test
    public void getLiteral_TypeSpecific_FormatModifier() throws Exception {

        FormatModifier m = new FormatModifier("-5");
        WildFlyException s = getConversionSpecifierToTest(m);

        assertEquals("%-5E", s.getLiteral());
    }

    // parseLogContent() -----------------------------------------------------------------------------------------------

    @Test
    public void parseLogContent_TypeSpecific() throws Exception {

        WildFlyException cs = new WildFlyException();

        RenderedLogEvent e = cs.parseLogContent(": a.b.C: something", 0, null);

        assertEquals(0, e.from());
        assertEquals(": a.b.C: something".length(), e.to());
        assertEquals(": a.b.C: something", e.get());
    }

    @Test
    public void parseLogContent_PatternMismatch() throws Exception {

        WildFlyException cs = new WildFlyException();

        RenderedLogEvent e = cs.parseLogContent("something", 0, null);

        //
        // TODO: the current behavior is incorrect, because we're not performing pattern matching in parseLogContent()
        //       on accounts of optimization. If we ever change that, this test will fail
        //

        assertEquals(0, e.from());
        assertEquals("something".length(), e.to());
        assertEquals("something", e.get());
    }

    // findNext() ------------------------------------------------------------------------------------------------------

    @Test
    public void findNext_ExceptionPattern() throws Exception {

        String s = "something: com.microsoft.sqlserver.jdbc.SQLServerException: something else";

        WildFlyException e = new WildFlyException();

        Integer i = e.findNext(s, 0);

        assertEquals(9, i.intValue());
    }

    @Test
    public void findNext_ExceptionPattern2() throws Exception {

        String s = "a message that accompanies an exception: java.lang.Exception: SYNTHETIC";

        WildFlyException e = new WildFlyException();

        Integer i = e.findNext(s, 0);

        assertEquals("a message that accompanies an exception".length(), i.intValue());
    }

    @Test
    public void findNext_NoMatch() throws Exception {

        String s = "something";

        WildFlyException e = new WildFlyException();

        Integer i = e.findNext(s, 0);

        assertNull(i);
    }

    // parseLiteralAfterFormatModifierWasUnapplied() -------------------------------------------------------------------

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied() throws Exception {

        //
        // minimal processing, we rely on the fact that findNext() matched before
        //

        WildFlyException e = new WildFlyException();

        RenderedLogEvent re = e.parseLiteralAfterFormatModifierWasUnapplied(new ProcessedString(10, "something", 20));

        assertEquals("something", re.get());
        assertEquals(10, re.from());
        assertEquals(20, re.to());
    }

    // injectIntoLog4jEvent() ------------------------------------------------------------------------------------------

    @Test
    public void injectIntoLog4jEvent() throws Exception {

        WildFlyException cs = new WildFlyException();

        Log4jEventImpl e = new Log4jEventImpl();

        cs.injectIntoLog4jEvent(e, "first line of the exception");

        String s = e.getExceptionRendering();
        assertEquals("first line of the exception", s);
    }

    @Test
    public void injectIntoLog4jEvent_InvalidType() throws Exception {

        WildFlyException cs = new WildFlyException();

        Log4jEventImpl e = new Log4jEventImpl();

        try {

            cs.injectIntoLog4jEvent(e, new Integer(3));

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

        return ": a.b.C: blah";
    }

    @Override
    protected WildFlyException getConversionSpecifierToTest(FormatModifier m) throws Exception {

        WildFlyException cs = new WildFlyException();
        cs.setFormatModifier(m);
        return cs;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
