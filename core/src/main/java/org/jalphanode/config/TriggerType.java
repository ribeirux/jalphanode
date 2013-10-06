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

import java.util.TimeZone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Trigger configuration.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "triggerType", propOrder = {"expression", "timezone"})
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
     * @return  the expression property
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * Sets the expression property.
     *
     * @param  expression  the expression to set
     */
    public void setExpression(final String expression) {
        this.expression = expression;
    }

    /**
     * Gets the timezone property.
     *
     * @return  the timezone property
     */
    public String getTimezone() {
        return this.timezone;
    }

    /**
     * Sets the timezone property.
     *
     * @param  timezone  the timezone to set
     */
    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

}
