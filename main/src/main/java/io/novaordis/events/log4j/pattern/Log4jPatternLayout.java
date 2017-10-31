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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public static final char PATTERN_ELEMENT_MARKER = '%';

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String patternLiteral;

    private List<Log4jPatternElement> elements;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Log4jPatternLayout(String literal) throws Log4jPatternLayoutException {

        if (literal == null) {

            throw new IllegalArgumentException("null log4j pattern layout literal");
        }

        this.patternLiteral = literal;

        this.elements = new ArrayList<>();

        parse();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the literal representation of the pattern layout, as provided to this instance by the layer that creates
     * it.
     */
    public String getLiteral() {

        return patternLiteral;
    }

    public Iterator<Log4jPatternElement> getPatternElementIterator() {

        throw new RuntimeException("NYE");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void parse() throws Log4jPatternLayoutException {

        int i = 0;

        Log4jPatternElement current = null;
        Log4jPatternElementFactory patternElementFactory = new Log4jPatternElementFactory();

        for(; i < patternLiteral.length(); i ++) {

            char c = patternLiteral.charAt(i);

            if (c == PATTERN_ELEMENT_MARKER) {

                if (current != null) {

                    //
                    // record the current one
                    //
                    elements.add(current);
                }

                if (i == patternLiteral.length() - 1) {

                    throw new Log4jPatternLayoutException("'" + Log4jPatternLayout.PATTERN_ELEMENT_MARKER +
                            "' not followed by any pattern element");
                }

                char patternElementChar = patternLiteral.charAt(++ i);
                current = patternElementFactory.getInstance(patternElementChar);
            }
            else if (current != null) {

                AddResult result = current.add(c);

                if (AddResult.LAST.equals(result)) {

                    elements.add(current);
                    current = null;
                }
                else {

                    throw new RuntimeException("NYE");
                }
            }
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
