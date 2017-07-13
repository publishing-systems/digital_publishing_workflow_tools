/* Copyright (C) 2017 Stephan Kreutzer
 *
 * This file is part of text_position_retriever_1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * text_position_retriever_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * text_position_retriever_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with text_position_retriever_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/htx/gui/text_position_retriever/text_position_retriever_1/KeyEventListener.java
 * @author Stephan Kreutzer
 * @since 2017-07-13
 */



import javax.swing.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.*;



class KeyEventListener implements KeyListener
{
    public KeyEventListener(JTextField positionField)
    {
        if (positionField == null)
        {
            throw new NullPointerException();
        }

        this.positionField = positionField;
    }

    public void keyPressed(KeyEvent event)
    {
        // Otherwise default Ctrl + C could win over the keyReleased() event.
        event.consume();
    }

    public void keyReleased(KeyEvent event)
    {
        if (event.getKeyCode() == KeyEvent.VK_C && ((event.getModifiers() & KeyEvent.CTRL_MASK) != 0))
        {
            StringSelection stringSelection = new StringSelection(this.positionField.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }

    public void keyTyped(KeyEvent event)
    {

    }

    protected JTextField positionField = null;
}
