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
     * Extract and parse the string value corresponding to this pattern component, returning the parsed component value.
     * May not return a null value, if no string representation of this component is found at the given position in the
     * string, Log4jPatternLayoutException is thrown.
     *
     * @exception Log4jPatternLayoutException if no valid string representation of this component is found at the given
     * position in the string,
     *
     * @param next the next pattern component that follows this one in the log4j pattern layout, or null if this is the
     *             last pattern component in the pattern layout.
     */
    RenderedLogEventElement parse(String s, int from, ConversionPatternComponent next)
            throws Log4jPatternLayoutException;


}
