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

        parsePatternLayout();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the literal representation of the pattern layout, as provided to this instance by the layer that creates
     * it.
     */
    public String getLiteral() {

        return patternLiteral;
    }

    /**
     * @return the number of distinct pattern elements. Literal pattern elements count as distinct pattern elements.
     */
    public int getPatternElementCount() {

        return elements.size();
    }

    public Iterator<Log4jPatternElement> getPatternElementIterator() {

        return elements.iterator();
    }

    @Override
    public String toString() {

        return "Log4jPatternLayout[" + getLiteral() + "]";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void parsePatternLayout() throws Log4jPatternLayoutException {

        int i = 0;

        ElementHolder current = new ElementHolder();
        Log4jPatternElementFinder patternElementFactory = new Log4jPatternElementFinder();

        for(; i < patternLiteral.length(); i ++) {

            char c = patternLiteral.charAt(i);

            if (c == PATTERN_ELEMENT_MARKER) {

                if (current.get() != null) {

                    //
                    // record the current element
                    //
                    elements.add(current.remove());
                }

                if (i == patternLiteral.length() - 1) {

                    throw new Log4jPatternLayoutException("'" + Log4jPatternLayout.PATTERN_ELEMENT_MARKER +
                            "' not followed by any pattern element");
                }

                i = patternElementFactory.lookup(patternLiteral, i + 1, current);
            }
            else if (current.get() != null) {

                Log4jPatternElement e = current.get();

                AddResult result = e.add(c);

                if (AddResult.NOT_ACCEPTED.equals(result) || AddResult.LAST.equals(result)) {

                    elements.add(e);
                    current.remove();
                }

                if (AddResult.NOT_ACCEPTED.equals(result)) {

                    i --;
                }

                //
                // else continue adding to the current element ...
                //
            }
            else {

                //
                // the current element is null and the current character is not an element marker, start a literal
                //

                LiteralPatternElement e = new LiteralPatternElement();
                
                current.set(e);

                AddResult result = e.add(c);

                if (AddResult.NOT_ACCEPTED.equals(result)) {

                    throw new IllegalStateException("'" + c + "' not accepted by literal " + e);
                }
            }
        }

        Log4jPatternElement last = current.get();

        if (last != null) {

            elements.add(last);
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
