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

package io.novaordis.events.log4j.pattern.convspec;

import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Test;

import io.novaordis.events.log4j.impl.Log4jEventImpl;
import io.novaordis.events.log4j.pattern.AddResult;
import io.novaordis.events.log4j.pattern.FormatModifier;
import io.novaordis.events.log4j.pattern.LiteralText;
import io.novaordis.events.log4j.pattern.Log4jPatternLayoutException;
import io.novaordis.events.log4j.pattern.ProcessedString;
import io.novaordis.events.log4j.pattern.RenderedLogEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 10/30/17
 */
public class DateTest extends ConversionSpecifierTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void addAfterLast() throws Exception {

        Date d = new Date();

        Assert.assertEquals(AddResult.ACCEPTED, d.add('{'));
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

        Date d = new Date();
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

    //
    // FormatModifier and LineSeparator are NOT used together
    //

    @Test
    @Override
    public void getFormatModifier() throws Exception {

        // noop
    }

    @Test
    @Override
    public void getFormatModifier_2() throws Exception {

        // noop
    }

    @Test
    @Override
    public void getLiteral_ConversionSpecifier_WithFormatModifier() throws Exception {

        // noop
    }

    @Test
    @Override
    public void parseLogContent_With_FormatModifier() throws Exception {

        // noop
    }

    @Test
    @Override
    public void parseLogContent_With_FormatModifier_2() throws Exception {

        // noop
    }

    @Test
    @Override
    public void parseLogContent_With_FormatModifier_3() throws Exception {

        // noop
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_InvalidIdentifier() throws Exception {

        try {

            new Date("e{HH}");

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing expected conversion character '" + Date.CONVERSION_CHARACTER + "'"));
        }
    }

    @Test
    public void constructor() throws Exception {

        Date d = new Date("d{MM/dd/yyyy HH:mm:ss}");
        assertEquals("%d{MM/dd/yyyy HH:mm:ss}", d.getLiteral());
    }

    // add() -----------------------------------------------------------------------------------------------------------

    @Test
    public void add_SimpleDateFormat() throws Exception {

        Date d = new Date();

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

        Date d = new Date();

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

        Date d = new Date();

        assertEquals(AddResult.NOT_ACCEPTED, d.add('a'));

        assertEquals("%d", d.getLiteral());

        assertEquals(Date.DEFAULT_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_DEFAULT() throws Exception {

        String s = "{DEFAULT}";

        Date d = new Date();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(Date.DEFAULT_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_ISO8601() throws Exception {

        String s = "{ISO8601}";

        Date d = new Date();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(Date.ISO8601_PATTERN, d.getSimpleDateFormat().toPattern());
    }
    @Test
    public void add_predefined_ISO8601_BASIC() throws Exception {

        String s = "{ISO8601_BASIC}";

        Date d = new Date();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(Date.ISO8601_BASIC_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_ABSOLUTE() throws Exception {

        String s = "{ABSOLUTE}";

        Date d = new Date();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(Date.ABSOLUTE_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_DATE() throws Exception {

        String s = "{DATE}";

        Date d = new Date();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(Date.DATE_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_COMPACT() throws Exception {

        String s = "{COMPACT}";

        Date d = new Date();

        int i = 0;
        for(; i < s.length() - 1; i ++) {

            assertEquals(AddResult.ACCEPTED, d.add(s.charAt(i)));
        }

        assertEquals(AddResult.LAST, d.add(s.charAt(i)));

        assertEquals("%d" + s, d.getLiteral());
        assertEquals(Date.COMPACT_PATTERN, d.getSimpleDateFormat().toPattern());
    }

    @Test
    public void add_predefined_UNIX() throws Exception {

        String s = "{UNIX}";

        Date d = new Date();

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

        Date d = new Date();

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

        Date e = getConversionSpecifierToTest(null);

        assertEquals("%d{MM/dd/yyyy HH:mm:ss}", e.getLiteral());
    }

    @Test
    public void getLiteral_FormatModifier() throws Exception {

        Date e = getConversionSpecifierToTest(null);

        e.setFormatModifier(new FormatModifier("-5"));

        assertEquals("%-5d{MM/dd/yyyy HH:mm:ss}", e.getLiteral());
    }

    // parse() ---------------------------------------------------------------------------------------------------------

    @Test
    public void parse() throws Exception {

        String line = "something that does not matter 09:01:55,011 INFO";

        int from = 31;

        LiteralText next = new LiteralText(" ");

        Date cs = new Date("d{HH:mm:ss,SSS}");

        RenderedLogEvent p = cs.parseLogContent(line, from, next);

        java.util.Date d = (java.util.Date)p.get();

        assertEquals("09:01:55,011", new SimpleDateFormat("HH:mm:ss,SSS").format(d));

        assertEquals(31, p.from());
        assertEquals(43, p.to());
        assertEquals("09:01:55,011", line.substring(p.from(), p.to()));
    }

    // parseLiteralAfterFormatModifierWasUnapplied() -------------------------------------------------------------------

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied_EmptyString() throws Exception {

        Date cs = new Date("d{HH:mm:ss,SSS}");

        ProcessedString ps = new ProcessedString(0, "", 0);

        try {

            cs.parseLiteralAfterFormatModifierWasUnapplied(ps);

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("empty string when expecting a time stamp"));
        }
    }

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied_RenderingShorterThanExpected() throws Exception {

        Date cs = new Date("d{HH:mm:ss,SSS}");

        ProcessedString ps = new ProcessedString(0, "blah", 4);

        try {

            cs.parseLiteralAfterFormatModifierWasUnapplied(ps);

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains(
                    "date rendering string is shorted that what would have been expected given the pattern"));
            assertTrue(msg.contains("HH:mm:ss,SSS"));
        }
    }

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied_RenderingLongerThanExpected() throws Exception {

        Date cs = new Date("d{HH:mm:ss,SSS}");

        ProcessedString ps = new ProcessedString(0, "blahblahblahblahblah", 20);

        try {

            cs.parseLiteralAfterFormatModifierWasUnapplied(ps);

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains(
                    "date rendering string is longer that what would have been expected given the pattern"));
            assertTrue(msg.contains("HH:mm:ss,SSS"));
        }
    }

    @Test
    public void parseLiteralAfterFormatModifierWasUnapplied_RenderingDoesNotMatchPattern() throws Exception {

        Date cs = new Date("d{HH:mm:ss,SSS}");

        ProcessedString ps = new ProcessedString(0, "AA_BB_CC_DDD", "AA_BB_CC_DDD".length());

        try {

            cs.parseLiteralAfterFormatModifierWasUnapplied(ps);

            fail("should have thrown exception");
        }
        catch(Log4jPatternLayoutException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains(""));
            assertTrue(msg.contains("does not match pattern"));
            assertTrue(msg.contains("HH:mm:ss,SSS"));
        }
    }

    // injectIntoEvent() ------------------------------------------------------------------------------------------

    @Test
    public void injectIntoLog4jEvent() throws Exception {

        Date cs = new Date();

        Log4jEventImpl e = new Log4jEventImpl();

        cs.injectIntoEvent(e, new java.util.Date(7L));

        assertEquals(7L, e.getTime().longValue());
    }

    @Test
    public void injectIntoEvent_InvalidType() throws Exception {

        Date cs = new Date();

        Log4jEventImpl e = new Log4jEventImpl();

        try {

            cs.injectIntoEvent(e, "something");

            fail("should have thrown exception");
        }
        catch(IllegalArgumentException ex) {

            String msg = ex.getMessage();
            assertTrue(msg.contains("invalid value type"));
            assertTrue(msg.contains("String"));
            assertTrue(msg.contains("expected java.util.Date"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected String getMatchingLogContent() throws Exception {

        return "12/01/2017 18:10:20";
    }

    @Override
    protected Date getConversionSpecifierToTest(FormatModifier m) throws Exception {

        Date cs = new Date("d{MM/dd/yyyy HH:mm:ss}");
        cs.setFormatModifier(m);
        return cs;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
