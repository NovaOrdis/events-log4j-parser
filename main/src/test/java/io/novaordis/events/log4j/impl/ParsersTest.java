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

package io.novaordis.events.log4j.impl;

import io.novaordis.events.api.parser.ParsingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class ParsersTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // find() ----------------------------------------------------------------------------------------------------------

    @Test
    public void find_NoCategory() throws Exception {

        ParsingResult r = Parsers.find("blah", 0, '[', ']', 1L);
        assertNull(r);
    }

    @Test
    public void find() throws Exception {

        ParsingResult r = Parsers.find(" something [this is what we expect] something else", 0, '[', ']', 1L);

        assertNotNull(r);

        assertEquals("this is what we expect", r.getValue());
        assertEquals(35, r.getNext());
    }

    @Test
    public void find_NestedBrackets() throws Exception {

        ParsingResult r = Parsers.find(" blah [this [is [what] we] [expect]] something else", 0, '[', ']', 1L);

        assertNotNull(r);

        assertEquals("this [is [what] we] [expect]", r.getValue());
        assertEquals(36, r.getNext());
    }

    @Test
    public void find_UnbalancedNestedBracketsNotPartOfCategory() throws Exception {

        ParsingResult r = Parsers.find("blah [ blah2 ] ] something else", 0, '[', ']', 1L);

        assertNotNull(r);

        assertEquals(" blah2 ", r.getValue());
        assertEquals(14, r.getNext());
    }

    @Test
    public void find_UnbalancedNestedBrackets() throws Exception {

        try {

            Parsers.find(" blah [ ", 0, '[', ']', 7L);
            fail("should have thrown exception");
        }
        catch(ParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced"));
            assertTrue(msg.contains("["));
            assertTrue(msg.contains("]"));
            assertEquals(7L, e.getLineNumber().longValue());

            // where the bracket sequence starts
            assertEquals(6, e.getPositionInLine().intValue());
        }
    }

    @Test
    public void find_UnbalancedNestedBrackets2() throws Exception {

        try {

            Parsers.find(" blah [ something [ something else ] ", 0, '[', ']', 7L);
            fail("should have thrown exception");
        }
        catch(ParsingException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced"));
            assertTrue(msg.contains("["));
            assertTrue(msg.contains("]"));
            assertEquals(7L, e.getLineNumber().longValue());;

            // where the bracket sequence starts
            assertEquals(6, e.getPositionInLine().intValue());
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
