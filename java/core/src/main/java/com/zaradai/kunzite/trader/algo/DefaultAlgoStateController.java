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
package com.zaradai.kunzite.trader.algo;

import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.events.AlgoStatusUpdateEvent;

public class DefaultAlgoStateController implements AlgoStateController {
    private final ContextLogger logger;
    private final EventAggregator eventAggregator;
    private AlgoState state;
    private Algo controlled;

    public DefaultAlgoStateController(ContextLogger logger, EventAggregator eventAggregator) {
        this.logger = logger;
        this.eventAggregator = eventAggregator;
    }


    @Override
    public void control(Algo controlled) {
        if (this.controlled == null) {
            this.controlled = controlled;
            changeState(AlgoState.Off, "Initializing");
        }
    }

    @Override
    public void start() {
        if (controlled == null) {
            logNoControlledAlgo();
            return;
        }
        logRequestToStartAlgo();

        AlgoState request = AlgoState.Starting;

        if (getState() == AlgoState.Off || getState() == AlgoState.Suspended) {
            changeState(request, "");

            try {
                controlled.start();
                changeState(AlgoState.On, "");
            } catch (AlgoException e) {
                logAlgoException(e);
                suspend(e.getMessage());
            }
        } else {
            logWrongState(request, getState());
        }
    }

    @Override
    public void stop() {
        if (controlled == null) {
            logNoControlledAlgo();
            return;
        }
        logRequestToStopAlgo();

        AlgoState request = AlgoState.Stopping;
        String reason = "";

        if (getState() == AlgoState.On || getState() == AlgoState.Suspended) {
            changeState(request, "");

            try {
                controlled.stop();
            } catch (AlgoException e) {
                logAlgoException(e);
                reason = e.getMessage();
            }
            // even if failed to stop will still set to stopped state
            changeState(AlgoState.Off, reason);
        } else {
            logWrongState(request, getState());
        }
    }

    @Override
    public void suspend(String reason) {
        if (getState() == AlgoState.On || getState() == AlgoState.Starting) {
            changeState(AlgoState.Suspended, reason);
        } else {
            // consider 2 enums one for request and one for actual, odd to see suspended as a request
            logWrongState(AlgoState.Suspended, getState());
        }
    }

    @Override
    public AlgoState getState() {
        return state;
    }

    public Algo getControlled() {
        return controlled;
    }

    private void changeState(AlgoState newState, String reason) {
        logStateChange(newState, reason);
        setState(newState);
        publishState(reason);
    }

    private void logStateChange(AlgoState newState, String reason) {
        LogHelper.info(logger)
                .addContext("Algo state change")
                .addReason(reason)
                .add("from", getState())
                .add("to", newState)
                .add("Id", controlled.getId())
                .log();
    }

    private void publishState(String reason) {
        AlgoStatusUpdateEvent event = new AlgoStatusUpdateEvent(getState(), this.controlled.getId(), reason);
        this.eventAggregator.publish(event);
    }

    private void setState(AlgoState newState) {
        state = newState;
    }

    private void logNoControlledAlgo() {
        LogHelper.error(logger)
                .addContext("Algo state controller")
                .addReason("null algo controlled")
                .log();
    }

    private void logRequestToStartAlgo() {
        LogHelper.info(logger)
                .addContext("Start algo request")
                .add("current", getState())
                .add("Id", controlled.getId())
                .log();
    }

    private void logRequestToStopAlgo() {
        LogHelper.info(logger)
                .addContext("Stop algo request")
                .add("current", getState())
                .add("Id", controlled.getId())
                .log();
    }

    private void logAlgoException(AlgoException e) {
        LogHelper.error(logger)
                .addContext("Algo Exception")
                .add("Id", controlled.getId())
                .addReason(e.getMessage())
                .add("state", getState())
                .log();
    }

    private void logWrongState(AlgoState request, AlgoState state) {
        LogHelper.warn(logger)
                .addContext("Change state command")
                .addReason("Invalid state to call command")
                .add("from", getState())
                .add("to", request)
                .add("Id", controlled.getId())
                .log();
    }
}
