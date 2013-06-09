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
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jalphanode.DefaultTaskManager;
import org.jalphanode.TaskManager;

import org.jalphanode.TaskManager.Status;

import org.jalphanode.annotation.AfterTask;
import org.jalphanode.annotation.BeforeTask;
import org.jalphanode.annotation.Listener;
import org.jalphanode.annotation.ViewChanged;

import org.jalphanode.cluster.NodeAddress;

import org.jalphanode.config.ConfigException;
import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.JAlphaNodeConfigBuilder;
import org.jalphanode.config.JAlphaNodeType;
import org.jalphanode.config.TaskConfig;

import org.jalphanode.notification.Event;
import org.jalphanode.notification.ViewChangedEvent;

import com.google.common.collect.ImmutableMap;

/**
 * JAlphaNode GUI.
 *
 * @author   ribeirux
 * @version  $Revision: 280 $
 */
@Listener
public class JAlphaNodeGui {

    private static final Log LOG = LogFactory.getLog(JAlphaNodeGui.class);

    private JFrame frmTaskManager;

    private JPanel imageContainer;

    private JPanel taskContainer;

    private JButton btnStart;

    private TaskManager taskManager;

    private JButton btnStop;

    private ImmutableMap<String, JComponent> tasks;

    /**
     * Create the application.
     */
    public JAlphaNodeGui() {

        this.frmTaskManager = new JFrame();
        this.frmTaskManager.setTitle(Messages.getString("gui.frmTaskManager.title")); // $NON-NLS-1$
        this.frmTaskManager.setBounds(100, 100, 450, 300);
        this.frmTaskManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frmTaskManager.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(final WindowEvent event) {
                    if ((taskManager != null) && taskManager.getStatus() == Status.RUNNING) {
                        taskManager.shutdown();
                    }
                }
            });

        final JScrollPane taskScrollPane = new JScrollPane();
        final JScrollPane imageScrollPane = new JScrollPane();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, taskScrollPane, imageScrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        this.frmTaskManager.getContentPane().add(splitPane, BorderLayout.CENTER);

        this.taskContainer = new JPanel();
        taskScrollPane.setViewportView(this.taskContainer);
        taskContainer.setLayout(new BoxLayout(taskContainer, BoxLayout.Y_AXIS));

        this.imageContainer = new JPanel();
        this.imageContainer.setLayout(new GridLayout(0, 4));
        imageScrollPane.setViewportView(this.imageContainer);

        final JPanel commandPannel = new JPanel();
        frmTaskManager.getContentPane().add(commandPannel, BorderLayout.SOUTH);
        commandPannel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        this.btnStart = new JButton(Messages.getString("gui.button.text")); // $NON-NLS-1$
        commandPannel.add(this.btnStart);
        this.btnStart.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent event) {
                    btnStart.setEnabled(false);

                    boolean started = false;
                    final JFileChooser chooser = new JFileChooser();
                    final FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            Messages.getString("gui.fileChooserExtensionDescription.text"),
                            JAlphaNodeType.FILE_EXTENSIONS);
                    chooser.setFileFilter(filter);

                    final int returnVal = chooser.showOpenDialog(frmTaskManager);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        try {
                            startTaskManager(chooser.getSelectedFile().getAbsolutePath());
                            btnStop.setEnabled(true);
                            started = true;
                        } catch (final ConfigException e) {
                            dispatchException(e, "error.invalidConfig");
                        } catch (final Throwable e) {
                            dispatchException(e, "error.unexpected");
                        }
                    }

                    if (!started) {
                        btnStart.setEnabled(true);
                    }
                }
            });

        this.btnStop = new JButton(Messages.getString("gui.btnStop.text"));
        this.btnStop.setEnabled(false);
        this.btnStop.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    stopTaskManager();
                }
            });
        commandPannel.add(this.btnStop);
    }

    private void startTaskManager(final String path) throws ConfigException {
        JAlphaNodeConfig config = JAlphaNodeConfigBuilder.buildFromFile(path);
        taskManager = new DefaultTaskManager(config);
        taskManager.addListener(this);
        taskManager.start();

        // add tasks
        ImmutableMap.Builder<String, JComponent> builder = new ImmutableMap.Builder<String, JComponent>();
        for (TaskConfig task : config.getTasks().getTask()) {
            String taskName = task.getTaskName();
            JTextField label = new JTextField(taskName);
            label.setAlignmentX(JTextField.CENTER_ALIGNMENT);
            label.setAlignmentY(JTextField.CENTER_ALIGNMENT);
            label.setHorizontalAlignment(JTextField.CENTER);
            label.setEditable(false);
            label.setOpaque(true);
            label.setBackground(Color.LIGHT_GRAY);
            builder.put(taskName, label);
            this.taskContainer.add(label);
        }

        this.tasks = builder.build();
        this.taskContainer.repaint();
        this.taskContainer.revalidate();
    }

    private void dispatchException(final Throwable exception, final String messageKey) {
        JAlphaNodeGui.LOG.error(exception.getMessage(), exception);

        JOptionPane.showMessageDialog(this.frmTaskManager, Messages.getString(messageKey),
            Messages.getString("gui.frmTaskManager.title"), JOptionPane.ERROR_MESSAGE);
    }

    private void stopTaskManager() {
        if (this.btnStop.isEnabled()) {
            this.clearTaskContainer();
            this.clearImageContainer();
            this.taskManager.shutdown();
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

    @BeforeTask
    public void setTaskRunning(final Event event) {
        JComponent label = tasks.get(event.getComponentName());
        label.setBackground(Color.GREEN);
        label.repaint();
        label.revalidate();
    }

    @AfterTask
    public void setTaskStopped(final Event event) {
        JComponent label = tasks.get(event.getComponentName());
        label.setBackground(Color.LIGHT_GRAY);
        label.repaint();
        label.revalidate();
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

    // TODO check concurrency
    private void clearTaskContainer() {
        this.taskContainer.removeAll();
        this.taskContainer.repaint();
        this.taskContainer.revalidate();
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
