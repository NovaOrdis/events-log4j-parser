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
 * @since 10/31/17
 */
public abstract class Log4jPatternElementBase implements Log4jPatternElement {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String formatModifierLiteral;

    private boolean closed;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Instances of this class can only be created by the Log4jPatternElementFinder or within the package.
     */
    protected Log4jPatternElementBase() {

        this.closed = false;
    }

    // Log4jPatternElement implementation ------------------------------------------------------------------------------

    @Override
    public String getFormatModifierLiteral() {

        return formatModifierLiteral;
    }

    @Override
    public String getLiteral() {

        String m = getFormatModifierLiteral();

        return "" + Log4jPatternLayout.PATTERN_ELEMENT_MARKER + (m == null ? "" : m) + getIdentifier();
    }

    @Override
    public AddResult add(char c) throws Log4jPatternLayoutException {

        if (closed) {

            throw new Log4jPatternLayoutException("attempt to add more characters to a closed element");
        }

        closed = true;

        return AddResult.NOT_ACCEPTED;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return getLiteral();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    void setFormatModifierLiteral(String s) throws Log4jPatternLayoutException {

        this.formatModifierLiteral = s;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
