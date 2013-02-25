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
package org.jalphanode.cluster.jgroups;

import java.net.URL;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jalphanode.AbstractConfigHolder;
import org.jalphanode.cluster.MembershipException;
import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.TypedPropertiesConfig;
import org.jalphanode.util.ConfigurationUtils;
import org.jalphanode.util.FileUtils;
import org.jgroups.Channel;
import org.jgroups.JChannel;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Creates a new channel according to the jalphanode configuration.
 * 
 * @author ribeirux
 * @version $Revision$
 */
public class ChannelProvider extends AbstractConfigHolder implements Provider<Channel> {

    private static final Log LOG = LogFactory.getLog(ChannelProvider.class);

    /**
     * JGroups properties.
     * 
     * @author ribeirux
     */
    public enum JGroupsProperties {

        /**
         * File path.
         */
        CONFIGURATION_FILE("configurationFile"),
        /**
         * XMl document.
         */
        CONFIGURATION_XML("configurationXML"),
        /**
         * String content.
         */
        CONFIGURATION_STRING("configurationString");

        private final String key;

        private JGroupsProperties(final String key) {
            this.key = key;
        }

        /**
         * Gets property key.
         * 
         * @return property key
         */
        public String getKey() {
            return this.key;
        }
    }

    /**
     * Creates a new channel provider instance with specified configuration.
     * 
     * @param config the configuration
     */
    @Inject
    public ChannelProvider(final JAlphaNodeConfig config) {
        super(config);
    }

    @Override
    public Channel get() {

        Channel channel = null;

        final TypedPropertiesConfig props = this.getConfig().getMembership().getProperties();

        if (props.containsKey(JGroupsProperties.CONFIGURATION_FILE.getKey())) {
            final String value = props.getProperty(JGroupsProperties.CONFIGURATION_FILE.getKey());
            final URL fileURL = FileUtils.lookupFileLocation(value);

            if (fileURL == null) {
                throw new MembershipException(MessageFormat.format("JGroups configuration file {0} not found.", value));
            }

            try {
                channel = new JChannel(fileURL);
            } catch (final Exception e) {
                throw new MembershipException(MessageFormat.format(
                        "Cannot initialize protocol stack through configuration file: {0}. Cause: {1}", value,
                        e.getLocalizedMessage()), e);
            }
        } else if (props.containsKey(JGroupsProperties.CONFIGURATION_XML.getKey())) {
            final String value = props.getProperty(JGroupsProperties.CONFIGURATION_XML.getKey());

            try {
                channel = new JChannel(ConfigurationUtils.stringToElement(value));
            } catch (final Exception e) {
                throw new MembershipException(MessageFormat.format(
                        "Cannot initialize protocol stack through configuration XML: {0}. Cause: {1}", value,
                        e.getLocalizedMessage()), e);
            }
        } else if (props.containsKey(JGroupsProperties.CONFIGURATION_STRING.getKey())) {
            final String value = props.getProperty(JGroupsProperties.CONFIGURATION_STRING.getKey());

            try {
                channel = new JChannel(value);
            } catch (final Exception e) {
                throw new MembershipException(MessageFormat.format(
                        "Cannot initialize protocol stack through configuration string: {0}. Cause: {1}", value,
                        e.getLocalizedMessage()), e);
            }
        } else {
            ChannelProvider.LOG.info("JGroups configuration not defined. Using default JGroups configuration!");

            try {
                channel = new JChannel();
            } catch (final Exception e) {
                throw new MembershipException(MessageFormat.format("Cannot initialize protocol stack. Cause: {0}",
                        e.getLocalizedMessage()), e);
            }
        }

        return channel;
    }

}
