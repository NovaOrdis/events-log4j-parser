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

import io.novaordis.events.log4j.impl.Log4jEventImpl;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.log4j.pattern.ProcessedString;
import io.novaordis.events.log4j.pattern.RenderedLogEvent;
import io.novaordis.utilities.logging.log4j.Log4jLevel;

/**
 * The level of the logging event ('p').
 *
 * The type of the corresponding parsed object instance is io.novaordis.events.log4j.impl.Log4jLevel
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class Level extends ConversionSpecifierBase {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final char CONVERSION_CHARACTER = 'p';

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public Level() {
    }

    /**
     * @param pattern, without the pattern element marker ('%'). Example: "-5p"
     */
    protected Level(String pattern) throws Log4jPatternLayoutException {

        super(pattern);
    }

    // ConversionSpecifier implementation ------------------------------------------------------------------------------

    @Override
    public char getConversionCharacter() {

        return CONVERSION_CHARACTER;
    }

    @Override
    protected RenderedLogEvent parseLiteralAfterFormatModifierWasUnapplied(ProcessedString ps)
            throws Log4jPatternLayoutException {

        String s = ps.getProcessedString();

        Log4jLevel l = Log4jLevel.find(s, 0);

        if (l == null) {

            throw new Log4jPatternLayoutException("not a valid log4j level: " + s);
        }

        int to;
        int from = ps.from();

        if (s.length() == l.name().length()) {

            to = ps.to();
        }
        else {

            to = from + l.name().length();
        }

        return new RenderedLogEvent(l, from, to);
    }

    @Override
    protected void injectIntoEvent(Log4jEventImpl e, Object value) {

        //
        // null is handled by the upper layer
        //

        if (!(value instanceof Log4jLevel)) {

            throw new IllegalArgumentException(
                    "invalid value type " + value.getClass().getSimpleName() + ", expected Log4jLevel");
        }

        e.setLevel((Log4jLevel) value);
    }

    // ConversionSpecifierBase overrides -------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
