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

import io.novaordis.events.log4j.impl.Log4jEventImpl;
import io.novaordis.events.log4j.pattern.convspec.wildfly.WildFlyException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/15/17
 */
public abstract class ConversionPatternComponentTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // getLiteral() ----------------------------------------------------------------------------------------------------

    /**
     * May be overridden by subclasses.
     */
    @Test
    public void getLiteral() throws Exception {

        ConversionPatternComponent c = getConversionPatternComponentToTest();

        String literal = c.getLiteral();

        assertNotNull(literal);
    }

    // parseLogContent() -----------------------------------------------------------------------------------------------

    @Test
    public void parseLogContent() throws Exception {

        ConversionPatternComponent c = getConversionPatternComponentToTest();

        String matchingLogContent = getMatchingLogContent();

        RenderedLogEvent rendering = c.parseLogContent(matchingLogContent, 0, null);

        assertNotNull(rendering.get());
        assertEquals(0, rendering.from());
        assertEquals(matchingLogContent.length(), rendering.to());
    }

    /**
     * This will be overridden by WildFlyExceptionTest with a noop, because we don't support the case of two
     * successive "%E%E"
     */
    @Test
    public void parseLogContent_NextConversionPatternComponentIsException() throws Exception {

        //
        // Exception (%E) is particular in that:
        //
        // 1. It may or may not result in an exception in the log.
        // 2. The exception may be multi-lines.
        //

        ConversionPatternComponent c = getConversionPatternComponentToTest();

        String matchingLogContent = getMatchingLogContent();

        WildFlyException exceptionComponent = new WildFlyException();

        RenderedLogEvent rendering = c.parseLogContent(matchingLogContent, 0, exceptionComponent);

        assertNotNull(rendering.get());
        assertEquals(0, rendering.from());
        assertEquals(matchingLogContent.length(), rendering.to());
    }

    // find() ------------------------------------------------------------------------------------------------------

    @Test
    public void findNext_NullLogContent() throws Exception {

        ConversionPatternComponent c = getConversionPatternComponentToTest();

        try {

            c.find(null, 0);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("null log content"));
        }
    }

    @Test
    public void findNext_OverBounds() throws Exception {

        ConversionPatternComponent c = getConversionPatternComponentToTest();

        try {

            c.find("something", -1);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("invalid 'from' index: "));
            assertTrue(msg.contains("-1"));
        }
    }

    @Test
    public void findNext_OnTheEdge() throws Exception {

        ConversionPatternComponent c = getConversionPatternComponentToTest();

        String line = "something";
        int from = "something".length();

        Integer i = c.find(line, from);
        assertNull(i);
    }

    @Test
    public void findNext_OverBounds2() throws Exception {

        ConversionPatternComponent c = getConversionPatternComponentToTest();

        try {

            c.find("something", "something".length() + 1);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("invalid 'from' index: "));
            assertTrue(msg.contains("" + ("something".length() + 1)));
        }
    }

    @Test
    public void findNext_OverBounds3() throws Exception {

        ConversionPatternComponent c = getConversionPatternComponentToTest();

        try {

            c.find("something", 1000);

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("invalid 'from' index: "));
            assertTrue(msg.contains("1000"));
        }
    }

    // injectIntoEvent() ------------------------------------------------------------------------------------------

    @Test
    public void injectIntoLog4jEvent_NullValue() throws Exception {

        ConversionPatternComponent c = getConversionPatternComponentToTest();

        Log4jEventImpl e = new Log4jEventImpl();

        int propertyCount = e.getProperties().size();

        c.injectIntoEvent(e, null);

        int propertyCount2 = e.getProperties().size();

        //
        // no properties are injected and the method does not throw exception
        //

        assertEquals(propertyCount, propertyCount2);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract ConversionPatternComponent getConversionPatternComponentToTest() throws Exception;

    protected abstract String getMatchingLogContent() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
