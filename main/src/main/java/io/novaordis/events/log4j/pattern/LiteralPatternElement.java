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
 * The type of the corresponding parsed object instance is a String containing the literal value.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class LiteralPatternElement extends Log4jPatternElementBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String literal;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Instances of this class can only be created by the Log4jPatternElementFinder or within the package.
     */
    protected LiteralPatternElement() {
    }

    /**
     * Instances of this class can only be created by the Log4jPatternElementFinder or within the package.
     */
    protected LiteralPatternElement(String literal) {

        for(char c: literal.toCharArray()) {

            try {

                add(c);
            }
            catch(Log4jPatternLayoutException e) {

                throw new IllegalStateException(e);
            }
        }
    }

    // Log4jPatternElement implementation ------------------------------------------------------------------------------

    @Override
    public Character getIdentifier() {

        return null;
    }

    @Override
    public String getLiteral() {

        return literal;
    }

    @Override
    public AddResult add(char c) throws Log4jPatternLayoutException {

        if (literal == null) {

            literal = new String(new char[] {c});
        }
        else {

            literal += c;
        }

        return AddResult.ACCEPTED;
    }

    @Override
    public ParsedElement parse(String s, int from, Log4jPatternElement next) throws Log4jPatternLayoutException {

        int length = literal.length();
        int to = from + length;

        String s2 = s.substring(from, to);

        if (!literal.equals(s2)) {

            throw new Log4jPatternLayoutException(
                    "pattern element's literal \"" + literal +
                            "\" does not match the parsed string literal \"" + s2 + "\"");
        }

        return new ParsedElement(literal, literal, from, to);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        String literal = getLiteral();

        if (literal == null) {

            return "null";
        }

        return "'" + literal + "'";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
