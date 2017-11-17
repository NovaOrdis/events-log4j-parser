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

import io.novaordis.events.log4j.impl.Log4jEvent;
import io.novaordis.events.log4j.impl.Log4jEventImpl;
import io.novaordis.events.log4j.pattern.convspec.ConversionSpecifier;
import io.novaordis.events.log4j.pattern.convspec.ConversionSpecifierFinder;

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

    public static final char CONVERSION_SPECIFIER_MARKER = '%';

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String patternLiteral;

    private List<ConversionPatternComponent> components;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Log4jPatternLayout(String literal) throws Log4jPatternLayoutException {

        if (literal == null) {

            throw new IllegalArgumentException("null log4j pattern layout literal");
        }

        this.patternLiteral = literal;

        this.components = new ArrayList<>();

        parsePatternLayout();

        validatePatternLayout();
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
    public int getPatternComponentCount() {

        return components.size();
    }

    public Iterator<ConversionPatternComponent> getPatternComponentIterator() {

        return components.iterator();
    }

    /**
     * @throws IllegalStateException if there are no conversion pattern component.
     */
    public Log4jEvent parse(long lineNumber, String line) throws Log4jPatternLayoutException {

        Iterator<ConversionPatternComponent> i = getPatternComponentIterator();

        if (!i.hasNext()) {

            throw new IllegalStateException("no conversion pattern components");
        }

        int from = 0;
        ConversionPatternComponent crt = i.next();
        ConversionPatternComponent next = null;

        if (i.hasNext()) {

            next = i.next();
        }

        Log4jEventImpl log4jEvent = new Log4jEventImpl();
        log4jEvent.setLineNumber(lineNumber);

        do {

            //
            // process the current conversion pattern
            //

            RenderedLogEvent e = crt.parseLogContent(line, from, next);

            crt.injectIntoLog4jEvent(log4jEvent, e.get());

            crt = next;
            from = e.to();

            if (i.hasNext()) {

                next = i.next();
            }
            else {

                next = null;
            }
        }
        while (crt != null && from < line.length());

        //
        // parsing successful, load the raw representation
        //

        log4jEvent.append(line);

        return log4jEvent;
    }

    @Override
    public String toString() {

        String s = "";

        for(Iterator<ConversionPatternComponent> i = getPatternComponentIterator(); i.hasNext(); ) {

            s += i.next().getLiteral();
        }

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void parsePatternLayout() throws Log4jPatternLayoutException {

        int i = 0;

        ConversionPatternComponentHolder current = new ConversionPatternComponentHolder();

        ConversionSpecifierFinder conversionSpecifierFinder = new ConversionSpecifierFinder();

        for(; i < patternLiteral.length(); i ++) {

            char c = patternLiteral.charAt(i);

            if (c == CONVERSION_SPECIFIER_MARKER) {

                if (current.getInstance() != null) {

                    //
                    // record the current element
                    //
                    components.add(current.removeInstance());
                }

                if (i == patternLiteral.length() - 1) {

                    throw new Log4jPatternLayoutException("'" + Log4jPatternLayout.CONVERSION_SPECIFIER_MARKER +
                            "' not followed by any pattern element");
                }

                i = conversionSpecifierFinder.lookup(patternLiteral, i + 1, current);
            }
            else if (current.getInstance() != null) {

                ConversionPatternComponent e = current.getInstance();

                AddResult result = e.add(c);

                if (AddResult.NOT_ACCEPTED.equals(result) || AddResult.LAST.equals(result)) {

                    components.add(e);
                    current.removeInstance();
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

                LiteralText txt = new LiteralText();

                current.setInstance(txt);

                AddResult result = txt.add(c);

                if (AddResult.NOT_ACCEPTED.equals(result)) {

                    throw new IllegalStateException("'" + c + "' not accepted by literal " + txt);
                }
            }
        }

        ConversionPatternComponent last = current.getInstance();

        if (last != null) {

            components.add(last);
        }
    }

    private void validatePatternLayout() throws Log4jPatternLayoutException {

        //
        // we need to have at least one conversion specifier
        //

        for(ConversionPatternComponent c: components) {

            if (c instanceof ConversionSpecifier) {

                return;
            }
        }

        throw new Log4jPatternLayoutException(
                "the log4j pattern layout must contain at least on conversion specifier (%...): " + getLiteral());

    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
