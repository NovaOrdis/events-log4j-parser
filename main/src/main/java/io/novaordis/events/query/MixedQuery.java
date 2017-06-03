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

package io.novaordis.events.query;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.api.event.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A query comprising structured (event property definitions) and unstructured (keywords, regular expressions) text.
 *
 * Example: "blah blah name='something'"
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/2/17
 */
public class MixedQuery implements Query {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<String> keywords;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * A mixed query with no query tokens, equivalent with NullQuery.
     */
    public MixedQuery() throws QueryException {

        this(Collections.emptyList());
    }

    /**
     * @param queryTokens pre-parsed atomic tokens (keywords, event property definition, that make up the query
     */
    public MixedQuery(List<String> queryTokens) throws QueryException {

        this.keywords = new ArrayList<>();

        //noinspection Convert2streamapi
        for(String s: queryTokens) {

            keywords.add(s);
        }
    }

    // Query implementation --------------------------------------------------------------------------------------------

    @Override
    public boolean selects(Event e) {

        if (e == null) {

            throw new IllegalArgumentException("null event");
        }

        if (keywords.isEmpty()) {

            return true;
        }

        for(Property p: e.getProperties()) {

            Object o = p.getValue();

            if (o instanceof String) {

                for(String k: keywords) {

                    if (((String) o).contains(k)) {

                        return true;
                    }
                }
            }
            else {

                throw new RuntimeException("we don't know how to match non-String properties");
            }
        }

        return false;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
