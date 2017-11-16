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

package io.novaordis.events.log4j.pattern.convspec.wildfly;

import io.novaordis.events.log4j.pattern.ConversionPatternComponent;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.log4j.pattern.RenderedLogEvent;
import io.novaordis.events.log4j.pattern.convspec.ConversionSpecifierBase;

/**
 * A WildFly log4j pattern layout extension that renders the log event exception.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class WildFlyException extends ConversionSpecifierBase {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final char CONVERSION_CHARACTER = 'E';

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public WildFlyException() {
    }

    /**
     * @param pattern, without the pattern element marker ('%'). Example: "E"
     */
    protected WildFlyException(String pattern) throws Log4jPatternLayoutException {

        super(pattern);
    }

    // ConversionSpecifier implementation ------------------------------------------------------------------------------

    @Override
    public Character getConversionCharacter() {

        return CONVERSION_CHARACTER;
    }

    @Override
    protected RenderedLogEvent parseLiteralAfterFormatModifierHandling(
            String logContent, int from, ConversionPatternComponent next) throws Log4jPatternLayoutException {

        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
