/* Copyright (C) 2014-2017 Stephan Kreutzer
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
 * @file $/htx/gui/tracking_text_editor/tracking_text_editor_1/tracking_text_editor_1.java
 * @brief A simple text editor that tracks all changes.
 * @author Stephan Kreutzer
 * @since 2017-10-14
 */



import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.File;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import javax.swing.text.BadLocationException;



public class tracking_text_editor_1
  extends JFrame
{
    public static void main(String[] args)
    {
        System.out.print("tracking_text_editor_1 Copyright (C) 2014-2017 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");

        tracking_text_editor_1 editor = new tracking_text_editor_1();

        try
        {
            editor.edit(args);
        }
        catch (ProgramTerminationException ex)
        {
            editor.handleTermination(ex);
        }

        if (editor.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(editor.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by tracking_text_editor_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<tracking-text-editor-1-result-information>\n");
                writer.write("  <success>\n");

                if (editor.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = editor.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = editor.getInfoMessages().get(i);

                        writer.write("      <info-message number=\"" + i + "\">\n");
                        writer.write("        <timestamp>" + infoMessage.getTimestamp() + "</timestamp>\n");

                        String infoMessageText = infoMessage.getMessage();
                        String infoMessageId = infoMessage.getId();
                        String infoMessageBundle = infoMessage.getBundle();
                        Object[] infoMessageArguments = infoMessage.getArguments();

                        if (infoMessageBundle != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageBundle = infoMessageBundle.replaceAll("&", "&amp;");
                            infoMessageBundle = infoMessageBundle.replaceAll("<", "&lt;");
                            infoMessageBundle = infoMessageBundle.replaceAll(">", "&gt;");

                            writer.write("        <id-bundle>" + infoMessageBundle + "</id-bundle>\n");
                        }

                        if (infoMessageId != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageId = infoMessageId.replaceAll("&", "&amp;");
                            infoMessageId = infoMessageId.replaceAll("<", "&lt;");
                            infoMessageId = infoMessageId.replaceAll(">", "&gt;");

                            writer.write("        <id>" + infoMessageId + "</id>\n");
                        }

                        if (infoMessageText != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageText = infoMessageText.replaceAll("&", "&amp;");
                            infoMessageText = infoMessageText.replaceAll("<", "&lt;");
                            infoMessageText = infoMessageText.replaceAll(">", "&gt;");

                            writer.write("        <message>" + infoMessageText + "</message>\n");
                        }

                        if (infoMessageArguments != null)
                        {
                            writer.write("        <arguments>\n");

                            int argumentCount = infoMessageArguments.length;

                            for (int j = 0; j < argumentCount; j++)
                            {
                                if (infoMessageArguments[j] == null)
                                {
                                    writer.write("          <argument number=\"" + j + "\">\n");
                                    writer.write("            <class></class>\n");
                                    writer.write("            <value>null</value>\n");
                                    writer.write("          </argument>\n");

                                    continue;
                                }

                                String className = infoMessageArguments[j].getClass().getName();

                                // Ampersand needs to be the first, otherwise it would double-encode
                                // other entities.
                                className = className.replaceAll("&", "&amp;");
                                className = className.replaceAll("<", "&lt;");
                                className = className.replaceAll(">", "&gt;");

                                String value = infoMessageArguments[j].toString();

                                // Ampersand needs to be the first, otherwise it would double-encode
                                // other entities.
                                value = value.replaceAll("&", "&amp;");
                                value = value.replaceAll("<", "&lt;");
                                value = value.replaceAll(">", "&gt;");

                                writer.write("          <argument number=\"" + j + "\">\n");
                                writer.write("            <class>" + className + "</class>\n");
                                writer.write("            <value>" + value + "</value>\n");
                                writer.write("          </argument>\n");
                            }

                            writer.write("        </arguments>\n");
                        }

                        Exception exception = infoMessage.getException();

                        if (exception != null)
                        {
                            writer.write("        <exception>\n");

                            String className = exception.getClass().getName();

                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            className = className.replaceAll("&", "&amp;");
                            className = className.replaceAll("<", "&lt;");
                            className = className.replaceAll(">", "&gt;");

                            writer.write("          <class>" + className + "</class>\n");

                            StringWriter stringWriter = new StringWriter();
                            PrintWriter printWriter = new PrintWriter(stringWriter);
                            exception.printStackTrace(printWriter);
                            String stackTrace = stringWriter.toString();

                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            stackTrace = stackTrace.replaceAll("&", "&amp;");
                            stackTrace = stackTrace.replaceAll("<", "&lt;");
                            stackTrace = stackTrace.replaceAll(">", "&gt;");

                            writer.write("          <stack-trace>" + stackTrace + "</stack-trace>\n");
                            writer.write("        </exception>\n");
                        }

                        writer.write("      </info-message>\n");
                    }

                    writer.write("    </info-messages>\n");
                }

                writer.write("  </success>\n");
                writer.write("</tracking-text-editor-1-result-information>\n");
                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                ex.printStackTrace();
                System.exit(-1);
            }
            catch (UnsupportedEncodingException ex)
            {
                ex.printStackTrace();
                System.exit(-1);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
    }

    public tracking_text_editor_1()
    {
        super("tracking_text_editor_1");

        addWindowListener(new FlushingWindowAdapter(this));
    }

    public int edit(String[] args)
    {
        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\ttracking_text_editor_1 " + getI10nString("messageParameterList") + "\n");
        }

        File resultInfoFile = new File(args[1]);

        try
        {
            resultInfoFile = resultInfoFile.getCanonicalFile();
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageResultInfoFileCantGetCanonicalPath", ex, null, resultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageResultInfoFileCantGetCanonicalPath", ex, null, resultInfoFile.getAbsolutePath());
        }

        if (resultInfoFile.exists() == true)
        {
            if (resultInfoFile.isFile() == true)
            {
                if (resultInfoFile.canWrite() != true)
                {
                    throw constructTermination("messageResultInfoFileIsntWritable", null, null, resultInfoFile.getAbsolutePath());
                }
            }
            else
            {
                throw constructTermination("messageResultInfoPathIsntAFile", null, null, resultInfoFile.getAbsolutePath());
            }
        }

        tracking_text_editor_1.resultInfoFile = resultInfoFile;
        File jobFile = new File(args[0]);

        try
        {
            jobFile = jobFile.getCanonicalFile();
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageJobFileCantGetCanonicalPath", ex, null, jobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageJobFileCantGetCanonicalPath", ex, null, jobFile.getAbsolutePath());
        }

        if (jobFile.exists() != true)
        {
            throw constructTermination("messageJobFileDoesntExist", null, null, jobFile.getAbsolutePath());
        }

        if (jobFile.isFile() != true)
        {
            throw constructTermination("messageJobPathIsntAFile", null, null, jobFile.getAbsolutePath());
        }

        if (jobFile.canRead() != true)
        {
            throw constructTermination("messageJobFileIsntReadable", null, null, jobFile.getAbsolutePath());
        }

        System.out.println("tracking_text_editor_1: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));

        File inputFile = null;
        this.outputFile = null;
        int fontSize = 16;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(jobFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equals("input-file") == true)
                    {
                        if (inputFile != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        Attribute attributePath = event.asStartElement().getAttributeByName(new QName("path"));

                        if (attributePath == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        inputFile = new File(attributePath.getValue());

                        if (inputFile.isAbsolute() != true)
                        {
                            inputFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + attributePath.getValue());
                        }

                        if (inputFile.exists() != true)
                        {
                            throw constructTermination("messageJobFileInputFileDoesntExist", null, null, jobFile.getAbsolutePath(), inputFile.getAbsolutePath());
                        }

                        if (inputFile.isFile() != true)
                        {
                            throw constructTermination("messageJobFileInputPathIsntAFile", null, null, jobFile.getAbsolutePath(), inputFile.getAbsolutePath());
                        }

                        if (inputFile.canRead() != true)
                        {
                            throw constructTermination("messageJobFileInputFileIsntReadable", null, null, jobFile.getAbsolutePath(), inputFile.getAbsolutePath());
                        }
                    }
                    else if (tagName.equals("output-file") == true)
                    {
                        if (this.outputFile != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        Attribute attributePath = event.asStartElement().getAttributeByName(new QName("path"));

                        if (attributePath == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        this.outputFile = new File(attributePath.getValue());

                        if (this.outputFile.isAbsolute() != true)
                        {
                            this.outputFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + attributePath.getValue());
                        }

                        if (this.outputFile.exists() == true)
                        {
                            throw constructTermination("messageJobFileOutputFileExistsAlready", null, null, jobFile.getAbsolutePath(), this.outputFile.getAbsolutePath());
                        }
                    }
                    else if (tagName.equals("plaintext-file") == true)
                    {
                        if (this.plaintextFile != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        Attribute attributePath = event.asStartElement().getAttributeByName(new QName("path"));

                        if (attributePath == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        this.plaintextFile = new File(attributePath.getValue());

                        if (this.plaintextFile.isAbsolute() != true)
                        {
                            this.plaintextFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + attributePath.getValue());
                        }

                        if (this.plaintextFile.exists() == true)
                        {
                            throw constructTermination("messageJobFilePlaintextFileExistsAlready", null, null, jobFile.getAbsolutePath(), this.plaintextFile.getAbsolutePath());
                        }
                    }
                    else if (tagName.equals("font-size") == true)
                    {
                        Attribute attributePoint = event.asStartElement().getAttributeByName(new QName("point"));

                        if (attributePoint == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "point");
                        }

                        fontSize = Integer.parseInt(attributePoint.getValue());
                    }
                    else if (tagName.equals("autosave") == true)
                    {
                        Attribute attributeCharacters = event.asStartElement().getAttributeByName(new QName("characters"));

                        if (attributeCharacters == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "characters");
                        }

                        this.autosaveCharacters = Integer.parseInt(attributeCharacters.getValue());
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageJobFileErrorWhileReading", ex, null, jobFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageJobFileErrorWhileReading", ex, null, jobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageJobFileErrorWhileReading", ex, null, jobFile.getAbsolutePath());
        }

        if (inputFile == null)
        {
            throw constructTermination("messageJobFileInputFileIsntConfigured", null, null, jobFile.getAbsolutePath());
        }

        if (this.outputFile == null)
        {
            throw constructTermination("messageJobFileOutputFileIsntConfigured", null, null, this.outputFile.getAbsolutePath());
        }

        StringBuilder inputText = new StringBuilder();

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));

            while (true)
            {
                int character = reader.read();

                if (character >= 0)
                {
                    boolean isUTF16 = false;

                    if (Character.isHighSurrogate((char)character) == true)
                    {
                        int character2 = reader.read();

                        if (character2 == 0)
                        {
                            throw constructTermination("messageInputFileSurrogateAborted", null, null, (char)character, String.format("0x%X", (int)character));
                        }

                        if (Character.isLowSurrogate((char)character2) != true)
                        {
                            throw constructTermination("messageInputFileSurrogateIncomplete", null, null, (char)character2, String.format("0x%X", (int)character2));
                        }

                        character = character * 0x10000;
                        character += character2;

                        isUTF16 = true;
                    }

                    if (isUTF16 == false)
                    {
                        inputText.append((char)character);
                    }
                    else
                    {
                        byte[] codePoints = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(character).array();
                        inputText.append(new String(codePoints, "UTF-16"));
                    }
                }
                else
                {
                    break;
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageErrorWhileReadingInputFile", ex, null, inputFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageErrorWhileReadingInputFile", ex, null, inputFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageErrorWhileReadingInputFile", ex, null, inputFile.getAbsolutePath());
        }

        try
        {
            this.outputWriter = new BufferedWriter(
                                new OutputStreamWriter(
                                new FileOutputStream(this.outputFile),
                                "UTF-8"));

            this.outputWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            this.outputWriter.append("<!-- This file was created by tracking_text_editor_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
            this.outputWriter.append("<tracking-text-editor-1-text-history version=\"0.1.0\">\n");
            this.outputWriter.flush();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile.getAbsolutePath());
        }


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));

        GridBagLayout gridbag = new GridBagLayout();
        mainPanel.setLayout(gridbag);

        GridBagConstraints gridbagConstraints = new GridBagConstraints();
        gridbagConstraints.anchor = GridBagConstraints.NORTH;
        gridbagConstraints.gridy = 0;
        gridbagConstraints.weightx = 1.0;
        gridbagConstraints.weighty = 1.0;
        gridbagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridbagConstraints.fill = GridBagConstraints.BOTH;

        this.textArea = new JTextArea(inputText.toString());
        JTextField positionField = new JTextField();

        PositionIndicatorCaret caret = new PositionIndicatorCaret(this, this.textArea);

        this.textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        this.textArea.setEditable(true);
        this.textArea.setHighlighter(null);
        this.textArea.setCaret(caret);
        this.textArea.addKeyListener(caret);
        this.textArea.setFocusable(true);

        JScrollPane scrollPane = new JScrollPane(this.textArea);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(scrollPane, gridbagConstraints);

        positionField.setText(getI10nString("windowStatusInfoStart"));
        positionField.setEditable(false);

        gridbagConstraints = new GridBagConstraints();
        gridbagConstraints.anchor = GridBagConstraints.SOUTH;
        gridbagConstraints.gridy = 1;
        gridbagConstraints.weightx = 1.0;
        gridbagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridbagConstraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(positionField, gridbagConstraints);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        setLocation(100, 100);
        setSize(500, 400);
        setVisible(true);

        this.positionLast = this.textArea.getCaretPosition();
        this.positionOperationStart = -1;
        this.editMode = EDITMODE_NONE;

        return 0;
    }

    public void caretMoved()
    {
        if (this.textArea.getSelectionStart() != this.textArea.getSelectionEnd())
        {
            // Removes even the hidden selection (important because VK_DELETE could
            // be executed on it).
            /** @todo Allow editing with selected ranges. */
            this.textArea.setCaretPosition(this.textArea.getCaretPosition());
        }

        /** @todo For optimization of the output, if the user clicks away from
          * current caret position, clicks back to it and continues the same
          * edit operation, that could be continued as the same operation,
          * but as this probably doesn't happen too often and can be
          * consolidated later by optimizing the finished output file,
          * so this is left as a TODO. */

        if (this.editMode == EDITMODE_ADD)
        {
            try
            {
                this.outputWriter.append("<add position=\"" + this.positionOperationStart + "\">");

                String text = this.textArea.getText(this.positionOperationStart, this.positionLast - this.positionOperationStart);
                // Ampersand needs to be the first, otherwise it would double-encode
                // other entities.
                text = text.replaceAll("&", "&amp;");
                text = text.replaceAll("<", "&lt;");
                text = text.replaceAll(">", "&gt;");

                this.outputWriter.append(text + "</add>\n");
                this.outputWriter.flush();
            }
            catch (IOException ex)
            {
                throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile);
            }
            catch (BadLocationException ex)
            {
                throw constructTermination("messageUnableToObtainPortionOfTextFromTextArea", ex, null);
            }
        }
        else if (this.editMode == EDITMODE_DELETE)
        {
            try
            {
                this.outputWriter.append("<delete position=\"" + this.positionOperationStart + "\" count=\"" + (this.positionLast - this.positionOperationStart) + "\" />\n");
                this.outputWriter.flush();
            }
            catch (IOException ex)
            {
                throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile);
            }
        }

        this.positionLast = this.textArea.getCaretPosition();
        this.positionOperationStart = -1;
        this.editMode = EDITMODE_NONE;
    }

    public void keyTyped()
    {
        int positionCurrent = this.textArea.getCaretPosition();

        if (this.positionLast < positionCurrent)
        {
            try
            {
                this.lastCharacterAdded = Character.codePointAt(this.textArea.getText(this.positionLast, positionCurrent - this.positionLast).toCharArray(), 0);
            }
            catch (BadLocationException ex)
            {
                throw constructTermination("messageUnableToObtainPortionOfTextFromTextArea", ex, null);
            }

            if (this.editMode == EDITMODE_NONE)
            {
                this.positionOperationStart = this.positionLast;
            }
            else if (this.editMode == EDITMODE_ADD)
            {

            }
            else if (this.editMode == EDITMODE_DELETE)
            {
                try
                {
                    this.outputWriter.append("<delete position=\"" + this.positionOperationStart + "\" count=\"" + (this.positionLast - this.positionOperationStart) + "\" />\n");
                    this.outputWriter.flush();
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile);
                }

                this.positionOperationStart = this.positionLast;
            }
            else
            {
                throw new UnsupportedOperationException();
            }

            if (this.autosaveCharacters > 0 &&
                (this.positionLast - this.positionOperationStart) >= this.autosaveCharacters)
            {
                try
                {
                    this.outputWriter.append("<add position=\"" + this.positionOperationStart + "\">");

                    String text = this.textArea.getText(this.positionOperationStart, this.positionLast - this.positionOperationStart);

                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    text = text.replaceAll("&", "&amp;");
                    text = text.replaceAll("<", "&lt;");
                    text = text.replaceAll(">", "&gt;");

                    this.outputWriter.append(text + "</add>\n");
                    this.outputWriter.flush();
                }
                catch (BadLocationException ex)
                {
                    throw constructTermination("messageUnableToObtainPortionOfTextFromTextArea", ex, null);
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile);
                }

                this.positionOperationStart = this.positionLast;
            }

            this.editMode = EDITMODE_ADD;
        }
        else if (this.positionLast > positionCurrent)
        {
            if (this.editMode == EDITMODE_NONE)
            {
                this.positionOperationStart = this.positionLast;
            }
            else if (this.editMode == EDITMODE_DELETE)
            {

            }
            else if (this.editMode == EDITMODE_ADD)
            {
                try
                {
                    this.outputWriter.append("<add position=\"" + this.positionOperationStart + "\">");

                    String text = this.textArea.getText(this.positionOperationStart, positionCurrent - this.positionOperationStart);

                    if (this.lastCharacterAdded != null)
                    {
                        text += new String(Character.toChars(this.lastCharacterAdded));
                    }

                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    text = text.replaceAll("&", "&amp;");
                    text = text.replaceAll("<", "&lt;");
                    text = text.replaceAll(">", "&gt;");

                    this.outputWriter.append(text + "</add>\n");
                    this.outputWriter.flush();
                }
                catch (BadLocationException ex)
                {
                    throw constructTermination("messageUnableToObtainPortionOfTextFromTextArea", ex, null);
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile);
                }

                this.positionOperationStart = this.positionLast;
            }
            else
            {
                throw new UnsupportedOperationException();
            }

            if (this.autosaveCharacters > 0 &&
                (this.positionOperationStart - this.positionLast) >= this.autosaveCharacters)
            {
                try
                {
                    this.outputWriter.append("<delete position=\"" + this.positionOperationStart + "\" count=\"" + (this.positionLast - this.positionOperationStart) + "\" />\n");
                    this.outputWriter.flush();
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile);
                }

                this.positionOperationStart = this.positionLast;
            }

            this.lastCharacterAdded = null;
            this.editMode = EDITMODE_DELETE;
        }

        this.positionLast = positionCurrent;
    }

    public void reverseDelete(long charactersDeleted)
    {
        int positionCurrent = this.textArea.getCaretPosition();

        if (charactersDeleted > 0)
        {
            if (this.editMode == EDITMODE_NONE)
            {
                this.positionOperationStart = positionCurrent + (int)charactersDeleted;
                this.positionLast = positionCurrent;
            }
            else if (this.editMode == EDITMODE_DELETE)
            {
                this.positionLast = positionCurrent;
                this.positionOperationStart += charactersDeleted;
            }
            else if (this.editMode == EDITMODE_ADD)
            {
                try
                {
                    this.outputWriter.append("<add position=\"" + this.positionOperationStart + "\">");

                    String text = this.textArea.getText(this.positionOperationStart, positionCurrent - this.positionOperationStart);

                    if (this.lastCharacterAdded != null)
                    {
                        text += new String(Character.toChars(this.lastCharacterAdded));
                    }

                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    text = text.replaceAll("&", "&amp;");
                    text = text.replaceAll("<", "&lt;");
                    text = text.replaceAll(">", "&gt;");

                    this.outputWriter.append(text + "</add>\n");
                    this.outputWriter.flush();
                }
                catch (BadLocationException ex)
                {
                    throw constructTermination("messageUnableToObtainPortionOfTextFromTextArea", ex, null);
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile);
                }

                this.positionOperationStart = this.positionLast;
            }
            else
            {
                throw new UnsupportedOperationException();
            }

            if (this.autosaveCharacters > 0 &&
                (this.positionOperationStart - this.positionLast) >= this.autosaveCharacters)
            {
                try
                {
                    this.outputWriter.append("<delete position=\"" + this.positionOperationStart + "\" count=\"" + (this.positionLast - this.positionOperationStart) + "\" />\n");
                    this.outputWriter.flush();
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile);
                }

                this.positionOperationStart = positionCurrent;
                charactersDeleted = 0;
            }

            this.lastCharacterAdded = null;
            this.editMode = EDITMODE_DELETE;
        }
    }

    public void closeOutput()
    {
        if (this.editMode == EDITMODE_ADD &&
            (this.positionLast - this.positionOperationStart) > 0)
        {
            try
            {
                this.outputWriter.append("<add position=\"" + this.positionOperationStart + "\">");

                String text = this.textArea.getText(this.positionOperationStart, this.positionLast - this.positionOperationStart);
                // Ampersand needs to be the first, otherwise it would double-encode
                // other entities.
                text = text.replaceAll("&", "&amp;");
                text = text.replaceAll("<", "&lt;");
                text = text.replaceAll(">", "&gt;");

                this.outputWriter.append(text + "</add>\n");
            }
            catch (IOException ex)
            {
                throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile);
            }
            catch (BadLocationException ex)
            {
                throw constructTermination("messageUnableToObtainPortionOfTextFromTextArea", ex, null);
            }
        }
        else if (this.editMode == EDITMODE_DELETE &&
                 (this.positionOperationStart - this.positionLast) > 0)
        {
            try
            {
                this.outputWriter.append("<delete position=\"" + this.positionOperationStart + "\" count=\"" + (this.positionLast - this.positionOperationStart) + "\" />\n");
            }
            catch (IOException ex)
            {
                throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile);
            }
        }

        try
        {
            this.outputWriter.append("</tracking-text-editor-1-text-history>\n");
            this.outputWriter.flush();
            this.outputWriter.close();
        }
        catch (IOException ex)
        {
            throw constructTermination("messageErrorWhileWritingOutputFile", ex, null, this.outputFile);
        }

        if (this.plaintextFile != null)
        {
            try
            {
                BufferedWriter plaintextOutputWriter = new BufferedWriter(
                                                       new OutputStreamWriter(
                                                       new FileOutputStream(this.plaintextFile),
                                                       "UTF-8"));

                plaintextOutputWriter.append(this.textArea.getText());
                plaintextOutputWriter.flush();
                plaintextOutputWriter.close();
            }
            catch (FileNotFoundException ex)
            {
                throw constructTermination("messageErrorWhileWritingPlaintextFile", ex, null, this.plaintextFile.getAbsolutePath());
            }
            catch (UnsupportedEncodingException ex)
            {
                throw constructTermination("messageErrorWhileWritingPlaintextFile", ex, null, this.plaintextFile.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageErrorWhileWritingPlaintextFile", ex, null, this.plaintextFile.getAbsolutePath());
            }
        }
    }

    public InfoMessage constructInfoMessage(String id,
                                            boolean outputToConsole,
                                            Exception exception,
                                            String message,
                                            Object ... arguments)
    {
        if (message == null)
        {
            if (arguments == null)
            {
                message = "tracking_text_editor_1: " + getI10nString(id);
            }
            else
            {
                message = "tracking_text_editor_1: " + getI10nStringFormatted(id, arguments);
            }
        }

        if (outputToConsole == true)
        {
            System.out.println(message);

            if (exception != null)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        return new InfoMessage(id, exception, message, L10N_BUNDLE, arguments);
    }

    public ProgramTerminationException constructTermination(String id, Exception cause, String message, Object ... arguments)
    {
        if (message == null)
        {
            if (arguments == null)
            {
                message = "tracking_text_editor_1: " + getI10nString(id);
            }
            else
            {
                message = "tracking_text_editor_1: " + getI10nStringFormatted(id, arguments);
            }
        }

        return new ProgramTerminationException(id, cause, message, L10N_BUNDLE, arguments);
    }

    public int handleTermination(ProgramTerminationException ex)
    {
        String message = ex.getMessage();
        String id = ex.getId();
        String bundle = ex.getBundle();
        Object[] arguments = ex.getArguments();
        boolean normalTermination = ex.isNormalTermination();

        if (message != null)
        {
            System.err.println(message);
        }

        Throwable innerException = ex.getCause();

        if (innerException != null)
        {
            System.out.println(innerException.getMessage());
            innerException.printStackTrace();
        }

        if (tracking_text_editor_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(tracking_text_editor_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by tracking_text_editor_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<tracking-text-editor-1-result-information>\n");

                if (normalTermination == false)
                {
                    writer.write("  <failure>\n");
                }
                else
                {
                    writer.write("  <success>\n");
                }

                writer.write("    <timestamp>" + ex.getTimestamp() + "</timestamp>\n");

                if (bundle != null)
                {
                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    bundle = bundle.replaceAll("&", "&amp;");
                    bundle = bundle.replaceAll("<", "&lt;");
                    bundle = bundle.replaceAll(">", "&gt;");

                    writer.write("    <id-bundle>" + bundle + "</id-bundle>\n");
                }

                if (id != null)
                {
                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    id = id.replaceAll("&", "&amp;");
                    id = id.replaceAll("<", "&lt;");
                    id = id.replaceAll(">", "&gt;");

                    writer.write("    <id>" + id + "</id>\n");
                }

                if (message != null)
                {
                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    message = message.replaceAll("&", "&amp;");
                    message = message.replaceAll("<", "&lt;");
                    message = message.replaceAll(">", "&gt;");

                    writer.write("    <message>" + message + "</message>\n");
                }

                if (arguments != null)
                {
                    writer.write("    <arguments>\n");

                    int argumentCount = arguments.length;

                    for (int i = 0; i < argumentCount; i++)
                    {
                        if (arguments[i] == null)
                        {
                            writer.write("      <argument number=\"" + i + "\">\n");
                            writer.write("        <class></class>\n");
                            writer.write("        <value>null</value>\n");
                            writer.write("      </argument>\n");

                            continue;
                        }

                        String className = arguments[i].getClass().getName();

                        // Ampersand needs to be the first, otherwise it would double-encode
                        // other entities.
                        className = className.replaceAll("&", "&amp;");
                        className = className.replaceAll("<", "&lt;");
                        className = className.replaceAll(">", "&gt;");

                        String value = arguments[i].toString();

                        // Ampersand needs to be the first, otherwise it would double-encode
                        // other entities.
                        value = value.replaceAll("&", "&amp;");
                        value = value.replaceAll("<", "&lt;");
                        value = value.replaceAll(">", "&gt;");

                        writer.write("      <argument number=\"" + i + "\">\n");
                        writer.write("        <class>" + className + "</class>\n");
                        writer.write("        <value>" + value + "</value>\n");
                        writer.write("      </argument>\n");
                    }

                    writer.write("    </arguments>\n");
                }

                if (innerException != null)
                {
                    writer.write("    <exception>\n");

                    String className = innerException.getClass().getName();

                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    className = className.replaceAll("&", "&amp;");
                    className = className.replaceAll("<", "&lt;");
                    className = className.replaceAll(">", "&gt;");

                    writer.write("      <class>" + className + "</class>\n");

                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    innerException.printStackTrace(printWriter);
                    String stackTrace = stringWriter.toString();

                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    stackTrace = stackTrace.replaceAll("&", "&amp;");
                    stackTrace = stackTrace.replaceAll("<", "&lt;");
                    stackTrace = stackTrace.replaceAll(">", "&gt;");

                    writer.write("      <stack-trace>" + stackTrace + "</stack-trace>\n");
                    writer.write("    </exception>\n");
                }

                if (this.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = this.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = this.getInfoMessages().get(i);

                        writer.write("      <info-message>\n");
                        writer.write("        <timestamp>" + infoMessage.getTimestamp() + "</timestamp>\n");

                        String infoMessageText = infoMessage.getMessage();
                        String infoMessageId = ex.getId();
                        String infoMessageBundle = ex.getBundle();
                        Object[] infoMessageArguments = ex.getArguments();

                        if (infoMessageBundle != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageBundle = infoMessageBundle.replaceAll("&", "&amp;");
                            infoMessageBundle = infoMessageBundle.replaceAll("<", "&lt;");
                            infoMessageBundle = infoMessageBundle.replaceAll(">", "&gt;");

                            writer.write("        <id-bundle>" + infoMessageBundle + "</id-bundle>\n");
                        }

                        if (infoMessageId != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageId = infoMessageId.replaceAll("&", "&amp;");
                            infoMessageId = infoMessageId.replaceAll("<", "&lt;");
                            infoMessageId = infoMessageId.replaceAll(">", "&gt;");

                            writer.write("        <id>" + infoMessageId + "</id>\n");
                        }

                        if (infoMessageText != null)
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            infoMessageText = infoMessageText.replaceAll("&", "&amp;");
                            infoMessageText = infoMessageText.replaceAll("<", "&lt;");
                            infoMessageText = infoMessageText.replaceAll(">", "&gt;");

                            writer.write("        <message>" + infoMessageText + "</message>\n");
                        }

                        if (infoMessageArguments != null)
                        {
                            writer.write("        <arguments>\n");

                            int argumentCount = infoMessageArguments.length;

                            for (int j = 0; j < argumentCount; j++)
                            {
                                if (infoMessageArguments[j] == null)
                                {
                                    writer.write("          <argument number=\"" + j + "\">\n");
                                    writer.write("            <class></class>\n");
                                    writer.write("            <value>null</value>\n");
                                    writer.write("          </argument>\n");

                                    continue;
                                }

                                String className = infoMessageArguments[j].getClass().getName();

                                // Ampersand needs to be the first, otherwise it would double-encode
                                // other entities.
                                className = className.replaceAll("&", "&amp;");
                                className = className.replaceAll("<", "&lt;");
                                className = className.replaceAll(">", "&gt;");

                                String value = infoMessageArguments[j].toString();

                                // Ampersand needs to be the first, otherwise it would double-encode
                                // other entities.
                                value = value.replaceAll("&", "&amp;");
                                value = value.replaceAll("<", "&lt;");
                                value = value.replaceAll(">", "&gt;");

                                writer.write("          <argument number=\"" + j + "\">\n");
                                writer.write("            <class>" + className + "</class>\n");
                                writer.write("            <value>" + value + "</value>\n");
                                writer.write("          </argument>\n");
                            }

                            writer.write("        </arguments>\n");
                        }

                        writer.write("      </info-message>\n");
                    }

                    writer.write("    </info-messages>\n");
                }

                if (normalTermination == false)
                {
                    writer.write("  </failure>\n");
                }
                else
                {
                    writer.write("  </success>\n");
                }

                writer.write("</tracking-text-editor-1-result-information>\n");
                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex2)
            {
                ex2.printStackTrace();
            }
            catch (UnsupportedEncodingException ex2)
            {
                ex2.printStackTrace();
            }
            catch (IOException ex2)
            {
                ex2.printStackTrace();
            }
        }

        tracking_text_editor_1.resultInfoFile = null;

        System.exit(-1);
        return -1;
    }

    public List<InfoMessage> getInfoMessages()
    {
        return this.infoMessages;
    }

    public Locale getLocale()
    {
        return Locale.getDefault();
    }

    /**
     * @brief This method interprets l10n strings from a .properties file as encoded in UTF-8.
     */
    private String getI10nString(String key)
    {
        if (this.l10n == null)
        {
            this.l10n = ResourceBundle.getBundle(L10N_BUNDLE, this.getLocale());
        }

        try
        {
            return new String(this.l10n.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            return this.l10n.getString(key);
        }
    }

    private String getI10nStringFormatted(String i10nStringName, Object ... arguments)
    {
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(this.getLocale());

        formatter.applyPattern(getI10nString(i10nStringName));
        return formatter.format(arguments);
    }

    public final int EDITMODE_NONE = 0;
    public final int EDITMODE_ADD = 1;
    public final int EDITMODE_DELETE = 2;

    protected JTextArea textArea = null;
    protected int positionLast = -1;
    protected int positionOperationStart = -1;
    protected int editMode = EDITMODE_NONE;
    protected Integer lastCharacterAdded = null;

    protected File outputFile = null;
    protected File plaintextFile = null;
    protected BufferedWriter outputWriter = null;
    protected int autosaveCharacters = -1;

    public static File resultInfoFile = null;
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nTrackingTextEditor1";
    private ResourceBundle l10n;
}
