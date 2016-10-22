/* Copyright (C) 2014-2016  Stephan Kreutzer
 *
 * This file is part of wordpress_media_library_file_uploader_1 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * wordpress_media_library_file_uploader_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * wordpress_media_library_file_uploader_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with wordpress_media_library_file_uploader_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/wordpress_media_library_file_uploader/wordpress_media_library_file_uploader_1/wordpress_media_library_file_uploader_1.java
 * @brief Uploads files to the WordPress Media Library via the XMLRPC API.
 * @author Stephan Kreutzer
 * @since 2016-02-29
 */



import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Scanner;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.XMLStreamException;



public class wordpress_media_library_file_uploader_1
{
    public static void main(String args[])
    {
        System.out.print("wordpress_media_library_file_uploader_1 workflow Copyright (C) 2014-2016 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");


        wordpress_media_library_file_uploader_1 uploader = new wordpress_media_library_file_uploader_1();

        uploader.getInfoMessages().clear();

        try
        {
            uploader.upload(args);
        }
        catch (ProgramTerminationException ex)
        {
            uploader.handleTermination(ex);
        }

        if (uploader.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(uploader.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wordpress_media_library_file_uploader_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<wordpress-media-library-file-uploader-1-workflow-result-information>\n");
                writer.write("  <success>\n");

                if (uploader.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = uploader.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = uploader.getInfoMessages().get(i);

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
                writer.write("</wordpress-media-library-file-uploader-1-workflow-result-information>\n");
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

        uploader.getInfoMessages().clear();
        uploader.resultInfoFile = null;
    }

    public int upload(String args[]) throws ProgramTerminationException
    {
        this.getInfoMessages().clear();

        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\twordpress_media_library_file_uploader_1 " + getI10nString("messageParameterList") + "\n");
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

        wordpress_media_library_file_uploader_1.resultInfoFile = resultInfoFile;

        String programPath = wordpress_media_library_file_uploader_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

        JobFileProcessor jobFileProcessor = new JobFileProcessor(jobFile);
        jobFileProcessor.Run();

        this.infoMessages.addAll(jobFileProcessor.getInfoMessages());

        System.out.println("wordpress_media_library_file_uploader_1 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));

        if (jobFileProcessor.getInputFiles().isEmpty() == true)
        {
            this.infoMessages.add(constructInfoMessage("messageJobFileNoInputFiles", true, null, null, jobFile.getAbsolutePath()));
            return 1;
        }

        if (jobFileProcessor.getJobSettings().containsKey("wordpress-xmlrpc-url") != true)
        {
            throw constructTermination("messageJobFileNoWordPressXMLRPCURL", null, null, jobFile.getAbsolutePath());
        }

        String xmlrpcURL = jobFileProcessor.getJobSettings().get("wordpress-xmlrpc-url");

        /**
         * @todo At the moment, only WordPress user name and password in the XML-RPC
         *     payload is supported (support for the "Secure XML-RPC" plugin for WordPress
         *     is still missing), therefore the request has to be made via HTTPS.
         */
        if (xmlrpcURL.startsWith("https://") == true)
        {
            xmlrpcURL = xmlrpcURL.substring(new String("https://").length());
        }
        else
        {
            throw constructTermination("messageJobFileXMLRPCURLDoesntStartWithHTTPS", null, null, xmlrpcURL, jobFile.getAbsolutePath());
        }

        int slashPosition = Math.max(xmlrpcURL.indexOf('/'), xmlrpcURL.indexOf('\\'));

        if (slashPosition < 1)
        {
            throw constructTermination("messageJobFileXMLRPCURLDoesntContainHostAndFilePath", null, null, xmlrpcURL, jobFile.getAbsolutePath());
        }

        String host = xmlrpcURL.substring(0, slashPosition);
        String xmlrpcPath = xmlrpcURL.substring(slashPosition);

        if (host.length() <= 0)
        {
            throw constructTermination("messageJobFileXMLRPCURLDoesntContainHost", null, null, xmlrpcURL, jobFile.getAbsolutePath());
        }

        if (xmlrpcPath.length() <= 0)
        {
            throw constructTermination("messageJobFileXMLRPCURLDoesntContainFilePath", null, null, xmlrpcURL, jobFile.getAbsolutePath());
        }

        if (xmlrpcPath.indexOf('/') != 0 &&
            xmlrpcPath.indexOf('\\') != 0)
        {
            throw constructTermination("messageJobFileXMLRPCURLFilePathDoesntStartWithLeadingSlash", null, null, xmlrpcURL, jobFile.getAbsolutePath());
        }

        if (jobFileProcessor.getJobSettings().containsKey("wordpress-user-name") != true)
        {
            throw constructTermination("messageJobFileNoWordPressUserName", null, null, jobFile.getAbsolutePath());
        }

        if (jobFileProcessor.getJobSettings().containsKey("wordpress-user-name") != true)
        {
            throw constructTermination("messageJobFileNoWordPressUserPassword", null, null, jobFile.getAbsolutePath());
        }


        for (int i = 0, max = jobFileProcessor.getInputFiles().size(); i < max; i++)
        {
            InputFileInfo inputFileInfo = jobFileProcessor.getInputFiles().get(i);
            File httpsClient1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_https_client_1_" + i + ".xml");
            File httpsClient1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_https_client_1_" + i + ".xml");
            File xmlRpcDataFile = new File(tempDirectory.getAbsolutePath() + File.separator + "wordpress_xmlrpc_data_" + i + ".xml");
            File responseFile = new File(tempDirectory.getAbsolutePath() + File.separator + "response_" + i + ".xml");

            if (httpsClient1JobFile.exists() == true)
            {
                if (httpsClient1JobFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = httpsClient1JobFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (httpsClient1JobFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileIsntWritable", true, null, null, httpsClient1JobFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobPathExistsButIsntAFile", true, null, null, httpsClient1JobFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                    continue;
                }
            }

            if (httpsClient1ResultInfoFile.exists() == true)
            {
                if (httpsClient1ResultInfoFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = httpsClient1ResultInfoFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (httpsClient1ResultInfoFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileIsntWritable", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoPathExistsButIsntAFile", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                    continue;
                }
            }

            if (xmlRpcDataFile.exists() == true)
            {
                if (xmlRpcDataFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = xmlRpcDataFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (xmlRpcDataFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageXmlRpcDataFileIsntWritable", true, null, null, xmlRpcDataFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageXmlRpcDataPathExistsButIsntAFile", true, null, null, xmlRpcDataFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                    continue;
                }
            }

            if (responseFile.exists() == true)
            {
                if (responseFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = responseFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (responseFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageResponseFileIsntWritable", true, null, null, responseFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageResponsePathExistsButIsntAFile", true, null, null, responseFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                    continue;
                }
            }


            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(xmlRpcDataFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                writer.write("<methodCall>");
                writer.write(  "<methodName>wp.uploadFile</methodName>");
                writer.write(  "<params>");
                writer.write(    "<param>");
                writer.write(      "<value>");
                writer.write(        "<array>");
                writer.write(          "<data>");
                writer.write(            "<value><int></int></value>");

                String wordpressUserName = "";
                String wordpressUserPassword = "";

                if (jobFileProcessor.getJobSettings().containsKey("wordpress-user-name") == true)
                {
                    wordpressUserName = jobFileProcessor.getJobSettings().get("wordpress-user-name");

                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    wordpressUserName = wordpressUserName.replaceAll("&", "&amp;");
                    wordpressUserName = wordpressUserName.replaceAll("<", "&lt;");
                    wordpressUserName = wordpressUserName.replaceAll(">", "&gt;");
                }

                if (jobFileProcessor.getJobSettings().containsKey("wordpress-user-password") == true)
                {
                    wordpressUserPassword = jobFileProcessor.getJobSettings().get("wordpress-user-password");

                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    wordpressUserPassword = wordpressUserPassword.replaceAll("&", "&amp;");
                    wordpressUserPassword = wordpressUserPassword.replaceAll("<", "&lt;");
                    wordpressUserPassword = wordpressUserPassword.replaceAll(">", "&gt;");
                }

                writer.write(            "<value><string>" + wordpressUserName + "</string></value>");
                writer.write(            "<value><string>" + wordpressUserPassword + "</string></value>");
                writer.write(            "<value>");
                writer.write(              "<struct>");
                writer.write(                "<member>");
                writer.write(                  "<name>name</name>");
                writer.write(                  "<value><string>" + inputFileInfo.getName() + "</string></value>");
                writer.write(                "</member>");
                writer.write(                "<member>");
                writer.write(                  "<name>type</name>");
                writer.write(                  "<value><string>" + inputFileInfo.getType() + "</string></value>");
                writer.write(                "</member>");
                writer.write(                "<member>");
                writer.write(                  "<name>bits</name>");
                writer.write(                  "<value>");
                writer.write(                    "<base64>");

                {
                    // Needs to be a multiple of 3, so Base64Coder.encode() can
                    // be used on a stream of byte arrays.
                    byte[] buffer = new byte[1026];

                    FileInputStream reader = null;

                    try
                    {
                        reader = new FileInputStream(inputFileInfo.getInputFile());

                        int bytesRead = reader.read(buffer, 0, buffer.length);

                        while (bytesRead > 0)
                        {
                            writer.write(Base64Coder.encode(buffer, bytesRead));
                            bytesRead = reader.read(buffer, 0, buffer.length);
                        }

                        reader.close();
                    }
                    catch (FileNotFoundException ex)
                    {
                        this.infoMessages.add(constructInfoMessage("messageInputFileErrorWhileReading", true, ex, null, i, inputFileInfo.getInputFile().getAbsolutePath()));
                        continue;
                    }
                    catch (IOException ex)
                    {
                        this.infoMessages.add(constructInfoMessage("messageInputFileErrorWhileReading", true, ex, null, i, inputFileInfo.getInputFile().getAbsolutePath()));
                        continue;
                    }
                }

                writer.write(                    "</base64>");
                writer.write(                  "</value>");
                writer.write(                "</member>");
                writer.write(                "<member>");
                writer.write(                  "<name>overwrite</name>");

                if (inputFileInfo.getOverwrite() == true)
                {
                    writer.write(                  "<value><boolean>1</boolean></value>");
                }
                else
                {
                    writer.write(                  "<value><boolean>0</boolean></value>");
                }

                writer.write(                "</member>");
                writer.write(              "</struct>");
                writer.write(            "</value>");
                writer.write(          "</data>");
                writer.write(        "</array>");
                writer.write(      "</value>");
                writer.write(    "</param>");
                writer.write(  "</params>");
                writer.write("</methodCall>");

                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlRpcDataFileErrorWhileWriting", true, ex, null, xmlRpcDataFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlRpcDataFileErrorWhileWriting", true, ex, null, xmlRpcDataFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlRpcDataFileErrorWhileWriting", true, ex, null, xmlRpcDataFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }

            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(httpsClient1JobFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wordpress_media_library_file_uploader_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<https-client-1-jobfile>\n");
                writer.write("  <request url=\"" + jobFileProcessor.getJobSettings().get("wordpress-xmlrpc-url") + "\" method=\"POST\">\n");
                writer.write("    <header>\n");
                writer.write("      <field name=\"Content-Type\" value=\"text/xml\"/>\n");
                writer.write("      <field name=\"charset\" value=\"utf-8\"/>\n");

                if (jobFileProcessor.getJobSettings().containsKey("http-header-field-authorization") == true)
                {
                    writer.write("      <field name=\"Authorization\" value=\"" + jobFileProcessor.getJobSettings().get("http-header-field-authorization") + "\"/>\n");
                }

                writer.write("    </header>\n");
                writer.write("    <data source=\"" + xmlRpcDataFile.getAbsolutePath() + "\"/>\n");

                if (jobFileProcessor.getJobSettings().containsKey("https-accept-host-for-certificate-mismatch") == true)
                {
                    writer.write("    <!--\n");
                    writer.write("      Only set this if certificates for other hosts should be accepted as valid for the host\n");
                    writer.write("      specified here, which should be the same as the one configured within the request\n");
                    writer.write("      URL above. This is for testing purposes only. If active in a production environment,\n");
                    writer.write("      IT WOULD BREAK YOUR SECURITY!\n");
                    writer.write("    -->\n");
                    writer.write("    <accept-host-for-certificate-mismatch host=\"" + jobFileProcessor.getJobSettings().get("https-accept-host-for-certificate-mismatch") + "\"/>\n");
                }

                writer.write("  </request>\n");
                writer.write("  <response destination=\"" + responseFile.getAbsolutePath() + "\" omit-certificates-info=\"false\"/>\n");
                writer.write("</https-client-1-jobfile>\n");
                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileErrorWhileWriting", true, ex, null, httpsClient1JobFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileErrorWhileWriting", true, ex, null, httpsClient1JobFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileErrorWhileWriting", true, ex, null, httpsClient1JobFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }

            ProcessBuilder builder = new ProcessBuilder("java", "https_client_1", httpsClient1JobFile.getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath());
            builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "https_client" + File.separator + "https_client_1"));
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
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ErrorWhileReadingOutput", true, ex, null, i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }


            if (httpsClient1ResultInfoFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileDoesntExistButShould", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }

            if (httpsClient1ResultInfoFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoPathExistsButIsntAFile", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }

            if (httpsClient1ResultInfoFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileIsntReadable", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }

            boolean wasSuccess = false;

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(httpsClient1ResultInfoFile);
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
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileErrorWhileReading", true, ex, null, httpsClient1ResultInfoFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileErrorWhileReading", true, ex, null, httpsClient1ResultInfoFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileErrorWhileReading", true, ex, null, httpsClient1ResultInfoFile.getAbsolutePath(), i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }

            if (wasSuccess != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1CallWasntSuccessful", true, null, null, i, inputFileInfo.getInputFile().getAbsolutePath()));
                continue;
            }
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
                message = "wordpress_media_library_file_uploader_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "wordpress_media_library_file_uploader_1 workflow: " + getI10nStringFormatted(id, arguments);
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
                message = "wordpress_media_library_file_uploader_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "wordpress_media_library_file_uploader_1 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (wordpress_media_library_file_uploader_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(wordpress_media_library_file_uploader_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by wordpress_media_library_file_uploader_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<wordpress-media-library-file-uploader-1-workflow-result-information>\n");

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

                writer.write("</wordpress-media-library-file-uploader-1-workflow-result-information>\n");
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

        wordpress_media_library_file_uploader_1.resultInfoFile = null;

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

    private static final String L10N_BUNDLE = "l10n.l10nWordpressMediaLibraryFileUploader1WorkflowConsole";
    private ResourceBundle l10nConsole;
}
