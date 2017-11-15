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

import static org.junit.Assert.assertEquals;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/15/17
 */
public class FormatModifierTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_Literal() throws Exception {

        String s = "20";

        FormatModifier m = new FormatModifier(s);

        assertEquals("20", m.getLiteral());
    }

    @Test
    public void constructor_Literal2() throws Exception {

        String s = "-20";

        FormatModifier m = new FormatModifier(s);

        assertEquals("-20", m.getLiteral());
    }

    @Test
    public void constructor_Literal3() throws Exception {

        String s = "%.30";

        FormatModifier m = new FormatModifier(s);

        assertEquals(".30", m.getLiteral());
    }

    @Test
    public void constructor_Literal4() throws Exception {

        String s = "20.30";

        FormatModifier m = new FormatModifier(s);

        assertEquals("20.30", m.getLiteral());
    }

    @Test
    public void constructor_Literal5() throws Exception {

        String s = "-20.30";

        FormatModifier m = new FormatModifier(s);

        assertEquals("-20.30", m.getLiteral());
    }

    @Test
    public void constructor_Literal6() throws Exception {

        String s = "-20.-30";

        FormatModifier m = new FormatModifier(s);

        assertEquals("-20.-30", m.getLiteral());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
