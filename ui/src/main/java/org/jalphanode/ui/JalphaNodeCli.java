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
package org.jalphanode.ui;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.jalphanode.DefaultTaskManager;
import org.jalphanode.TaskManager;

import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.JAlphaNodeConfigBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

/**
 * JalphaNodeCli UI class.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class JalphaNodeCli {

    private static final Logger LOG = LoggerFactory.getLogger(JalphaNodeCli.class);

    private static final String SCRIPT_NAME = Messages.getString("cli.script.name");

    private static final boolean HELP_AUTO_USAGE = true;

    private enum ComandLineOptions {

        /**
         * Path to the configuration file.
         */
        CONFIG_PATH(OptionBuilder.hasOptionalArg().withArgName(Messages.getString("cli.option.config.arg"))
                .withDescription(Messages.getString("cli.option.config.description")).create(
                Messages.getString("cli.option.config"))),

        /**
         * Starts the GUI.
         */
        GUI(OptionBuilder.withDescription(Messages.getString("cli.option.gui.description")).create(
                Messages.getString("cli.option.gui"))),

        /**
         * Print help message.
         */
        HELP(OptionBuilder.withDescription(Messages.getString("cli.option.help.description")).create(
                Messages.getString("cli.option.help")));

        private final Option option;

        private ComandLineOptions(final Option option) {
            this.option = option;
        }

        public Option getOption() {
            return this.option;
        }
    }

    private Options buildCmdOptions() {

        final OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        optionGroup.addOption(ComandLineOptions.CONFIG_PATH.getOption());
        optionGroup.addOption(ComandLineOptions.GUI.getOption());
        optionGroup.addOption(ComandLineOptions.HELP.getOption());

        final Options options = new Options();
        options.addOptionGroup(optionGroup);

        return options;
    }

    private void startCmd(final String path) {

        JAlphaNodeConfig config = null;
        try {
            if (!Strings.isNullOrEmpty(path)) {
                config = JAlphaNodeConfigBuilder.buildFromFile(path);
            }

            final TaskManager manager = new DefaultTaskManager(config);

            manager.start();

            Runtime.getRuntime().addShutdownHook(new Thread() {

                    @Override
                    public void run() {
                        manager.shutdown();
                    }
                });

        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void run(final CommandLineParser parser, final String[] args) {

        final HelpFormatter formatter = new HelpFormatter();
        final Options options = this.buildCmdOptions();

        try {
            final CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption(ComandLineOptions.CONFIG_PATH.getOption().getOpt())) {
                this.startCmd(cmd.getOptionValue(ComandLineOptions.CONFIG_PATH.getOption().getOpt()));
            } else if (cmd.hasOption(ComandLineOptions.GUI.getOption().getOpt())) {
                JAlphaNodeGui.startGui();
            } else {
                formatter.printHelp(JalphaNodeCli.SCRIPT_NAME, options, JalphaNodeCli.HELP_AUTO_USAGE);
            }
        } catch (final ParseException e) {
            formatter.printHelp(JalphaNodeCli.SCRIPT_NAME, options, JalphaNodeCli.HELP_AUTO_USAGE);
        }
    }

    /**
     * Starts the ui.
     *
     * @param  args
     */
    public static void main(final String[] args) {
        final JalphaNodeCli jalphaNodeCli = new JalphaNodeCli();
        jalphaNodeCli.run(new PosixParser(), args);
    }
}
