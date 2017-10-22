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
 * @file $/htx/gui/tracking_text_editor/tracking_text_editor_1/MouseEventListener.java
 * @author Stephan Kreutzer
 * @since 2017-07-13
 */



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;



class MouseEventListener extends MouseAdapter
{
    public MouseEventListener(tracking_text_editor_1 parent)
    {
        if (parent == null)
        {
            throw new NullPointerException();
        }

        this.parent = parent;
    }

    public void mousePressed(MouseEvent event)
    {

    }

    public void mouseReleased(MouseEvent event)
    {
        this.parent.caretMoved();
    }

    protected tracking_text_editor_1 parent = null;
}
