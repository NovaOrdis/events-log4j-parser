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
public class Log4jPatternElementFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    public Log4jPatternElement getInstance(char c) throws Log4jPatternLayoutException {

        if (DatePatternElement.VALUE == c) {

            return new DatePatternElement();
        }
        else if (LevelPatternElement.VALUE == c) {

            return new LevelPatternElement();
        }
        else if (LineSeparatorPatternElement.VALUE == c) {

            return new LineSeparatorPatternElement();
        }
        else if (LoggerPatternElement.VALUE == c) {

            return new LoggerPatternElement();
        }
        else if (ThreadNamePatternElement.VALUE == c) {

            return new ThreadNamePatternElement();
        }
        else {

            return new UnknownPatternElement();
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
