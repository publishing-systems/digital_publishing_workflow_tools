/* Copyright (C) 2016-2018 Stephan Kreutzer
 *
 * This file is part of wordpress_retriever_1 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * wordpress_retriever_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * wordpress_retriever_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with wordpress_retriever_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/wordpress_retriever/wordpress_retriever_1/wordpress_retriever_1.java
 * @brief Retrieves all posts from a WordPress base URL.
 * @author Stephan Kreutzer
 * @since 2018-03-31
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
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.Stack;
import java.util.Iterator;



public class wordpress_retriever_1
{
    public static void main(String args[])
    {
        System.out.print("wordpress_retriever_1 workflow Copyright (C) 2016-2018 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");

        wordpress_retriever_1 instance = new wordpress_retriever_1();

        try
        {
            instance.call(args);
        }
        catch (ProgramTerminationException ex)
        {
            instance.handleTermination(ex);
        }

        if (instance.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(instance.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wordpress_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<wordpress-retriever-1-workflow-result-information>\n");
                writer.write("  <success>\n");

                if (instance.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = instance.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = instance.getInfoMessages().get(i);

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
                writer.write("</wordpress-retriever-1-workflow-result-information>\n");
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

    public int call(String args[])
    {
        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\twordpress_retriever_1 " + getI10nString("messageParameterList") + "\n");
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

        wordpress_retriever_1.resultInfoFile = resultInfoFile;

        String programPath = wordpress_retriever_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

        System.out.println("wordpress_retriever_1 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        String inputUrl = null;
        File outputFile = null;
        File tempDirectory = null;

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

                    if (tagName.equals("input") == true)
                    {
                        StartElement inputElement = event.asStartElement();
                        Attribute urlAttribute = inputElement.getAttributeByName(new QName("url"));

                        if (urlAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "url");
                        }

                        if (inputUrl != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        inputUrl = urlAttribute.getValue();
                    }
                    else if (tagName.equals("temp-directory") == true)
                    {
                        Attribute attributePath = event.asStartElement().getAttributeByName(new QName("path"));

                        if (attributePath == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        if (tempDirectory != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        tempDirectory = new File(attributePath.getValue());

                        if (tempDirectory.isAbsolute() != true)
                        {
                            tempDirectory = new File(jobFile.getAbsoluteFile().getParent() + File.separator + attributePath.getValue());
                        }
                    }
                    else if (tagName.equals("output-file") == true)
                    {
                        Attribute attributePath = event.asStartElement().getAttributeByName(new QName("path"));

                        if (attributePath == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        if (outputFile != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        outputFile = new File(attributePath.getValue());

                        if (outputFile.isAbsolute() != true)
                        {
                            outputFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + attributePath.getValue());
                        }

                        try
                        {
                            outputFile = outputFile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageOutputFileCantGetCanonicalPath", ex, null, outputFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageOutputFileCantGetCanonicalPath", ex, null, outputFile.getAbsolutePath());
                        }

                        if (outputFile.exists() == true)
                        {
                            if (outputFile.isFile() == true)
                            {
                                if (outputFile.canWrite() != true)
                                {
                                    throw constructTermination("messageOutputFileIsntWritable", null, null, outputFile.getAbsolutePath());
                                }
                            }
                            else
                            {
                                throw constructTermination("messageOutputPathIsntAFile", null, null, outputFile.getAbsolutePath());
                            }
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

        if (inputUrl == null)
        {
            throw constructTermination("messageJobFileInputUrlIsntConfigured", null, null, jobFile.getAbsolutePath(), "input");
        }

        if (outputFile == null)
        {
            throw constructTermination("messageJobFileOutputFileIsntConfigured", null, null, jobFile.getAbsolutePath(), "output-file");
        }

        if (tempDirectory == null)
        {
            tempDirectory = new File(programPath + "temp");
        }

        try
        {
            tempDirectory = tempDirectory.getCanonicalFile();
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageTempDirectoryCantGetCanonicalPath", ex, null, tempDirectory.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageTempDirectoryCantGetCanonicalPath", ex, null, tempDirectory.getAbsolutePath());
        }

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


        List<File> pageFiles = new ArrayList<File>();
        int pageIndex = 0;
        boolean wasSuccess = true;

        while (wasSuccess == true)
        {
            File jobFileWordpressClient1Workflow = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_wordpress_client_1_workflow_job_" + pageIndex + ".xml");

            if (jobFileWordpressClient1Workflow.exists() == true)
            {
                if (jobFileWordpressClient1Workflow.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = jobFileWordpressClient1Workflow.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (jobFileWordpressClient1Workflow.canWrite() != true)
                        {
                            throw constructTermination("messageWordpressClient1WorkflowJobFileIsntWritable", null, null, jobFileWordpressClient1Workflow.getAbsolutePath());
                        }
                    }
                }
                else
                {
                    throw constructTermination("messageWordpressClient1WorkflowJobPathExistsButIsntAFile", null, null, jobFileWordpressClient1Workflow.getAbsolutePath());
                }
            }

            File resultInfoFileWordpressClient1Workflow = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_wordpress_client_1_workflow_job_" + pageIndex + ".xml");

            if (resultInfoFileWordpressClient1Workflow.exists() == true)
            {
                if (resultInfoFileWordpressClient1Workflow.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = resultInfoFileWordpressClient1Workflow.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (resultInfoFileWordpressClient1Workflow.canWrite() != true)
                        {
                            throw constructTermination("messageWordpressClient1WorkflowResultInfoFileIsntWritable", null, null, resultInfoFileWordpressClient1Workflow.getAbsolutePath());
                        }
                    }
                }
                else
                {
                    throw constructTermination("messageWordpressClient1WorkflowResultInfoPathExistsButIsntAFile", null, null, resultInfoFileWordpressClient1Workflow.getAbsolutePath());
                }
            }

            /** @todo Check both, tempDirectoryWordpressClient1Workflow and pageFile, and delete pageFile if already existing. */
            File tempDirectoryWordpressClient1Workflow = new File(tempDirectory.getAbsolutePath() + File.separator + "temp_wordpress_client_1_workflow_" + pageIndex);
            File pageFile = new File(tempDirectory.getAbsolutePath() + File.separator + "posts_" + pageIndex + ".json");

            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(jobFileWordpressClient1Workflow),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wordpress_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<wordpress-client-1-workflow-jobfile>\n");
                writer.write("  <request url=\"" + inputUrl + "\" action=\"retrieve-posts-list\">\n");
                writer.write("    <parameter name=\"page\" value=\"" + (pageIndex + 1) + "\"/>\n");
                writer.write("  </request>\n");
                writer.write("  <temp-directory path=\"" + tempDirectoryWordpressClient1Workflow.getAbsolutePath() + "\"/>\n");
                writer.write("  <output-file path=\"" + pageFile.getAbsolutePath() + "\"/>\n");
                writer.write("</wordpress-client-1-workflow-jobfile>\n");
                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                throw constructTermination("messageWordpressClient1WorkflowJobFileErrorWhileWriting", ex, null, jobFileWordpressClient1Workflow.getAbsolutePath());
            }
            catch (UnsupportedEncodingException ex)
            {
                throw constructTermination("messageWordpressClient1WorkflowJobFileErrorWhileWriting", ex, null, jobFileWordpressClient1Workflow.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageWordpressClient1WorkflowJobFileErrorWhileWriting", ex, null, jobFileWordpressClient1Workflow.getAbsolutePath());
            }

            ProcessBuilder builder = new ProcessBuilder("java", "wordpress_client_1", jobFileWordpressClient1Workflow.getAbsolutePath(), resultInfoFileWordpressClient1Workflow.getAbsolutePath());
            builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + "wordpress_client" + File.separator + "wordpress_client_1"));
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
                throw constructTermination("messageWordpressClient1WorkflowErrorWhileReadingOutput", ex, null);
            }

            if (resultInfoFileWordpressClient1Workflow.exists() != true)
            {
                throw constructTermination("messageWordpressClient1WorkflowResultInfoFileDoesntExistButShould", null, null, resultInfoFileWordpressClient1Workflow.getAbsolutePath());
            }

            if (resultInfoFileWordpressClient1Workflow.isFile() != true)
            {
                throw constructTermination("messageWordpressClient1WorkflowResultInfoPathExistsButIsntAFile", null, null, resultInfoFileWordpressClient1Workflow.getAbsolutePath());
            }

            if (resultInfoFileWordpressClient1Workflow.canRead() != true)
            {
                throw constructTermination("messageWordpressClient1WorkflowResultInfoFileIsntReadable", null, null, resultInfoFileWordpressClient1Workflow.getAbsolutePath());
            }

            wasSuccess = false;

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(resultInfoFileWordpressClient1Workflow);
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
                throw constructTermination("messageWordpressClient1WorkflowResultInfoFileErrorWhileReading", ex, null, resultInfoFileWordpressClient1Workflow.getAbsolutePath());
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageWordpressClient1WorkflowResultInfoFileErrorWhileReading", ex, null, resultInfoFileWordpressClient1Workflow.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageWordpressClient1WorkflowResultInfoFileErrorWhileReading", ex, null, resultInfoFileWordpressClient1Workflow.getAbsolutePath());
            }

            if (wasSuccess != true)
            {
                /**
                 * @todo Identify legitimate fails because the client reached the end of the post
                 *     list so the server refused to send more posts, or an internal failure of
                 *     wordpress_client_1 workflow.
                 */
                //throw constructTermination("messageWordpressClient1WorkflowCallWasntSuccessful", null, null, jobFileWordpressClient1Workflow.getAbsolutePath());
                break;
            }

            File xmlFile = new File(tempDirectory.getAbsolutePath() + File.separator + "page_" + pageIndex + ".xml");

            if (TransformJsonToXml(pageFile, pageIndex, programPath, tempDirectory, xmlFile) != 0)
            {
                wasSuccess = false;
                break;
            }

            File xmlFilePrepared = new File(tempDirectory.getAbsolutePath() + File.separator + "page_" + pageIndex + "_prepared.xml");

            if (TransformXml(xmlFile, pageIndex, programPath, tempDirectory, xmlFilePrepared) != 0)
            {
                wasSuccess = false;
                break;
            }

            File xmlFileDeescaped = new File(tempDirectory.getAbsolutePath() + File.separator + "page_" + pageIndex + "_deescaped.xml");

            if (DeescapeXml(xmlFilePrepared, pageIndex, programPath, tempDirectory, xmlFileDeescaped) != 0)
            {
                wasSuccess = false;
                break;
            }

            if (ValidateXml(xmlFileDeescaped, pageIndex, programPath, tempDirectory) != 0)
            {
                wasSuccess = false;
                break;
            }

            pageFiles.add(xmlFileDeescaped);
            ++pageIndex;
        }

        if (pageFiles.size() <= 0)
        {
            return -1;
        }

        {
            File xmlConcatenator1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_xml_concatenator_1_job.xml");

            if (xmlConcatenator1JobFile.exists() == true)
            {
                if (xmlConcatenator1JobFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = xmlConcatenator1JobFile.delete();
                    }
                    catch (SecurityException ex)
                    {
                    }

                    if (deleteSuccessful != true)
                    {
                        if (xmlConcatenator1JobFile.canWrite() != true)
                        {
                            throw constructTermination("messageXmlConcatenator1JobFileIsntWritable", null, null, xmlConcatenator1JobFile.getAbsolutePath());
                        }
                    }
                }
                else
                {
                    throw constructTermination("messageXmlConcatenator1JobPathIsntAFile", null, null, xmlConcatenator1JobFile.getAbsolutePath());
                }
            }

            File xmlConcatenator1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_xml_concatenator_1_job.xml");

            if (xmlConcatenator1ResultInfoFile.exists() == true)
            {
                if (xmlConcatenator1ResultInfoFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = xmlConcatenator1ResultInfoFile.delete();
                    }
                    catch (SecurityException ex)
                    {
                    }

                    if (deleteSuccessful != true)
                    {
                        if (xmlConcatenator1ResultInfoFile.canWrite() != true)
                        {
                            throw constructTermination("messageXmlConcatenator1ResultInfoFileIsntWritable", null, null, xmlConcatenator1ResultInfoFile.getAbsolutePath());
                        }
                    }
                }
                else
                {
                    throw constructTermination("messageXmlConcatenator1ResultInfoPathIsntAFile", null, null, xmlConcatenator1ResultInfoFile.getAbsolutePath());
                }
            }

            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(xmlConcatenator1JobFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wordpress_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<xml-concatenator-1-job>\n");
                writer.write("  <input-files>\n");

                for (int i = 0, max = pageFiles.size(); i < max; i++)
                {
                    writer.write("    <input-file path=\"" + pageFiles.get(i).getAbsolutePath() + "\"/>\n");
                }

                writer.write("  </input-files>\n");
                writer.write("  <output-file path=\"" + outputFile.getAbsolutePath() + "\" processing-instruction-data=\"encoding=&quot;UTF-8&quot;\" root-element-name=\"wordpress\"/>\n");
                writer.write("</xml-concatenator-1-job>\n");
                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                throw constructTermination("messageXmlConcatenator1JobFileErrorWhileWriting", ex, null, xmlConcatenator1JobFile.getAbsolutePath());
            }
            catch (UnsupportedEncodingException ex)
            {
                throw constructTermination("messageXmlConcatenator1JobFileErrorWhileWriting", ex, null, xmlConcatenator1JobFile.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageXmlConcatenator1JobFileErrorWhileWriting", ex, null, xmlConcatenator1JobFile.getAbsolutePath());
            }

            ProcessBuilder builder = new ProcessBuilder("java", "xml_concatenator_1", xmlConcatenator1JobFile.getAbsolutePath(), xmlConcatenator1ResultInfoFile.getAbsolutePath());
            builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_concatenator" + File.separator + "xml_concatenator_1"));
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
                throw constructTermination("messageXmlConcatenator1ErrorWhileReadingOutput", ex, null, xmlConcatenator1JobFile.getAbsolutePath());
            }

            if (xmlConcatenator1ResultInfoFile.exists() != true)
            {
                throw constructTermination("messageXmlConcatenator1ResultInfoFileDoesntExistButShould", null, null, xmlConcatenator1ResultInfoFile.getAbsolutePath());
            }

            if (xmlConcatenator1ResultInfoFile.isFile() != true)
            {
                throw constructTermination("messageXmlConcatenator1ResultInfoPathIsntAFile", null, null, xmlConcatenator1ResultInfoFile.getAbsolutePath());
            }

            if (xmlConcatenator1ResultInfoFile.canRead() != true)
            {
                throw constructTermination("messageXmlConcatenator1ResultInfoFileIsntReadable", null, null, xmlConcatenator1ResultInfoFile.getAbsolutePath());
            }

            wasSuccess = false;

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(xmlConcatenator1ResultInfoFile);
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
                throw constructTermination("messageXmlConcatenator1ResultInfoFileErrorWhileReading", ex, null, xmlConcatenator1ResultInfoFile.getAbsolutePath());
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageXmlConcatenator1ResultInfoFileErrorWhileReading", ex, null, xmlConcatenator1ResultInfoFile.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageXmlConcatenator1ResultInfoFileErrorWhileReading", ex, null, xmlConcatenator1ResultInfoFile.getAbsolutePath());
            }

            if (wasSuccess != true)
            {
                throw constructTermination("messageXmlConcatenator1CallWasntSuccessful", null, null, xmlConcatenator1JobFile.getAbsolutePath());
            }
        }

        return 0;
    }

    protected int TransformJsonToXml(File jsonFile, int fileIndex, String programPath, File tempDirectory, File xmlFile)
    {
        File textConcatenator1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_text_concatenator_1_job_" + fileIndex + ".xml");

        if (textConcatenator1JobFile.exists() == true)
        {
            if (textConcatenator1JobFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = textConcatenator1JobFile.delete();
                }
                catch (SecurityException ex)
                {
                }

                if (deleteSuccessful != true)
                {
                    if (textConcatenator1JobFile.canWrite() != true)
                    {
                        throw constructTermination("messageTextConcatenator1JobFileIsntWritable", null, null, textConcatenator1JobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageTextConcatenator1JobPathIsntAFile", null, null, textConcatenator1JobFile.getAbsolutePath());
            }
        }

        File textConcatenator1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_text_concatenator_1_job_" + fileIndex + ".xml");

        if (textConcatenator1ResultInfoFile.exists() == true)
        {
            if (textConcatenator1ResultInfoFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = textConcatenator1ResultInfoFile.delete();
                }
                catch (SecurityException ex)
                {
                }

                if (deleteSuccessful != true)
                {
                    if (textConcatenator1ResultInfoFile.canWrite() != true)
                    {
                        throw constructTermination("messageTextConcatenator1ResultInfoFileIsntWritable", null, null, textConcatenator1ResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageTextConcatenator1ResultInfoPathIsntAFile", null, null, textConcatenator1ResultInfoFile.getAbsolutePath());
            }
        }


        if (xmlFile.exists() == true)
        {
            if (xmlFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = xmlFile.delete();
                }
                catch (SecurityException ex)
                {
                }

                if (deleteSuccessful != true)
                {
                    if (xmlFile.canWrite() != true)
                    {
                        throw constructTermination("messageXmlFileIsntWritable", null, null, xmlFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageXmlPathIsntAFile", null, null, xmlFile.getAbsolutePath());
            }
        }

        File jsonToXml2JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_json_to_xml_2_job_" + fileIndex + ".xml");

        if (jsonToXml2JobFile.exists() == true)
        {
            if (jsonToXml2JobFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = jsonToXml2JobFile.delete();
                }
                catch (SecurityException ex)
                {
                }

                if (deleteSuccessful != true)
                {
                    if (jsonToXml2JobFile.canWrite() != true)
                    {
                        throw constructTermination("messageJsonToXml2JobFileIsntWritable", null, null, jsonToXml2JobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageJsonToXml2JobPathIsntAFile", null, null, jsonToXml2JobFile.getAbsolutePath());
            }
        }

        File jsonToXml2ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_json_to_xml_2_job_" + fileIndex + ".xml");

        if (jsonToXml2ResultInfoFile.exists() == true)
        {
            if (jsonToXml2ResultInfoFile.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = jsonToXml2ResultInfoFile.delete();
                }
                catch (SecurityException ex)
                {
                }

                if (deleteSuccessful != true)
                {
                    if (jsonToXml2ResultInfoFile.canWrite() != true)
                    {
                        throw constructTermination("messageJsonToXml2ResultInfoFileIsntWritable", null, null, jsonToXml2ResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageJsonToXml2ResultInfoPathIsntAFile", null, null, jsonToXml2ResultInfoFile.getAbsolutePath());
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(jsonToXml2JobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by wordpress_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
            writer.write("<json-to-xml-2-jobfile>\n");
            writer.write("  <json-input-file path=\"" + jsonFile.getAbsolutePath() + "\"/>\n");
            writer.write("  <xml-output-file path=\"" + xmlFile.getAbsolutePath() + "\"/>\n");
            writer.write("</json-to-xml-2-jobfile>\n");
            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageJsonToXml2JobFileErrorWhileWriting", ex, null, jsonToXml2JobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageJsonToXml2JobFileErrorWhileWriting", ex, null, jsonToXml2JobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageJsonToXml2JobFileErrorWhileWriting", ex, null, jsonToXml2JobFile.getAbsolutePath());
        }

        ProcessBuilder builder = new ProcessBuilder("java", "json_to_xml_2", jsonToXml2JobFile.getAbsolutePath(), jsonToXml2ResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "json_to_xml" + File.separator + "json_to_xml_2"));
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
            throw constructTermination("messageJsonToXml2ErrorWhileReadingOutput", ex, null, jsonToXml2JobFile.getAbsolutePath());
        }

        if (jsonToXml2ResultInfoFile.exists() != true)
        {
            throw constructTermination("messageJsonToXml2ResultInfoFileDoesntExistButShould", null, null, jsonToXml2ResultInfoFile.getAbsolutePath());
        }

        if (jsonToXml2ResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageJsonToXml2ResultInfoPathIsntAFile", null, null, jsonToXml2ResultInfoFile.getAbsolutePath());
        }

        if (jsonToXml2ResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageJsonToXml2ResultInfoFileIsntReadable", null, null, jsonToXml2ResultInfoFile.getAbsolutePath());
        }

        boolean wasSuccess = false;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(jsonToXml2ResultInfoFile);
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
            throw constructTermination("messageJsonToXml2ResultInfoFileErrorWhileReading", ex, null, jsonToXml2ResultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageJsonToXml2ResultInfoFileErrorWhileReading", ex, null, jsonToXml2ResultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageJsonToXml2ResultInfoFileErrorWhileReading", ex, null, jsonToXml2ResultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageJsonToXml2CallWasntSuccessful", null, null, jsonToXml2JobFile.getAbsolutePath());
        }

        return 0;
    }

    protected int TransformXml(File xmlFile, int fileIndex, String programPath, File tempDirectory, File xmlFilePrepared)
    {
        File xmlXsltTransformator1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_xml_xslt_transformator_1_job_" + fileIndex + ".xml");

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
                        throw constructTermination("messageXmlXsltTransformator11JobFileIsntWritable", null, null, xmlXsltTransformator1JobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageXmlXsltTransformator11JobPathIsntAFile", null, null, xmlXsltTransformator1JobFile.getAbsolutePath());
            }
        }

        File xmlXsltTransformator1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_xml_xslt_transformator_1_job_" + fileIndex + ".xml");

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
                        throw constructTermination("messageXmlXsltTransformator1ResultInfoFileIsntWritable", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageXmlXsltTransformator1ResultInfoPathIsntAFile", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
            }
        }

        if (xmlFilePrepared.exists() == true)
        {
            if (xmlFilePrepared.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = xmlFilePrepared.delete();
                }
                catch (SecurityException ex)
                {
                }

                if (deleteSuccessful != true)
                {
                    if (xmlFilePrepared.canWrite() != true)
                    {
                        throw constructTermination("messageXmlFilePreparedIsntWritable", null, null, xmlFilePrepared.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageXmlPathPreparedIsntAFile", null, null, xmlFilePrepared.getAbsolutePath());
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(xmlXsltTransformator1JobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by wordpress_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
            writer.write("<xml-xslt-transformator-1-jobfile>\n");
            writer.write("  <job input-file=\"" + xmlFile.getAbsolutePath() + "\" entities-resolver-config-file=\"" + programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1" + File.separator + "entities" + File.separator + "config_empty.xml\" stylesheet-file=\"" + programPath + "xml_prepare.xsl\" output-file=\"" + xmlFilePrepared.getAbsolutePath() + "\"/>\n");
            writer.write("</xml-xslt-transformator-1-jobfile>\n");
            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageXmlXsltTransformator1JobFileErrorWhileWriting", ex, null, xmlXsltTransformator1JobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageXmlXsltTransformator1JobFileErrorWhileWriting", ex, null, xmlXsltTransformator1JobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageXmlXsltTransformator1JobFileErrorWhileWriting", ex, null, xmlXsltTransformator1JobFile.getAbsolutePath());
        }

        ProcessBuilder builder = new ProcessBuilder("java", "xml_xslt_transformator_1", xmlXsltTransformator1JobFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1"));
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
            throw constructTermination("messageXmlXsltTransformator1ErrorWhileReadingOutput", ex, null, xmlXsltTransformator1JobFile.getAbsolutePath());
        }

        if (xmlXsltTransformator1ResultInfoFile.exists() != true)
        {
            throw constructTermination("messageXmlXsltTransformator1ResultInfoFileDoesntExistButShould", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
        }

        if (xmlXsltTransformator1ResultInfoFile.isFile() != true)
        {
            throw constructTermination("messageXmlXsltTransformator1ResultInfoPathIsntAFile", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
        }

        if (xmlXsltTransformator1ResultInfoFile.canRead() != true)
        {
            throw constructTermination("messageXmlXsltTransformator1ResultInfoFileIsntReadable", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
        }

        boolean wasSuccess = false;

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
            throw constructTermination("messageXmlXsltTransformator1CallWasntSuccessful", null, null, xmlXsltTransformator1JobFile.getAbsolutePath());
        }

        return 0;
    }

    protected int DeescapeXml(File xmlFilePrepared, int pageIndex, String programPath, File tempDirectory, File xmlFileDeescaped)
    {
        if (xmlFileDeescaped.exists() == true)
        {
            if (xmlFileDeescaped.isFile() == true)
            {
                boolean deleteSuccessful = false;

                try
                {
                    deleteSuccessful = xmlFileDeescaped.delete();
                }
                catch (SecurityException ex)
                {
                }

                if (deleteSuccessful != true)
                {
                    if (xmlFileDeescaped.canWrite() != true)
                    {
                        throw constructTermination("messageXmlFileDeescapedIsntWritable", null, null, xmlFileDeescaped.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageXmlPathDeescapedIsntAFile", null, null, xmlFileDeescaped.getAbsolutePath());
            }
        }


        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(xmlFileDeescaped),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by wordpress_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(xmlFilePrepared);
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

                Stack<String> structureStack = new Stack<String>();

                while (eventReader.hasNext() == true)
                {
                    XMLEvent event = eventReader.nextEvent();

                    if (event.isStartElement() == true)
                    {
                        QName elementName = event.asStartElement().getName();
                        String fullElementName = elementName.getLocalPart();

                        if (elementName.getPrefix().isEmpty() != true)
                        {
                            fullElementName = elementName.getPrefix() + ":" + fullElementName;
                        }

                        writer.write("<" + fullElementName);

                        // http://coding.derkeiler.com/Archive/Java/comp.lang.java.help/2008-12/msg00090.html
                        @SuppressWarnings("unchecked")
                        Iterator<Namespace> namespaces = (Iterator<Namespace>)event.asStartElement().getNamespaces();

                        if (namespaces.hasNext() == true)
                        {
                            Namespace namespace = namespaces.next();

                            if (namespace.isDefaultNamespaceDeclaration() == true &&
                                namespace.getPrefix().length() <= 0)
                            {
                                writer.write(" xmlns=\"" + namespace.getNamespaceURI() + "\"");
                            }
                            else
                            {
                                writer.write(" xmlns:" + namespace.getPrefix() + "=\"" + namespace.getNamespaceURI() + "\"");
                            }
                        }

                        // http://coding.derkeiler.com/Archive/Java/comp.lang.java.help/2008-12/msg00090.html
                        @SuppressWarnings("unchecked")
                        Iterator<Attribute> attributes = (Iterator<Attribute>)event.asStartElement().getAttributes();

                        while (attributes.hasNext() == true)
                        {
                            Attribute attribute = attributes.next();
                            QName attributeName = attribute.getName();
                            String fullAttributeName = attributeName.getLocalPart();

                            if (attributeName.getPrefix().length() > 0)
                            {
                                fullAttributeName = attributeName.getPrefix() + ":" + fullAttributeName;
                            }

                            String attributeValue = attribute.getValue();

                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            attributeValue = attributeValue.replaceAll("&", "&amp;");
                            attributeValue = attributeValue.replaceAll("\"", "&quot;");
                            attributeValue = attributeValue.replaceAll("'", "&apos;");
                            attributeValue = attributeValue.replaceAll("<", "&lt;");
                            attributeValue = attributeValue.replaceAll(">", "&gt;");

                            writer.write(" " + fullAttributeName + "=\"" + attributeValue + "\"");
                        }

                        writer.write(">");

                        structureStack.push(fullElementName);
                    }
                    else if (event.isEndElement() == true)
                    {
                        QName elementName = event.asEndElement().getName();
                        String fullElementName = elementName.getLocalPart();

                        if (elementName.getPrefix().isEmpty() != true)
                        {
                            fullElementName = elementName.getPrefix() + ":" + fullElementName;
                        }

                        writer.write("</" + fullElementName + ">");

                        if (fullElementName.equals(structureStack.pop()) != true)
                        {

                        }
                    }
                    else if (event.isCharacters() == true)
                    {
                        String structurePath = "/";

                        for (String elementName : structureStack)
                        {
                            structurePath += elementName + "/";
                        }

                        if (structurePath.equals("/wordpress-posts/wordpress-post/content/") == true)
                        {
                            // The reader already de-escaped the first order of escape-entities.

                            String text = event.asCharacters().getData();

                            for (int i = 0, max = text.length(); i < max; i++)
                            {
                                writer.write(text.charAt(i));
                            }
                        }
                        else
                        {
                            String text = event.asCharacters().getData();

                            for (int i = 0, max = text.length(); i < max; i++)
                            {
                                char character = text.charAt(i);

                                switch (character)
                                {
                                case '&':
                                    writer.write("&amp;");
                                    break;
                                case '<':
                                    writer.write("&lt;");
                                    break;
                                case '>':
                                    writer.write("&gt;");
                                    break;
                                default:
                                    writer.write(character);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            catch (XMLStreamException ex)
            {
                throw constructTermination("messageXmlFilePreparedErrorWhileReading", ex, null, xmlFilePrepared.getAbsolutePath());
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageXmlFilePreparedErrorWhileReading", ex, null, xmlFilePrepared.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageXmlFilePreparedErrorWhileReading", ex, null, xmlFilePrepared.getAbsolutePath());
            }

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageXmlFileDeescapedErrorWhileWriting", ex, null, xmlFileDeescaped.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageXmlFileDeescapedErrorWhileWriting", ex, null, xmlFileDeescaped.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageXmlFileDeescapedErrorWhileWriting", ex, null, xmlFileDeescaped.getAbsolutePath());
        }

        return 0;
    }

    protected int ValidateXml(File xmlFileDeescaped, int pageIndex, String programPath, File tempDirectory)
    {
        /**
         * @todo Validate xmlFileDeescaped according to the allowed HTML tags in
         *     /wordpress-posts/wordpress-post/content/ as specified by
         *     https://core.trac.wordpress.org/browser/tags/4.9.5/src/wp-includes/kses.php#L0.
         */

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
                message = "wordpress_retriever_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "wordpress_retriever_1 workflow: " + getI10nStringFormatted(id, arguments);
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
                message = "wordpress_retriever_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "wordpress_retriever_1 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (wordpress_retriever_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(wordpress_retriever_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wordpress_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<wordpress-retriever-1-workflow-result-information>\n");

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

                writer.write("</wordpress-retriever-1-workflow-result-information>\n");
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

        wordpress_retriever_1.resultInfoFile = null;

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

    private static final String L10N_BUNDLE = "l10n.l10nWordpressRetriever1WorkflowConsole";
    private ResourceBundle l10nConsole;
}
