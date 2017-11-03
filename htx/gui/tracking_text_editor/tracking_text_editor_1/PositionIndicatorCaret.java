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
 * @file $/htx/gui/tracking_text_editor/tracking_text_editor_1/PositionIndicatorCaret.java
 * @brief Caret/Cursor that prevents double-click, selection of text ranges and reports
 *     any mouse movement. Also blocks keyboard input while mouse dragging.
 * @author Stephan Kreutzer
 * @since 2017-10-31
 */



import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;



public class PositionIndicatorCaret extends DefaultCaret implements KeyListener
{
    public PositionIndicatorCaret(tracking_text_editor_1 parent, JTextArea textArea)
    {
        if (parent == null ||
            textArea == null)
        {
            throw new NullPointerException();
        }

        this.parent = parent;
        this.textArea = textArea;
        this.mouseClicked = false;
        this.keyTyped = false;

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
        if (this.keyTyped == true)
        {
            event.consume();
            return;
        }

        this.mouseClicked = true;

        if (event.getClickCount() > 1)
        {
            event.consume();
        }

        super.mousePressed(event);
        this.parent.caretMoved();
    }

    public void mouseReleased(MouseEvent event)
    {
        if (this.keyTyped == true)
        {
            event.consume();
            return;
        }

        if (event.getClickCount() > 1)
        {
            event.consume();
        }

        super.mouseReleased(event);
        this.parent.caretMoved();

        this.mouseClicked = false;
    }

    public void mouseClicked(MouseEvent event)
    {
        if (this.keyTyped == true)
        {
            event.consume();
            return;
        }

        if (event.getClickCount() > 1)
        {
            event.consume();
        }

        super.mouseClicked(event);
    }

    public void mouseDragged(MouseEvent event)
    {
        if (this.keyTyped == true)
        {
            event.consume();
            return;
        }

        this.parent.caretMoved();
        super.mouseDragged(event);
        this.parent.caretMoved();
    }

    public void keyPressed(KeyEvent event)
    {
        if (this.mouseClicked == true)
        {
            event.consume();
            return;
        }

        this.keyTyped = true;

        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_DELETE)
        {
            this.textLengthLast = this.textArea.getText().length();
            return;
        }

        return;
    }

    public void keyReleased(KeyEvent event)
    {
        if (this.mouseClicked == true)
        {
            event.consume();
            return;
        }

        /** @todo For optimization of the output, if the user moves away from
          * current caret position, moves back to it and continues the same
          * edit operation, that could be continued as the same operation,
          * but as this probably doesn't happen too often and can be
          * consolidated later by optimizing the finished output file,
          * so this is left as a TODO. */

        int textLengthCurrent = this.textArea.getText().length();
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_UP ||
            keyCode == KeyEvent.VK_DOWN ||
            keyCode == KeyEvent.VK_LEFT ||
            keyCode == KeyEvent.VK_RIGHT ||
            keyCode == KeyEvent.VK_HOME ||
            keyCode == KeyEvent.VK_PAGE_UP ||
            keyCode == KeyEvent.VK_PAGE_DOWN ||
            keyCode == KeyEvent.VK_END)
        {
            this.parent.caretMoved();
            this.keyTyped = false;
            return;
        }
        else if (keyCode == KeyEvent.VK_DELETE)
        {
            this.parent.reverseDelete(this.textLengthLast - textLengthCurrent);
            this.textLengthLast = textLengthCurrent;
            this.keyTyped = false;
            return;
        }

        this.textLengthLast = textLengthCurrent;

        if (keyCode != 0)
        {
            this.parent.keyTyped();
        }

        this.keyTyped = false;

        return;
    }

    public void keyTyped(KeyEvent event)
    {
        if (this.mouseClicked == true)
        {
            event.consume();
            return;
        }

        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.VK_DELETE)
        {
            int textLengthCurrent = this.textArea.getText().length();

            this.parent.reverseDelete(this.textLengthLast - this.textArea.getText().length());
            this.textLengthLast = textLengthCurrent;
            return;
        }

        if (keyCode != 0)
        {
            this.parent.keyTyped();
        }

        return;
    }

    protected tracking_text_editor_1 parent = null;
    protected JTextArea textArea = null;
    protected long textLengthLast = -1;
    protected boolean mouseClicked = false;
    protected boolean keyTyped = false;
}