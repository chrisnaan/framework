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
package com.vaadin.tests.components.grid;

import com.vaadin.tests.util.PersonContainer;
import com.vaadin.ui.Grid;

public class GridEditorFrozenColumnsUI extends GridEditorUI {

    @Override
    protected Grid createGrid(PersonContainer container) {
        Grid grid = super.createGrid(container);

        grid.setFrozenColumnCount(2);

        grid.setWidth("600px");

        return grid;
    }

    @Override
    protected Integer getTicketNumber() {
        return 16727;
    }

    @Override
    protected String getTestDescription() {
        return "Frozen columns should also freeze cells in editor.";
    }
}
