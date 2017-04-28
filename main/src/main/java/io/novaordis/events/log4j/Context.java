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

import java.lang.Exception;import java.lang.IllegalArgumentException;import java.lang.String;import java.lang.System;import static io.novaordis.playground.LogProcessing.out;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public class Context {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Error last;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     *
     * @exception IllegalArgumentException if the event being set occurs before the current event
     */
    public void process(Error e) {

        try {

            analyze(e, last);
        }
        catch(Exception ex) {

            System.err.println(ex.getMessage());
        }
        finally {

            this.last = e;
        }
    }

    public void dump() {

        System.out.println("");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    /**
     * @param previous may be null if we are analyzing the first event
     */
    private void analyze(Error current, Error previous) throws Exception {

        if (previous == null) {

            return;
        }

        String comment = previous.getComment();

        if (!comment.contains("ConcurrentAccessTimeoutException")) {

            return;
        }

        if (!current.getThreadName().equals(previous.getThreadName())) {

            return;
        }

        long offset = current.getTimestamp() - previous.getTimestamp();

        LogProcessing.out(current.getComment());
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
