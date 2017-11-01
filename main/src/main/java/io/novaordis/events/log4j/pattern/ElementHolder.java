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
 * Utility class that allows the factory to return an extracted element in other way than through a result.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/31/17
 */
class ElementHolder {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Log4jPatternElement element;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * May return null.
     */
    public Log4jPatternElement get() {

        return element;
    }

    /**
     * May return null.
     */
    public Log4jPatternElement remove() {

        Log4jPatternElement result = element;
        element = null;
        return result;
    }


    public void set(Log4jPatternElement element) {

        this.element = element;
    }

    @Override
    public String toString() {

        return "o->" + element;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
