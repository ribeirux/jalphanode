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
package org.jalphanode.scheduler;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Cron iterator. The pattern is a list of six single space-separated fields: representing second, minute, hour, day,
 * month, weekday. Month and weekday names can be given as the first three letters of the English names.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
public class CronIterator implements ScheduleIterator {

    private static final int MAX_DAYS = 366;

    private static final int MAX_DAYS_MONTH = 31;

    private static final int NUMBER_MONTHS = 12;

    private final BitSet seconds = new BitSet(60);

    private final BitSet minutes = new BitSet(60);

    private final BitSet hours = new BitSet(24);

    private final BitSet daysOfWeek = new BitSet(7);

    private final BitSet daysOfMonth = new BitSet(31);

    private final BitSet months = new BitSet(12);

    private final String expression;

    private final TimeZone timeZone;

    /**
     * Construct a new instance with specified pattern and default timezone.
     *
     * @param   expression  a space-separated list of time fields
     *
     * @throws  SchedulerParseException  thrown if the pattern cannot be parse
     */
    public CronIterator(final String expression) throws SchedulerParseException {
        this(expression, TimeZone.getDefault());
    }

    /**
     * Construct a new instance with specified pattern and timeZone.
     *
     * @param   expression  a space-separated list of time fields
     * @param   timeZone    the TimeZone to use for generated trigger times
     *
     * @throws  SchedulerParseException  thrown if the pattern cannot be parse
     */
    public CronIterator(final String expression, final TimeZone timeZone) throws SchedulerParseException {
        this.expression = Preconditions.checkNotNull(expression, "expression");
        this.timeZone = Preconditions.checkNotNull(timeZone, "timeZone");
        this.parse(expression);
    }

    /**
     * Gets the expression property.
     *
     * @return  the expression property
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * Gets the timeZone property.
     *
     * @return  the timeZone property
     */
    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    /**
     * Get the next {@link Date} in the sequence matching the Cron pattern and after the value provided. The return
     * value will have a whole number of seconds, and will be after the input value.
     *
     * @param   date  a seed value
     *
     * @return  the next value matching the pattern
     */
    @Override
    public Date next(final Date date) {
        /*
         * The plan:
         *
         * 1 Round up to the next whole second
         *
         * 2 If seconds match move on, otherwise find the next match:
         * 2.1 If next match is in the next minute then roll forwards
         *
         * 3 If minute matches move on, otherwise find the next match
         * 3.1 If next match is in the next hour then roll forwards
         * 3.2 Reset the seconds and go to 2
         *
         * 4 If hour matches move on, otherwise find the next match
         * 4.1 If next match is in the next day then roll forwards,
         * 4.2 Reset the minutes and seconds and go to 2
         *
         * ...
         */

        final Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(this.timeZone);
        calendar.setTime(date);

        // Truncate to the next whole second
        calendar.add(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);

        this.doNext(calendar, calendar.get(Calendar.YEAR));

        return calendar.getTime();
    }

    private void doNext(final Calendar calendar, final int dot) {
        final List<Integer> resets = Lists.newLinkedList();

        final int second = calendar.get(Calendar.SECOND);
        final List<Integer> emptyList = Collections.emptyList();
        final int updateSecond = this.findNext(this.seconds, second, calendar, Calendar.SECOND, Calendar.MINUTE,
                emptyList);
        if (second == updateSecond) {
            resets.add(Calendar.SECOND);
        }

        final int minute = calendar.get(Calendar.MINUTE);
        final int updateMinute = this.findNext(this.minutes, minute, calendar, Calendar.MINUTE, Calendar.HOUR_OF_DAY,
                resets);
        if (minute == updateMinute) {
            resets.add(Calendar.MINUTE);
        } else {
            this.doNext(calendar, dot);
        }

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int updateHour = this.findNext(this.hours, hour, calendar, Calendar.HOUR_OF_DAY, Calendar.DAY_OF_WEEK,
                resets);
        if (hour == updateHour) {
            resets.add(Calendar.HOUR_OF_DAY);
        } else {
            this.doNext(calendar, dot);
        }

        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final int updateDayOfMonth = this.findNextDay(calendar, this.daysOfMonth, dayOfMonth, this.daysOfWeek,
                dayOfWeek, resets);
        if (dayOfMonth == updateDayOfMonth) {
            resets.add(Calendar.DAY_OF_MONTH);
        } else {
            this.doNext(calendar, dot);
        }

        final int month = calendar.get(Calendar.MONTH);
        final int updateMonth = this.findNext(this.months, month, calendar, Calendar.MONTH, Calendar.YEAR, resets);
        if (month != updateMonth) {
            if (calendar.get(Calendar.YEAR) - dot > 4) {
                throw new IllegalArgumentException("Invalid cron expression " + expression
                        + " led to runaway search for next trigger");
            }

            this.doNext(calendar, dot);
        }

    }

    private int findNextDay(final Calendar calendar, final BitSet daysOfMonth, final int dayOfMonth,
            final BitSet daysOfWeek, final int dayOfWeek, final List<Integer> resets) {

        int count = 0;

        int nextDay = dayOfMonth;
        int currentDayOfWeek = dayOfWeek;

        // the DAY_OF_WEEK values in java.util.Calendar start with 1 (Sunday),
        // but in the cron pattern, they start with 0, so we subtract 1 here
        while ((!daysOfMonth.get(dayOfMonth) || !daysOfWeek.get(currentDayOfWeek - 1))
                && (count++ < CronIterator.MAX_DAYS)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            nextDay = calendar.get(Calendar.DAY_OF_MONTH);
            currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            this.reset(calendar, resets);
        }

        if (count >= CronIterator.MAX_DAYS) {
            throw new IllegalArgumentException("Overflow in day for expression=" + this.expression);
        }

        return nextDay;
    }

    /**
     * Search the bits provided for the next set bit after the value provided, and reset the calendar.
     *
     * @param   bits         a {@link BitSet} representing the allowed values of the field
     * @param   value        the current value of the field
     * @param   calendar     the calendar to increment as we move through the bits
     * @param   field        the field to increment in the calendar (@see {@link Calendar} for the static constants
     *                       defining valid fields)
     * @param   lowerOrders  the Calendar field ids that should be reset (i.e. the ones of lower significance than the
     *                       field of interest)
     *
     * @return  the value of the calendar field that is next in the sequence
     */
    private int findNext(final BitSet bits, final int value, final Calendar calendar, final int field,
            final int nextField, final List<Integer> lowerOrders) {
        int nextValue = bits.nextSetBit(value);

        // roll over if needed
        if (nextValue == -1) {
            calendar.add(nextField, 1);
            this.reset(calendar, Arrays.asList(field));
            nextValue = bits.nextSetBit(0);
        }

        if (nextValue != value) {
            calendar.set(field, nextValue);
            this.reset(calendar, lowerOrders);
        }

        return nextValue;
    }

    /**
     * Reset the calendar setting all the fields provided to zero.
     */
    private void reset(final Calendar calendar, final List<Integer> fields) {
        for (final int field : fields) {
            calendar.set(field, field == Calendar.DAY_OF_MONTH ? 1 : 0);
        }
    }

    /**
     * Parse the given pattern expression.
     */
    private void parse(final String expression) throws SchedulerParseException {
        final String[] fields = expression.split(" ");

        if (fields.length != 6) {
            throw new SchedulerParseException(String.format("Cron expression must consist of 6 fields (found %d in %s)",
                    fields.length, expression));
        }

        this.setNumberHits(this.seconds, fields[0], 0, 60);
        this.setNumberHits(this.minutes, fields[1], 0, 60);
        this.setNumberHits(this.hours, fields[2], 0, 24);
        this.setDaysOfMonth(this.daysOfMonth, fields[3]);
        this.setMonths(this.months, fields[4]);
        this.setDays(this.daysOfWeek, this.replaceOrdinals(fields[5], "SUN,MON,TUE,WED,THU,FRI,SAT"), 8);

        if (this.daysOfWeek.get(7)) {

            // Sunday can be represented as 0 or 7
            this.daysOfWeek.set(0);
            this.daysOfWeek.clear(7);
        }
    }

    /**
     * Replace the values in the commaSeparatedList (case insensitive) with their index in the list.
     *
     * @return  a new string with the values from the list replaced
     */
    private String replaceOrdinals(final String value, final String commaSeparatedList) {
        final String[] list = commaSeparatedList.toUpperCase().split(",");
        String result = value.toUpperCase();

        for (int i = 0; i < list.length; i++) {
            result = result.replaceAll(list[i], String.valueOf(i));
        }

        return result;
    }

    private void setDaysOfMonth(final BitSet bits, final String field) throws SchedulerParseException {

        // Days of month start with 1 (in Cron and Calendar) so add one
        this.setDays(bits, field, CronIterator.MAX_DAYS_MONTH + 1);

        // ... and remove it from the front
        bits.clear(0);
    }

    private void setDays(final BitSet bits, final String field, final int max) throws SchedulerParseException {
        String currentField = field;
        if (currentField.contains("?")) {
            currentField = "*";
        }

        this.setNumberHits(bits, currentField, 0, max);
    }

    private void setMonths(final BitSet bits, final String value) throws SchedulerParseException {

        final String currentValue = this.replaceOrdinals(value, "FOO,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC");
        final BitSet currentMonths = new BitSet(13);

        // Months start with 1 in Cron and 0 in Calendar, so push the values first into a longer bit set
        this.setNumberHits(currentMonths, currentValue, 1, CronIterator.NUMBER_MONTHS + 1);

        // ... and then rotate it to the front of the months
        for (int i = 1; i <= CronIterator.NUMBER_MONTHS; i++) {
            if (currentMonths.get(i)) {
                bits.set(i - 1);
            }
        }
    }

    private void setNumberHits(final BitSet bits, final String value, final int min, final int max)
        throws SchedulerParseException {
        final String[] fields = value.split(",");

        for (final String field : fields) {
            if (!field.contains("/")) {

                // Not an incrementer so it must be a range (possibly empty)
                final int[] range = this.getRange(field, min, max);
                bits.set(range[0], range[1] + 1);
            } else {
                final String[] split = field.split("/");
                if (split.length > 2) {
                    throw new SchedulerParseException("Incrementer has more than two fields: " + field);
                }

                final int[] range = this.getRange(split[0], min, max);
                if (!split[0].contains("-")) {
                    range[1] = max - 1;
                }

                final int delta = Integer.valueOf(split[1]);
                for (int i = range[0]; i <= range[1]; i += delta) {
                    bits.set(i);
                }
            }
        }
    }

    private int[] getRange(final String field, final int min, final int max) throws SchedulerParseException {

        final int[] result = new int[2];

        if (field.contains("*")) {
            result[0] = min;
            result[1] = max - 1;
        } else {
            if (!field.contains("-")) {
                result[0] = Integer.valueOf(field);
                result[1] = result[0];
            } else {
                final String[] split = field.split("-");
                if (split.length > 2) {
                    throw new SchedulerParseException("Range has more than two fields: " + field);
                }

                result[0] = Integer.valueOf(split[0]);
                result[1] = Integer.valueOf(split[1]);
            }

            if ((result[0] >= max) || (result[1] >= max)) {
                throw new SchedulerParseException("Range exceeds maximum (" + max + "): " + field);
            }

            if ((result[0] < min) || (result[1] < min)) {
                throw new SchedulerParseException("Range less than minimum (" + min + "): " + field);
            }
        }

        return result;
    }

}
