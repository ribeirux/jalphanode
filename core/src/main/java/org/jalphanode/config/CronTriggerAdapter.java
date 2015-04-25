/**
 *    Copyright 2011 Pedro Ribeiro
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.jalphanode.config;

import com.google.common.base.Preconditions;
import org.jalphanode.scheduler.CronIterator;
import org.jalphanode.scheduler.ScheduleIterator;
import org.jalphanode.scheduler.SchedulerParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.MessageFormat;
import java.util.TimeZone;

/**
 * Converts a trigger into a Schedule iterator.
 *
 * @author   ribeirux
 * @version  $Revision: 118 $
 */
public class CronTriggerAdapter extends XmlAdapter<TriggerType, ScheduleIterator> {

    @Override
    public TriggerType marshal(final ScheduleIterator cronIterator) {

        final CronIterator cron = (CronIterator) Preconditions.checkNotNull(cronIterator, "cronIterator");

        final TriggerType triggerType = new TriggerType();
        triggerType.setExpression(cron.getExpression());
        triggerType.setTimezone(cron.getTimeZone().getDisplayName());

        return triggerType;
    }

    @Override
    public ScheduleIterator unmarshal(final TriggerType cronExpression) throws SchedulerParseException {
        return CronTriggerAdapter.unmarshallInternal(cronExpression);
    }

    /**
     * Creates default scheduler iterator.
     *
     * @return  the default schedule iterator
     */
    public static ScheduleIterator buildDefaultScheduleIterator() {

        final TriggerType triggerType = new TriggerType();
        try {
            return CronTriggerAdapter.unmarshallInternal(triggerType);
        } catch (final SchedulerParseException e) {
            throw new IllegalStateException(MessageFormat.format("Default cron expression {0} is invalid.",
                    triggerType.getExpression()), e);
        }
    }

    private static ScheduleIterator unmarshallInternal(final TriggerType triggerType) throws SchedulerParseException {
        Preconditions.checkNotNull(triggerType, "cronExpression");

        final TimeZone cronTimezone = TimeZone.getTimeZone(triggerType.getTimezone());

        return new CronIterator(triggerType.getExpression(), cronTimezone);
    }
}
