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

import io.novaordis.events.log4j.experimental.MessageProcessing;
import io.novaordis.events.processing.Procedure;
import io.novaordis.events.processing.ProcedureFactory;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/9/17
 */
public class Log4jParserProcedureFactory implements ProcedureFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // ProcedureFactory implementation ---------------------------------------------------------------------------------

    @Override
    public Procedure find(String commandLineLabel, int from, List<String> arguments) {

        if ("message-processing".equals(commandLineLabel)) {

            return new MessageProcessing();
        }

        return null;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
