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
 * @since 10/30/17
 */
public enum AddResult {

    // Constants -------------------------------------------------------------------------------------------------------

    /**
     * The character was accepted, and it is not the last character, more characters may be added.
     */
    ACCEPTED,

    /**
     * The character was accepted, but is the last character, and no more characters can be added. An attempt to invoke
     * add() again will throw Log4jPatternLayoutException.
     */
    LAST,

    /**
     * The character is not accepted. An attempt to invoke add() again - with the same character or any other
     * character - will throw Log4jPatternLayoutException.
     */
    NOT_ACCEPTED

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

}
