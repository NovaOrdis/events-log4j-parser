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

package io.novaordis.events.log4j.cli;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/28/17
 */
public class MainTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void jbossServerLogEndToEnd() throws Exception {

        File f = new File(System.getProperty("basedir"), "src/test/resources/data/jboss.server.log");
        assertTrue(f.isFile());

        String[] args = new String[] { f.getPath() };

        //
        // install a mock System.out so we can capture the output
        //

        PrintStream originalSystemOut = System.out;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream mockOut = new PrintStream(baos);

        try {

            System.setOut(mockOut);

            Main.main(args);
        }
        finally {

            System.setOut(originalSystemOut);
        }

        assertTrue(baos.size() != 0);

        BufferedReader originalContentReader = new BufferedReader(new FileReader(f));
        BufferedReader parsedContentReader =
                new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));

        int lineNumber = 0;

        String originalLine, lineAfterParsing;

        while((originalLine = originalContentReader.readLine()) != null) {

            lineNumber ++;
            lineAfterParsing = parsedContentReader.readLine();

            if (!originalLine.equals(lineAfterParsing)) {

                fail("lines " + lineNumber + " differ");
            }
        }

        //
        // we should also have consumed all parsed lines
        //

        assertNull(parsedContentReader.readLine());

        originalContentReader.close();
        parsedContentReader.close();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
