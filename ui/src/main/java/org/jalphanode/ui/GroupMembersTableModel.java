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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.jalphanode.cluster.NodeAddress;

import org.jalphanode.notification.ViewChangedEvent;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class GroupMembersTableModel extends AbstractTableModel {

    List<String> collumnNames = ImmutableList.of(Messages.getString("gui.group.local.address.title"),
            Messages.getString("gui.group.local.title"), Messages.getString("gui.group.master.title"));

    private Object[][] data;

    public GroupMembersTableModel() {
        this.data = new Object[0][0];
    }

    @Override
    public int getColumnCount() {
        return collumnNames.size();
    }

    @Override
    public String getColumnName(final int column) {
        return collumnNames.get(column);
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public Class<?> getColumnClass(final int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public void update(final ViewChangedEvent event) {
        Preconditions.checkNotNull(event, "event");

        List<NodeAddress> members = event.getNewMembers();
        Object[][] newData = new Object[members.size()][];

        int rowIndex = 0;
        for (NodeAddress node : members) {
            int collumnIndex = 0;
            Object[] row = new Object[collumnNames.size()];
            row[collumnIndex++] = node.toString();
            row[collumnIndex++] = node.equals(event.getLocalAddress());
            row[collumnIndex++] = node.equals(event.getMasterAddress());

            newData[rowIndex++] = row;
        }

        data = newData;
        fireTableDataChanged();
    }

    public void removeAllRows() {
        int rows = data.length;
        data = new Object[0][0];

        // clear internal data structure
        fireTableRowsDeleted(0, rows);
    }

}
