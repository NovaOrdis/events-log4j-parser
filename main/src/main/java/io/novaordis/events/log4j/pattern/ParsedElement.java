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
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/5/17
 */
public class ParsedElement {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private int from;
    private int to;
    private String literal;
    private Object object;

    // Constructors ----------------------------------------------------------------------------------------------------

    public ParsedElement(Object o, String literal, int from, int to) {

        this.object = o;
        this.literal = literal;
        this.from = from;
        this.to = to;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the position in string at this
     */
    public int from() {

        return from;
    }

    /**
     * @return the position in string of the first character that immediately follows the element.
     */
    public int to() {

        return to;
    }

    /**
     * @return the string representation of the element, as it shows up in the original string.
     */
    public String getLiteral() {

        return literal;
    }

    /**
     * @return the parsed object, whose type depends on the Log4jPatternElement it corresponds to.
     */
    public Object get() {

        return object;
    }

    @Override
    public String toString() {

        return "(" + from + ", " + to + ") \"" + literal + "\" " + object;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
