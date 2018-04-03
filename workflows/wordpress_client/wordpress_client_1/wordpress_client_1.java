/* Copyright (C) 2016-2018 Stephan Kreutzer
 *
 * This file is part of wordpress_client_1 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * wordpress_client_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * wordpress_client_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with wordpress_client_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/wordpress_client/wordpress_client_1/wordpress_client_1.java
 * @brief Wraps the WordPress API.
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
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.util.Map;
import java.util.HashMap;
import java.net.URLDecoder;
import java.util.Scanner;



public class wordpress_client_1
{
    public static void main(String args[])
    {
        System.out.print("wordpress_client_1 workflow Copyright (C) 2016-2018 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");

        wordpress_client_1 instance = new wordpress_client_1();

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
                writer.write("<!-- This file was created by wordpress_client_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<wordpress-client-1-workflow-result-information>\n");
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

                if (instance.getResultData().size() > 0)
                {
                    writer.write("    <result-data>\n");

                    for (Map.Entry<String, String> entry : instance.getResultData().entrySet())
                    {
                        String name = entry.getKey();
                        // Ampersand needs to be the first, otherwise it would double-encode
                        // other entities.
                        name = name.replaceAll("&", "&amp;");
                        name = name.replaceAll("<", "&lt;");
                        name = name.replaceAll(">", "&gt;");
                        name = name.replaceAll("\"", "&quot;");

                        String value = entry.getValue();
                        // Ampersand needs to be the first, otherwise it would double-encode
                        // other entities.
                        value = value.replaceAll("&", "&amp;");
                        value = value.replaceAll("<", "&lt;");
                        value = value.replaceAll(">", "&gt;");
                        value = value.replaceAll("\"", "&quot;");

                        writer.write("      <data name=\"" + name + "\" value=\"" + value + "\"/>\n");
                    }

                    writer.write("    </result-data>\n");
                }

                writer.write("  </success>\n");
                writer.write("</wordpress-client-1-workflow-result-information>\n");
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
        resultData.clear();

        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\twordpress_client_1 " + getI10nString("messageParameterList") + "\n");
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

        wordpress_client_1.resultInfoFile = resultInfoFile;
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

        System.out.println("wordpress_client_1 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        /** @todo Allow multiple requests in a list and report results individually
          * for each request via a mapping in the result file? */
        String baseUrl = null;
        String action = null;
        Map<String, String> parameters = null;
        File tempDirectory = null;
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

                    if (tagName.equals("request") == true)
                    {
                        StartElement inputElement = event.asStartElement();
                        Attribute attributeUrl = inputElement.getAttributeByName(new QName("url"));

                        if (attributeUrl == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "url");
                        }

                        if (baseUrl != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        baseUrl = attributeUrl.getValue();

                        Attribute attributeAction = inputElement.getAttributeByName(new QName("action"));

                        if (attributeAction == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "action");
                        }

                        if (action != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        action = attributeAction.getValue();

                        if (parameters != null)
                        {
                            throw constructTermination("messageJobFileElementConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        while (eventReader.hasNext() == true)
                        {
                            event = eventReader.nextEvent();

                            if (event.isStartElement() == true)
                            {
                                String tagNameLocal = event.asStartElement().getName().getLocalPart();

                                if (tagNameLocal.equals("parameter") == true)
                                {
                                    StartElement parameterElement = event.asStartElement();
                                    Attribute attributeName = parameterElement.getAttributeByName(new QName("name"));

                                    if (attributeName == null)
                                    {
                                        throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagNameLocal, "name");
                                    }

                                    Attribute attributeValue = parameterElement.getAttributeByName(new QName("value"));

                                    if (attributeValue == null)
                                    {
                                        throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagNameLocal, "value");
                                    }

                                    if (parameters == null)
                                    {
                                        parameters = new HashMap<String, String>();
                                    }

                                    parameters.put(attributeName.getValue(), attributeValue.getValue());
                                }
                            }
                            else if (event.isEndElement() == true)
                            {
                                String tagNameLocal = event.asEndElement().getName().getLocalPart();

                                if (tagNameLocal.equals(tagName) == true)
                                {
                                    break;
                                }
                            }
                        }
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
                        StartElement outputFileElement = event.asStartElement();
                        Attribute attributePath = outputFileElement.getAttributeByName(new QName("path"));

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
                                throw constructTermination("messageOutputFileDoesAlreadyExist", null, null, outputFile.getAbsolutePath());
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

        if (baseUrl == null)
        {
            throw constructTermination("messageJobFileBaseUrlIsntConfigured", null, null, jobFile.getAbsolutePath(), "request");
        }

        if (action == null)
        {
            throw constructTermination("messageJobFileActionIsntConfigured", null, null, jobFile.getAbsolutePath(), "request");
        }

        if (outputFile == null)
        {
            throw constructTermination("messageJobFileOutputFileIsntConfigured", null, null, jobFile.getAbsolutePath(), "output-file");
        }

        String urlScheme = "";

        {
            int posSeparator = baseUrl.indexOf("://");

            if (posSeparator < 0)
            {
                throw constructTermination("messageJobFileBaseUrlWithoutProtocol", null, null, jobFile.getAbsolutePath(), baseUrl);
            }

            urlScheme = baseUrl.substring(0, posSeparator);
        }

        if (urlScheme.equals("http") != true &&
            urlScheme.equals("https") != true)
        {
            throw constructTermination("messageJobFileBaseUrlProtocolNotSupported", null, null, jobFile.getAbsolutePath(), baseUrl, urlScheme);
        }

        String programPath = wordpress_client_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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


        if (action.equals("retrieve-posts-list") == true)
        {
            RetrievePostsList(baseUrl, urlScheme, parameters, tempDirectory, programPath, outputFile);
        }
        else
        {
            throw constructTermination("messageJobFileActionNotSupported", null, null, jobFile.getAbsolutePath(), action);
        }

        return 0;
    }

    public int RetrievePostsList(String baseUrl, String scheme, Map<String, String> parameters, File tempDirectory, String programPath, File outputFile)
    {
        String url = baseUrl;

        if (url.endsWith("/") != true)
        {
            url += "/";
        }

        url += "wp-json/wp/v2/posts";

        if (parameters.size() > 0)
        {
            String arguments = null;

            for (Map.Entry<String, String> entry : parameters.entrySet())
            {
                String key = entry.getKey();

                /*if (key.equals("page") != true)
                {
                    throw constructTermination("messageJobFileRequestParameterNotSupported", null, null, key);
                }*/

                if (arguments == null)
                {
                    arguments = "?";
                }
                else
                {
                    arguments += "&amp;";
                }

                /** @todo URL-escape. */
                arguments += key + "=" + entry.getValue();
            }

            url += arguments;
        }

        File jobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_" + scheme + "_client_1.xml");
        File resultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_" + scheme + "_client_1.xml");

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
                        throw constructTermination("messageH" + scheme.substring(1) + "Client1JobFileIsntWritable", null, null, jobFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageH" + scheme.substring(1) + "Client1JobPathExistsButIsntAFile", null, null, jobFile.getAbsolutePath());
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
                        throw constructTermination("messageH" + scheme.substring(1) + "Client1ResultInfoFileIsntWritable", null, null, resultInfoFile.getAbsolutePath());
                    }
                }
            }
            else
            {
                throw constructTermination("messageH" + scheme.substring(1) + "Client1ResultInfoPathExistsButIsntAFile", null, null, resultInfoFile.getAbsolutePath());
            }
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(jobFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by wordpress_client_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
            writer.write("<" + scheme + "-client-1-jobfile>\n");
            writer.write("  <request url=\"" + url + "\" method=\"GET\"/>\n");
            writer.write("  <response destination=\"" + outputFile.getAbsolutePath() + "\"/>\n");
            writer.write("</" + scheme + "-client-1-jobfile>\n");
            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageH" + scheme.substring(1) + "Client1JobFileErrorWhileWriting", ex, null, jobFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageH" + scheme.substring(1) + "Client1JobFileErrorWhileWriting", ex, null, jobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageH" + scheme.substring(1) + "Client1JobFileErrorWhileWriting", ex, null, jobFile.getAbsolutePath());
        }

        ProcessBuilder builder = new ProcessBuilder("java", scheme + "_client_1", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath());
        builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + scheme + "_client" + File.separator + scheme + "_client_1"));
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
            throw constructTermination("messageH" + scheme.substring(1) + "Client1ErrorWhileReadingOutput", ex, null);
        }

        if (resultInfoFile.exists() != true)
        {
            throw constructTermination("messageH" + scheme.substring(1) + "Client1ResultInfoFileDoesntExistButShould", null, null, resultInfoFile.getAbsolutePath());
        }

        if (resultInfoFile.isFile() != true)
        {
            throw constructTermination("messageH" + scheme.substring(1) + "Client1ResultInfoPathExistsButIsntAFile", null, null, resultInfoFile.getAbsolutePath());
        }

        if (resultInfoFile.canRead() != true)
        {
            throw constructTermination("messageH" + scheme.substring(1) + "Client1ResultInfoFileIsntReadable", null, null, resultInfoFile.getAbsolutePath());
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

                        /** @todo Put that into parse-tree-like methods? */
                        while (eventReader.hasNext() == true)
                        {
                            event = eventReader.nextEvent();

                            if (event.isStartElement() == true)
                            {
                                tagName = event.asStartElement().getName().getLocalPart();

                                if (tagName.equals("response-header-fields") == true &&
                                    wasSuccess == true)
                                {
                                    while (eventReader.hasNext() == true)
                                    {
                                        event = eventReader.nextEvent();

                                        if (event.isStartElement() == true)
                                        {
                                            tagName = event.asStartElement().getName().getLocalPart();
  
                                            if (tagName.equals("field") == true)
                                            {
                                                StartElement elementField = event.asStartElement();
                                                Attribute attributeName = elementField.getAttributeByName(new QName("name"));

                                                if (attributeName == null)
                                                {
                                                    continue;
                                                }

                                                String name = attributeName.getValue();

                                                if (name.equals("X-WP-TotalPages") == true)
                                                {
                                                    StringBuilder sb = new StringBuilder();

                                                    while (eventReader.hasNext() == true)
                                                    {
                                                        event = eventReader.nextEvent();

                                                        if (event.isCharacters() == true)
                                                        {
                                                            sb.append(event.asCharacters());
                                                        }
                                                        else if (event.isEndElement() == true)
                                                        {
                                                            tagName = event.asEndElement().getName().getLocalPart();

                                                            if (tagName.equals("field") == true)
                                                            {
                                                                resultData.put("page-count", sb.toString());
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                                else if (name.equals("Link") == true)
                                                {
                                                    StringBuilder sb = new StringBuilder();

                                                    while (eventReader.hasNext() == true)
                                                    {
                                                        event = eventReader.nextEvent();

                                                        if (event.isCharacters() == true)
                                                        {
                                                            sb.append(event.asCharacters());
                                                        }
                                                        else if (event.isEndElement() == true)
                                                        {
                                                            tagName = event.asEndElement().getName().getLocalPart();

                                                            if (tagName.equals("field") == true)
                                                            {
                                                                String nextLink = ParseNextLink(sb.toString());

                                                                if (nextLink != null)
                                                                {
                                                                    resultData.put("url-next-page", sb.toString());
                                                                }

                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    break;
                                }
                                else if (tagName.equals("http-status-code") == true)
                                {
                                    StringBuilder sb = new StringBuilder();

                                    while (eventReader.hasNext() == true)
                                    {
                                        event = eventReader.nextEvent();

                                        if (event.isCharacters() == true)
                                        {
                                            sb.append(event.asCharacters());
                                        }
                                        else if (event.isEndElement() == true)
                                        {
                                            tagName = event.asEndElement().getName().getLocalPart();

                                            if (tagName.equals("http-status-code") == true)
                                            {
                                                if (sb.toString().equals("200") != true)
                                                {
                                                    wasSuccess = false;
                                                }

                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        break;
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageH" + scheme.substring(1) + "Client1ResultInfoFileErrorWhileReading", ex, null, resultInfoFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageH" + scheme.substring(1) + "Client1ResultInfoFileErrorWhileReading", ex, null, resultInfoFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageH" + scheme.substring(1) + "Client1ResultInfoFileErrorWhileReading", ex, null, resultInfoFile.getAbsolutePath());
        }

        if (wasSuccess != true)
        {
            throw constructTermination("messageH" + scheme.substring(1) + "Client1CallWasntSuccessful", null, null);
        }

        return 0;
    }

    String ParseNextLink(String link)
    {
        /** @todo Write a parser that only returns the URL, and only if it is
          * the "next" link, otherwise null. */
        return link;
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
                message = "wordpress_client_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "wordpress_client_1 workflow: " + getI10nStringFormatted(id, arguments);
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
                message = "wordpress_client_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "wordpress_client_1 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (wordpress_client_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(wordpress_client_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wordpress_client_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<wordpress-client-1-workflow-result-information>\n");

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

                writer.write("</wordpress-client-1-workflow-result-information>\n");
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

        wordpress_client_1.resultInfoFile = null;

        System.exit(-1);
        return -1;
    }

    public List<InfoMessage> getInfoMessages()
    {
        return this.infoMessages;
    }

    public Map<String, String> getResultData()
    {
        return this.resultData;
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

    protected Map<String, String> resultData = new HashMap<String, String>();

    public static File resultInfoFile = null;
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nWordpressClient1WorkflowConsole";
    private ResourceBundle l10nConsole;
}
