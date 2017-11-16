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
import io.novaordis.events.log4j.pattern.RenderedLogEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

    // add() -----------------------------------------------------------------------------------------------------------

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

    // getFormatModifier() ---------------------------------------------------------------------------------------------

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
    public void getLiteral_ConversionSpecifier() throws Exception {

        ConversionSpecifier e = getConversionSpecifierToTest(null);

        assertNull(e.getFormatModifier());

        String expected = "%" + e.getConversionCharacter();
        String l = e.getLiteral();

        assertEquals(expected, l);
    }

    @Test
    public void getLiteral_ConversionSpecifier_WithFormatModifier() throws Exception {

        ConversionSpecifier e = getConversionSpecifierToTest(null);

        ((ConversionSpecifierBase)e).setFormatModifier(new FormatModifier("-5"));

        String expected = "%-5" + e.getConversionCharacter();
        String l = e.getLiteral();

        assertEquals(expected, l);
    }

    // parseLogContent() -----------------------------------------------------------------------------------------------

    @Test
    public void parseLogContent_NoFormatModifier() throws Exception {

        FormatModifier m = null;

        //noinspection ConstantConditions
        String rendering = getMatchingLogContent(m);

        //noinspection ConstantConditions
        String s = ">" + rendering;

        //noinspection ConstantConditions
        ConversionSpecifier cs = getConversionSpecifierToTest(m);

        RenderedLogEvent e = cs.parseLogContent(s, 1, null);

        assertEquals(rendering, e.getLiteral());
        assertNotNull(e.get());
        assertEquals(1, e.from());
        assertEquals(s.length(), e.to());
    }

    @Test
    public void parseLogContent_WithFormatModifier() throws Exception {

        String ms = "5.6";

        FormatModifier m = new FormatModifier(ms);

        String rendering = getMatchingLogContent(m);

        String s = ">" + rendering + "<";

        ConversionSpecifier cs = getConversionSpecifierToTest(m);

        RenderedLogEvent e = cs.parseLogContent(s, 1, null);

        assertEquals(rendering, e.getLiteral());
        assertNotNull(e.get());
        assertEquals(1, e.from());
        assertEquals(1 + rendering.length(), e.to());
    }

    @Test
    public void parseLogContent_WithFormatModifier2() throws Exception {

        String ms = "100";

        FormatModifier m = new FormatModifier(ms);

        String rendering = getMatchingLogContent(m);
        String trimmedRendering = rendering.trim();

        String s = ">" + rendering;

        ConversionSpecifier cs = getConversionSpecifierToTest(m);

        RenderedLogEvent e = cs.parseLogContent(s, 1, null);

        assertEquals(trimmedRendering, e.getLiteral());
        assertNotNull(e.get());
        assertEquals(1, e.from());
        assertEquals(1 + rendering.length(), e.to());
    }

    @Test
    public void parseLogContent_WithFormatModifier3() throws Exception {

        String ms = "-100";

        FormatModifier m = new FormatModifier(ms);

        String rendering = getMatchingLogContent(m);
        String trimmedRendering = rendering.trim();

        String s = ">" + rendering;

        ConversionSpecifier cs = getConversionSpecifierToTest(m);

        RenderedLogEvent e = cs.parseLogContent(s, 1, null);

        assertEquals(trimmedRendering, e.getLiteral());
        assertNotNull(e.get());
        assertEquals(1, e.from());
        assertEquals(1 + rendering.length(), e.to());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected ConversionPatternComponent getConversionPatternComponentToTest() throws Exception {

        return getConversionSpecifierToTest(null);
    }

    protected abstract ConversionSpecifier getConversionSpecifierToTest(FormatModifier m) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    /**
     * @param m may be null, which means no format modifier. If m is not null, apply the log content using the given
     *          format modifier.
     */
    private String getMatchingLogContent(FormatModifier m) throws Exception {

        String c = getMatchingLogContent();

        if (m == null) {

            return c;
        }

        return m.apply(c);
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
