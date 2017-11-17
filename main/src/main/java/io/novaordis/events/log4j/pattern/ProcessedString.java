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
 * @since 11/15/17
 */
public class ProcessedString {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // original "from"
    private int from;

    // original "to"
    private int to;

    //
    // post-processing value
    //
    private String processed;

    // Constructors ----------------------------------------------------------------------------------------------------

    public ProcessedString(int from) {

        this.from = from;
    }

    public ProcessedString(int from, String processedString) {

        this.from = from;
        this.processed = processedString;
        this.to = from + processed.length();
    }

    public ProcessedString(int from, String processedString, int to) {

        this.from = from;
        this.processed = processedString;
        this.to = to;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * The original "from", before processing.
     */
    public int from() {

        return from;
    }

    /**
     * The original "to", before processing.
     */
    public int to() {

        return to;
    }

    public void setTo(int i) {

        this.to = i;
    }

    public String getProcessedString() {

        return processed;
    }

    public void setProcessedString(String s) {

        this.processed = s;
    }

    @Override
    public String toString() {

        return "" + from + ":" + to + " " + processed;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
