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

import java.text.MessageFormat;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles XML validation events.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class XMLEventHandler implements ValidationEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(XMLEventHandler.class);

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

        boolean result = true;
        if (event.getSeverity() == ValidationEvent.WARNING) {
            LOG.warn(details);
        } else {
            LOG.error(details);
            result = false;
        }

        return result;
    }

}
