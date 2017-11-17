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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.novaordis.events.log4j.impl.Log4jEvent;
import io.novaordis.events.log4j.impl.Log4jEventImpl;
import io.novaordis.events.log4j.pattern.ConversionPatternComponent;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.log4j.pattern.ProcessedString;
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

    private static final Pattern EXCEPTION_RENDERING_PATTERN = Pattern.compile(
            ".*(: )[a-zA-Z][a-zA-Z_0-9]*(\\.[a-zA-Z][a-zA-Z_0-9]*)*: .+");

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
    protected RenderedLogEvent parseLiteralAfterFormatModifierWasUnapplied(ProcessedString ps)
            throws Log4jPatternLayoutException {

        //
        // TODO: this should contain pattern matching to insure that the string fragment is indeed an exception
        // rendering; for the time being, because %E is usually at the end of the conversion specifier string, so
        // it is validated by findNext()
        //

        return new RenderedLogEvent(ps.getProcessedString(), ps.from(), ps.to());
    }

    /**
     * TODO: Not thread safe.
     */
    @Override
    public Integer findNext(String logContent, int from) {

        ConversionPatternComponent.checkConsistency(logContent, from);

        Matcher m = EXCEPTION_RENDERING_PATTERN.matcher(logContent.substring(from));

        if (!m.matches()) {

            return null;
        }

        return m.start(1);
    }

    @Override
    public void injectIntoLog4jEvent(Log4jEvent e, Object value) {

        if (value == null) {

            //
            // noop
            //

            return;
        }

        if (!(value instanceof String)) {

            throw new IllegalArgumentException(
                    "invalid value type " + value.getClass().getSimpleName() + ", expected String");
        }

        ((Log4jEventImpl)e).appendToException((String) value);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
