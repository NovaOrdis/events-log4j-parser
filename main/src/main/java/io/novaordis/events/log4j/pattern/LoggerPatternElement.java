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

/**
 * The name of the logger that publishes the logging event.
 *
 * c{precision}
 *
 * The type of the corresponding parsed object instance is a String containing the logger value.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class LoggerPatternElement extends Log4jPatternElementBase {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final char IDENTIFIER = 'c';

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Log4jPatternElement implementation ------------------------------------------------------------------------------

    @Override
    public Character getIdentifier() {

        return IDENTIFIER;
    }

    @Override
    public ParsedElement parse(String s, int from, Log4jPatternElement next) throws Log4jPatternLayoutException {
        throw new RuntimeException("parse() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
