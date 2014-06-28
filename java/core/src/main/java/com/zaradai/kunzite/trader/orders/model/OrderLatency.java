/**
 * Copyright 2014 Zaradai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaradai.kunzite.trader.orders.model;

import org.joda.time.DateTime;

import static com.google.common.base.Preconditions.checkNotNull;

public class OrderLatency {
    private DateTime created;   // time the algo created the order
    private DateTime sending;   // time that the order left the trader to the order service
    private DateTime sent;      // time that order service put the order on thw wire to the exchange
    private DateTime ack;       // time that a response from the exchange was received.

    private OrderLatency(DateTime created) {
        this.created = created;
    }

    public static OrderLatency newInstance() {
        return new OrderLatency(DateTime.now());
    }

    public static OrderLatency newInstanceFrom(DateTime created) {
        checkNotNull(created, "Invalid DateTime provided");

        return new OrderLatency(created);
    }

    public DateTime getCreated() {
        return created;
    }

    public DateTime getSending() {
        return sending;
    }

    public void setSending(DateTime sending) {
        this.sending = sending;
    }

    public DateTime getSent() {
        return sent;
    }

    public void setSent(DateTime sent) {
        this.sent = sent;
    }

    public DateTime getAck() {
        return ack;
    }

    public void setAck(DateTime ack) {
        this.ack = ack;
    }

    public long getLatency() {
        checkNotNull(ack, "Latency should have ack time");

        return ack.getMillis() - created.getMillis();
    }
}
