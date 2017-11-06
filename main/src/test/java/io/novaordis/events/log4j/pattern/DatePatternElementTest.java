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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class DatePatternElementTest extends Log4jPatternElementTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void addAfterLast() throws Exception {

        DatePatternElement d = new DatePatternElement();

        assertEquals(AddResult.ACCEPTED, d.add('{'));
        assertEquals(AddResult.ACCEPTED, d.add('H'));
        assertEquals(AddResult.ACCEPTED, d.add('H'));
        assertEquals(AddResult.LAST, d.add('}'));

        try {

            d.add(' ');

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("attempt to add more characters to a closed date pattern element"));
        }
    }

    @Test
    @Override
    public void addAfterNotAccepted() throws Exception {

        DatePatternElement d = new DatePatternElement();
        assertEquals(AddResult.NOT_ACCEPTED, d.add(' '));

        try {

            d.add(' ');

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("attempt to add more characters to a closed date pattern element"));
        }
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_InvalidIdentifier() throws Exception {

        try {

            new DatePatternElement("e{HH}");

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid date pattern element identifier: 'e'"));
        }
    }

    // add() -----------------------------------------------------------------------------------------------------------

    @Test
    public void add_SimpleDateFormat() throws Exception {

        DatePatternElement d = new DatePatternElement();

        String literal = "{HH:mm:ss,SSS}";

        int i = 0;

        for(; i < literal.length() - 1; i ++) {

            char c = literal.charAt(i);
            assertEquals(AddResult.ACCEPTED, d.add(c));
        }

        assertEquals(AddResult.LAST, d.add(literal.charAt(i)));

        assertEquals("%d{HH:mm:ss,SSS}", d.getLiteral());

        SimpleDateFormat df = d.getSimpleDateFormat();

        assertEquals("HH:mm:ss,SSS", df.toPattern());
    }

    @Test
    public void add_InvalidFormat() throws Exception {

        DatePatternElement d = new DatePatternElement();

        String literal = "{blah}";

        int i = 0;

        for(; i < literal.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(literal.charAt(i)));
        }

        try {

            d.add(literal.charAt(i));

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();

            assertTrue(msg.contains("invalid date format \"blah\""));
        }
    }

    @Test
    public void add_default() throws Exception {

        DatePatternElement d = new DatePatternElement();

        assertEquals(AddResult.NOT_ACCEPTED, d.add('a'));

        assertEquals("%d", d.getLiteral());

        assertEquals(DatePatternElement.DEFAULT_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_DEFAULT() throws Exception {

        String s = "{DEFAULT}";

        DatePatternElement d = new DatePatternElement();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(DatePatternElement.DEFAULT_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_ISO8601() throws Exception {

        String s = "{ISO8601}";

        DatePatternElement d = new DatePatternElement();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(DatePatternElement.ISO8601_PATTERN, d.getSimpleDateFormat().toPattern());
    }
    @Test
    public void add_predefined_ISO8601_BASIC() throws Exception {

        String s = "{ISO8601_BASIC}";

        DatePatternElement d = new DatePatternElement();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(DatePatternElement.ISO8601_BASIC_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_ABSOLUTE() throws Exception {

        String s = "{ABSOLUTE}";

        DatePatternElement d = new DatePatternElement();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(DatePatternElement.ABSOLUTE_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_DATE() throws Exception {

        String s = "{DATE}";

        DatePatternElement d = new DatePatternElement();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(DatePatternElement.DATE_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_COMPACT() throws Exception {

        String s = "{COMPACT}";

        DatePatternElement d = new DatePatternElement();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(DatePatternElement.COMPACT_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_UNIX() throws Exception {

        String s = "{UNIX}";

        DatePatternElement d = new DatePatternElement();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

//        assertEquals(AddResult.LAST, d.add(s.charAt(i)));
//
//        assertEquals("%d" + s, d.getLiteral());
//
//        // TODO: test format

        try {

            d.add(s.charAt(i));

            fail("should have thrown exception");
        }
        catch(RuntimeException e) {

            String msg = e.getMessage();
            assertEquals("Support for UNIX pattern NOT YET IMPLEMENTED", msg);
        }
    }

    @Test
    public void add_predefined_UNIX_MILLIS() throws Exception {

        String s = "{UNIX_MILLIS}";

        DatePatternElement d = new DatePatternElement();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

//        assertEquals(AddResult.LAST, d.add(s.charAt(i)));
//
//        assertEquals("%d" + s, d.getLiteral());
//
//        // TODO: test format

        try {

            d.add(s.charAt(i));

            fail("should have thrown exception");
        }
        catch(RuntimeException e) {

            String msg = e.getMessage();
            assertEquals("Support for UNIX_MILLIS pattern NOT YET IMPLEMENTED", msg);
        }
    }

    // getLiteral() ----------------------------------------------------------------------------------------------------

    @Test
    public void getLiteral() throws Exception {

        DatePatternElement e = getLog4jPatternElementToTest();

        assertEquals("%d", e.getLiteral());
    }

    @Test
    public void getLiteral_FormatModifier() throws Exception {

        DatePatternElement e = getLog4jPatternElementToTest();

        e.setFormatModifierLiteral("-5");

        assertEquals("%-5d", e.getLiteral());
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse() throws Exception {

        String line = "something that does not matter 09:01:55,011 INFO";

        int from = 31;

        LiteralPatternElement next = new LiteralPatternElement(" ");

        DatePatternElement pe = new DatePatternElement("d{HH:mm:ss,SSS}");

        ParsedElement p = pe.parse(line, from, next);

        Date d = (Date)p.get();

        assertEquals("09:01:55,011", new SimpleDateFormat("HH:mm:ss,SSS").format(d));

        assertEquals(31, p.from());
        assertEquals(43, p.to());
        assertEquals("09:01:55,011", p.getLiteral());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected DatePatternElement getLog4jPatternElementToTest() throws Exception {

        return new DatePatternElement();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
