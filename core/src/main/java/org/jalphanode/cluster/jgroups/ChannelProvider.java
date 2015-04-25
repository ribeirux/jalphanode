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
package org.jalphanode.cluster.jgroups;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.jalphanode.cluster.MembershipException;
import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.TypedPropertiesConfig;
import org.jalphanode.util.ConfigurationUtils;
import org.jalphanode.util.FileUtils;
import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.MessageFormat;

/**
 * Creates a new channel according to the jalphanode configuration.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
public class ChannelProvider implements Provider<Channel> {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelProvider.class);

    private final JAlphaNodeConfig config;

    /**
     * Creates a new channel provider instance with specified configuration.
     *
     * @param  config  the configuration
     */
    @Inject
    public ChannelProvider(final JAlphaNodeConfig config) {
        this.config = Preconditions.checkNotNull(config, "config");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Channel get() {
        Channel channel;
        final TypedPropertiesConfig props = this.config.getMembership().getProperties();

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
            LOG.info("JGroups configuration not defined. Using default JGroups configuration");

            try {
                channel = new JChannel();
            } catch (final Exception e) {
                throw new MembershipException(MessageFormat.format("Cannot initialize protocol stack. Cause: {0}",
                        e.getLocalizedMessage()), e);
            }
        }

        return channel;
    }

    /**
     * JGroups properties.
     *
     * @author  ribeirux
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

        JGroupsProperties(final String key) {
            this.key = key;
        }

        /**
         * Gets property key.
         *
         * @return  property key
         */
        public String getKey() {
            return this.key;
        }
    }
}
