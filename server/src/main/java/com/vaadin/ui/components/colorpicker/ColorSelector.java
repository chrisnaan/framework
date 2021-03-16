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
package com.vaadin.ui.components.colorpicker;

import java.io.Serializable;

import com.vaadin.shared.ui.colorpicker.Color;

/**
 * An interface for a color selector.
 *
 * @since 7.0.0
 */
public interface ColorSelector extends Serializable, HasColorChangeListener {

    /**
     * Sets the color.
     *
     * @param color
     *            the new color
     */
    public void setColor(Color color);

    /**
     * Gets the color.
     *
     * @return the color
     */
    public Color getColor();
}
