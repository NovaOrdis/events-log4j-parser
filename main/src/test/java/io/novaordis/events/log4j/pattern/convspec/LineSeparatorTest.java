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

import io.novaordis.events.log4j.impl.Log4jEventImpl;
import io.novaordis.events.log4j.pattern.AddResult;
import io.novaordis.events.log4j.pattern.FormatModifier;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.log4j.pattern.ProcessedString;
import io.novaordis.events.log4j.pattern.RenderedLogEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class LineSeparatorTest extends ConversionSpecifierTest {

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

        LineSeparator e = getConversionSpecifierToTest(null);

        AddResult r = e.add(' ');
        assertEquals(AddResult.NOT_ACCEPTED, r);

        try {

            e.add(' ');

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains("attempt to add more characters to a closed conversion pattern component"));
        }
    }

    //
    // FormatModifier and LineSeparator are NOT used together
    //

    @Test
    @Override
    public void getFormatModifier() throws Exception {

        // noop
    }

    @Test
    @Override
    public void getFormatModifier_2() throws Exception {

        // noop
    }

    @Test
    @Override
    public void getLiteral_ConversionSpecifier_WithFormatModifier() throws Exception {

        // noop
    }

    @Test
    @Override
    public void parseLogContent_With_FormatModifier() throws Exception {

        // noop
    }

    @Test
    @Override
    public void parseLogContent_With_FormatModifier_2() throws Exception {

        // noop
    }

    @Test
    @Override
    public void parseLogContent_With_FormatModifier_3() throws Exception {

        // noop
    }

    @Test
    @Override
    public void parseLogContent_NextConversionPatternComponentIsException() throws Exception {

        //
        // noop - "%n%E" does not make sense
        //
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // find() ------------------------------------------------------------------------------------------------------

    @Test
    public void findNext() throws Exception {

        LineSeparator ls = new LineSeparator();

        Integer i = ls.find("something", 0);

        assertNotNull(i);
        assertEquals("something".length(), i.intValue());

        Integer i2 = ls.find("something", 1);

        assertNotNull(i2);
        assertEquals("something".length(), i2.intValue());

        Integer i3 = ls.find("something", "something".length() - 1);

        assertNotNull(i3);
        assertEquals("something".length(), i3.intValue());

        Integer i4 = ls.find("something", "something".length());

        assertNull(i4);
    }

    // parseLiteralAfterFormatModifierWasUnapplied() -------------------------------------------------------------------

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied_InvalidBoundaries() throws Exception {

        LineSeparator ls = new LineSeparator();

        try {

            ls.parseLiteralAfterFormatModifierWasUnapplied(new ProcessedString(1, "something", 2));
            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("invalid boundaries"));
        }
    }

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied() throws Exception {

        LineSeparator ls = new LineSeparator();

        RenderedLogEvent e = ls.parseLiteralAfterFormatModifierWasUnapplied(new ProcessedString(10, "something", 10));

        assertEquals(10, e.from());
        assertEquals(10, e.to());
    }

    // injectIntoEvent() -----------------------------------------------------------------------------------------------

    @Test
    public void injectIntoEvent() throws Exception {

        //
        // we're a noop
        //

        Log4jEventImpl e = new Log4jEventImpl();

        int propertyCount = e.getProperties().size();

        LineSeparator ls = new LineSeparator();

        ls.injectIntoEvent(7L, e, "something else");

        int propertyCount2 = e.getProperties().size();

        assertEquals(propertyCount, propertyCount2);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected String getMatchingLogContent() throws Exception {

        return "";
    }

    @Override
    protected LineSeparator getConversionSpecifierToTest(FormatModifier m) throws Exception {

        LineSeparator cs = new LineSeparator();
        cs.setFormatModifier(m);
        return cs;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
