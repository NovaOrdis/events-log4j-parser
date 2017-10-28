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

package io.novaordis.events.log4j.cli;

import java.util.List;

import io.novaordis.events.cli.Configuration;
import io.novaordis.events.cli.TopLevelArgumentProcessor;
import io.novaordis.utilities.UserErrorException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/28/17
 */
public class Log4jTopLevelArgumentProcessor implements TopLevelArgumentProcessor {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String FORMAT_SHORT_OPTION = "-f";
    public static final String FORMAT_LONG_OPTION = "--format=";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // TopLevelArgumentProcessor implementation ------------------------------------------------------------------------

    @Override
    public void process(List<String> mutableArgumentList, Configuration c) throws UserErrorException {

        String log4jPatternLayoutLiteral = null;

        for(int i = 0; i < mutableArgumentList.size(); i ++) {

            String arg = mutableArgumentList.get(i);

            if (FORMAT_SHORT_OPTION.equals(arg)) {

                //
                // we expect the format specification as the next argument
                //

                if (i == mutableArgumentList.size() - 1) {

                    throw new UserErrorException(
                            "log4j pattern layout specification implied by " + FORMAT_SHORT_OPTION + " is missing");
                }

                mutableArgumentList.remove(i);

                log4jPatternLayoutLiteral = mutableArgumentList.get(i);

                mutableArgumentList.remove(i);

                break;
            }
            else if (arg.startsWith(FORMAT_LONG_OPTION)) {

                log4jPatternLayoutLiteral = arg.substring(FORMAT_LONG_OPTION.length());

                if (log4jPatternLayoutLiteral.isEmpty()) {

                    throw new UserErrorException(
                            "log4j pattern layout specification that should follow " + FORMAT_LONG_OPTION + " is missing");

                }

                mutableArgumentList.remove(i);

                break;
            }
        }

        if (log4jPatternLayoutLiteral != null) {

            processLog4jPatternLayoutLiteral(log4jPatternLayoutLiteral, c);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    void processLog4jPatternLayoutLiteral(String log4jPatternLayoutLiteral, Configuration c) throws UserErrorException {

        if (log4jPatternLayoutLiteral == null) {

            throw new IllegalArgumentException("null log4j pattern layout literal");
        }

        if (c == null) {

            throw new IllegalArgumentException("null configuration");
        }

        Log4jConfiguration lc = (Log4jConfiguration)c.getApplicationSpecificConfiguration();

        if (lc == null) {

            lc = new Log4jConfiguration();
            c.setApplicationSpecificConfiguration(lc);
        }

        lc.setPatternLayoutString(log4jPatternLayoutLiteral);
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
