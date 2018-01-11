/* Copyright (C) 2016-2018 Stephan Kreutzer
 *
 * This file is part of edl_fulfiller_1 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * edl_fulfiller_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * edl_fulfiller_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with edl_fulfiller_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/htx/workflows/edl_fulfiller/edl_fulfiller_1/edl_fulfiller_1.java
 * @brief Retrieves all source material referenced by an EDL file in XML format and
 *     concatenates the text span portions.
 * @author Stephan Kreutzer
 * @since 2017-12-26
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
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.util.Scanner;
import java.lang.NumberFormatException;
import java.util.Map;
import java.util.HashMap;




public class edl_fulfiller_1
{
    public static void main(String args[])
    {
        System.out.print("edl_fulfiller_1 workflow Copyright (C) 2016-2018 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");

        edl_fulfiller_1 fulfiller = new edl_fulfiller_1();

        try
        {
            fulfiller.fulfill(args);
        }
        catch (ProgramTerminationException ex)
        {
            fulfiller.handleTermination(ex);
        }

        if (fulfiller.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(fulfiller.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by edl_fulfiller_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<edl-fulfiller-1-workflow-result-information>\n");
                writer.write("  <success>\n");

                if (fulfiller.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = fulfiller.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = fulfiller.getInfoMessages().get(i);

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
                writer.write("</edl-fulfiller-1-workflow-result-information>\n");
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

    public int fulfill(String args[])
    {
        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\tedl_fulfiller_1 " + getI10nString("messageParameterList") + "\n");
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

        edl_fulfiller_1.resultInfoFile = resultInfoFile;

        String programPath = edl_fulfiller_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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
                throw constructTermination("messageTempPathIsntDirectory", null, null, tempDirectory.getAbsolutePath());
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

        System.out.println("edl_fulfiller_1 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        File inputFile = null;
        File outputFile = null;

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
                        StartElement inputFileElement = event.asStartElement();
                        Attribute pathAttribute = inputFileElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        if (inputFile != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        inputFile = new File(pathAttribute.getValue());

                        if (inputFile.isAbsolute() != true)
                        {
                            inputFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + pathAttribute.getValue());
                        }

                        try
                        {
                            inputFile = inputFile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageInputFileCantGetCanonicalPath", ex, null, inputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageInputFileCantGetCanonicalPath", ex, null, inputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (inputFile.exists() != true)
                        {
                            throw constructTermination("messageInputFileDoesntExist", null, null, inputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (inputFile.isFile() != true)
                        {
                            throw constructTermination("messageInputPathIsntAFile", null, null, inputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (inputFile.canRead() != true)
                        {
                            throw constructTermination("messageInputFileIsntReadable", null, null, inputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (inputFile != null &&
                            outputFile != null)
                        {
                            break;
                        }
                    }
                    else if (tagName.equals("output-file") == true)
                    {
                        StartElement outputFileElement = event.asStartElement();
                        Attribute pathAttribute = outputFileElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        if (outputFile != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        outputFile = new File(pathAttribute.getValue());

                        if (outputFile.isAbsolute() != true)
                        {
                            outputFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + pathAttribute.getValue());
                        }

                        try
                        {
                            outputFile = outputFile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageOutputFileCantGetCanonicalPath", ex, null, outputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageOutputFileCantGetCanonicalPath", ex, null, outputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (outputFile.exists() == true)
                        {
                            if (outputFile.isFile() == true)
                            {
                                if (outputFile.canWrite() != true)
                                {
                                    throw constructTermination("messageOutputFileIsntWritable", null, null, outputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                                }
                            }
                            else
                            {
                                throw constructTermination("messageOutputPathIsntAFile", null, null, outputFile.getAbsolutePath(), jobFile.getAbsolutePath());
                            }
                        }

                        if (inputFile != null &&
                            outputFile != null)
                        {
                            break;
                        }
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
            throw constructTermination("messageJobFileInputFileIsntConfigured", null, null, jobFile.getAbsolutePath(), "input-file");
        }

        if (outputFile == null)
        {
            throw constructTermination("messageJobFileOutputFileIsntConfigured", null, null, jobFile.getAbsolutePath(), "output-file");
        }


        List<SpanInfo> spanInfos = new ArrayList<SpanInfo>();

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(inputFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equals("span") == true)
                    {
                        StartElement spanElement = event.asStartElement();
                        Attribute identifierAttribute = spanElement.getAttributeByName(new QName("identifier"));
                        Attribute startAttribute = spanElement.getAttributeByName(new QName("start"));
                        Attribute lengthAttribute = spanElement.getAttributeByName(new QName("length"));

                        if (identifierAttribute == null)
                        {
                            throw constructTermination("messageInputFileEntryIsMissingAnAttribute", null, null, inputFile.getAbsolutePath(), tagName, "identifier");
                        }

                        if (startAttribute == null)
                        {
                            throw constructTermination("messageInputFileEntryIsMissingAnAttribute", null, null, inputFile.getAbsolutePath(), tagName, "start");
                        }

                        if (lengthAttribute == null)
                        {
                            throw constructTermination("messageInputFileEntryIsMissingAnAttribute", null, null, inputFile.getAbsolutePath(), tagName, "length");
                        }

                        long startNumber = 0;

                        try
                        {
                            startNumber = Long.parseLong(startAttribute.getValue());
                        }
                        catch (NumberFormatException ex)
                        {
                            throw constructTermination("messageInputFileEntryAttributeIsntANumber", ex, null, inputFile.getAbsolutePath(), tagName, "start", startAttribute.getValue());
                        }

                        long lengthNumber = 0;

                        try
                        {
                            lengthNumber = Long.parseLong(lengthAttribute.getValue());
                        }
                        catch (NumberFormatException ex)
                        {
                            throw constructTermination("messageInputFileEntryAttributeIsntANumber", ex, null, inputFile.getAbsolutePath(), tagName, "length", lengthAttribute.getValue());
                        }

                        spanInfos.add(new SpanInfo(identifierAttribute.getValue(),
                                                   null,
                                                   startNumber,
                                                   lengthNumber));
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageInputFileErrorWhileReading", ex, null, inputFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageInputFileErrorWhileReading", ex, null, inputFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageInputFileErrorWhileReading", ex, null, inputFile.getAbsolutePath());
        }

        if (spanInfos.isEmpty() == true)
        {
            this.getInfoMessages().add(constructInfoMessage("messageInputFileNoSpansSpecified", true, null, null, inputFile.getAbsolutePath()));
            System.exit(0);
        }


        File edlToResourceRetriever1Jobfile1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_edl_to_resource_retriever_1_jobfile_1.xml");

        if (edlToResourceRetriever1Jobfile1JobFile.exists() == true)
        {
            if (edlToResourceRetriever1Jobfile1JobFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = edlToResourceRetriever1Jobfile1JobFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (edlToResourceRetriever1Jobfile1JobFile.canWrite() != true)
                    {
                        throw constructTermination("messageEdlToResourceRetriever1Jobfile1JobFileExistsButIsntWritable", null, null, edlToResourceRetriever1Jobfile1JobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageEdlToResourceRetriever1Jobfile1JobPathExistsButIsntAFile", null, null, edlToResourceRetriever1Jobfile1JobFile.getAbsolutePath());
            }
        }

        File edlToResourceRetriever1Jobfile1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_edl_to_resource_retriever_1_jobfile_1.xml");

        if (edlToResourceRetriever1Jobfile1ResultInfoFile.exists() == true)
        {
            if (edlToResourceRetriever1Jobfile1ResultInfoFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = edlToResourceRetriever1Jobfile1ResultInfoFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (edlToResourceRetriever1Jobfile1ResultInfoFile.canWrite() != true)
                    {
                        throw constructTermination("messageEdlToResourceRetriever1Jobfile1ResultInfoFileExistsButIsntWritable", null, null, edlToResourceRetriever1Jobfile1ResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageEdlToResourceRetriever1Jobfile1ResultInfoPathExistsButIsntAFile", null, null, edlToResourceRetriever1Jobfile1ResultInfoFile.getAbsolutePath());
            }
        }

        File resourceRetriever1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_resource_retriever_1.xml");

        if (resourceRetriever1JobFile.exists() == true)
        {
            if (resourceRetriever1JobFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = resourceRetriever1JobFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (resourceRetriever1JobFile.canWrite() != true)
                    {
                        throw constructTermination("messageResourceRetriever1JobFileExistsButIsntWritable", null, null, resourceRetriever1JobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageResourceRetriever1JobPathExistsButIsntAFile", null, null, resourceRetriever1JobFile.getAbsolutePath());
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(edlToResourceRetriever1Jobfile1JobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by edl_fulfiller_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
            writer.write("<edl-to-resource-retriever-1-jobfile-1-workflow-job>\n");
            writer.write("  <input-file path=\"" + inputFile.getAbsolutePath() + "\"/>\n");
            writer.write("  <output-file path=\"" + resourceRetriever1JobFile.getAbsolutePath() + "\"/>\n");
            writer.write("</edl-to-resource-retriever-1-jobfile-1-workflow-job>\n");

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageEdlToResourceRetriever1Jobfile1JobFileWritingError", ex, null, edlToResourceRetriever1Jobfile1JobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageEdlToResourceRetriever1Jobfile1JobFileWritingError", ex, null, edlToResourceRetriever1Jobfile1JobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageEdlToResourceRetriever1Jobfile1JobFileWritingError", ex, null, edlToResourceRetriever1Jobfile1JobFile.getAbsolutePath());
        }


        ProcessBuilder builder = new ProcessBuilder("java", "edl_to_resource_retriever_1_jobfile_1", edlToResourceRetriever1Jobfile1JobFile.getAbsolutePath(), edlToResourceRetriever1Jobfile1ResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + "edl_to_resource_retriever_1_jobfile" + File.separator + "edl_to_resource_retriever_1_jobfile_1"));
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
            throw constructTermination("messageEdlToResourceRetriever1Jobfile1ErrorWhileReadingOutput", ex, null);
        }


        if (edlToResourceRetriever1Jobfile1ResultInfoFile.exists() != true)
        {
            throw constructTermination("messageEdlToResourceRetriever1Jobfile1ResultInfoFileDoesntExistButShould", null, null, edlToResourceRetriever1Jobfile1ResultInfoFile.getAbsolutePath());
        }

        if (edlToResourceRetriever1Jobfile1ResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageEdlToResourceRetriever1Jobfile1ResultInfoPathExistsButIsntAFile", null, null, edlToResourceRetriever1Jobfile1ResultInfoFile.getAbsolutePath());
        }

        if (edlToResourceRetriever1Jobfile1ResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageEdlToResourceRetriever1Jobfile1ResultInfoFileIsntReadable", null, null, edlToResourceRetriever1Jobfile1ResultInfoFile.getAbsolutePath());
        }

        boolean wasSuccess = false;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(edlToResourceRetriever1Jobfile1ResultInfoFile);
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
            throw constructTermination("messageEdlToResourceRetriever1Jobfile1ResultInfoFileErrorWhileReading", ex, null, edlToResourceRetriever1Jobfile1ResultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageEdlToResourceRetriever1Jobfile1ResultInfoFileErrorWhileReading", ex, null, edlToResourceRetriever1Jobfile1ResultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageEdlToResourceRetriever1Jobfile1ResultInfoFileErrorWhileReading", ex, null, edlToResourceRetriever1Jobfile1ResultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageEdlToResourceRetriever1Jobfile1CallWasntSuccessful", null, null);
        }


        File xmlXsltTransformator1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_xml_xslt_transformator_1.xml");

        if (xmlXsltTransformator1JobFile.exists() == true)
        {
            if (xmlXsltTransformator1JobFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = xmlXsltTransformator1JobFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (xmlXsltTransformator1JobFile.canWrite() != true)
                    {
                        throw constructTermination("messageXmlXsltTransformator1JobFileExistsButIsntWritable", null, null, xmlXsltTransformator1JobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageXmlXsltTransformator1JobPathExistsButIsntAFile", null, null, xmlXsltTransformator1JobFile.getAbsolutePath());
            }
        }

        File xmlXsltTransformator1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_xml_xslt_transformator_1.xml");

        if (xmlXsltTransformator1ResultInfoFile.exists() == true)
        {
            if (xmlXsltTransformator1ResultInfoFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = xmlXsltTransformator1ResultInfoFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (xmlXsltTransformator1ResultInfoFile.canWrite() != true)
                    {
                        throw constructTermination("messageXmlXsltTransformator1ResultInfoFileExistsButIsntWritable", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageXmlXsltTransformator1ResultInfoPathExistsButIsntAFile", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
            }
        }

        File resourceRetriever1JobFilePrepared = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_resource_retriever_1_prepared.xml");

        if (resourceRetriever1JobFilePrepared.exists() == true)
        {
            if (resourceRetriever1JobFilePrepared.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = resourceRetriever1JobFilePrepared.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (resourceRetriever1JobFilePrepared.canWrite() != true)
                    {
                        throw constructTermination("messageResourceRetriever1JobFilePreparedExistsButIsntWritable", null, null, resourceRetriever1JobFilePrepared.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageResourceRetriever1JobPathPreparedExistsButIsntAFile", null, null, resourceRetriever1JobFilePrepared.getAbsolutePath());
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(xmlXsltTransformator1JobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by edl_fulfiller_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
            writer.write("<xml-xslt-transformator-1-job>\n");
            writer.write("  <job input-file=\"" + resourceRetriever1JobFile.getAbsolutePath() + "\" entities-resolver-config-file=\"" + programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1" + File.separator + "entities" + File.separator + "config_empty.xml\" stylesheet-file=\"" + programPath + "resource_retriever_1_jobfile_add_output.xsl\" output-file=\"" + resourceRetriever1JobFilePrepared.getAbsolutePath() + "\"/>\n");
            writer.write("</xml-xslt-transformator-1-job>\n");

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageXmlXsltTransformator1JobFileWritingError", ex, null, xmlXsltTransformator1JobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageXmlXsltTransformator1JobFileWritingError", ex, null, xmlXsltTransformator1JobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageXmlXsltTransformator1JobFileWritingError", ex, null, xmlXsltTransformator1JobFile.getAbsolutePath());
        }

        builder = new ProcessBuilder("java", "xml_xslt_transformator_1", xmlXsltTransformator1JobFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1"));
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
            throw constructTermination("messageXmlXsltTransformator1ErrorWhileReadingOutput", ex, null);
        }


        if (xmlXsltTransformator1ResultInfoFile.exists() != true)
        {
            throw constructTermination("messageXmlXsltTransformator1ResultInfoFileDoesntExistButShould", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
        }

        if (xmlXsltTransformator1ResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageXmlXsltTransformator1ResultInfoPathExistsButIsntAFile", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
        }

        if (xmlXsltTransformator1ResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageXmlXsltTransformator1ResultInfoFileIsntReadable", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
        }

        wasSuccess = false;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(xmlXsltTransformator1ResultInfoFile);
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
            throw constructTermination("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", ex, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", ex, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", ex, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageXmlXsltTransformator1CallWasntSuccessful", null, null);
        }


        File resourceRetriever1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_resource_retriever_1.xml");

        if (resourceRetriever1ResultInfoFile.exists() == true)
        {
            if (resourceRetriever1ResultInfoFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = resourceRetriever1ResultInfoFile.delete();
                }
                catch (SecurityException ex)
                {

                }

                if (deleteSuccessful != true)
                {
                    if (resourceRetriever1ResultInfoFile.canWrite() != true)
                    {
                        throw constructTermination("messageResourceRetriever1ResultInfoFileExistsButIsntWritable", null, null, resourceRetriever1ResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageResourceRetriever1ResultInfoPathExistsButIsntAFile", null, null, resourceRetriever1ResultInfoFile.getAbsolutePath());
            }
        }

        builder = new ProcessBuilder("java", "resource_retriever_1", resourceRetriever1JobFilePrepared.getAbsolutePath(), resourceRetriever1ResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "workflows" + File.separator + "resource_retriever" + File.separator + "resource_retriever_1"));
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
            throw constructTermination("messageResourceRetriever1ErrorWhileReadingOutput", ex, null);
        }

        if (resourceRetriever1ResultInfoFile.exists() != true)
        {
            throw constructTermination("messageResourceRetriever1ResultInfoFileDoesntExistButShould", null, null, resourceRetriever1ResultInfoFile.getAbsolutePath());
        }

        if (resourceRetriever1ResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageResourceRetriever1ResultInfoPathExistsButIsntAFile", null, null, resourceRetriever1ResultInfoFile.getAbsolutePath());
        }

        if (resourceRetriever1ResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageResourceRetriever1ResultInfoFileIsntReadable", null, null, resourceRetriever1ResultInfoFile.getAbsolutePath());
        }

        wasSuccess = false;

        Map<String, File> identifierResourceMapping = new HashMap<String, File>();

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(resourceRetriever1ResultInfoFile);
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
                    else if (tagName.equals("retrieved-resource") == true)
                    {
                        StartElement retrievedResourceElement = event.asStartElement();
                        Attribute identifierAttribute = retrievedResourceElement.getAttributeByName(new QName("identifier"));
                        Attribute successAttribute = retrievedResourceElement.getAttributeByName(new QName("success"));
                        Attribute pathAttribute = retrievedResourceElement.getAttributeByName(new QName("path"));

                        if (identifierAttribute == null)
                        {
                            throw constructTermination("messageResourceRetriever1ResultInfoFileEntryIsMissingAnAttribute", null, null, resourceRetriever1ResultInfoFile.getAbsolutePath(), tagName, "identifier");
                        }

                        if (successAttribute == null)
                        {
                            throw constructTermination("messageResourceRetriever1ResultInfoFileEntryIsMissingAnAttribute", null, null, resourceRetriever1ResultInfoFile.getAbsolutePath(), tagName, "success");
                        }

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageResourceRetriever1ResultInfoFileEntryIsMissingAnAttribute", null, null, resourceRetriever1ResultInfoFile.getAbsolutePath(), tagName, "path");
                        }

                        if (successAttribute.getValue().equals("true") != true)
                        {
                            throw constructTermination("messageResourceRetriever1ResultInfoFileRetrievalWasntSuccessful", null, null, resourceRetriever1ResultInfoFile.getAbsolutePath(), identifierAttribute.getValue());
                        }

                        File resourceFile = new File(pathAttribute.getValue());

                        if (resourceFile.isAbsolute() != true)
                        {
                            resourceFile = new File(resourceRetriever1ResultInfoFile.getAbsoluteFile().getParent() + File.separator + pathAttribute.getValue());
                        }

                        try
                        {
                            resourceFile = resourceFile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageResourceRetriever1ResultInfoFileResourceFileCantGetCanonicalPath", ex, null, resourceFile.getAbsolutePath(), resourceRetriever1ResultInfoFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageResourceRetriever1ResultInfoFileResourceFileCantGetCanonicalPath", ex, null, resourceFile.getAbsolutePath(), resourceRetriever1ResultInfoFile.getAbsolutePath());
                        }

                        if (resourceFile.exists() != true)
                        {
                            throw constructTermination("messageResourceFileDoesntExist", null, null, resourceFile.getAbsolutePath(), resourceRetriever1ResultInfoFile.getAbsolutePath());
                        }

                        if (resourceFile.isFile() != true)
                        {
                            throw constructTermination("messageResourcePathIsntAFile", null, null, resourceFile.getAbsolutePath(), resourceRetriever1ResultInfoFile.getAbsolutePath());
                        }

                        if (resourceFile.canRead() != true)
                        {
                            throw constructTermination("messageResourceFileIsntReadable", null, null, resourceFile.getAbsolutePath(), resourceRetriever1ResultInfoFile.getAbsolutePath());
                        }

                        identifierResourceMapping.put(identifierAttribute.getValue(), resourceFile);
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageResourceRetriever1ResultInfoFileErrorWhileReading", ex, null, resourceRetriever1ResultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageResourceRetriever1ResultInfoFileErrorWhileReading", ex, null, resourceRetriever1ResultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageResourceRetriever1ResultInfoFileErrorWhileReading", ex, null, resourceRetriever1ResultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageResourceRetriever1CallWasntSuccessful", null, null);
        }

        for (int i = 0; i < spanInfos.size(); i++)
        {
            SpanInfo spanInfo = spanInfos.get(i);

            if (identifierResourceMapping.containsKey(spanInfo.getIdentifier()) == true)
            {
                spanInfo.setResource(identifierResourceMapping.get(spanInfo.getIdentifier()));
            }
            else
            {
                throw constructTermination("messageIdentifierNotFoundInResourceMapping", null, null, spanInfo.getIdentifier());
            }
        }

        for (int i = 0; i < spanInfos.size(); i++)
        {
            SpanInfo spanInfo = spanInfos.get(i);

            System.out.println(spanInfo.getIdentifier() + ", " + spanInfo.getResource().getAbsolutePath() + ", " + spanInfo.getStart() + ", " + spanInfo.getLength()); 
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
                message = "edl_fulfiller_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "edl_fulfiller_1 workflow: " + getI10nStringFormatted(id, arguments);
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
                message = "edl_fulfiller_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "edl_fulfiller_1 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (edl_fulfiller_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(edl_fulfiller_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by edl_fulfiller_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<edl-fulfiller-1-workflow-jobfile-result-information>\n");

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

                writer.write("</edl-fulfiller-1-workflow-result-information>\n");
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

        edl_fulfiller_1.resultInfoFile = null;

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

    protected ArrayList<String> tokens = null;

    public static File resultInfoFile = null;
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nEdlFulfiller1WorkflowConsole";
    private ResourceBundle l10nConsole;
}
