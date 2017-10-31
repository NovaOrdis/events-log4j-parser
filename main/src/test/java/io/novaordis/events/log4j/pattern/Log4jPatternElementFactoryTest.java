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

import static org.junit.Assert.assertNotNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class Log4jPatternElementFactoryTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void getInstance_Date() throws Exception {

        Log4jPatternElementFactory f = new Log4jPatternElementFactory();

        DatePatternElement e = (DatePatternElement)f.getInstance(DatePatternElement.VALUE);

        assertNotNull(e);
    }

    @Test
    public void getInstance_Level() throws Exception {

        Log4jPatternElementFactory f = new Log4jPatternElementFactory();

        LevelPatternElement e = (LevelPatternElement)f.getInstance(LevelPatternElement.VALUE);

        assertNotNull(e);
    }

    @Test
    public void getInstance_LineSeparator() throws Exception {

        Log4jPatternElementFactory f = new Log4jPatternElementFactory();

        LineSeparatorPatternElement e = (LineSeparatorPatternElement)f.getInstance(LineSeparatorPatternElement.VALUE);

        assertNotNull(e);
    }

    @Test
    public void getInstance_Logger() throws Exception {

        Log4jPatternElementFactory f = new Log4jPatternElementFactory();

        LoggerPatternElement e = (LoggerPatternElement)f.getInstance(LoggerPatternElement.VALUE);

        assertNotNull(e);
    }

    @Test
    public void getInstance_ThreadName() throws Exception {

        Log4jPatternElementFactory f = new Log4jPatternElementFactory();

        ThreadNamePatternElement e = (ThreadNamePatternElement)f.getInstance(ThreadNamePatternElement.VALUE);

        assertNotNull(e);
    }

    @Test
    public void getInstance_Unknown() throws Exception {

        Log4jPatternElementFactory f = new Log4jPatternElementFactory();

        UnknownPatternElement e = (UnknownPatternElement)f.getInstance('s');

        assertNotNull(e);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
