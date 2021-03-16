/*
 * Copyright 2000-2021 Vaadin Ltd.
 *
 * Licensed under the Commercial Vaadin Developer License version 4.0 (CVDLv4); 
 * you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * https://vaadin.com/license/cvdl-4.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.ui;

import java.io.Serializable;

/**
 * A base class for generating an unique object that is serializable.
 * <p>
 * This class is abstract but has no abstract methods to force users to create
 * an anonymous inner class. Otherwise each instance will not be unique.
 *
 * @author Vaadin Ltd
 * @since 6.8.0
 *
 */
public abstract class UniqueSerializable implements Serializable {

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass();
    }
}
