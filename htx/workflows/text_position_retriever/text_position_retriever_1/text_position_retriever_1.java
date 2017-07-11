/* Copyright (C) 2016-2017  Stephan Kreutzer
 *
 * This file is part of text_position_retriever_1 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * text_position_retriever_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * text_position_retriever_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with text_position_retriever_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/htx/workflows/text_position_retriever/text_position_retriever_1/text_position_retriever_1.java
 * @brief Select the input file first, then call text_position_retriever_1.
 * @author Stephan Kreutzer
 * @since 2017-07-11
 */



import java.util.List;
import java.util.ArrayList;
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
import java.net.URLDecoder;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.util.Scanner;



public class text_position_retriever_1
{
    public static void main(String[] args)
    {
        System.out.print("text_position_retriever_1 workflow Copyright (C) 2016-2017 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");

        text_position_retriever_1 retriever = new text_position_retriever_1();

        try
        {
            retriever.retrieve(args);
        }
        catch (ProgramTerminationException ex)
        {
            retriever.handleTermination(ex);
        }

        if (retriever.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(retriever.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by text_position_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<text-position-retriever-1-workflow-result-information>\n");
                writer.write("  <success>\n");

                if (retriever.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = retriever.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = retriever.getInfoMessages().get(i);

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
                writer.write("</text-position-retriever-1-workflow-result-information>\n");
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

    public int retrieve(String[] args)
    {
        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\ttext_position_retriever_1 " + getI10nString("messageParameterList") + "\n");
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

        text_position_retriever_1.resultInfoFile = resultInfoFile;


        String programPath = text_position_retriever_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        try
        {
            programPath = new File(programPath).getCanonicalPath() + File.separator;
            programPath = URLDecoder.decode(programPath, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageCantDetermineProgramPath", ex, null);
        }
        catch (IOException ex)
        {
            throw constructTermination("messageCantDetermineProgramPath", ex, null);
        }

        File tempDirectory = new File(programPath + "temp");

        if (tempDirectory.exists() == true)
        {
            if (tempDirectory.isDirectory() == true)
            {
                if (tempDirectory.canWrite() != true)
                {
                    throw constructTermination("messageTempDirectoryIsntWritable", null, null, tempDirectory.getAbsolutePath());
                }
            }
            else
            {
                throw constructTermination("messageTempPathIsntADirectory", null, null, tempDirectory.getAbsolutePath());
            }
        }
        else
        {
            try
            {
                tempDirectory.mkdirs();
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageTempDirectoryCantCreate", null, null, tempDirectory.getAbsolutePath());
            }
        }

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

        System.out.println("text_position_retriever_1 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        File startDirectory = null;
        Map<String, String> fileExtensions = new LinkedHashMap<String, String>();
        Integer fontSize = null;
        String positionFormatString = null;

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

                    if (tagName.equals("start-directory") == true)
                    {
                        Attribute pathAttribute = event.asStartElement().getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        if (startDirectory != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        startDirectory = new File(pathAttribute.getValue());

                        if (startDirectory.isAbsolute() != true)
                        {
                            startDirectory = new File(jobFile.getAbsoluteFile().getParent() + File.separator + pathAttribute.getValue());
                        }

                        try
                        {
                            startDirectory = startDirectory.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageStartDirectoryCantGetCanonicalPath", ex, null, startDirectory.getAbsolutePath(), jobFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageStartDirectoryCantGetCanonicalPath", ex, null, startDirectory.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (startDirectory.exists() != true)
                        {
                            throw constructTermination("messageStartDirectoryDoesntExist", null, null, startDirectory.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (startDirectory.isDirectory() != true)
                        {
                            throw constructTermination("messageStartPathIsntADirectory", null, null, startDirectory.getAbsolutePath(), jobFile.getAbsolutePath());
                        }
                    }
                    else if (tagName.equals("file-type") == true)
                    {
                        Attribute attributeExtension = event.asStartElement().getAttributeByName(new QName("extension"));

                        if (attributeExtension == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "extension");
                        }

                        String extension = attributeExtension.getValue();
                        String extensionDisplayName = new String();

                        while (eventReader.hasNext() == true)
                        {
                            event = eventReader.nextEvent();

                            if (event.isCharacters() == true)
                            {
                                extensionDisplayName += event.asCharacters();
                            }
                            else if (event.isEndElement() == true)
                            {
                                QName elementEndName = event.asEndElement().getName();
                                String elementEndNameString = elementEndName.getLocalPart();

                                if (elementEndNameString.equalsIgnoreCase("file-type") == true)
                                {
                                    break;
                                }
                            }
                        }

                        if (fileExtensions.containsKey(extension) != true)
                        {
                            fileExtensions.put(extension, extensionDisplayName);
                        }
                        else
                        {
                            throw constructTermination("messageJobFileExtensionSpecifiedMoreThanOnce", null, null, jobFile.getAbsolutePath(), extension);                       
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
                    else if (tagName.equals("position-format-string") == true)
                    {
                        Attribute attributeFormat = event.asStartElement().getAttributeByName(new QName("format"));

                        if (attributeFormat == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "format");
                        }

                        positionFormatString = attributeFormat.getValue();

                        // Ampersand needs to be the first, otherwise it would double-encode
                        // other entities.
                        positionFormatString = positionFormatString.replaceAll("&", "&amp;");
                        positionFormatString = positionFormatString.replaceAll("<", "&lt;");
                        positionFormatString = positionFormatString.replaceAll(">", "&gt;");
                        positionFormatString = positionFormatString.replaceAll("\"", "&quot;");
                        positionFormatString = positionFormatString.replaceAll("'", "&apos;");
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


        File filePicker1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_file_picker_1.xml");

        if (filePicker1JobFile.exists() == true)
        {
            if (filePicker1JobFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = filePicker1JobFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (filePicker1JobFile.canWrite() != true)
                    {
                        throw constructTermination("messageFilePicker1JobFileExistsButIsntWritable", null, null, filePicker1JobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageFilePicker1JobPathExistsButIsntAFile", null, null, filePicker1JobFile.getAbsolutePath());
            }
        }

        File filePicker1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_file_picker_1.xml");

        if (filePicker1ResultInfoFile.exists() == true)
        {
            if (filePicker1ResultInfoFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = filePicker1ResultInfoFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (filePicker1ResultInfoFile.canWrite() != true)
                    {
                        throw constructTermination("messageFilePicker1ResultInfoFileExistsButIsntWritable", null, null, filePicker1ResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageFilePicker1ResultInfoPathExistsButIsntAFile", null, null, filePicker1ResultInfoFile.getAbsolutePath());
            }
        }


        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(filePicker1JobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by text_position_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
            writer.write("<file-picker-1-jobfile>\n");

            if (startDirectory != null)
            {
                writer.write("  <start-directory path=\"" + startDirectory.getAbsolutePath() + "\"/>\n");
            }

            for (Map.Entry<String, String> entry : fileExtensions.entrySet())
            {
                writer.write("  <file-type extension=\"" + entry.getKey() + "\">" + entry.getValue() + "</file-type>\n");
            }

            writer.write("</file-picker-1-jobfile>\n");

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageFilePicker1JobFileWritingError", ex, null, filePicker1JobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageFilePicker1JobFileWritingError", ex, null, filePicker1JobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageFilePicker1JobFileWritingError", ex, null, filePicker1JobFile.getAbsolutePath());
        }

        ProcessBuilder builder = new ProcessBuilder("java", "file_picker_1", filePicker1JobFile.getAbsolutePath(), filePicker1ResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "gui" + File.separator + "file_picker" + File.separator + "file_picker_1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                System.out.println(scanner.next());
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            throw constructTermination("messageFilePicker1ErrorWhileReadingOutput", ex, null);
        }

        if (filePicker1ResultInfoFile.exists() != true)
        {
            throw constructTermination("messageFilePicker1ResultInfoFileDoesntExistButShould", null, null, filePicker1ResultInfoFile.getAbsolutePath());
        }

        if (filePicker1ResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageFilePicker1ResultInfoPathExistsButIsntAFile", null, null, filePicker1ResultInfoFile.getAbsolutePath());
        }

        if (filePicker1ResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageFilePicker1ResultInfoFileIsntReadable", null, null, filePicker1ResultInfoFile.getAbsolutePath());
        }

        File inputFile = null;
        boolean wasSuccess = false;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(filePicker1ResultInfoFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equals("success") == true)
                    {
                        wasSuccess = true;
                    }
                    else if (tagName.equals("selected-file") == true)
                    {
                        String inputFileString = new String();

                        while (eventReader.hasNext() == true)
                        {
                            event = eventReader.nextEvent();

                            if (event.isCharacters() == true)
                            {
                                inputFileString += event.asCharacters();
                            }
                            else if (event.isEndElement() == true)
                            {
                                QName elementEndName = event.asEndElement().getName();
                                String elementEndNameString = elementEndName.getLocalPart();

                                if (elementEndNameString.equalsIgnoreCase("selected-file") == true)
                                {
                                    break;
                                }
                            }
                        }

                        inputFile = new File(inputFileString);

                        if (inputFile.isAbsolute() != true)
                        {
                            inputFile = new File(filePicker1ResultInfoFile.getAbsoluteFile().getParent() + File.separator + inputFileString);
                        }

                        if (inputFile.exists() != true)
                        {
                            throw constructTermination("messageFilePicker1ResultInfoFileSelectedFileDoesntExist", null, null, filePicker1ResultInfoFile.getAbsolutePath(), inputFile.getAbsolutePath());
                        }

                        if (inputFile.isFile() != true)
                        {
                            throw constructTermination("messageFilePicker1ResultInfoFileSelectedPathIsntAFile", null, null, filePicker1ResultInfoFile.getAbsolutePath(), inputFile.getAbsolutePath());
                        }

                        if (inputFile.canRead() != true)
                        {
                            throw constructTermination("messageFilePicker1ResultInfoFileSelectedFileIsntReadable", null, null, filePicker1ResultInfoFile.getAbsolutePath(), inputFile.getAbsolutePath());
                        }
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageFilePicker1ResultInfoFileErrorWhileReading", ex, null, filePicker1ResultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageFilePicker1ResultInfoFileErrorWhileReading", ex, null, filePicker1ResultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageFilePicker1ResultInfoFileErrorWhileReading", ex, null, filePicker1ResultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageFilePicker1CallWasntSuccessful", null, null);
        }

        if (inputFile == null)
        {
            throw constructTermination("messageFilePicker1ResultInfoFileSelectedIsntConfigured", null, null, jobFile.getAbsolutePath());
        }


        File textPositionRetriever1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_text_position_retriever_1.xml");

        if (textPositionRetriever1JobFile.exists() == true)
        {
            if (textPositionRetriever1JobFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = textPositionRetriever1JobFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (textPositionRetriever1JobFile.canWrite() != true)
                    {
                        throw constructTermination("messageTextPositionRetriever1JobFileExistsButIsntWritable", null, null, textPositionRetriever1JobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageTextPositionRetriever1JobPathExistsButIsntAFile", null, null, textPositionRetriever1JobFile.getAbsolutePath());
            }
        }

        File textPositionRetriever1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_text_position_retriever_1.xml");

        if (textPositionRetriever1ResultInfoFile.exists() == true)
        {
            if (textPositionRetriever1ResultInfoFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = textPositionRetriever1ResultInfoFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (textPositionRetriever1ResultInfoFile.canWrite() != true)
                    {
                        throw constructTermination("messageTextPositionRetriever1ResultInfoFileExistsButIsntWritable", null, null, textPositionRetriever1ResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageTextPositionRetriever1ResultInfoPathExistsButIsntAFile", null, null, textPositionRetriever1ResultInfoFile.getAbsolutePath());
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(textPositionRetriever1JobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by text_position_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
            writer.write("<text-position-retriever-1-jobfile>\n");
            writer.write("  <input-file path=\"" + inputFile.getAbsolutePath() + "\"/>\n");

            if (fontSize != null)
            {
                writer.write("  <font-size point=\"" + fontSize + "\"/>\n");
            }

            if (positionFormatString != null)
            {
                writer.write("  <position-format-string format=\"" + positionFormatString + "\"/>\n");
            }

            writer.write("</text-position-retriever-1-jobfile>\n");

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageTextPositionRetriever1JobFileWritingError", ex, null, textPositionRetriever1JobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageTextPositionRetriever1JobFileWritingError", ex, null, textPositionRetriever1JobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageTextPositionRetriever1JobFileWritingError", ex, null, textPositionRetriever1JobFile.getAbsolutePath());
        }

        builder = new ProcessBuilder("java", "text_position_retriever_1", textPositionRetriever1JobFile.getAbsolutePath(), textPositionRetriever1ResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "gui" + File.separator + "text_position_retriever" + File.separator + "text_position_retriever_1"));
        builder.redirectErrorStream(true);

        try
        {
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream()).useDelimiter("\n");

            while (scanner.hasNext() == true)
            {
                System.out.println(scanner.next());
            }

            scanner.close();
        }
        catch (IOException ex)
        {
            throw constructTermination("messageTextPositionRetriever1ErrorWhileReadingOutput", ex, null);
        }

        if (textPositionRetriever1ResultInfoFile.exists() != true)
        {
            throw constructTermination("messageTextPositionRetriever1ResultInfoFileDoesntExistButShould", null, null, textPositionRetriever1ResultInfoFile.getAbsolutePath());
        }

        if (textPositionRetriever1ResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageTextPositionRetriever1ResultInfoPathExistsButIsntAFile", null, null, textPositionRetriever1ResultInfoFile.getAbsolutePath());
        }

        if (textPositionRetriever1ResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageTextPositionRetriever1ResultInfoFileIsntReadable", null, null, textPositionRetriever1ResultInfoFile.getAbsolutePath());
        }

        wasSuccess = false;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(textPositionRetriever1ResultInfoFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equals("success") == true)
                    {
                        wasSuccess = true;
                        break;
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageTextPositionRetriever1ResultInfoFileErrorWhileReading", ex, null, textPositionRetriever1ResultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageTextPositionRetriever1ResultInfoFileErrorWhileReading", ex, null, textPositionRetriever1ResultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageTextPositionRetriever1ResultInfoFileErrorWhileReading", ex, null, textPositionRetriever1ResultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageTextPositionRetriever1CallWasntSuccessful", null, null);
        }

        return 0;
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
                message = "text_position_retriever_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "text_position_retriever_1 workflow: " + getI10nStringFormatted(id, arguments);
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
                message = "text_position_retriever_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "text_position_retriever_1 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (text_position_retriever_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(text_position_retriever_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by text_position_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<text-position-retriever-1-workflow-result-information>\n");

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

                writer.write("</text-position-retriever-1-workflow-result-information>\n");
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

        text_position_retriever_1.resultInfoFile = null;

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
        if (this.l10nConsole == null)
        {
            this.l10nConsole = ResourceBundle.getBundle(L10N_BUNDLE, this.getLocale());
        }

        try
        {
            return new String(this.l10nConsole.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            return this.l10nConsole.getString(key);
        }
    }

    private String getI10nStringFormatted(String i10nStringName, Object ... arguments)
    {
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(this.getLocale());

        formatter.applyPattern(getI10nString(i10nStringName));
        return formatter.format(arguments);
    }

    public static File resultInfoFile = null;
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nTextPositionRetriever1WorkflowConsole";
    private ResourceBundle l10nConsole;
}

