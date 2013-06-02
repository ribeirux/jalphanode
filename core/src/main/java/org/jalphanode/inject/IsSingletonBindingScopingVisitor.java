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
 * $Id: TaskType.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.inject;

import java.lang.annotation.Annotation;

import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.spi.BindingScopingVisitor;

public class IsSingletonBindingScopingVisitor implements BindingScopingVisitor<Boolean> {

    @Override
    public Boolean visitEagerSingleton() {
        return Boolean.TRUE;
    }

    @Override
    public Boolean visitScope(final Scope scope) {
        return scope == Scopes.SINGLETON;
    }

    @Override
    public Boolean visitScopeAnnotation(final Class<? extends Annotation> scopeAnnotation) {
        return scopeAnnotation == Singleton.class;
    }

    @Override
    public Boolean visitNoScoping() {
        return Boolean.FALSE;
    }

}
