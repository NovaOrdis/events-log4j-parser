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
 * @since 11/17/17
 */
public abstract class ConversionPatternComponentBase implements ConversionPatternComponent {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // ConversionPatternComponent implementation -----------------------------------------------------------------------

    @Override
    public final Integer find(String s, int from) {

        if (s == null) {

            throw new IllegalArgumentException("null log content");
        }

        if (from < 0) {

            throw new IllegalArgumentException("invalid 'from' index: " + from);
        }

        if (from > s.length()) {

            throw new IllegalArgumentException("invalid 'from' index: " + from);
        }

        if (s.length() == from) {

            return null;
        }

        Integer i = find(s.substring(from));

        if (i == null) {

            return null;
        }

        return from + i;
    }


    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    /**
     * Scan the given string, which represents log content, in an attempt to find the first occurrence of
     * <b>this</b> conversion pattern component.
     *
     * @return the index of the first occurrence of this conversion pattern component, within the string, or null if
     * there is no such component or if is not possible to identify an occurrence of the component. The s.length() value
     * is interpreted as "the end of the string" and it is a valid return value.
     */
    protected Integer find(String s) {

        throw new RuntimeException("find() NOT YET IMPLEMENTED FOR " + this);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
