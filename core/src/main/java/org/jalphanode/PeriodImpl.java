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
 * $Id: PeriodImpl.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode;

import org.joda.time.ReadableInstant;
import org.joda.time.base.BasePeriod;

import com.google.common.base.Preconditions;

/**
 * Time period implementation.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public class PeriodImpl extends BasePeriod implements Period {

    private static final long serialVersionUID = -3136128959120851735L;

    /**
     * Creates a new instance.
     * 
     * @param start start instant
     * @param end end instant
     */
    public PeriodImpl(final ReadableInstant start, final ReadableInstant end) {
        super(start, end, null);
    }

    @Override
    public int getPeriodField(final PeriodField field) {
        return this.getValue(Preconditions.checkNotNull(field, "field").getIndex());
    }

}
