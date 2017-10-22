/* Copyright (C) 2017 Stephan Kreutzer
 *
 * This file is part of tracking_text_editor_1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * tracking_text_editor_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * tracking_text_editor_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with tracking_text_editor_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/htx/gui/tracking_text_editor/tracking_text_editor_1/FlushingWindowAdapter.java
 * @brief Custom WindowAdapter for flushing the last changes at the window close event.
 * @author Stephan Kreutzer
 * @since 2017-10-17
 */



import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;



class FlushingWindowAdapter extends WindowAdapter
{
    public FlushingWindowAdapter(tracking_text_editor_1 parent)
    {
        if (parent == null)
        {
            throw new NullPointerException();
        }

        this.parent = parent;
    }

    public void windowClosing(WindowEvent event)
    {
        this.parent.closeOutput();
        event.getWindow().setVisible(false);
        event.getWindow().dispose();
    }

    tracking_text_editor_1 parent = null;
}
