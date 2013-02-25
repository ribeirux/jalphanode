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
 * $Id: TypedPropertiesAdapter.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.config;

import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * TypedPropertiesAdapter is JAXB XmlAdapter for TypedProperties.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public class TypedPropertiesAdapter extends XmlAdapter<PropertiesType, TypedProperties> {

    @Override
    public PropertiesType marshal(final TypedProperties tProps) throws Exception {

        final PropertiesType pxml = new PropertiesType();

        if (tProps != null) {
            PropertyType newProp;
            for (final Entry<Object, Object> entry : tProps.entrySet()) {
                newProp = new PropertyType();
                newProp.setName((String) entry.getKey());
                newProp.setValue((String) entry.getValue());
                pxml.getProperty().add(newProp);
            }
        }

        return pxml;
    }

    @Override
    public TypedProperties unmarshal(final PropertiesType props) throws Exception {

        final TypedProperties tProps = new TypedProperties();

        if ((props != null)) {
            for (final PropertyType p : props.getProperty()) {
                tProps.put(p.getName(), p.getValue());
            }
        }

        return tProps;
    }
}
