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

import io.novaordis.events.log4j.pattern.ConversionPatternComponent;
import io.novaordis.events.log4j.pattern.FormatModifier;

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
     * May never return.
     */
    char getConversionCharacter();

    /**
     * May return null if hte element has no format modifier.
     */
    FormatModifier getFormatModifier();

}
