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
import io.novaordis.events.processing.count.Count;
import io.novaordis.events.processing.describe.Describe;
import io.novaordis.events.processing.exclude.Exclude;
import io.novaordis.events.query.FieldQuery;
import io.novaordis.events.query.KeywordQuery;
import io.novaordis.events.query.MixedQuery;
import io.novaordis.events.query.Query;
import io.novaordis.utilities.UserErrorException;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NoArguments() throws Exception {

        String[] args = new String[0];

        Configuration c = new Configuration(args);

        Procedure p = c.getProcedure();
        assertTrue(p instanceof Help);

        assertTrue(c.getFiles().isEmpty());
    }

    @Test
    public void constructor_OneFile() throws Exception {

        File f = new File(System.getProperty("basedir"), "pom.xml");
        assertTrue(f.isFile());

        String[] args = {

                f.getPath()
        };

        Configuration c = new Configuration(args);

        List<File> files = c.getFiles();

        assertEquals(1, files.size());
        assertEquals(f, files.get(0));

        assertNull(c.getProcedure());
        assertNull(c.getQuery());
    }

    @Test
    public void constructor_MultipleFiles() throws Exception {

        File f = new File(System.getProperty("basedir"), "pom.xml");
        assertTrue(f.isFile());

        File f2 = new File(System.getProperty("basedir"), "src/test/resources/log4j.xml");
        assertTrue(f2.isFile());

        String[] args = {

                f.getPath(),
                f2.getPath()
        };

        Configuration c = new Configuration(args);

        List<File> files = c.getFiles();

        assertEquals(2, files.size());
        assertEquals(f, files.get(0));
        assertEquals(f2, files.get(1));

        assertNull(c.getProcedure());
        assertNull(c.getQuery());
    }

    @Test
    public void constructor_Count_Query_File() throws Exception {

        File f = new File(System.getProperty("basedir"), "pom.xml");
        assertTrue(f.isFile());

        String[] args = {

                "count",
                "log-level:ERROR",
                f.getPath()
        };

        Configuration c = new Configuration(args);

        List<File> files = c.getFiles();
        assertEquals(1, files.size());
        assertEquals(f, files.get(0));

        Procedure p = c.getProcedure();
        assertTrue(p instanceof Count);

        FieldQuery fq = (FieldQuery)c.getQuery();
        assertEquals("log-level", fq.getFieldName());
        assertEquals("ERROR", fq.getValue());
    }

    @Test
    public void constructor_AbbreviatedCount_Query_File() throws Exception {

        File f = new File(System.getProperty("basedir"), "pom.xml");
        assertTrue(f.isFile());

        String[] args = {

                "-c",
                "log-level:ERROR",
                f.getPath()
        };

        Configuration c = new Configuration(args);

        List<File> files = c.getFiles();
        assertEquals(1, files.size());
        assertEquals(f, files.get(0));

        Procedure p = c.getProcedure();
        assertTrue(p instanceof Count);

        FieldQuery fq = (FieldQuery)c.getQuery();
        assertEquals("log-level", fq.getFieldName());
        assertEquals("ERROR", fq.getValue());
    }

    @Test
    public void constructor_NoCommand_MixedQuery() throws Exception {

        File f = new File(System.getProperty("basedir"), "pom.xml");
        assertTrue(f.isFile());

        String[] args = {

                "red",
                "blue",
                f.getPath(),
        };

        Configuration c = new Configuration(args);

        List<File> files = c.getFiles();
        assertEquals(1, files.size());
        assertEquals(f, files.get(0));

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
                "red",
                "blue",
                f.getPath(),
        };

        Configuration c = new Configuration(args);

        List<File> files = c.getFiles();
        assertEquals(1, files.size());
        assertEquals(f, files.get(0));

        Describe p = (Describe)c.getProcedure();
        assertNotNull(p);

        Query q = c.getQuery();

        MixedQuery mq = (MixedQuery)q;

        List<KeywordQuery> keywords = mq.getKeywordQueries();

        assertEquals(2, keywords.size());

        assertEquals("red", keywords.get(0).getKeyword());
        assertEquals("blue", keywords.get(1).getKeyword());
    }


    @Test
    public void constructor_QueryAndFile() throws Exception {

        File f = new File(System.getProperty("basedir"), "pom.xml");
        assertTrue(f.isFile());

        String[] args = {

                "log-level:ERROR",
                f.getPath(),
        };

        Configuration c = new Configuration(args);

        List<File> files = c.getFiles();
        assertEquals(1, files.size());
        assertEquals(f, files.get(0));

        assertNull(c.getProcedure());

        FieldQuery fq = (FieldQuery)c.getQuery();
        assertEquals("log-level", fq.getFieldName());
        assertEquals("ERROR", fq.getValue());
    }

    @Test
    public void constructor_MissingFile() throws Exception {

        String[] args = {

                "-c",
                "something",
        };

        try {

            new Configuration(args);
            fail("should throw exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("no file specified"));
        }
    }

    // heuristics ------------------------------------------------------------------------------------------------------

    @Test
    public void heuristics_AnExcludeProcedureIsInitializedWithTheQuery() throws Exception {

        File f = new File(System.getProperty("basedir"), "pom.xml");
        assertTrue(f.isFile());

        String[] args = {

                Exclude.COMMAND_LINE_LABEL,
                "log-level:ERROR",
                f.getPath(),
        };

        Configuration c = new Configuration(args);

        //
        // make sure the procedure is initialized with the query
        //

        Exclude exclude = (Exclude)c.getProcedure();

        FieldQuery q = (FieldQuery)exclude.getQuery();
        assertEquals("log-level", q.getFieldName());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
