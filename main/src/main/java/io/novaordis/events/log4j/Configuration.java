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

package io.novaordis.events.log4j;

import io.novaordis.events.processing.Procedure;
import io.novaordis.events.processing.ProcedureFactory;
import io.novaordis.events.query.NullQuery;
import io.novaordis.events.query.Query;
import io.novaordis.utilities.UserErrorException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The command line configuration.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
class Configuration {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private File file;
    private Query query;
    private Procedure procedure;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Configuration(String[] argsa) throws UserErrorException {

        if (argsa.length == 0) {

            throw new UserErrorException("log file name missing");
        }

        String commandOrFile = argsa[0];

        procedure = ProcedureFactory.find(commandOrFile);

        String fileName;

        if (procedure != null) {

            if (argsa.length == 1) {

                throw new UserErrorException("log file name missing");
            }

            fileName = argsa[1];
        }
        else {

            fileName = commandOrFile;
        }

        file = new File(fileName);

        if (!file.isFile() || !file.canRead()) {

            throw new UserErrorException("file " + file + " does not exist or cannot be read");
        }

        List<String> args = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(argsa, 1, argsa.length)));

        //
        // separate options (-... or --...=)
        //

        for(int i = 0; i < args.size(); i ++) {

            String crt = args.get(i);

            if (crt.startsWith("-")) {

                throw new RuntimeException("we don't know how to handle " + crt);
            }
            else if (query == null) {

                try {

                    query = Query.fromArguments(args, i);
                }
                catch (Exception e) {

                    throw new UserErrorException(e);
                }
            }
            else {

                throw new UserErrorException("unknown argument \"" + crt + "\"");
            }
        }

        if (query == null) {

            query = new NullQuery();
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public File getFile() {

        return file;
    }

    public Query getQuery() {

        return query;
    }

    /**
     * May return null if there is no procedure.
     */
    public Procedure getProcedure() {

        return procedure;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
