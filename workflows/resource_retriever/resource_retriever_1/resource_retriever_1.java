/* Copyright (C) 2016-2018  Stephan Kreutzer
 *
 * This file is part of resource_retriever_1 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * resource_retriever_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * resource_retriever_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with resource_retriever_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/resource_retriever/resource_retriever_1/resource_retriever_1.java
 * @brief Attempts to retrieve multiple resources as specified by scheme ("protocol").
 * @author Stephan Kreutzer
 * @since 2017-07-15
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
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.util.Scanner;



public class resource_retriever_1
{
    public static void main(String[] args)
    {
        System.out.print("resource_retriever_1 workflow Copyright (C) 2016-2018 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");

        resource_retriever_1 fulfiller = new resource_retriever_1();

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
                writer.write("<!-- This file was created by resource_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<resource-retriever-1-workflow-result-information>\n");
                writer.write("  <success>\n");

                if (fulfiller.GetRetrievedResourceInfo().size() > 0)
                {
                    writer.write("    <retrieved-resources>\n");

                    for (int i = 0, max = fulfiller.GetRetrievedResourceInfo().size(); i < max; i++)
                    {
                        RetrievedResourceInfo info = fulfiller.GetRetrievedResourceInfo().get(i);

                        String identifier = info.GetIdentifier();
                        int resourceIndex = info.GetIndex();
                        boolean success = info.GetSuccess();
                        File resourceFile = info.GetResourceFile();

                        // Ampersand needs to be the first, otherwise it would double-encode
                        // other entities.
                        identifier = identifier.replaceAll("&", "&amp;");
                        identifier = identifier.replaceAll("<", "&lt;");
                        identifier = identifier.replaceAll(">", "&gt;");
                        identifier = identifier.replaceAll("\"", "&quot;");
                        identifier = identifier.replaceAll("'", "&apos;");

                        writer.write("      <retrieved-resource identifier=\"" + identifier + "\" index=\"" + resourceIndex + "\" ");

                        if (success == true)
                        {
                            writer.write("success=\"true\" ");

                            if (resourceFile != null)
                            {
                                writer.write("path=\"" + resourceFile.getAbsolutePath() + "\" ");
                            }
                        }
                        else
                        {
                            writer.write("success=\"false\" ");
                        }

                        writer.write("/>\n");
                    }

                    writer.write("    </retrieved-resources>\n");
                }

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
                        int resourceIndex = infoMessage.GetResourceIndex();

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

                        if (resourceIndex >= 0)
                        {
                            writer.write("        <resource-index>" + resourceIndex + "</resource-index>\n");
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
                writer.write("</resource-retriever-1-workflow-result-information>\n");
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

    public int fulfill(String[] args)
    {
        this.retrievedResourceInfo.clear();

        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\tresource_retriever_1 " + getI10nString("messageParameterList") + "\n");
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

        resource_retriever_1.resultInfoFile = resultInfoFile;


        String programPath = resource_retriever_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

        System.out.println("resource_retriever_1 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        List<String> requestedResources = new ArrayList<String>();
        File outputDirectory = null;

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

                    if (tagName.equals("resource") == true)
                    {
                        Attribute attributeIdentifier = event.asStartElement().getAttributeByName(new QName("identifier"));

                        if (attributeIdentifier == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "identifier");
                        }

                        String identifier = attributeIdentifier.getValue();

                        if (requestedResources.contains(identifier) != true)
                        {
                            requestedResources.add(identifier);
                        }
                    }
                    else if (tagName.equals("output-directory") == true)
                    {
                        Attribute attributePath = event.asStartElement().getAttributeByName(new QName("path"));

                        if (attributePath == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        outputDirectory = new File(attributePath.getValue());

                        if (outputDirectory.isAbsolute() != true)
                        {
                            outputDirectory = new File(jobFile.getAbsoluteFile().getParent() + File.separator + attributePath.getValue());
                        }

                        if (outputDirectory.exists() == true)
                        {
                            if (outputDirectory.isDirectory() == true)
                            {
                                if (outputDirectory.canWrite() != true)
                                {
                                    throw constructTermination("messageOutputDirectoryIsntWritable", null, null, outputDirectory.getAbsolutePath());
                                }
                            }
                            else
                            {
                                throw constructTermination("messageOutputPathIsntADirectory", null, null, outputDirectory.getAbsolutePath());
                            }
                        }
                        else
                        {
                            try
                            {
                                outputDirectory.mkdirs();
                            }
                            catch (SecurityException ex)
                            {
                                throw constructTermination("messageOutputDirectoryCantCreate", null, null, outputDirectory.getAbsolutePath());
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

        if (requestedResources.isEmpty() == true)
        {
            this.infoMessages.add(constructInfoMessage("messageNoRequestedResources", true, null, null, jobFile.getAbsolutePath()));
            return 0;
        }

        if (outputDirectory == null)
        {
            throw constructTermination("messageJobFileNoOutputDirectory", null, null, jobFile.getAbsolutePath());
        }

        int resourceIndex = 0;

        /** @todo Time delay between retrieval attempts if targeting the same server. */

        for (int i = 0, max = requestedResources.size(); i < max; i++)
        {
            attemptRetrieval(requestedResources.get(i), i, programPath, tempDirectory, outputDirectory, jobFile.getAbsoluteFile().getParent());
        }

        return 0;
    }

    public int attemptRetrieval(String identifier, int resourceIndex, String programPath, File tempDirectory, File outputDirectory, String jobFileDirectory)
    {
        int pos = identifier.indexOf("://");

        if (pos < 0)
        {
            throw constructTermination("messageJobFileIdentifierWithoutProtocol", null, null, identifier);
        }

        String protocol = identifier.substring(0, pos);

        if (protocol.equals("http") == true ||
            protocol.equals("https") == true)
        {
            /** @todo Consider HTTP range requests: https://developer.mozilla.org/en-US/docs/Web/HTTP/Range_requests */

            String url = identifier;

            // Ampersand needs to be the first, otherwise it would double-encode
            // other entities.
            url = url.replaceAll("&", "&amp;");
            url = url.replaceAll("<", "&lt;");
            url = url.replaceAll(">", "&gt;");
            url = url.replaceAll("\"", "&quot;");
            url = url.replaceAll("'", "&apos;");

            File jobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_" + protocol + "_client_1_" + resourceIndex + ".xml");
            File resultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_" + protocol + "_client_1_" + resourceIndex + ".xml");
            File resourceFile = new File(outputDirectory.getAbsolutePath() + File.separator + "resource_" + resourceIndex);

            if (jobFile.exists() == true)
            {
                if (jobFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = jobFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (jobFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1JobFileIsntWritable", resourceIndex, true, null, null, jobFile.getAbsolutePath()));
                            this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                            return -1;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1JobPathExistsButIsntAFile", resourceIndex, true, null, null, jobFile.getAbsolutePath()));
                    this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                    return -1;
                }
            }

            if (resultInfoFile.exists() == true)
            {
                if (resultInfoFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = resultInfoFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (resultInfoFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1ResultInfoFileIsntWritable", resourceIndex, true, null, null, resultInfoFile.getAbsolutePath()));
                            this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                            return -1;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1ResultInfoPathExistsButIsntAFile", resourceIndex, true, null, null, resultInfoFile.getAbsolutePath()));
                    this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                    return -1;
                }
            }

            if (resourceFile.exists() == true)
            {
                if (resourceFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = resourceFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (resourceFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessageWithResourceIndex("messageResourceFileIsntWritable", resourceIndex, true, null, null, resourceFile.getAbsolutePath()));
                            this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                            return -1;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessageWithResourceIndex("messageResourcePathExistsButIsntAFile", resourceIndex, true, null, null, resourceFile.getAbsolutePath()));
                    this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                    return -1;
                }
            }

            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(jobFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by resource_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<" + protocol + "-client-1-jobfile>\n");
                writer.write("  <request url=\"" + url + "\" method=\"GET\"/>\n");
                writer.write("  <response destination=\"" + resourceFile.getAbsolutePath() + "\"/>\n");
                writer.write("</" + protocol + "-client-1-jobfile>\n");
                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1JobFileErrorWhileWriting", resourceIndex, true, ex, null, jobFile.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1JobFileErrorWhileWriting", resourceIndex, true, ex, null, jobFile.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1JobFileErrorWhileWriting", resourceIndex, true, ex, null, jobFile.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }

            ProcessBuilder builder = new ProcessBuilder("java", protocol + "_client_1", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath());
            builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + protocol + "_client" + File.separator + protocol + "_client_1"));
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
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1ErrorWhileReadingOutput", resourceIndex, true, ex, null));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }

            if (resultInfoFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1ResultInfoFileDoesntExistButShould", resourceIndex, true, null, null, resultInfoFile.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }

            if (resultInfoFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1ResultInfoPathExistsButIsntAFile", resourceIndex, true, null, null, resultInfoFile.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }

            if (resultInfoFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1ResultInfoFileIsntReadable", resourceIndex, true, null, null, resultInfoFile.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }

            boolean wasSuccess = false;

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(resultInfoFile);
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
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1ResultInfoFileErrorWhileReading", resourceIndex, true, ex, null, resultInfoFile.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1ResultInfoFileErrorWhileReading", resourceIndex, true, ex, null, resultInfoFile.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1ResultInfoFileErrorWhileReading", resourceIndex, true, ex, null, resultInfoFile.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }

            if (wasSuccess == true)
            {
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, wasSuccess, resourceFile));
            }
            else
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageH" + protocol.substring(1) + "Client1CallWasntSuccessful", resourceIndex, true, null, null, resourceIndex));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }
        }
        else if (protocol.equals("file") == true)
        {
            String path = identifier.substring(pos + new String("://").length());

            File localResource = new File(path);

            if (localResource.isAbsolute() != true)
            {
                localResource = new File(jobFileDirectory + File.separator + path);
            }

            try
            {
                localResource = localResource.getCanonicalFile();
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageLocalResourceFileCantGetCanonicalPath", resourceIndex, true, ex, null, localResource.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageLocalResourceFileCantGetCanonicalPath", resourceIndex, true, ex, null, localResource.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }

            if (localResource.exists() != true)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageLocalResourceFileDoesntExist", resourceIndex, true, null, null, localResource.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }

            if (localResource.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageLocalResourcePathIsntAFile", resourceIndex, true, null, null, localResource.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }

            if (localResource.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessageWithResourceIndex("messageLocalResourceFileIsntReadable", resourceIndex, true, null, null, localResource.getAbsolutePath()));
                this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, false, null));
                return -1;
            }

            // Just passing the file path to the list of retrieved resources.
            this.retrievedResourceInfo.add(new RetrievedResourceInfo(identifier, resourceIndex, true, localResource));
        }
        else
        {
            throw constructTermination("messageProtocolNotSupported", null, null, protocol);
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
                message = "resource_retriever_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "resource_retriever_1 workflow: " + getI10nStringFormatted(id, arguments);
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

    public InfoMessage constructInfoMessageWithResourceIndex(String id,
                                                             int resourceIndex,
                                                             boolean outputToConsole,
                                                             Exception exception,
                                                             String message,
                                                             Object ... arguments)
    {
        InfoMessage info = constructInfoMessage(id, outputToConsole, exception, message, arguments);
        info.SetResourceIndex(resourceIndex);

        return info;
    }

    public ProgramTerminationException constructTermination(String id, Exception cause, String message, Object ... arguments)
    {
        if (message == null)
        {
            if (arguments == null)
            {
                message = "resource_retriever_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "resource_retriever_1 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (resource_retriever_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(resource_retriever_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by resource_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<resource-retriever-1-workflow-result-information>\n");

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

                writer.write("</resource-retriever-1-workflow-result-information>\n");
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

        resource_retriever_1.resultInfoFile = null;

        System.exit(-1);
        return -1;
    }

    public List<RetrievedResourceInfo> GetRetrievedResourceInfo()
    {
        return this.retrievedResourceInfo;
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
    protected List<RetrievedResourceInfo> retrievedResourceInfo = new ArrayList<RetrievedResourceInfo>();

    private static final String L10N_BUNDLE = "l10n.l10nResourceRetriever1WorkflowConsole";
    private ResourceBundle l10nConsole;
}

