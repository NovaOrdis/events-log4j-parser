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
 * Models unrecognized pattern elements.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class UnknownPatternElement extends Log4jPatternElementBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private char identifier;

    // Constructors ----------------------------------------------------------------------------------------------------

    protected UnknownPatternElement(char identifier) {

        this.identifier = identifier;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Log4jPatternElement implementation ------------------------------------------------------------------------------

    @Override
    public Character getIdentifier() {

        return identifier;
    }

    @Override
    public ParsedElement parse(String s, int from, Log4jPatternElement next) throws Log4jPatternLayoutException {
        throw new RuntimeException("parse() NOT YET IMPLEMENTED");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
