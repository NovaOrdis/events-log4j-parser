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

import java.util.Arrays;

/**
 * A log4j pattern layout format modifier representation. It can be used to to change the minimum field width, the
 * maximum field width and justification.
 *
 * Example: the "-5" from "%-5p".
 *
 * See https://kb.novaordis.com/index.php/Log4j_Pattern_Layout#Format_Modifier
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/15/17
 */
public class FormatModifier {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final boolean MINIMUM = false;
    private static final boolean MAXIMUM = true;

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String literal;


    //
    // by default, the content is right justified if its length is smaller than "minimum field width".
    //
    private boolean rightJustified;

    private Integer minimumFieldWidth;

    //
    // by default, if maximum field width is specified, and it is smaller than the length of the actual field, the
    // field is truncated from front
    //
    private boolean truncateFromFront;

    private Integer maximumFieldWidth;

    // Constructors ----------------------------------------------------------------------------------------------------

    public FormatModifier(String literal) throws Log4jPatternLayoutException {

        if (literal == null) {

            throw new IllegalArgumentException("null literal");
        }

        this.literal = literal;

        this.rightJustified = true;
        this.truncateFromFront = true;

        parse(literal);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return may return null if the minimum field width is not specified.
     */
    public Integer getMinimumFieldWidth() {

        return minimumFieldWidth;
    }

    /**
     * @return true whether to right justify (pad to left) field values that are shorter than the minimum field width.
     * This is the default behavior. If the method returns false, the field values that are shorter than the minimum
     * field with should be left justified (padded to the right).
     */
    public boolean isRightJustified() {

        return rightJustified;
    }

    /**
     * @return may return null if the maximum field width is not specified.
     */
    public Integer getMaximumFieldWidth() {

        return maximumFieldWidth;
    }

    /**
     * @return whether to truncate from front, if maximum field width is specified, and it is smaller than the length of
     * the actual field. This is the default behavior.
     */
    public boolean isTruncateFromFront() {

        return truncateFromFront;
    }

    /**
     * @return the format modifier literal. Will never return null.
     */
    public String getLiteral() {

        return literal;
    }

    /**
     * Apply the format modifier to the  string representation of the given object. Null will throw an
     * IllegalArgumentException.
     */
    public String apply(Object o) {

        if (o == null) {

            throw new IllegalArgumentException("null argument");
        }

        String s = o.toString();

        if (minimumFieldWidth != null && s.length() < minimumFieldWidth) {

            char[] content = new char[minimumFieldWidth - s.length()];
            Arrays.fill(content, ' ');

            if (rightJustified) {


                s = new String(content) + s;

            }
            else {

                s = s + new String(content);
            }
        }

        if (maximumFieldWidth != null && s.length() > maximumFieldWidth) {

            if (truncateFromFront) {

                s = s.substring(s.length() - maximumFieldWidth);

            }
            else {

                s = s.substring(0, maximumFieldWidth);
            }
        }


        return s;
    }

    /**
     * The inverse transformation to apply(). Of course, is not always possible to generate the original string.
     */
    public ProcessedString unapply(String s, int from) {

        ProcessedString result = new ProcessedString(from);

        result.setTo(s.length());

        String arg = s.substring(from);

        if (minimumFieldWidth != null) {

            if (arg.length() < minimumFieldWidth) {

                //
                // this is an illegal state
                //

                throw new IllegalArgumentException("string argument shorter than minimum field width");
            }

            if (rightJustified) {

                //
                // drop spaces from the left of the string
                //

                int i = 0;

                while(i < arg.length()) {

                    if (arg.charAt(i) != ' ') {

                        break;
                    }

                    i ++;
                }

                arg = arg.substring(i);
            }
            else {

                //
                // drop spaces from the right of the string
                //

                int i = arg.length() - 1;

                while(i >= 0) {

                    if (arg.charAt(i) != ' ') {

                        break;
                    }

                    i --;
                }

                arg = arg.substring(0, i + 1);
            }
        }

        //
        // we cannot re-adjust dropped characters but we can return only the characters that were in the original
        // string
        //

        if (maximumFieldWidth != null) {

            if (arg.length() > maximumFieldWidth) {

                arg = arg.substring(0, maximumFieldWidth);

                result.setTo(from + maximumFieldWidth);
            }
        }

        result.setProcessedString(arg);

        return result;
    }

    @Override
    public String toString() {

        if (literal == null) {

            return "UNINITIALIZED";
        }

        return literal;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void parse(String literal) throws Log4jPatternLayoutException {

        int i = literal.indexOf('.');

        if (i == -1) {

            //
            // no maximum field width
            //

            parseFieldWidth(literal, MINIMUM);
        }
        else {

            //
            // we do have maximum field width
            //

            parseFieldWidth(literal.substring(0, i), MINIMUM);
            parseFieldWidth(literal.substring(i + 1), MAXIMUM);
        }

    }

    /**
     * @param isMaximumFieldWidth: MINIMUM/MAXIMUM
     */
    private void parseFieldWidth(String literal, boolean isMaximumFieldWidth) throws Log4jPatternLayoutException {

        if (literal.startsWith("-")) {

            literal = literal.substring(1);

            if (!isMaximumFieldWidth) {

                rightJustified = false;
            }
            else {

                truncateFromFront = false;
            }
        }

        if (literal.trim().isEmpty()) {

            //
            // empty string is acceptable and diverted around
            //

            return;
        }

        if (!isMaximumFieldWidth) {

            //
            // empty string is acceptable and diverted around
            //

            try {

                minimumFieldWidth = Integer.parseInt(literal);

            } catch (NumberFormatException e) {

                throw new Log4jPatternLayoutException("invalid minimum field width specification: " + literal, e);
            }
        }
        else {

            try {

                maximumFieldWidth = Integer.parseInt(literal);

            } catch (NumberFormatException e) {

                throw new Log4jPatternLayoutException("invalid maximum field width specification: " + literal, e);
            }
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
