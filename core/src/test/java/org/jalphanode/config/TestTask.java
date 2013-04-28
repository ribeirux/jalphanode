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
package org.jalphanode.config;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jalphanode.task.Task;

/**
 * Dummy task.
 *
 * @author   ribeirux
 * @version  $Revision: 149 $
 */
public final class TestTask implements Task {

    private static final Log LOG = LogFactory.getLog(TestTask.class);

    @Override
    public void onTimeout(final TaskConfig config) {
        final Date currentTime = new Date();
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss SSS");

        TestTask.LOG.warn(MessageFormat.format("Dummy task: {0} -> {1}", config.getTaskName(),
                dateFormat.format(currentTime)));
    }

}
