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
package org.jalphanode.config;

import org.jalphanode.JAlphaNodeException;

/**
 * Super class of configuration exceptions.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class ConfigException extends JAlphaNodeException {

    private static final long serialVersionUID = -6858598278230636743L;

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
    public ConfigException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param  message  the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *                  method.
     */
    public ConfigException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param  message  the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param  cause    the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>
     *                  null</tt> value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ConfigException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause  the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *                value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ConfigException(final Throwable cause) {
        super(cause);
    }
}
