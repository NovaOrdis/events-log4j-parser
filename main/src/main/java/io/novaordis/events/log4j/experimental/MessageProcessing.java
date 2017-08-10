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

package io.novaordis.events.log4j.experimental;

import io.novaordis.events.api.event.Event;
import io.novaordis.events.log4j.impl.Log4jEvent;
import io.novaordis.events.processing.EventProcessingException;
import io.novaordis.events.processing.ProcedureBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This is still experimental, there are no tests.
 *
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/9/17
 */
public class MessageProcessing extends ProcedureBase {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MessageProcessing.class);

    private static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm:ss.SSS");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Map<String, MessagingThreadContext> map;

    private boolean header;

    private long origin;

    private boolean dontProcess = false;

    private long previousTime = -1;

    private MessagingThreadContext lastMessagingThreadContext;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MessageProcessing() {

        this.map = new HashMap<>();

        this.header = true;

        try {
            this.origin = TIMESTAMP_FORMAT.parse("08/08/17 00:00:00.000").getTime() - 5L * 3600 * 1000;
        }
        catch(Exception e) {

            throw new IllegalStateException(e);
        }
    }

    // Procedure implementation ----------------------------------------------------------------------------------------

    @Override
    public List<String> getCommandLineLabels() {

        return Collections.singletonList("message-processing");
    }

    @Override
    protected void process(AtomicLong invocationCount, Event e) throws EventProcessingException {

        if (dontProcess) {

            return;
        }

        if (!(e instanceof Log4jEvent)) {

            log.warn("not a Log4jEvent " + e);
            return;
        }

        if (header) {

            System.out.println("# timestamp, message processing time (ms)");
            header = false;
        }

        Log4jEvent le = (Log4jEvent)e;

        String category = le.getLogCategory();

        String thread = le.getThreadName();

        String message = le.getMessage();

        long time = le.getTime();

        if (previousTime > time) {

            //
            // switch origin
            //

            try {
                origin = TIMESTAMP_FORMAT.parse("08/09/17 00:00:00.000").getTime() - 5L * 3600 * 1000;
            }
            catch(Exception e2) {

                throw new EventProcessingException(e2);
            }

        }
        previousTime = time;


//        System.out.println(le.getRawRepresentation());
//        System.out.println(TIMESTAMP_FORMAT.format(origin + time));

//        if (invocationCount.get() == 1L) {
//            dontProcess = true;
//        }

        if (message.contains("] processing message")) {

            MessagingThreadContext c = map.get(thread);

            if (c != null) {

                System.out.println("leak: " + e);
                return;
            }

            map.put(thread, new MessagingThreadContext(le));

        }
        else if (message.contains("done processing message")) {

            MessagingThreadContext c = map.remove(thread);

            if (c == null) {

                System.out.println("cross: " + e);
                return;
            }

            c.delta(le);

            lastMessagingThreadContext = c;
        }
        else {

            //log.warn("unknown message: " + in);
        }

        //com.ge.mp.alexandria.service.GramListener:Thread-37 (HornetQ-client-global-threads-531916524):Alexandria GramListener[7b77d006] processing message HornetQMessage[ID:2069fa4e-7c9f-11e7-a8fb-01c94948c8e4]:PERSISTENT
    }

    // Public ----------------------------------------------------------------------------------------------------------

    private class MessagingThreadContext {

        private Log4jEvent in;
        private Log4jEvent out;

        public MessagingThreadContext(Log4jEvent in) {

            this.in = in;
        }

        public void delta(Log4jEvent out) {

            this.out = out;

            long t0 = in.getTime();
            long t1 = out.getTime();

            System.out.println(TIMESTAMP_FORMAT.format(origin + in.getTime()) + ", " + (t1 - t0));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
