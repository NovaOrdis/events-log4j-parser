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
 * The representation of an individual element (component) in a conversion pattern string.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/15/17
 */
public interface ConversionPatternComponent {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the conversion specifier literal - the conversion specifier marker ('%') and the conversion character,
     * possibly including extra format specification, such as the format modifier, etc. May return null if the element
     * is not initialized yet.
     */
    String getLiteral();

    /**
     * Attempt to add to the pattern element the next character read from the pattern element literal stream. The
     * character may be accepted or not, and the decision is reflected in the return value of the add() method:
     *
     * @return AddResult.ACCEPTED if the character was accepted, and it is not the last character, more characters may
     * be added, AddResult.LAST if the character was accepted, but is the last character, and no more characters can be
     * added (an attempt to invoke add() again will throw Log4jPatternLayoutException) and AddResult.NOT_ACCEPTED if
     * the character was not accepted.
     *
     * @exception Log4jPatternLayoutException if the character cannot be possibly part of a valid pattern element
     * specification, or the internal state of this ConversionSpecifier instance does not allow adding.
     */
    AddResult add(char c) throws Log4jPatternLayoutException;

    /**
     * Identify the boundaries, then extract and parse the log content string value corresponding to this conversion
     * pattern component. The method returns a composite structure, including, among others, the parsed component value.
     * Must not return a null value. If no string representation of this component is found at the given position in the
     * string, Log4jPatternLayoutException is thrown.
     *
     * @exception Log4jPatternLayoutException if no valid string representation of this component is found at the given
     * position in the string,
     *
     * @param next the next pattern component that follows this one in the log4j pattern layout, or null if this is the
     *             last pattern component in the pattern layout.
     */
    RenderedLogEvent parseLogContent(String logContent, int from, ConversionPatternComponent next)
            throws Log4jPatternLayoutException;

    /**
     * Scan the log content, starting with the 'from' index character in an attempt to find the first occurrence of
     * <b>this</b> conversion pattern component.
     *
     * @return the index of the first occurrence of this conversion pattern component, within the string, starting with
     * the 'from' index character, or null if there is no such component or if is not possible to identify an occurrence
     * of the component. The s.length() value is interpreted as "the end of the string" and it is a valid return value.
     */
    Integer find(String s, int from);

    /**
     * Interprets the given value as the value of the corresponding log event property, and injects the property into
     * the log event.
     *
     * @param value null is a noop
     *
     * @exception IllegalArgumentException if the conversion of the given value to the corresponding log event property
     * is not possible.
     */
    void injectIntoEvent(Log4jEventImpl e, Object value);

}
