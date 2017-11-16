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

import io.novaordis.events.log4j.pattern.AddResult;
import io.novaordis.events.log4j.pattern.FormatModifier;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
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

            assertTrue(msg.contains("expected identifier '" + WildFlyException.CONVERSION_CHARACTER + "' not found"));
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

        fail("return here");
    }

    @Test
    public void parseLogContent_PatternMismatch() throws Exception {

        //
        // we cannot really mismatch a message, as anything is valid as a message - noop
        //

        fail("return here");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected String getMatchingLogContent() throws Exception {

        throw new RuntimeException("DETERMINE WHAT IS A MATCHING LOG CONTENT FOR %E");
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
