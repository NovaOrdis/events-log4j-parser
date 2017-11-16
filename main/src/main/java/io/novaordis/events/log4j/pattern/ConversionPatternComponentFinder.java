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

import io.novaordis.events.log4j.pattern.convspec.ConversionSpecifierBase;
import io.novaordis.events.log4j.pattern.convspec.Date;
import io.novaordis.events.log4j.pattern.convspec.Level;
import io.novaordis.events.log4j.pattern.convspec.LineSeparator;
import io.novaordis.events.log4j.pattern.convspec.Logger;
import io.novaordis.events.log4j.pattern.convspec.ThreadName;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
class ConversionPatternComponentFinder {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Scans the entry pattern literal from the given position and finds the beginning (and the modifier) of the next
     * conversion specifier. Returns the position of the last character in element, to cooperate with a upper layer
     * 'for' loop, which will increment the index.
     *
     * @param patternLiteral the entire pattern layout.
     *
     * @param from the position to start looking from.
     *
     * @param holder the holder to deposit the next identified conversion specifier instance into.
     *
     * @return the position in the pattern layout the last character was read from.
     *
     * @throws Log4jPatternLayoutException
     */
    int lookup(String patternLiteral, int from, ConversionPatternComponentHolder holder)
            throws Log4jPatternLayoutException {

        if (from >= patternLiteral.length()) {

            throw new IllegalArgumentException("'from' beyond the edge");
        }

        int i = from;
        String formatModifierLiteral = null;
        ConversionSpecifierBase component = null;

        while(component == null) {

            char c = patternLiteral.charAt(i);

            if (Date.IDENTIFIER == c) {

                component = new Date();
            }
            else if (Level.IDENTIFIER == c) {

                component = new Level();
            }
            else if (LineSeparator.IDENTIFIER == c) {

                component = new LineSeparator();
            }
            else if (Logger.IDENTIFIER == c) {

                component = new Logger();
            }
            else if (ThreadName.IDENTIFIER == c) {

                component = new ThreadName();
            }
            else if ('0' <= c && c <= '9' || c == '.' || c == '-') {

                //
                // format modifier
                //

                if (formatModifierLiteral == null) {

                    formatModifierLiteral = "" + c;
                }
                else {

                    formatModifierLiteral += c;
                }

                i ++;
            }
            else {

                throw new Log4jPatternLayoutException("unknown conversion character: " + c);
            }
        }

        if (formatModifierLiteral != null) {

            FormatModifier m = new FormatModifier(formatModifierLiteral);
            throw new RuntimeException("RETURN HERE");
            //component.setFormatModifier(m);
        }

        holder.setInstance(component);

        return i;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
