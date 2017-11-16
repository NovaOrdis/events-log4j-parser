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
 * Utility class that allows the factory to return an extracted conversion specifier instance in other way than through
 * a result.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/31/17
 */
class ConversionPatternComponentHolder {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private ConversionPatternComponent c;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * May return null.
     */
    public ConversionPatternComponent getInstance() {

        return c;
    }

    /**
     * May return null.
     */
    public ConversionPatternComponent removeInstance() {

        ConversionPatternComponent result = c;

        c = null;

        return result;
    }

    public void setInstance(ConversionPatternComponent element) {

        this.c = element;
    }

    @Override
    public String toString() {

        return "o->" + c;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
