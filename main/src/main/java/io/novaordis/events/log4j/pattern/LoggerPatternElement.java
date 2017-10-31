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
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class LoggerPatternElement implements Log4jPatternElement {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final char VALUE = 'c';

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Instances of this class can only be created by the Log4jPatternElementFactory or within the package.
     */
    protected LoggerPatternElement() {
    }

    // Log4jPatternElement implementation ------------------------------------------------------------------------------

    @Override
    public String getLiteral() {
        throw new RuntimeException("getLiteral() NOT YET IMPLEMENTED");
    }

    @Override
    public AddResult add(char c) throws Log4jPatternLayoutException {
        throw new RuntimeException("add() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
