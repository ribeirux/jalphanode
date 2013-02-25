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

import java.util.TimeZone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Trigger configuration.
 * 
 * @author ribeirux
 * @version $Revision$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "triggerType", propOrder = { "expression", "timezone" })
public class TriggerType {

    private static final String DEFAULT_EXPRESSION = "*/1 * * * * ?";

    private static final String DEFAULT_EXPRESSION_TIMEZONE = TimeZone.getDefault().getDisplayName();

    @XmlElement(required = true)
    private String expression;

    @XmlElement
    private String timezone;

    /**
     * Creates a new trigger with default configuration.
     */
    public TriggerType() {
        this.expression = TriggerType.DEFAULT_EXPRESSION;
        this.timezone = TriggerType.DEFAULT_EXPRESSION_TIMEZONE;
    }

    /**
     * Gets the expression property.
     * 
     * @return the expression property
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * Sets the expression property.
     * 
     * @param expression the expression to set
     */
    public void setExpression(final String expression) {
        this.expression = expression;
    }

    /**
     * Gets the timezone property.
     * 
     * @return the timezone property
     */
    public String getTimezone() {
        return this.timezone;
    }

    /**
     * Sets the timezone property.
     * 
     * @param timezone the timezone to set
     */
    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

}
