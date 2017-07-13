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
 * @file $/htx/gui/text_position_retriever/text_position_retriever_1/MouseEventListener.java
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
    public MouseEventListener(JTextArea textArea, JTextField positionField, String positionFormatString, String contextMenuCopyItemCaption)
    {
        if (textArea == null ||
            positionField == null ||
            contextMenuCopyItemCaption == null)
        {
            throw new NullPointerException();
        }

        this.textArea = textArea;
        this.positionField = positionField;

        if (positionFormatString != null)
        {
            this.positionFormatString = positionFormatString;
        }

        this.contextMenuCopy = new JPopupMenu();

        this.contextMenuCopyItem = new JMenuItem(contextMenuCopyItemCaption);
        this.contextMenuCopyItem.addMouseListener(this);
        this.contextMenuCopy.add(contextMenuCopyItem);
    }

    public void mousePressed(MouseEvent event)
    {
        Object source = event.getSource();

        if (source == this.textArea)
        {
            // Recover the caret in case the context menu was shown.
            this.textArea.getCaret().setVisible(true);
            this.textArea.getCaret().setSelectionVisible(true);
        }
        else if (source == this.positionField)
        {

        }
        else if (source == this.contextMenuCopyItem)
        {
            StringSelection stringSelection = new StringSelection(this.positionField.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
        else
        {
            throw new UnsupportedOperationException();
        }
    }

    public void mouseReleased(MouseEvent event)
    {
        Object source = event.getSource();

        if (source == this.textArea)
        {
            if (!SwingUtilities.isRightMouseButton(event))
            {
                int startPosition = textArea.getSelectionStart();
                int endPosition = textArea.getSelectionEnd();

                MessageFormat formatter = new MessageFormat("");
                formatter.applyPattern(this.positionFormatString);
                Object positions[] = { startPosition, endPosition - startPosition };
                positionField.setText(formatter.format(positions));

            }
            else
            {
                this.contextMenuCopy.show(event.getComponent(),
                                          event.getX(),
                                          event.getY()); 
            }
        }
        else if (source == this.positionField)
        {
            if (SwingUtilities.isRightMouseButton(event))
            {
                this.contextMenuCopy.show(event.getComponent(),
                                          event.getX(),
                                          event.getY()); 
            }
        }
        else if (source == this.contextMenuCopyItem)
        {
            event.consume();
        }
        else
        {
            throw new UnsupportedOperationException();
        }
    }

    protected JTextArea textArea = null;
    protected JTextField positionField = null;

    protected JPopupMenu contextMenuCopy = null;
    protected JMenuItem contextMenuCopyItem = null;

    protected String positionFormatString = "{0,number,#}, {1,number,#}";
}
