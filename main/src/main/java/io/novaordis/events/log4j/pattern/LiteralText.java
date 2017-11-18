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

import io.novaordis.events.log4j.impl.Log4jEventImpl;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class LiteralText extends ConversionPatternComponentBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String literal;

    private boolean closed;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Instances of this class can only be created by the Log4jPatternElementFinder or within the package.
     */
    protected LiteralText() {

        this.closed = false;
    }

    public LiteralText(String literal) {

        this();

        for(char c: literal.toCharArray()) {

            try {

                add(c);
            }
            catch(Log4jPatternLayoutException e) {

                throw new IllegalStateException(e);
            }
        }
    }

    // ConversionPatternComponent implementation -----------------------------------------------------------------------

    @Override
    public String getLiteral() {

        return literal;
    }

    @Override
    public AddResult add(char c) throws Log4jPatternLayoutException {

        if (closed) {

            throw new Log4jPatternLayoutException(
                    "attempt to add more characters to a closed conversion pattern component");
        }

        if (literal == null) {

            literal = new String(new char[] {c});
        }
        else {

            if (Log4jPatternLayout.CONVERSION_SPECIFIER_MARKER == c) {

                closed = true;

                return AddResult.NOT_ACCEPTED;
            }

            literal += c;
        }

        return AddResult.ACCEPTED;
    }

    @Override
    public RenderedLogEvent parseLogContent(String s, int from, ConversionPatternComponent next)
            throws Log4jPatternLayoutException {

        int length = literal.length();

        int to = from + length;

        String s2 = s.substring(from, to);

        if (!literal.equals(s2)) {

            throw new Log4jPatternLayoutException(
                    "pattern element's literal \"" + literal +
                            "\" does not match the parsed string literal \"" + s2 + "\"");
        }

        return new RenderedLogEvent(literal, from, to);
    }

    @Override
    public Integer find(String s) {

        int i = s.indexOf(getLiteral());

        if (i == -1) {

            return null;
        }

        return i;
    }

    @Override
    public void injectIntoEvent(Log4jEventImpl e, Object value) {

        //
        // noop, we are not injecting anything
        //
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        String literal = getLiteral();

        if (literal == null) {

            return "null";
        }

        return "'" + literal + "'";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
