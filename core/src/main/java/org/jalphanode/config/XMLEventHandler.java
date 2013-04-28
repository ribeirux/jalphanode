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
 * $Id: XMLEventHandler.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.config;

import java.text.MessageFormat;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Handles XML validation events.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class XMLEventHandler implements ValidationEventHandler {

    private static final Log LOG = LogFactory.getLog(XMLEventHandler.class);

    /**
     * Validates and event.
     *
     * <p>The validation only continues if the there are a warning, otherwise it is stopped.
     *
     * @param   event  the encapsulated validation event information. It is a provider error if this parameter is null.
     *
     * @return  true if the JAXB Provider should attempt to continue the current unmarshal, validate, or marshal
     *          operation after handling this warning/error, false if the provider should terminate the current
     *          operation with the appropriate <tt>UnmarshalException</tt>, <tt>ValidationException</tt>, or <tt>
     *          MarshalException</tt>.
     */
    @Override
    public boolean handleEvent(final ValidationEvent event) {

        final ValidationEventLocator eventLocation = event.getLocator();

        final String details = MessageFormat.format("Line: [{0}:{1}] Message: {2}", eventLocation.getLineNumber(),
                eventLocation.getColumnNumber(), event.getMessage());

        boolean result;

        if (event.getSeverity() == ValidationEvent.WARNING) {
            XMLEventHandler.LOG.warn(details);
            result = true;
        } else {
            XMLEventHandler.LOG.error(details);
            result = false;
        }

        return result;
    }

}
