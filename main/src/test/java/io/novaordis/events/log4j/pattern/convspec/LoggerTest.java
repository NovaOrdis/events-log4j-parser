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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class LoggerTest extends ConversionSpecifierTest {

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

        Logger e = getConversionSpecifierToTest(null);

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

    // Tests -----------------------------------------------------------------------------------------------------------

    // parseLiteralAfterFormatModifierWasUnapplied() -------------------------------------------------------------------

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied() throws Exception {

        Logger l = new Logger();

        assertNull(l.getFormatModifier());

        ProcessedString ps = new ProcessedString(2, "io.novaordis.something.SomethingElse");

        RenderedLogEvent e = l.parseLiteralAfterFormatModifierWasUnapplied(ps);

        assertEquals(2, e.from());
        assertEquals(2 + "io.novaordis.something.SomethingElse".length(), e.to());
        assertEquals("io.novaordis.something.SomethingElse", e.get());
    }

    // injectIntoEvent() ------------------------------------------------------------------------------------------

    @Test
    public void injectIntoLog4jEvent() throws Exception {

        Logger cs = new Logger();

        Log4jEventImpl e = new Log4jEventImpl();

        cs.injectIntoEvent(e, "a.b.C");

        assertEquals("a.b.C", e.getLogger());
    }

    @Test
    public void injectIntoEvent_InvalidType() throws Exception {

        Logger cs = new Logger();

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

        return "io.novaordis.something.SomethingElse";
    }

    @Override
    protected Logger getConversionSpecifierToTest(FormatModifier m) throws Exception {

        Logger cs = new Logger();
        cs.setFormatModifier(m);
        return cs;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
