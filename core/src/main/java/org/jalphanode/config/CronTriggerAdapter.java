/*******************************************************************************
 * JAlphaNode: Java Clustered Timer
 * Copyright (C) 2011 Pedro Ribeiro
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * $Id$
 *******************************************************************************/
package org.jalphanode.config;

import java.text.MessageFormat;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jalphanode.scheduler.ScheduleIterator;
import org.jalphanode.scheduler.SchedulerParseException;
import org.jalphanode.scheduler.iterator.CronIterator;

import com.google.common.base.Preconditions;

/**
 * Converts a trigger into a Schedule iterator.
 * 
 * @author ribeirux
 * @version $Revision: 118 $
 */
public class CronTriggerAdapter extends XmlAdapter<TriggerType, ScheduleIterator> {

    @Override
    public TriggerType marshal(final ScheduleIterator cronIterator) throws Exception {

        final CronIterator cron = (CronIterator) Preconditions.checkNotNull(cronIterator, "cronIterator");

        final TriggerType triggerType = new TriggerType();
        triggerType.setExpression(cron.getExpression());
        triggerType.setTimezone(cron.getTimeZone().getDisplayName());

        return triggerType;
    }

    @Override
    public ScheduleIterator unmarshal(final TriggerType cronExpression) throws Exception {

        return CronTriggerAdapter.unmarshallInternal(cronExpression);
    }

    /**
     * Creates default scheduler iterator.
     * 
     * @return the default schedule iterator
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
