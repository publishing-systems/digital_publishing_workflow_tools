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
 * @file $/htx/gui/tracking_text_editor/tracking_text_editor_1/SingleClickCaret.java
 * @brief Caret/Cursor that prevents double-click.
 * @author Stephan Kreutzer
 * @since 2017-10-31
 */



import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;



public class SingleClickCaret extends DefaultCaret
{
    public SingleClickCaret(JTextArea textArea)
    {
        if (textArea == null)
        {
            throw new NullPointerException();
        }

        this.textArea = textArea;

        setBlinkRate(this.textArea.getCaret().getBlinkRate());
        setVisible(true);
        setSelectionVisible(false);
    }

    @Override
    public int getMark()
    {
        return getDot();
    }

    public void mousePressed(MouseEvent event)
    {
        if (event.getClickCount() > 1)
        {
            event.consume();
        }

        super.mousePressed(event);
    }

    public void mouseReleased(MouseEvent event)
    {
        if (event.getClickCount() > 1)
        {
            event.consume();
        }

        super.mouseReleased(event);
    }

    public void mouseClicked(MouseEvent event)
    {
        if (event.getClickCount() > 1)
        {
            event.consume();
        }

        super.mouseClicked(event);
    }

    protected JTextArea textArea = null;
}