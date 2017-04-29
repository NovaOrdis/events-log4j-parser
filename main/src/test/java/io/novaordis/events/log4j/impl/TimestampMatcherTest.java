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

import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public class TimestampMatcherTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // find() ----------------------------------------------------------------------------------------------------------

    @Test
    public void find() throws Exception {

        String s = "14:56:16,781      INFO  [org.jboss.modules] (main) JBoss Modules version 1.3.7.Final-redhat-1";

        TimestampMatcher t = TimestampMatcher.find(1, s);

        assertNotNull(t);

        long time = t.getTime();
        int i = t.getIndexOfNextCharInLine();

        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("14:56:16,781").getTime(), time);
        assertEquals(s.indexOf('I'), i);
    }

    @Test
    public void find_InvalidTimestampPatternInvalidHour() throws Exception {

        String s = "14:66:16,781      INFO  [org.jboss.modules] (main) JBoss Modules version 1.3.7.Final-redhat-1";

        TimestampMatcher t = TimestampMatcher.find(1, s);

        assertNull(t);
    }

    @Test
    public void find_LineWithInvalidLeadingChars() throws Exception {

        String s = "\u001B[0m14:56:16,781      INFO  [org.jboss.modules] (main) JBoss Modules version 1.3.7.Final-redhat-1";

        TimestampMatcher t = TimestampMatcher.find(1, s);

        assertNotNull(t);

        long time = t.getTime();
        int i = t.getIndexOfNextCharInLine();

        assertEquals(new SimpleDateFormat("HH:mm:ss,SSS").parse("14:56:16,781").getTime(), time);
        assertEquals(s.indexOf('I'), i);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}