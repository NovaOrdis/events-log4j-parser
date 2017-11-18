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
import io.novaordis.events.log4j.pattern.ConversionPatternComponentBase;
import io.novaordis.events.log4j.pattern.FormatModifier;
import io.novaordis.events.log4j.pattern.Log4jPatternLayout;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.log4j.pattern.ProcessedString;
import io.novaordis.events.log4j.pattern.RenderedLogEvent;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/31/17
 */
public abstract class ConversionSpecifierBase extends ConversionPatternComponentBase implements ConversionSpecifier {

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

            throw new Log4jPatternLayoutException("missing expected conversion character '" + identifier + "'");
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

        return "" + Log4jPatternLayout.CONVERSION_SPECIFIER_MARKER +
                (m == null ? "" : m.getLiteral()) + getConversionCharacter();
    }

    @Override
    public RenderedLogEvent parseLogContent(String line, int from, ConversionPatternComponent next)
            throws Log4jPatternLayoutException {

        Integer renderedLogEventEnd = null;

        if (next != null) {

            renderedLogEventEnd = next.find(line, from);
        }

        if (renderedLogEventEnd == null) {

            //
            // we cannot unequivocally determine the occurrence of the next conversion pattern component on the line
            // or such pattern does not exist on the line, or there are no more patterns
            //

            renderedLogEventEnd = line.length();
        }

        //
        // process format modifier information, if available, it'll give more details about how to parse the
        // literal
        //

        ProcessedString ps;

        if (formatModifier != null) {

            //
            // a format modifier was applied to the rendering of the log event via this conversion specifier, so
            // before an attempt to extract the original log event from the log content, apply the inverse of the
            // format modifier rendering transformation (if possible)
            //

            ps = formatModifier.unapply(line, from, renderedLogEventEnd);
        }
        else {

            ps = new ProcessedString(from, line.substring(from, renderedLogEventEnd), renderedLogEventEnd);
        }

         return parseLiteralAfterFormatModifierWasUnapplied(ps);
    }

    // ConversionSpecifier implementation ------------------------------------------------------------------------------

    @Override
    public FormatModifier getFormatModifier() {

        return formatModifier;
    }

    @Override
    public AddResult add(char c) throws Log4jPatternLayoutException {

        if (closed) {

            throw new Log4jPatternLayoutException(
                    "attempt to add more characters to a closed conversion pattern component");
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
    protected abstract RenderedLogEvent parseLiteralAfterFormatModifierWasUnapplied(ProcessedString ps)
            throws Log4jPatternLayoutException;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
