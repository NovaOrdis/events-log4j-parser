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

package io.novaordis.events.log4j.pattern.convspec;

import io.novaordis.events.log4j.pattern.AddResult;
import io.novaordis.events.log4j.pattern.ConversionPatternComponent;
import io.novaordis.events.log4j.pattern.FormatModifier;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;

/**
 * A conversion specifier: https://kb.novaordis.com/index.php/Log4j_Pattern_Layout#Conversion_Specifier.
 *
 * It is part of a Log4j conversion pattern: https://kb.novaordis.com/index.php/Log4j_Pattern_Layout#Conversion_Pattern
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public interface ConversionSpecifier extends ConversionPatternComponent {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * A conversion specifier's conversion character.
     *
     * https://kb.novaordis.com/index.php/Log4j_Pattern_Layout#Conversion_Character
     *
     * May return null.
     */
    Character getConversionCharacter();

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
     * specification, or the internal state of this ConversionSpecifier instance does not allow adding.
     */
    AddResult add(char c) throws Log4jPatternLayoutException;


}
