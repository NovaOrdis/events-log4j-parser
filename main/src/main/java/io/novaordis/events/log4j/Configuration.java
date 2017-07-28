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

    private List<File> files;
    private Query query;
    private Procedure procedure;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param argsa "log4jp [command] [command options] [query] <file1> [file2 ...]
     */
    public Configuration(String[] argsa) throws UserErrorException {

        files = new ArrayList<>();

        if (argsa.length == 0) {

            //
            // no arguments, display help
            //

            procedure = new Help();
            return;
        }

        List<String> args = new ArrayList<>(Arrays.asList(argsa));

        //
        // start from the back and identify the files
        //

        int i;

        for(i = args.size() - 1; i >= 0; i --) {

            String arg = args.get(i);

            File candidate = new File(arg);

            if (candidate.isFile()) {

                files.add(candidate);
            }

            else {

                break;
            }
        }

        //
        // revert the order of the files to match the command line order
        //

        if (files.size() > 1) {

            List<File> tmp = new ArrayList<>();

            for(int j = files.size() - 1; j >= 0; j--) {

                tmp.add(files.get(j));
            }

            files = tmp;
        }

        args = args.subList(0, i + 1);

        for(i = 0; i < args.size() - 1; i ++) {

            String arg = args.get(i);

            if (procedure == null) {

                procedure = ProcedureFactory.find(arg, i + 1, args);

                if (procedure != null) {

                    continue;
                }
            }

            if (query == null) {

                try {

                    query = Query.fromArguments(args, i);
                }
                catch (Exception e) {

                    throw new UserErrorException(e);
                }
            }
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public List<File> getFiles() {

        return files;
    }

    /**
     * May return null if there is no query.
     */
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
