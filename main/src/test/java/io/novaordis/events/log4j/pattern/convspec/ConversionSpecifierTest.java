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

import io.novaordis.events.log4j.pattern.ConversionPatternComponent;
import io.novaordis.events.log4j.pattern.ConversionPatternComponentTest;
import io.novaordis.events.log4j.pattern.FormatModifier;
import io.novaordis.events.log4j.pattern.RenderedLogEventElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public abstract class ConversionSpecifierTest extends ConversionPatternComponentTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    /**
     * Test that must be provided by subclasses and must insure that add() invoked after an add() that returned
     * AddResult.LAST throws Log4jPatternLayoutException.
     */
    @Test
    public abstract void addAfterLast() throws Exception;

    /**
     * Test that must be provided by subclasses and must insure that add() invoked after an add() that returned
     * AddResult.NOT_ACCEPTED throws Log4jPatternLayoutException.
     */
    @Test
    public abstract void addAfterNotAccepted() throws Exception;

    @Test
    public void getFormatModifier() throws Exception {

        ConversionSpecifier e = getConversionSpecifierToTest(null);

        FormatModifier m = e.getFormatModifier();

        assertNull(m);
    }

    @Test
    public void getFormatModifier2() throws Exception {

        FormatModifier m = new FormatModifier("-20.-30");

        ConversionSpecifier e = getConversionSpecifierToTest(m);

        FormatModifier m2 = e.getFormatModifier();

        assertEquals("-20.-30", m2.getLiteral());
    }

    // getLiteral() ----------------------------------------------------------------------------------------------------

    @Test
    public void getLiteral() throws Exception {

        ConversionSpecifier e = getConversionSpecifierToTest(null);

        assertNull(e.getFormatModifier());

        String expected = "%" + e.getConversionCharacter();
        String l = e.getLiteral();

        assertEquals(expected, l);
    }

    @Test
    public void getLiteral_WithFormatModifier() throws Exception {

        ConversionSpecifier e = getConversionSpecifierToTest(null);

        ((ConversionSpecifierBase)e).setFormatModifier(new FormatModifier("-5"));

        String expected = "%-5" + e.getConversionCharacter();
        String l = e.getLiteral();

        assertEquals(expected, l);
    }
    
    // parse() ---------------------------------------------------------------------------------------------------------
    
    @Test
    public void parse_NoFormatModifier() throws Exception {

        FormatModifier m = null;

        String s = ">" + renderWithConversionSpecifierToTest(m);

        ConversionSpecifier cs = getConversionSpecifierToTest(m);

        RenderedLogEventElement e = cs.parse(s, 1, null);

        e.getLiteral();
        e.get();
        e.from();
        e.to();
    }

    @Test
    public void parse_WithFormatModifier() throws Exception {

        String ms = "20";

        FormatModifier m = new FormatModifier(ms);

        String s = ">" + renderWithConversionSpecifierToTest(m);

        ConversionSpecifier cs = getConversionSpecifierToTest(m);

        RenderedLogEventElement e = cs.parse(s, 1, null);
    }

    @Test
    public void parse_WithFormatModifier2() throws Exception {

        String ms = "-20";

        fail("return here");
    }

    @Test
    public void parse_WithFormatModifier3() throws Exception {

        String ms = ".30";

        fail("return here");
    }

    @Test
    public void parse_WithFormatModifier4() throws Exception {

        String ms = "   .30";

        fail("return here");
    }

    @Test
    public void parse_WithFormatModifier5() throws Exception {

        String ms = "20.30";

        fail("return here");
    }

    @Test
    public void parse_WithFormatModifier6() throws Exception {

        String ms = "-20.30";

        fail("return here");
    }

    @Test
    public void parse_WithFormatModifier7() throws Exception {

        String ms = "-20.-30";

        fail("return here");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected ConversionPatternComponent getConversionPatternComponentToTest() throws Exception {

        return getConversionSpecifierToTest(null);
    }

    protected abstract ConversionSpecifier getConversionSpecifierToTest(FormatModifier m) throws Exception;

    /**
     * @param m may be null, which means no format modifier.
     */
    protected abstract String renderWithConversionSpecifierToTest(FormatModifier m) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
