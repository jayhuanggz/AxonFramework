/*
 * Copyright (c) 2010-2018. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.deadline;

import org.axonframework.common.IdentifierFactory;
import org.axonframework.messaging.Scope;
import org.axonframework.messaging.ScopeDescriptor;

import java.time.Duration;
import java.time.Instant;

/**
 * Contract for deadline managers. There are two sets of methods for scheduling - ones which accept a specified
 * {@code scheduleId} and ones which generate and return the id themselves. For callers that want to use the auto
 * generated schedule id approach, it is recommended to use {@link #generateScheduleId()} method.
 *
 * @author Milan Savic
 * @author Steven van Beelen
 * @since 3.3
 */
public interface DeadlineManager {

    /**
     * Schedules a deadline at given {@code triggerDateTime}. The returned {@code scheduleId} can be used to cancel the
     * scheduled deadline. The scope within which this call is made will be retrieved by the DeadlineManager itself.
     * <p>
     * The given {@code messageOrPayload} may be any object, as well as a DeadlineMessage. In the latter case, the
     * instance provided is the donor for the payload and {@link org.axonframework.messaging.MetaData} of the actual
     * deadline being used. In the former case, the given {@code messageOrPayload} will be wrapped as the payload of a
     * {@link DeadlineMessage}.
     * </p>
     *
     * @param triggerDateTime  A {@link java.time.Instant} denoting the moment to trigger the deadline handling
     * @param messageOrPayload A {@link org.axonframework.messaging.Message} or payload for a message as an
     *                         {@link Object}
     * @return the {@code scheduleId} as a {@link String} to use when cancelling the schedule
     */
    default String schedule(Instant triggerDateTime, Object messageOrPayload) {
        return schedule(triggerDateTime, Scope.describeCurrentScope(), messageOrPayload);
    }

    /**
     * Schedules a deadline at given {@code triggerDateTime} with provided context. The returned {@code scheduleId} can
     * be used to cancel the scheduled deadline.
     * <p>
     * The given {@code messageOrPayload} may be any object, as well as a DeadlineMessage. In the latter case, the
     * instance provided is the donor for the payload and {@link org.axonframework.messaging.MetaData} of the actual
     * deadline being used. In the former case, the given {@code messageOrPayload} will be wrapped as the payload of a
     * {@link DeadlineMessage}.
     * </p>
     *
     * @param triggerDateTime  A {@link java.time.Instant} denoting the moment to trigger the deadline handling
     * @param deadlineScope    A {@link ScopeDescriptor} describing the scope within which the deadline was scheduled
     * @param messageOrPayload A {@link org.axonframework.messaging.Message} or payload for a message as an
     *                         {@link Object}
     * @return the {@code scheduleId} as a {@link String} to use when cancelling the schedule
     */
    default String schedule(Instant triggerDateTime, ScopeDescriptor deadlineScope, Object messageOrPayload) {
        String scheduleId = generateScheduleId();
        schedule(triggerDateTime, deadlineScope, messageOrPayload, scheduleId);
        return scheduleId;
    }

    /**
     * Schedules a deadline after the given {@code triggerDuration}. The returned {@code scheduleId} can be used to
     * cancel the scheduled deadline. The scope within which this call is made will be retrieved by the DeadlineManager
     * itself.
     * <p>
     * The given {@code messageOrPayload} may be any object, as well as a DeadlineMessage. In the latter case, the
     * instance provided is the donor for the payload and {@link org.axonframework.messaging.MetaData} of the actual
     * deadline being used. In the former case, the given {@code messageOrPayload} will be wrapped as the payload of a
     * {@link DeadlineMessage}.
     * </p>
     *
     * @param triggerDuration  A {@link java.time.Duration} describing the waiting period before handling the deadline
     * @param messageOrPayload A {@link org.axonframework.messaging.Message} or payload for a message as an
     *                         {@link Object}
     * @return the {@code scheduleId} as a {@link String} to use when cancelling the schedule
     */
    default String schedule(Duration triggerDuration, Object messageOrPayload) {
        return schedule(triggerDuration, Scope.describeCurrentScope(), messageOrPayload);
    }

    /**
     * Schedules a deadline after the given {@code triggerDuration} with provided context. The returned
     * {@code scheduleId} can be used to cancel the scheduled deadline.
     * <p>
     * The given {@code messageOrPayload} may be any object, as well as a DeadlineMessage. In the latter case, the
     * instance provided is the donor for the payload and {@link org.axonframework.messaging.MetaData} of the actual
     * deadline being used. In the former case, the given {@code messageOrPayload} will be wrapped as the payload of a
     * {@link DeadlineMessage}.
     * </p>
     *
     * @param triggerDuration  A {@link java.time.Duration} describing the waiting period before handling the deadline
     * @param deadlineScope    A {@link ScopeDescriptor} describing the scope within which the deadline was scheduled
     * @param messageOrPayload A {@link org.axonframework.messaging.Message} or payload for a message as an
     *                         {@link Object}
     * @return the {@code scheduleId} as a {@link String} to use when cancelling the schedule
     */
    default String schedule(Duration triggerDuration, ScopeDescriptor deadlineScope, Object messageOrPayload) {
        String scheduleId = generateScheduleId();
        schedule(triggerDuration, deadlineScope, messageOrPayload, scheduleId);
        return scheduleId;
    }

    /**
     * Schedules a deadline at given {@code triggerDateTime} with provided context. The provided {@code scheduleId} can
     * be used to cancel the scheduled deadline.
     * <p>
     * The given {@code messageOrPayload} may be any object, as well as a DeadlineMessage. In the latter case, the
     * instance provided is the donor for the payload and {@link org.axonframework.messaging.MetaData} of the actual
     * deadline being used. In the former case, the given {@code messageOrPayload} will be wrapped as the payload of a
     * {@link DeadlineMessage}.
     * </p>
     *
     * @param triggerDateTime  A {@link java.time.Instant} denoting the moment to trigger the deadline handling
     * @param deadlineScope    A {@link ScopeDescriptor} describing the scope within which the deadline was scheduled
     * @param messageOrPayload A {@link org.axonframework.messaging.Message} or payload for a message as an
     *                         {@link Object}
     * @param scheduleId       A {@link String} schedule id to use when cancelling the schedule
     * @throws IllegalArgumentException if the {@code scheduleId}is not compatible with this DeadlineManager
     */
    void schedule(Instant triggerDateTime,
                  ScopeDescriptor deadlineScope,
                  Object messageOrPayload,
                  String scheduleId) throws IllegalArgumentException;

    /**
     * Schedules a deadline after the given {@code triggerDuration} with provided context. The provided
     * {@code scheduleId} can be used to cancel the scheduled deadline.
     * <p>
     * The given {@code messageOrPayload} may be any object, as well as a DeadlineMessage. In the latter case, the
     * instance provided is the donor for the payload and {@link org.axonframework.messaging.MetaData} of the actual
     * deadline being used. In the former case, the given {@code messageOrPayload} will be wrapped as the payload of a
     * {@link DeadlineMessage}.
     * </p>
     *
     * @param triggerDuration  A {@link java.time.Duration} describing the waiting period before handling the deadline
     * @param deadlineScope    A {@link ScopeDescriptor} describing the scope within which the deadline was scheduled
     * @param messageOrPayload A {@link org.axonframework.messaging.Message} or payload for a message as an
     *                         {@link Object}
     * @param scheduleId       A {@link String} schedule id to use when cancelling the schedule
     * @throws IllegalArgumentException if the {@code scheduleId}is not compatible with this DeadlineManager
     */
    void schedule(Duration triggerDuration,
                  ScopeDescriptor deadlineScope,
                  Object messageOrPayload,
                  String scheduleId) throws IllegalArgumentException;

    /**
     * Generates a {@link String} schedule id.
     *
     * @return a {@link String} schedule id
     */
    default String generateScheduleId() {
        return IdentifierFactory.getInstance().generateIdentifier();
    }

    /**
     * Cancels the deadline. If the deadline is already handled, this method does nothing.
     *
     * @param scheduleId the {@link String} denoting the scheduled deadline to cancel
     * @throws IllegalArgumentException if the schedule id belongs to another scheduler
     */
    void cancelSchedule(String scheduleId) throws IllegalArgumentException;
}
