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

import io.novaordis.events.log4j.pattern.AddResult;
import io.novaordis.events.log4j.pattern.FormatModifier;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;

import static org.junit.Assert.assertEquals;
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
            assertTrue(msg.contains("attempt to add more characters to a closed element"));
        }
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected String getMatchingLogContent() throws Exception {
        throw new RuntimeException("getMatchingLogContent() NOT YET IMPLEMENTED");
    }

    @Override
    protected Logger getConversionSpecifierToTest(FormatModifier m) throws Exception {

        Logger cs = new Logger();
        cs.setFormatModifier(m);
        return cs;
    }

    @Override
    protected String getMatchingLogContent(FormatModifier m) throws Exception {
        throw new RuntimeException("renderWithConversionSpecifierToTest() NOT YET IMPLEMENTED");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
