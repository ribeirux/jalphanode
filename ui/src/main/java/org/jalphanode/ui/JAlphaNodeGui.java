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

import com.google.common.collect.ImmutableMap;
import org.jalphanode.DefaultTaskManager;
import org.jalphanode.TaskManager;
import org.jalphanode.TaskManager.Status;
import org.jalphanode.annotation.AfterTask;
import org.jalphanode.annotation.BeforeTask;
import org.jalphanode.annotation.Listener;
import org.jalphanode.annotation.ViewChanged;
import org.jalphanode.config.ConfigException;
import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.JAlphaNodeConfigBuilder;
import org.jalphanode.config.JAlphaNodeType;
import org.jalphanode.config.TaskConfig;
import org.jalphanode.notification.Event;
import org.jalphanode.notification.ViewChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * JAlphaNode GUI.
 *
 * @author   ribeirux
 * @version  $Revision: 280 $
 */
@Listener
public class JAlphaNodeGui {

    private static final Logger LOG = LoggerFactory.getLogger(JAlphaNodeGui.class);

    private final JFrame frmTaskManager;
    private final JPanel taskContainer;
    private final GroupMembersTableModel groupMembersModel;
    private final JButton btnStart;
    private final JButton btnStop;
    private TaskManager taskManager;
    private ImmutableMap<String, JComponent> tasks;

    /**
     * Create the application.
     */
    public JAlphaNodeGui() {

        this.frmTaskManager = new JFrame();
        this.frmTaskManager.setTitle(Messages.getString("gui.frmTaskManager.title")); // $NON-NLS-1$
        this.frmTaskManager.setBounds(100, 100, 600, 300);
        this.frmTaskManager.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frmTaskManager.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(final WindowEvent event) {
                    if ((taskManager != null) && taskManager.getStatus() == Status.RUNNING) {
                        taskManager.shutdown();
                    }
                }
            });

        this.taskContainer = new JPanel();
        taskContainer.setLayout(new BoxLayout(taskContainer, BoxLayout.Y_AXIS));

        final JScrollPane taskScrollPane = new JScrollPane(taskContainer);

        groupMembersModel = new GroupMembersTableModel();

        JTable table = new JTable(groupMembersModel);

        final JScrollPane groupMembersScrollPane = new JScrollPane(table);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, taskScrollPane, groupMembersScrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        this.frmTaskManager.getContentPane().add(splitPane, BorderLayout.CENTER);

        final JPanel commandPanel = new JPanel();
        frmTaskManager.getContentPane().add(commandPanel, BorderLayout.SOUTH);
        commandPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        this.btnStart = new JButton(Messages.getString("gui.button.text")); // $NON-NLS-1$
        commandPanel.add(this.btnStart);
        this.btnStart.addMouseListener(new MouseAdapter() {

            private JFileChooser createFileChooser() {
                final JFileChooser chooser = new JFileChooser();
                final FileNameExtensionFilter filter = new FileNameExtensionFilter(Messages.getString(
                        "gui.fileChooserExtensionDescription.text"), JAlphaNodeType.FILE_EXTENSIONS);
                chooser.setFileFilter(filter);

                return chooser;
            }

            private void dispatchException(final Throwable exception, final String messageKey) {
                LOG.error(exception.getMessage(), exception);

                JOptionPane.showMessageDialog(frmTaskManager, Messages.getString(messageKey),
                        Messages.getString("gui.frmTaskManager.title"), JOptionPane.ERROR_MESSAGE);
            }

            @Override
            public void mouseClicked(final MouseEvent event) {
                if (btnStart.isEnabled()) {
                    btnStart.setEnabled(false);

                    boolean started = false;

                    JFileChooser chooser = createFileChooser();
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
        commandPanel.add(this.btnStop);
    }

    private void startTaskManager(final String path) throws ConfigException {
        JAlphaNodeConfig config = JAlphaNodeConfigBuilder.buildFromFile(path);
        taskManager = new DefaultTaskManager(config);
        taskManager.addListener(this);
        taskManager.start();

        // add tasks
        ImmutableMap.Builder<String, JComponent> builder = new ImmutableMap.Builder<>();
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

    @BeforeTask
    public void setTaskRunning(final Event event) {
        JComponent component = tasks.get(event.getComponentName());
        component.setBackground(Color.GREEN);
        component.repaint();
        component.revalidate();
    }

    @AfterTask
    public void setTaskStopped(final Event event) {
        JComponent component = tasks.get(event.getComponentName());
        component.setBackground(Color.LIGHT_GRAY);
        component.repaint();
        component.revalidate();
    }

    private void stopTaskManager() {
        if (this.btnStop.isEnabled()) {

            this.btnStop.setEnabled(false);

            // shutdown
            try {
                this.taskManager.shutdown();
                this.btnStart.setEnabled(true);
            } catch (RuntimeException r) {
                this.btnStop.setEnabled(true);
                throw r;
            } finally {

                // clear task container
                this.taskContainer.removeAll();
                this.taskContainer.repaint();
                this.taskContainer.revalidate();

                // clear table
                groupMembersModel.removeAllRows();
            }
        }
    }

    @ViewChanged
    public void viewChanged(final ViewChangedEvent event) {
        groupMembersModel.update(event);
    }

    /**
     * Launch the application.
     */
    public static void startGui() {
        EventQueue.invokeLater(() -> {
            try {
                final JAlphaNodeGui window = new JAlphaNodeGui();
                window.frmTaskManager.setVisible(true);
            } catch (final Exception e) {
                LOG.error(e.getMessage(), e);
            }
        });
    }

    public static void main(final String[] args) {
        startGui();
    }
}
