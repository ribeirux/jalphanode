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
package org.jalphanode;

import org.jalphanode.Period.PeriodField;

import org.joda.time.DateTime;

import org.testng.Assert;

import org.testng.annotations.Test;

/**
 * Period test.
 *
 * @author  ribeirux
 */
public class PeriodTest {

    @Test
    public void testPeriod() {

        final DateTime start = DateTime.parse("1974-04-25T13:00:00.000");

        final DateTime end = DateTime.parse("1975-05-26T14:01:01.001");

        final Period period = new PeriodImpl(start, end);

        Assert.assertEquals(period.getPeriodField(PeriodField.YEARS), 1);
        Assert.assertEquals(period.getPeriodField(PeriodField.MONTHS), 1);
        Assert.assertEquals(period.getPeriodField(PeriodField.WEEKS), 0);
        Assert.assertEquals(period.getPeriodField(PeriodField.DAYS), 1);
        Assert.assertEquals(period.getPeriodField(PeriodField.HOURS), 1);
        Assert.assertEquals(period.getPeriodField(PeriodField.MINUTES), 1);
        Assert.assertEquals(period.getPeriodField(PeriodField.SECONDS), 1);
        Assert.assertEquals(period.getPeriodField(PeriodField.MILLIS), 1);
    }

}
