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
 * $Id: JalphaNodeCli.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jalphanode.DefaultTaskManager;
import org.jalphanode.TaskManager;
import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.JAlphaNodeConfigBuilder;

import com.google.common.base.Strings;

/**
 * JalphaNodeCli UI class.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public class JalphaNodeCli {

    private static final Log LOG = LogFactory.getLog(JalphaNodeCli.class);

    private static final String SCRIPT_NAME = Messages.getString("cli.script.name");

    private static final boolean HELP_AUTO_USAGE = true;

    private enum ComandLineOptions {

        /**
         * Path to the configuration file.
         */
        CONFIG_PATH(OptionBuilder.hasOptionalArg().withArgName(Messages.getString("cli.option.config.arg"))
                .withDescription(Messages.getString("cli.option.config.description"))
                .create(Messages.getString("cli.option.config"))),
        /**
         * Starts the GUI
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
            JalphaNodeCli.LOG.error(e.getMessage(), e);
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
     * @param args
     */
    public static void main(final String[] args) {
        final JalphaNodeCli jalphaNodeCli = new JalphaNodeCli();
        jalphaNodeCli.run(new PosixParser(), args);
    }
}
