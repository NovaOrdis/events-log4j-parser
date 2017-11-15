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
 * A log4j pattern layout format modifier representation. It can be used to to change the minimum field width, the
 * maximum field width and justification.
 *
 * Example: the "-5" from "%-5p".
 *
 * See https://kb.novaordis.com/index.php/Log4j_Pattern_Layout#Format_Modifier
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/15/17
 */
public class FormatModifier {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public FormatModifier(String literal) throws Log4jPatternLayoutException {

    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the format modifier literal. Will never return null.
     */
    public String getLiteral() {

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
