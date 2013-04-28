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
 * $Id: Period.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode;

/**
 * Time period.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public interface Period {

    /**
     * Period field type.
     *
     * @author  ribeirux
     */
    public enum PeriodField {

        /**
         * Number of years.
         */
        YEARS(0),
        /**
         * Number of months.
         */
        MONTHS(1),
        /**
         * Number of weeks.
         */
        WEEKS(2),
        /**
         * Number of days.
         */
        DAYS(3),
        /**
         * Number of hours.
         */
        HOURS(4),
        /**
         * Number of minutes.
         */
        MINUTES(5),
        /**
         * Number of seconds.
         */
        SECONDS(6),
        /**
         * Number of milliseconds.
         */
        MILLIS(7);

        private final int index;

        private PeriodField(final int index) {
            this.index = index;
        }

        /**
         * Gets the period index.
         *
         * @return  the period index
         */
        public int getIndex() {
            return this.index;
        }
    }

    /**
     * Gets the time associated to the specified field.
     *
     * @param   field  the period field
     *
     * @return  the time of the associated to the specified field
     */
    int getPeriodField(PeriodField field);

}
