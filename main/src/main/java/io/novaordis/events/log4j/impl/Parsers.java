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

import io.novaordis.events.api.parser.ParsingException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class Parsers {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Finds a token enclosed by the specified markers, accounting for nested marker pairs.
     *
     * @return may return null if a category finding heuristics does not identify any.
     *
     * @throws ParsingException on improperly formatted category.
     */
    public static ParsingResult find(String s, int from, char leftMarker, char rightMarker, Long lineNumber)
            throws ParsingException {

        int i = from;

        int start = -1;

        for(; i < s.length(); i ++) {

            if (s.charAt(i) == leftMarker) {

                start = i;
                break;
            }
        }

        if (start == -1) {

            return null;
        }

        i ++;

        //
        // skip nested marker pairs
        //

        int level = 0;

        for(; i < s.length(); i ++) {

            char c = s.charAt(i);

            if (c == rightMarker) {

                if (level == 0) {

                    //
                    // found our category
                    //

                    return new ParsingResult(s.substring(start + 1, i), i + 1);
                }

                level --;
            }
            else if (c == leftMarker) {

                level ++;
            }
        }

        throw new ParsingException(lineNumber, start, "unbalanced " + leftMarker + "..." + rightMarker);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private Parsers() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
