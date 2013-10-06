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

import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * TypedPropertiesAdapter is JAXB XmlAdapter for TypedProperties.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class TypedPropertiesAdapter extends XmlAdapter<PropertiesType, TypedProperties> {

    @Override
    public PropertiesType marshal(final TypedProperties tProps) throws Exception {

        final PropertiesType pxml = new PropertiesType();

        if (tProps != null) {
            for (final Entry<Object, Object> entry : tProps.entrySet()) {
                PropertyType newProp = new PropertyType();
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
