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
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/31/17
 */
public abstract class Log4jPatternElementBase implements Log4jPatternElement {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private FormatModifier formatModifier;

    private boolean closed;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Instances of this class can only be created by the Log4jPatternElementFinder or within the package.
     */
    protected Log4jPatternElementBase() {

        this.closed = false;
    }

    /**
     * @param pattern, without the pattern element marker ('%'). Example: "-5p" or "d{HH}".
     */
    protected Log4jPatternElementBase(String pattern) throws Log4jPatternLayoutException {

        char[] chars = pattern.toCharArray();

        String formatModifierLiteral = null;
        char identifier = getIdentifier();
        boolean add = false;

        for (char c : chars) {

            if (add) {

                add(c);
            }
            else if (c == identifier) {

                add = true;
            }
            else {

                if (formatModifierLiteral == null) {

                    formatModifierLiteral = "" + c;
                }
                else {

                    formatModifierLiteral += c;
                }
            }
        }

        if (!add) {

            //
            // identifier not encountered
            //

            throw new Log4jPatternLayoutException("identifier '" + identifier + "' not encountered");
        }
        else {

            closed = true;
        }

        if (formatModifierLiteral != null) {

            FormatModifier m = new FormatModifier(formatModifierLiteral);

            setFormatModifier(m);
        }

    }

    // Log4jPatternElement implementation ------------------------------------------------------------------------------

    @Override
    public String getLiteral() {

        FormatModifier m = getFormatModifier();

        return "" + Log4jPatternLayout.PATTERN_ELEMENT_MARKER + (m == null ? "" : m.getLiteral()) + getIdentifier();
    }

    @Override
    public FormatModifier getFormatModifier() {

        return formatModifier;
    }

    @Override
    public AddResult add(char c) throws Log4jPatternLayoutException {

        if (closed) {

            throw new Log4jPatternLayoutException("attempt to add more characters to a closed element");
        }

        closed = true;

        return AddResult.NOT_ACCEPTED;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return getLiteral();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    void setFormatModifier(FormatModifier m) throws Log4jPatternLayoutException {

        this.formatModifier = m;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
