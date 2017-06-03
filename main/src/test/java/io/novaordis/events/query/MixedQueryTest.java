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

package io.novaordis.events.query;

import io.novaordis.events.api.event.GenericTimedEvent;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class MixedQueryTest extends QueryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        MixedQuery q = new MixedQuery(Collections.singletonList("something"));

        GenericTimedEvent e = new GenericTimedEvent();

        e.setStringProperty("test-key", "blah blah something blah");

        assertTrue(q.selects(e));

        GenericTimedEvent e2 = new GenericTimedEvent();

        e2.setStringProperty("test-key", "blah blah blah");

        assertFalse(q.selects(e2));
    }

    // selects() -------------------------------------------------------------------------------------------------------

    @Test
    public void selects_noArgumentsIsNullQuery() throws Exception {

        MixedQuery q = new MixedQuery();
        assertTrue(q.selects(new GenericTimedEvent()));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Query getQueryToTest() throws Exception {

        return new MixedQuery();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
