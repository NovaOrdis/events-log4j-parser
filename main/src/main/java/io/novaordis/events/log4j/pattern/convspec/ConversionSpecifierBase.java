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

package io.novaordis.events.log4j.pattern.convspec;

import io.novaordis.events.log4j.pattern.AddResult;
import io.novaordis.events.log4j.pattern.ConversionPatternComponent;
import io.novaordis.events.log4j.pattern.FormatModifier;
import io.novaordis.events.log4j.pattern.Log4jPatternLayout;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.log4j.pattern.RenderedLogEvent;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/31/17
 */
public abstract class ConversionSpecifierBase implements ConversionSpecifier {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private FormatModifier formatModifier;

    private boolean closed;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Instances of this class can only be created by the ConversionSpecifierFinder or within the package.
     */
    protected ConversionSpecifierBase() {

        this.closed = false;
    }

    /**
     * @param pattern, without the pattern element marker ('%'). Example: "-5p" or "d{HH}".
     */
    protected ConversionSpecifierBase(String pattern) throws Log4jPatternLayoutException {

        char[] chars = pattern.toCharArray();

        String formatModifierLiteral = null;
        char identifier = getConversionCharacter();
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

            throw new Log4jPatternLayoutException("expected identifier '" + identifier + "' not found");
        }
        else {

            closed = true;
        }

        if (formatModifierLiteral != null) {

            FormatModifier m = new FormatModifier(formatModifierLiteral);

            setFormatModifier(m);
        }

    }

    // ConversionPatternComponent implementation -----------------------------------------------------------------------

    @Override
    public String getLiteral() {

        FormatModifier m = getFormatModifier();

        return "" + Log4jPatternLayout.PATTERN_ELEMENT_MARKER +
                (m == null ? "" : m.getLiteral()) + getConversionCharacter();
    }

    @Override
    public RenderedLogEvent parseLogContent(String logContent, int from, ConversionPatternComponent next)
            throws Log4jPatternLayoutException {

        //
        // process format modifier information, if available, it'll give more details about how to parse the
        // literal
        //

        if (formatModifier != null) {

            //
            // there is a format modifier
            //

            throw new RuntimeException("NYE");
        }


        return parseLiteralAfterFormatModifierHandling(logContent, from, next);
    }

    // ConversionSpecifier implementation ------------------------------------------------------------------------------

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

    public void setFormatModifier(FormatModifier m) throws Log4jPatternLayoutException {

        this.formatModifier = m;
    }

    @Override
    public String toString() {

        return getLiteral();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Handles the literal after the format modifier information has been factored in.
     */
    protected abstract RenderedLogEvent parseLiteralAfterFormatModifierHandling(
            String logContent, int from, ConversionPatternComponent next) throws Log4jPatternLayoutException;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
