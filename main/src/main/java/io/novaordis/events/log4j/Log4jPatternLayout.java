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

package io.novaordis.events.log4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.pattern.PatternParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A parsed representation of a Log4j pattern layout.
 *
 * See http://logging.apache.org/log4j/2.x/manual/layouts.html#PatternLayout
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/28/17
 */
public class Log4jPatternLayout {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(Log4jPatternLayout.class);

    // Static ----------------------------------------------------------------------------------------------------------

    public static void experimental(String patternLiteral) throws Exception {

        log.info("hook");

        Map converterRegistry = new HashMap<>();

        List patternConverters = new ArrayList<>();
        List formattingInfos = new ArrayList<>();

        PatternParser.parse(patternLiteral, patternConverters, formattingInfos, null, null);

    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private String literal;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Log4jPatternLayout(String literal) throws Log4jPatternLayoutException {

        if (literal == null) {

            throw new IllegalArgumentException("null log4j pattern layout literal");
        }

        this.literal = literal;

        //
        // very crude parsing, replace it with something better
        //

        if (!literal.contains("%")) {

            throw new Log4jPatternLayoutException("invalid log4j pattern layout specification: " + literal);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the literal representation of the pattern layout, as provided to this instance by the layer that creates
     * it.
     */
    public String getLiteral() {

        return literal;

    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
