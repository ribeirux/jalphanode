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
package org.jalphanode.demo;

import org.jalphanode.config.TaskConfig;
import org.jalphanode.task.Task;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.EventQueue;
import java.awt.Font;


public class Questions implements Task {

    private static class QuestionsFrame extends JFrame {

        private static final long serialVersionUID = 5709851257707006758L;

        private final String textMessage = "Questions?";

        public QuestionsFrame() {
            setLocationRelativeTo(null);

            JTextField label = new JTextField(textMessage);
            label.setAlignmentX(JTextField.CENTER_ALIGNMENT);
            label.setAlignmentY(JTextField.CENTER_ALIGNMENT);
            label.setHorizontalAlignment(JTextField.CENTER);
            label.setEditable(false);
            label.setFont(new Font("Impact", Font.BOLD, 250));
            getContentPane().add(label);
        }
    }

    private static void run() {
        EventQueue.invokeLater(() -> {
            final QuestionsFrame window = new QuestionsFrame();
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            window.setVisible(true);
        });
    }

    @Override
    public void onTimeout(final TaskConfig config) {
        run();
    }

    public static void main(final String[] args) {
        run();
    }
}
