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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public interface Log4jPatternElement {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * One char identifier:
     *
     * 'd' - date
     * 'p' - level
     * 'n' - line separator
     * 'c' - logger
     * 't' - thread name
     *
     * May return null.
     */
    Character getIdentifier();

    /**
     * @return the pattern element literal - the pattern element marker ('%') and the defining character, possibly
     * followed by any extra format specification. For LiteralPatternElement, it is the sequence of characters that
     * will be copied in the log. May return null if the element is not initialized yet.
     */
    String getLiteral();

    /**
     * May return null if hte element has no format modifier.
     */
    FormatModifier getFormatModifier();

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
     * specification, or the internal state of this Log4jPatternElement instance does not allow adding.
     */
    AddResult add(char c) throws Log4jPatternLayoutException;

    /**
     * Extract and parse the string value corresponding to this pattern element, returning the parsed element value. May
     * not return a null value, if no string representation of this element is found at the given position in the
     * string, Log4jPatternLayoutException is thrown.
     *
     * @exception Log4jPatternLayoutException if no valid string representation of this element is found at the given
     * position in the string,
     *
     * @param next the next pattern element that follows this one in the log4j pattern layout, or null if this is the
     *             last pattern element in the pattern layout.
     */
    ParsedElement parse(String s, int from, Log4jPatternElement next) throws Log4jPatternLayoutException;

}
