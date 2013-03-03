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
 * $Id: JAlphaNodeGui.java 280 2013-01-06 19:28:26Z ribeirux $
 *******************************************************************************/
package org.jalphanode.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jalphanode.DefaultTaskManager;
import org.jalphanode.TaskManager;
import org.jalphanode.annotation.Listener;
import org.jalphanode.annotation.ViewChanged;
import org.jalphanode.cluster.NodeAddress;
import org.jalphanode.config.ConfigException;
import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.JAlphaNodeConfigBuilder;
import org.jalphanode.config.JAlphaNodeType;
import org.jalphanode.notification.ViewChangedEvent;

import com.google.common.base.Strings;

/**
 * JAlphaNode GUI.
 * 
 * @author ribeirux
 * @version $Revision: 280 $
 */
@Listener
public class JAlphaNodeGui {

    private static final Log LOG = LogFactory.getLog(JAlphaNodeGui.class);

    private JFrame frmTaskManager;

    private JTextField configFile;

    private JPanel imageContainer;

    private JButton btnStart;

    private TaskManager taskManager;

    private JButton btnStop;

    /**
     * Create the application.
     */
    public JAlphaNodeGui() {
        this.initializeGui();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initializeGui() {

        this.frmTaskManager = new JFrame();
        this.frmTaskManager.setTitle(Messages.getString("gui.frmTaskManager.title")); //$NON-NLS-1$
        this.frmTaskManager.setBounds(100, 100, 450, 300);
        this.frmTaskManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frmTaskManager.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent event) {
                if ((JAlphaNodeGui.this.taskManager != null)
                        && JAlphaNodeGui.this.taskManager.getStatus().isShutdownAllowed()) {
                    JAlphaNodeGui.this.taskManager.shutdown();
                }
            }
        });

        final JPanel interactiveContainer = new JPanel();
        this.frmTaskManager.getContentPane().add(interactiveContainer, BorderLayout.SOUTH);

        final JPanel filePanel = new JPanel();
        filePanel.setLayout(new GridLayout(0, 3));

        final JLabel configLabel = new JLabel(Messages.getString("gui.label.text"));
        configLabel.setHorizontalAlignment(SwingConstants.CENTER);
        filePanel.add(configLabel);

        this.configFile = new JTextField();
        this.configFile.setHorizontalAlignment(SwingConstants.LEFT);
        filePanel.add(this.configFile);
        this.configFile.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JAlphaNodeGui.this.startTaskManager();
                }
            }
        });

        final JButton btnBrowse = new JButton(Messages.getString("gui.btnOpenFile.text"));
        filePanel.add(btnBrowse);

        final JPanel commandPannel = new JPanel();
        commandPannel.setLayout(new GridLayout(0, 2));

        this.btnStart = new JButton(Messages.getString("gui.button.text")); //$NON-NLS-1$
        commandPannel.add(this.btnStart);
        this.btnStart.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent event) {
                JAlphaNodeGui.this.startTaskManager();
            }
        });

        this.btnStop = new JButton(Messages.getString("gui.btnStop.text")); //$NON-NLS-1$
        this.btnStop.setEnabled(false);
        this.btnStop.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                JAlphaNodeGui.this.stopTaskManager();
            }
        });
        commandPannel.add(this.btnStop);
        interactiveContainer.setLayout(new GridLayout(0, 1));
        interactiveContainer.add(filePanel);
        interactiveContainer.add(commandPannel);
        btnBrowse.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                final JFileChooser chooser = new JFileChooser();
                final FileNameExtensionFilter filter = new FileNameExtensionFilter(Messages
                        .getString("gui.fileChooserExtensionDescription.text"), JAlphaNodeType.FILE_EXTENSIONS);
                chooser.setFileFilter(filter);
                final int returnVal = chooser.showOpenDialog(JAlphaNodeGui.this.frmTaskManager);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    JAlphaNodeGui.this.configFile.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        final JScrollPane scrollPane = new JScrollPane();
        this.frmTaskManager.getContentPane().add(scrollPane, BorderLayout.CENTER);

        this.imageContainer = new JPanel();
        this.imageContainer.setLayout(new GridLayout(0, 4));
        scrollPane.setViewportView(this.imageContainer);
    }

    private void startTaskManager() {
        if (this.btnStart.isEnabled()) {
            final String fileLocation = this.configFile.getText();
            try {
                JAlphaNodeConfig config = null;
                
                if (!Strings.isNullOrEmpty(fileLocation)) {
                    config = JAlphaNodeConfigBuilder.buildFromFile(fileLocation);
                }

                this.taskManager = new DefaultTaskManager(config);
                this.taskManager.addListener(this);

                this.taskManager.start();
                this.btnStart.setEnabled(false);
                this.btnStop.setEnabled(true);
            } catch (final ConfigException e) {
                this.dispatchException(e, "error.invalidConfig");
            } catch (final Exception e) {
                this.dispatchException(e, "error.unexpected");
            }
        }
    }

    private void dispatchException(final Exception exception, final String messageKey) {
        JAlphaNodeGui.LOG.error(exception.getMessage(), exception);

        JOptionPane.showMessageDialog(this.frmTaskManager, Messages.getString(messageKey),
                Messages.getString("gui.frmTaskManager.title"), JOptionPane.ERROR_MESSAGE);
    }

    private void stopTaskManager() {
        if (this.btnStop.isEnabled()) {
            this.taskManager.shutdown();
            this.clearImageContainer();
            this.btnStop.setEnabled(false);
            this.btnStart.setEnabled(true);
        }
    }

    @ViewChanged
    public void viewChanged(final ViewChangedEvent event) {

        final List<JLabel> picLabels = new ArrayList<JLabel>();
        for (final NodeAddress newMember : event.getNewMembers()) {
            JLabel picLabel;
            if (newMember.equals(event.getLocalAddress())) {
                if (newMember.equals(event.getMasterAddress())) {
                    picLabel = this.buildImage(NodeTypeImage.LOCAL_MASTER, newMember);
                } else {
                    picLabel = this.buildImage(NodeTypeImage.LOCAL_SLAVE, newMember);
                }
            } else if (newMember.equals(event.getMasterAddress())) {
                picLabel = this.buildImage(NodeTypeImage.REMOTE_MASTER, newMember);
            } else {
                picLabel = this.buildImage(NodeTypeImage.REMOTE_SLAVE, newMember);
            }
            picLabels.add(picLabel);
        }

        this.updateImageContainer(picLabels);
    }

    private synchronized void updateImageContainer(final List<JLabel> picLabels) {
        this.imageContainer.removeAll();

        for (final JLabel picLabel : picLabels) {
            this.imageContainer.add(picLabel);
        }

        this.imageContainer.repaint();
        this.imageContainer.revalidate();
    }

    private synchronized void clearImageContainer() {
        this.imageContainer.removeAll();
        this.imageContainer.repaint();
        this.imageContainer.revalidate();
    }

    private JLabel buildImage(final NodeTypeImage nodeTypeImage, final NodeAddress nodeAddress) {
        final JLabel picLabel = new JLabel(nodeTypeImage.getImage());
        picLabel.setToolTipText(nodeAddress.toString());

        return picLabel;
    }

    /**
     * Launch the application.
     */
    public static void startGui() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    final JAlphaNodeGui window = new JAlphaNodeGui();
                    window.frmTaskManager.setVisible(true);
                } catch (final Exception e) {
                    JAlphaNodeGui.LOG.error(e.getMessage(), e);
                }
            }
        });
    }
}
