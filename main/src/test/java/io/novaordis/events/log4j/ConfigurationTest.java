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

package io.novaordis.events.log4j;

import io.novaordis.events.processing.Procedure;
import io.novaordis.events.query.KeywordQuery;
import io.novaordis.events.query.MixedQuery;
import io.novaordis.events.query.Query;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class ConfigurationTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NoCommand() throws Exception {

        File f = new File(System.getProperty("basedir"), "pom.xml");
        assertTrue(f.isFile());

        String[] args = {
                
                f.getPath(),
                "red",
                "blue"
        };

        Configuration c = new Configuration(args);

        File f2 = c.getFile();

        assertEquals(f, f2);

        Query q = c.getQuery();

        MixedQuery mq = (MixedQuery)q;

        List<KeywordQuery> keywords = mq.getKeywordQueries();

        assertEquals(2, keywords.size());

        assertEquals("red", keywords.get(0).getKeyword());
        assertEquals("blue", keywords.get(1).getKeyword());
    }

    @Test
    public void constructor_Command() throws Exception {

        File f = new File(System.getProperty("basedir"), "pom.xml");
        assertTrue(f.isFile());

        String[] args = {

                "describe",
                f.getPath(),
                "red",
                "blue"
        };

        Configuration c = new Configuration(args);

        File f2 = c.getFile();

        assertEquals(f, f2);

        Procedure p = c.getProcedure();
        assertEquals("describe", p.getCommandLineLabel());

        Query q = c.getQuery();

        MixedQuery mq = (MixedQuery)q;

        List<KeywordQuery> keywords = mq.getKeywordQueries();

        assertEquals(2, keywords.size());

        assertEquals("red", keywords.get(0).getKeyword());
        assertEquals("blue", keywords.get(1).getKeyword());
    }


    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
