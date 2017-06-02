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

package io.novaordis.events.log4j.impl;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
class ParsingResult {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String value;
    private int next;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param result cannot be null
     */
    public ParsingResult(String result, int next) {

        if (result == null) {

            throw new IllegalArgumentException("null result");
        }

        this.value = result;
        this.next = next;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * The index of the next character in line where the caller should continue processing.
     * @return
     */
    public int getNext() {

        return next;
    }

    public String getValue() {

        return value;
    }

    @Override
    public String toString() {

        return "" + value + ", " + next;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
