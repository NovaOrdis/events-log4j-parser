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
 * The string value - as it shows up in the log - of a converted individual log4j log event element.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/5/17
 */
public class RenderedLogEvent {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // the index in the original string of the beginning of the substring the object value was extracted from.
    private int from;

    // the index in the original string of the end of the substring (the index of the next character) the object value
    // was extracted from.
    private int to;

    private Object object;

    // Constructors ----------------------------------------------------------------------------------------------------

    public RenderedLogEvent(Object o, int from, int to) {

        this.object = o;
        this.from = from;
        this.to = to;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the position in string at this
     */
    public int from() {

        return from;
    }

    public void setFrom(int i) {

        this.from = i;
    }

    /**
     * @return the position in string of the first character that immediately follows the element. It may go beyond the
     * end of the string.
     */
    public int to() {

        return to;
    }

    public void setTo(int i) {

        this.to = i;
    }

    /**
     * @return the parsed object, whose type depends on the ConversionSpecifier it corresponds to.
     */
    public Object get() {

        return object;
    }

    @Override
    public String toString() {

        return "(" + from + ", " + to + ") " + object;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
