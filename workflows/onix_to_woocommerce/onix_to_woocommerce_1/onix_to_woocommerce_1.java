/* Copyright (C) 2016-2017  Stephan Kreutzer
 *
 * This file is part of onix_to_woocommerce_1 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * onix_to_woocommerce_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * onix_to_woocommerce_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with onix_to_woocommerce_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/onix_to_woocommerce/onix_to_woocommerce_1/onix_to_woocommerce_1.java
 * @brief Workflow to automatically send the data of an ONIX file to WooCommerce,
 *     a WordPress plugin, via its REST API (takes JSON as hypermedia format).
 * @details While looping over ONIX input files, exceptions instead of info messages + continue
 *     are only thrown if there's a problem with the formats as produced by the
 *     digital_publishing_workflow_tools package, as it can be expected that the processing
 *     for the next ONIX input file won't be successful either, so those situations are
 *     probably non-continuable.
 * @todo Make sure that httpsClient1JobFile and wordpressMediaLibraryFileUploader1WorkflowJobFile
 *     gets also deleted in case of continue or termination exception.
 * @author Stephan Kreutzer
 * @since 2016-01-02
 */



import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.File;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;



public class onix_to_woocommerce_1
{
    protected onix_to_woocommerce_1()
    {
        // Singleton to protect onix_to_woocommerce_1.resultInfoFile from conflicting use.
    }

    public static onix_to_woocommerce_1 getInstance()
    {
        if (onix_to_woocommerce_1.onix_to_woocommerce_1Instance == null)
        {
            onix_to_woocommerce_1.onix_to_woocommerce_1Instance = new onix_to_woocommerce_1();
        }

        return onix_to_woocommerce_1.onix_to_woocommerce_1Instance;
    }

    public static void main(String args[])
    {
        System.out.print("onix_to_woocommerce_1 workflow Copyright (C) 2016 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");


        onix_to_woocommerce_1 client = onix_to_woocommerce_1.getInstance();

        client.getInfoMessages().clear();

        try
        {
            client.execute(args);
        }
        catch (ProgramTerminationException ex)
        {
            client.handleTermination(ex);
        }

        if (client.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(client.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by onix_to_woocommerce_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<onix-to-woocommerce-1-workflow-result-information>\n");

                if (client.getInfoMessages().size() <= 0)
                {
                    writer.write("  <success/>\n");
                }
                else
                {
                    writer.write("  <success>\n");
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = client.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = client.getInfoMessages().get(i);

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
                    writer.write("  </success>\n");
                }

                writer.write("</onix-to-woocommerce-1-workflow-result-information>\n");
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

        client.getInfoMessages().clear();
        client.resultInfoFile = null;
    }

    public int execute(String args[]) throws ProgramTerminationException
    {
        this.getInfoMessages().clear();

        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\tonix_to_woocommerce_1 " + getI10nString("messageParameterList") + "\n");
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

        onix_to_woocommerce_1.resultInfoFile = resultInfoFile;


        String programPath = onix_to_woocommerce_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

        System.out.println("onix_to_woocommerce_1 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        List<File> inputONIXFiles = new ArrayList<File>();
        String wooCommerceRESTAPIProductsURL = null;
        String wooCommerceRESTAPIPublicKey = null;
        String wooCommerceRESTAPISecretKey = null;
        String httpsAcceptHostForCertificateMismatch = null;
        File wordpressMediaLibraryFileUploader1WorkflowJobFileInput = null;
        File outputDirectory = null;

        try
        {
            boolean inInput = false;
            boolean inSettings = false;
            boolean inOutput = false;

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(jobFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equals("input") == true &&
                        inSettings == false &&
                        inOutput == false)
                    {
                        if (inInput == true)
                        {
                            throw constructTermination("messageJobFileTagNested", null, null, jobFile.getAbsolutePath(), tagName, tagName);
                        }

                        inInput = true;
                    }
                    else if (tagName.equals("settings") == true &&
                             inInput == false &&
                             inOutput == false)
                    {
                        if (inSettings == true)
                        {
                            throw constructTermination("messageJobFileTagNested", null, null, jobFile.getAbsolutePath(), tagName, tagName);
                        }

                        inSettings = true;
                    }
                    else if (tagName.equals("output") == true &&
                             inInput == false &&
                             inSettings == false)
                    {
                        if (inOutput == true)
                        {
                            throw constructTermination("messageJobFileTagNested", null, null, jobFile.getAbsolutePath(), tagName, tagName);
                        }

                        inOutput = true;
                    }
                    else if (tagName.equals("onix-file") == true &&
                             inInput == true)
                    {
                        StartElement onixFileElement = event.asStartElement();
                        Attribute sourceAttribute = onixFileElement.getAttributeByName(new QName("source"));

                        if (sourceAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "source");
                        }

                        String onixFileSource = sourceAttribute.getValue();

                        if (onixFileSource.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), tagName, "source");
                        }

                        File onixFile = new File(onixFileSource);

                        if (onixFile.isAbsolute() != true)
                        {
                            onixFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + onixFileSource);
                        }

                        try
                        {
                            onixFile = onixFile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageJobFileONIXFileCantGetCanonicalPath", ex, null, onixFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageJobFileONIXFileCantGetCanonicalPath", ex, null, onixFile.getAbsolutePath());
                        }

                        if (onixFile.exists() != true)
                        {
                            throw constructTermination("messageJobFileONIXFileDoesntExist", null, null, onixFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (onixFile.isFile() != true)
                        {
                            throw constructTermination("messageJobFileONIXPathIsntAFile", null, null, onixFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (onixFile.canRead() != true)
                        {
                            throw constructTermination("messageJobFileONIXFileIsntReadable", null, null, onixFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        inputONIXFiles.add(onixFile);
                    }
                    else if (tagName.equals("woocommerce-rest-api-products-url") == true &&
                             inSettings == true)
                    {
                        StartElement wooCommerceRESTAPIProductsURLElement = event.asStartElement();
                        Attribute urlAttribute = wooCommerceRESTAPIProductsURLElement.getAttributeByName(new QName("url"));

                        if (urlAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "url");
                        }

                        if (wooCommerceRESTAPIProductsURL != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName, "url");
                        }

                        wooCommerceRESTAPIProductsURL = urlAttribute.getValue();

                        if (wooCommerceRESTAPIProductsURL.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), tagName, "url");
                        }

                        if (wooCommerceRESTAPIProductsURL.startsWith("https://") != true)
                        {
                            throw constructTermination("messageJobFileWooCommerceRESTAPIProductsURLIsntHTTPS", null, null, jobFile.getAbsolutePath(), tagName, "url", "https://");
                        }
                    }
                    else if (tagName.equals("woocommerce-rest-api-public-key") == true &&
                             inSettings == true)
                    {
                        StartElement wooCommerceRESTAPIPublicKeyElement = event.asStartElement();
                        Attribute keyAttribute = wooCommerceRESTAPIPublicKeyElement.getAttributeByName(new QName("key"));

                        if (keyAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "key");
                        }

                        if (wooCommerceRESTAPIPublicKey != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName, "key");
                        }

                        wooCommerceRESTAPIPublicKey = keyAttribute.getValue();

                        if (wooCommerceRESTAPIPublicKey.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), tagName, "key");
                        }
                    }
                    else if (tagName.equals("woocommerce-rest-api-secret-key") == true &&
                             inSettings == true)
                    {
                        StartElement wooCommerceRESTAPISecretKeyElement = event.asStartElement();
                        Attribute keyAttribute = wooCommerceRESTAPISecretKeyElement.getAttributeByName(new QName("key"));

                        if (keyAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "key");
                        }

                        if (wooCommerceRESTAPISecretKey != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName, "key");
                        }

                        wooCommerceRESTAPISecretKey = keyAttribute.getValue();

                        if (wooCommerceRESTAPISecretKey.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), tagName, "key");
                        }
                    }
                    else if (tagName.equals("accept-host-for-certificate-mismatch") == true &&
                             inSettings == true)
                    {
                        StartElement acceptHostForCertificateMismatchElement = event.asStartElement();
                        Attribute hostAttribute = acceptHostForCertificateMismatchElement.getAttributeByName(new QName("host"));

                        if (hostAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "host");
                        }

                        if (httpsAcceptHostForCertificateMismatch != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName, "host");
                        }

                        httpsAcceptHostForCertificateMismatch = hostAttribute.getValue();

                        if (httpsAcceptHostForCertificateMismatch.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), tagName, "host");
                        }
                    }
                    else if (tagName.equals("wordpress-media-library-file-uploader-1-workflow-job-file") == true)
                    {
                        StartElement wordpressMediaLibraryFileUploader1WorkflowJobFileElement = event.asStartElement();
                        Attribute pathAttribute = wordpressMediaLibraryFileUploader1WorkflowJobFileElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        if (wordpressMediaLibraryFileUploader1WorkflowJobFileInput != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName, "key");
                        }

                        wordpressMediaLibraryFileUploader1WorkflowJobFileInput = new File(pathAttribute.getValue());

                        if (wordpressMediaLibraryFileUploader1WorkflowJobFileInput.isAbsolute() != true)
                        {
                            wordpressMediaLibraryFileUploader1WorkflowJobFileInput = new File(jobFile.getAbsoluteFile().getParent() + File.separator + pathAttribute.getValue());
                        }

                        try
                        {
                            wordpressMediaLibraryFileUploader1WorkflowJobFileInput = wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageJobFileWordpressMediaLibraryFileUploader1WorkflowInputJobFileCantGetCanonicalPath", ex, null, jobFile.getAbsolutePath(), wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageJobFileWordpressMediaLibraryFileUploader1WorkflowInputJobFileCantGetCanonicalPath", ex, null, jobFile.getAbsolutePath(), wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath());
                        }

                        if (wordpressMediaLibraryFileUploader1WorkflowJobFileInput.exists() != true)
                        {
                            throw constructTermination("messageJobFileWordpressMediaLibraryFileUploader1WorkflowInputJobFileDoesntExist", null, null, jobFile.getAbsolutePath(), wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath());
                        }

                        if (wordpressMediaLibraryFileUploader1WorkflowJobFileInput.isFile() != true)
                        {
                            throw constructTermination("messageJobFileWordpressMediaLibraryFileUploader1WorkflowInputJobPathIsntAFile", null, null, jobFile.getAbsolutePath(), wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath());
                        }

                        if (wordpressMediaLibraryFileUploader1WorkflowJobFileInput.canRead() != true)
                        {
                            throw constructTermination("messageJobFileWordpressMediaLibraryFileUploader1WorkflowInputJobFileIsntReadable", null, null, jobFile.getAbsolutePath(), wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath());
                        }
                    }
                    else if (tagName.equals("response-directory") == true &&
                             inOutput == true)
                    {
                        StartElement responseDirectoryElement = event.asStartElement();
                        Attribute destinationAttribute = responseDirectoryElement.getAttributeByName(new QName("destination"));

                        if (destinationAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "destination");
                        }

                        if (outputDirectory != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), tagName, "destination");
                        }

                        if (destinationAttribute.getValue().isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), tagName, "destination");
                        }

                        outputDirectory = new File(destinationAttribute.getValue());

                        if (outputDirectory.isAbsolute() != true)
                        {
                            outputDirectory = new File(jobFile.getAbsoluteFile().getParent() + File.separator + destinationAttribute.getValue());
                        }

                        try
                        {
                            outputDirectory = outputDirectory.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageJobFileOutputDirectoryCantGetCanonicalPath", ex, null, jobFile.getAbsolutePath(), outputDirectory.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageJobFileOutputDirectoryCantGetCanonicalPath", ex, null, jobFile.getAbsolutePath(), outputDirectory.getAbsolutePath());
                        }

                        if (outputDirectory.exists() == true)
                        {
                            if (outputDirectory.isDirectory() == true)
                            {
                                if (outputDirectory.canWrite() != true)
                                {
                                    throw constructTermination("messageJobFileOutputDirectoryIsntWritable", null, null, jobFile.getAbsolutePath(), outputDirectory.getAbsolutePath());
                                }
                            }
                            else
                            {
                                throw constructTermination("messageJobFileOutputPathExistsButIsntADirectory", null, null, jobFile.getAbsolutePath(), outputDirectory.getAbsolutePath());
                            }
                        }
                    }
                }
                else if (event.isEndElement() == true)
                {
                    String tagName = event.asEndElement().getName().getLocalPart();

                    if (tagName.equals("input") == true &&
                        inInput == true)
                    {
                        inInput = false;
                    }
                    else if (tagName.equals("settings") == true &&
                             inSettings == true)
                    {
                        inSettings = false;
                    }
                    else if (tagName.equals("output") == true &&
                             inSettings == true)
                    {
                        inSettings = false;
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

        if (inputONIXFiles.isEmpty() == true)
        {
            throw constructTermination("messageJobFileNoONIXInputFiles", null, null, jobFile.getAbsolutePath());
        }

        {
            List<Integer> excludeList = new ArrayList<Integer>();

            for (Integer i = 0, max = inputONIXFiles.size(); i < max; i++)
            {
                if (excludeList.contains(i) == true)
                {
                    continue;
                }

                for (Integer j = i + 1; j < max; j++)
                {
                    if (inputONIXFiles.get(i).equals(inputONIXFiles.get(j)) == true)
                    {
                        if (excludeList.contains(i) != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageJobFileONIXFileSpecifiedMoreThanOnce", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), jobFile.getAbsolutePath()));

                            excludeList.add(i);
                        }

                        excludeList.add(j);
                    }
                }
            }
        }

        if (wooCommerceRESTAPIProductsURL == null)
        {
            throw constructTermination("messageJobFileWooCommerceRESTAPIProductsURLIsntConfigured", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-products-url", "url");
        }

        if (wooCommerceRESTAPIPublicKey == null)
        {
            throw constructTermination("messageJobFileWooCommerceRESTAPIPublicKeyIsntConfigured", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-public-key", "key");
        }

        if (wooCommerceRESTAPISecretKey == null)
        {
            throw constructTermination("messageJobFileWooCommerceRESTAPISecretKeyIsntConfigured", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-secret-key", "key");
        }

        if (wordpressMediaLibraryFileUploader1WorkflowJobFileInput == null)
        {
            throw constructTermination("messageJobFileWordpressMediaLibraryFileUploader1WorkflowInputJobFileIsntConfigured", null, null, jobFile.getAbsolutePath(), "wordpress-media-library-file-uploader-1-workflow-job-file");
        }

        if (outputDirectory.exists() != true)
        {
            try
            {
                outputDirectory.mkdirs();
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageOutputDirectoryCantCreate", ex, null, jobFile.getAbsolutePath(), outputDirectory.getAbsolutePath());
            }
        }


        // Ampersand needs to be the first, otherwise it would double-encode
        // other entities.
        wooCommerceRESTAPIProductsURL = wooCommerceRESTAPIProductsURL.replaceAll("&", "&amp;");
        wooCommerceRESTAPIProductsURL = wooCommerceRESTAPIProductsURL.replaceAll("<", "&lt;");
        wooCommerceRESTAPIProductsURL = wooCommerceRESTAPIProductsURL.replaceAll(">", "&gt;");

        String httpBasicAuth = wooCommerceRESTAPIPublicKey + ":" + wooCommerceRESTAPISecretKey;
        httpBasicAuth = Base64Coder.encodeString(httpBasicAuth);

        // Ampersand needs to be the first, otherwise it would double-encode
        // other entities.
        httpBasicAuth = httpBasicAuth.replaceAll("&", "&amp;");
        httpBasicAuth = httpBasicAuth.replaceAll("<", "&lt;");
        httpBasicAuth = httpBasicAuth.replaceAll(">", "&gt;");

        if (httpsAcceptHostForCertificateMismatch != null)
        {
            // Ampersand needs to be the first, otherwise it would double-encode
            // other entities.
            httpsAcceptHostForCertificateMismatch = httpsAcceptHostForCertificateMismatch.replaceAll("&", "&amp;");
            httpsAcceptHostForCertificateMismatch = httpsAcceptHostForCertificateMismatch.replaceAll("<", "&lt;");
            httpsAcceptHostForCertificateMismatch = httpsAcceptHostForCertificateMismatch.replaceAll(">", "&gt;");
        }

        String wordpressXMLRPCURL = null;
        String wordpressUserName = null;
        String wordpressUserPassword = null;
        String wordpressXMLRPCHeaderFieldAuthorization = null;

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(wordpressMediaLibraryFileUploader1WorkflowJobFileInput);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equalsIgnoreCase("wordpress-xmlrpc-url") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            if (wordpressXMLRPCURL != null)
                            {
                                throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileWordpressXmlRpcUrlConfiguredMoreThanOnce", null, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath(), tagName);
                            }

                            wordpressXMLRPCURL = event.asCharacters().getData();
                        }
                    }
                    else if (tagName.equalsIgnoreCase("wordpress-user-name") == true)
                    {
                        StartElement wordpressUserNameElement = event.asStartElement();
                        Attribute valueAttribute = wordpressUserNameElement.getAttributeByName(new QName("value"));

                        if (valueAttribute == null)
                        {
                            throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileEntryIsMissingAnAttribute", null, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath(), tagName, "value");
                        }

                        if (wordpressUserName != null)
                        {
                            throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileSettingConfiguredMoreThanOnce", null, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath(), tagName, "value");
                        }

                        wordpressUserName = valueAttribute.getValue();
                    }
                    else if (tagName.equalsIgnoreCase("wordpress-user-password") == true)
                    {
                        StartElement wordpressUserPasswordElement = event.asStartElement();
                        Attribute valueAttribute = wordpressUserPasswordElement.getAttributeByName(new QName("value"));

                        if (valueAttribute == null)
                        {
                            throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileEntryIsMissingAnAttribute", null, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath(), tagName, "value");
                        }

                        if (wordpressUserPassword != null)
                        {
                            throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileSettingConfiguredMoreThanOnce", null, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath(), tagName, "value");
                        }

                        wordpressUserPassword = valueAttribute.getValue();
                    }
                    else if (tagName.equalsIgnoreCase("http-header-field-authorization") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            if (wordpressXMLRPCHeaderFieldAuthorization != null)
                            {
                                throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileXmlRpcHeaderFieldAuthorizationConfiguredMoreThanOnce", null, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath(), tagName);
                            }

                            wordpressXMLRPCHeaderFieldAuthorization = event.asCharacters().getData();
                        }
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileErrorWhileReading", ex, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileErrorWhileReading", ex, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileErrorWhileReading", ex, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath());
        }

        if (wordpressXMLRPCURL == null)
        {
            throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileSettingIsntConfigured", null, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath(), "wordpress-xmlrpc-url");
        }

        if (wordpressUserName == null)
        {
            throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileSettingIsntConfigured", null, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath(), "wordpress-user-name");
        }

        if (wordpressUserPassword == null)
        {
            throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowInputJobFileSettingIsntConfigured", null, null, wordpressMediaLibraryFileUploader1WorkflowJobFileInput.getAbsolutePath(), "wordpress-user-password");
        }

        // Ampersand needs to be the first, otherwise it would double-encode
        // other entities.
        wordpressUserName = wordpressUserName.replaceAll("&", "&amp;");
        wordpressUserName = wordpressUserName.replaceAll("<", "&lt;");
        wordpressUserName = wordpressUserName.replaceAll(">", "&gt;");
        wordpressUserName = wordpressUserName.replaceAll("\"", "&quot;");
        wordpressUserName = wordpressUserName.replaceAll("'", "&apos;");

        // Ampersand needs to be the first, otherwise it would double-encode
        // other entities.
        wordpressUserPassword = wordpressUserPassword.replaceAll("&", "&amp;");
        wordpressUserPassword = wordpressUserPassword.replaceAll("<", "&lt;");
        wordpressUserPassword = wordpressUserPassword.replaceAll(">", "&gt;");
        wordpressUserPassword = wordpressUserPassword.replaceAll("\"", "&quot;");
        wordpressUserPassword = wordpressUserPassword.replaceAll("'", "&apos;");

        if (wordpressXMLRPCHeaderFieldAuthorization != null)
        {
            wordpressXMLRPCHeaderFieldAuthorization = wordpressXMLRPCHeaderFieldAuthorization.replaceAll("&", "&amp;");
            wordpressXMLRPCHeaderFieldAuthorization = wordpressXMLRPCHeaderFieldAuthorization.replaceAll("<", "&lt;");
            wordpressXMLRPCHeaderFieldAuthorization = wordpressXMLRPCHeaderFieldAuthorization.replaceAll(">", "&gt;");
        }


        int successCount = 0;
        int max = inputONIXFiles.size();
        Map<Integer, List<MediaLink>> mediaLinks = new HashMap<Integer, List<MediaLink>>();

        for (int i = 0; i < max; i++)
        {
            File xmlXsltTransformator1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_xml_xslt_transformator_1_" + i + ".xml");

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
                            this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileExistsButIsntWritable", true, null, null, xmlXsltTransformator1JobFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobPathExistsButIsntAFile", true, null, null, xmlXsltTransformator1JobFile.getAbsolutePath()));
                    continue;
                }
            }

            File uploadListFile = new File(tempDirectory.getAbsolutePath() + File.separator + "wordpress_media_library_upload_list_" + i + ".xml");

            if (uploadListFile.exists() == true)
            {
                if (uploadListFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = uploadListFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (uploadListFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageUploadListFileExistsButIsntWritable", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), uploadListFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageUploadListPathExistsButIsntAFile", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), uploadListFile.getAbsolutePath()));
                    continue;
                }
            }

            File xmlXsltTransformator1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_xml_xslt_transformator_1_" + i + ".xml");

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
                            this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileExistsButIsntWritable", true, null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoPathExistsButIsntAFile", true, null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                    continue;
                }
            }


            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(xmlXsltTransformator1JobFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by onix_to_woocommerce_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<xml-xslt-transformator-1-jobfile>\n");
                writer.write("  <job input-file=\"" + inputONIXFiles.get(i).getAbsolutePath() + "\" entities-resolver-config-file=\"" + programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1" + File.separator + "entities" + File.separator + "config_onix_2_1_3_short.xml\" stylesheet-file=\"" + programPath + "extract_upload_files_list.xsl\" output-file=\"" + uploadListFile.getAbsolutePath() + "\"/>\n");
                writer.write("</xml-xslt-transformator-1-jobfile>\n");

                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
                continue;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
                continue;
            }

            ProcessBuilder builder = new ProcessBuilder("java", "xml_xslt_transformator_1", xmlXsltTransformator1JobFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
            builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1"));
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
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ErrorWhileReadingOutput", true, ex, null, inputONIXFiles.get(i).getAbsolutePath()));
                continue;
            }

            if (xmlXsltTransformator1ResultInfoFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileDoesntExistButShould", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (xmlXsltTransformator1ResultInfoFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoPathExistsButIsntAFile", true, null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (xmlXsltTransformator1ResultInfoFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileIsntReadable", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
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
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (wasSuccess != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1CallWasntSuccessful", true, null, null, inputONIXFiles.get(i).getAbsolutePath()));
                continue;
            }

            if (uploadListFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageUploadListFileDoesntExistButShould", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), uploadListFile.getAbsolutePath()));
                continue;
            }

            if (uploadListFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageUploadListPathExistsButIsntAFile", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), uploadListFile.getAbsolutePath()));
                continue;
            }

            if (uploadListFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageUploadListFileIsntReadable", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), uploadListFile.getAbsolutePath()));
                continue;
            }

            List<UploadFileMapping> uploadFiles = new ArrayList<UploadFileMapping>();
            boolean abort = false;

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(uploadListFile);
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

                String productID = null;

                while (eventReader.hasNext() == true)
                {
                    XMLEvent event = eventReader.nextEvent();

                    if (event.isStartElement() == true)
                    {
                        String tagName = event.asStartElement().getName().getLocalPart();

                        if (tagName.equals("product") == true)
                        {
                            if (productID != null)
                            {
                                throw constructTermination("messageUploadListFileSettingConfiguredMoreThanOnce", null, null, uploadListFile.getAbsolutePath(), tagName, "id");
                            }

                            StartElement productElement = event.asStartElement();
                            Attribute idAttribute = productElement.getAttributeByName(new QName("id"));

                            if (idAttribute == null)
                            {
                                throw constructTermination("messageUploadListFileEntryIsMissingAnAttribute", null, null, uploadListFile.getAbsolutePath(), tagName, "id");
                            }

                            productID = idAttribute.getValue();
                        }
                        else if (tagName.equals("upload") == true &&
                                 productID != null)
                        {
                            StartElement uploadElement = event.asStartElement();
                            Attribute pathAttribute = uploadElement.getAttributeByName(new QName("path"));

                            if (pathAttribute == null)
                            {
                                throw constructTermination("messageUploadListFileEntryIsMissingAnAttribute", null, null, uploadListFile.getAbsolutePath(), tagName, "path");
                            }

                            File uploadFile = new File(pathAttribute.getValue());

                            if (uploadFile.isAbsolute() != true)
                            {
                                uploadFile = new File(inputONIXFiles.get(i).getAbsoluteFile().getParent() + File.separator + pathAttribute.getValue());
                            }

                            try
                            {
                                uploadFile = uploadFile.getCanonicalFile();
                            }
                            catch (SecurityException ex)
                            {
                                this.infoMessages.add(constructInfoMessage("messageUploadFileCantGetCanonicalPath", true, ex, null, uploadFile.getAbsolutePath(), uploadListFile.getAbsolutePath()));
                                abort = true;
                                break;
                            }
                            catch (IOException ex)
                            {
                                this.infoMessages.add(constructInfoMessage("messageUploadFileCantGetCanonicalPath", true, ex, null, uploadFile.getAbsolutePath(), uploadListFile.getAbsolutePath()));
                                abort = true;
                                break;
                            }

                            if (uploadFile.exists() != true)
                            {
                                this.infoMessages.add(constructInfoMessage("messageUploadFileDoesntExist", true, null, null, uploadFile.getAbsolutePath(), uploadListFile.getAbsolutePath()));
                                abort = true;
                                break;
                            }

                            if (uploadFile.isFile() != true)
                            {
                                this.infoMessages.add(constructInfoMessage("messageUploadPathIsntAFile", true, null, null, uploadFile.getAbsolutePath(), uploadListFile.getAbsolutePath()));
                                abort = true;
                                break;
                            }

                            if (uploadFile.canRead() != true)
                            {
                                this.infoMessages.add(constructInfoMessage("messageUploadFileIsntReadable", true, null, null, uploadFile.getAbsolutePath(), uploadListFile.getAbsolutePath()));
                                abort = true;
                                break;
                            }

                            /** @todo Check, if file is already in the list. */
                            uploadFiles.add(new UploadFileMapping(uploadFile, uploadFile.getName()));
                        }
                    }
                    else if (event.isEndElement() == true)
                    {
                        String tagName = event.asEndElement().getName().getLocalPart();

                        if (tagName.equals("product") == true)
                        {
                            productID = null;
                        }
                    }
                }
            }
            catch (XMLStreamException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageUploadListFileErrorWhileReading", true, ex, null, uploadListFile.getAbsolutePath()));
                continue;
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageUploadListFileErrorWhileReading", true, ex, null, uploadListFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageUploadListFileErrorWhileReading", true, ex, null, uploadListFile.getAbsolutePath()));
                continue;
            }

            if (abort == true)
            {
                continue;
            }

            if (uploadFiles.isEmpty() == true)
            {
                continue;
            }

            File wordpressMediaLibraryFileUploader1WorkflowJobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_wordpress_media_library_file_uploader_1_" + i + ".xml");
            File wordpressMediaLibraryFileUploader1WorkflowResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_wordpress_media_library_file_uploader_1_" + i + ".xml");

            if (wordpressMediaLibraryFileUploader1WorkflowJobFile.exists() == true)
            {
                if (wordpressMediaLibraryFileUploader1WorkflowJobFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = wordpressMediaLibraryFileUploader1WorkflowJobFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (wordpressMediaLibraryFileUploader1WorkflowJobFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1JobFileExistsButIsntWritable", true, null, null, wordpressMediaLibraryFileUploader1WorkflowJobFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1JobPathExistsButIsntAFile", true, null, null, wordpressMediaLibraryFileUploader1WorkflowJobFile.getAbsolutePath()));
                    continue;
                }
            }

            if (wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.exists() == true)
            {
                if (wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1ResultInfoFileExistsButIsntWritable", true, null, null, wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1ResultInfoPathExistsButIsntAFile", true, null, null, wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath()));
                    continue;
                }
            }

            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(wordpressMediaLibraryFileUploader1WorkflowJobFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by onix_to_woocommerce_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<wordpress-media-library-file-uploader-1-workflow-job>\n");
                writer.write("  <input-files>\n");

                for (UploadFileMapping mapping : uploadFiles)
                {
                    String mimeType = null;

                    {
                        String fileName = mapping.getSourceFile().getName();
                        int extensionPos = fileName.lastIndexOf('.');

                        if (extensionPos > 0)
                        {
                            String extension = fileName.substring(extensionPos + 1);

                            if (extension.equals("epub") == true)
                            {
                                mimeType = "application/epub+zip";
                            }
                            else if (extension.equals("png") == true)
                            {
                                mimeType = "image/png";
                            }
                            else if (extension.equals("jpg") == true ||
                                     extension.equals("jpeg") == true)
                            {
                                mimeType = "image/jpeg";
                            }
                            else
                            {
                                this.infoMessages.add(constructInfoMessage("messageUploadListFileFiletypeNotSupported", true, null, null, mapping.getSourceFile().getAbsolutePath(), extension, uploadListFile.getAbsolutePath()));
                                continue;
                            }
                        }
                        else
                        {
                            this.infoMessages.add(constructInfoMessage("messageUploadListFileNoExtension", true, null, null, mapping.getSourceFile().getAbsolutePath(), uploadListFile.getAbsolutePath()));
                            continue;
                        }
                    }

                    if (mimeType == null)
                    {
                        this.infoMessages.add(constructInfoMessage("messageUploadListFileNoMimetypeDetermined", true, null, null, mapping.getSourceFile().getAbsolutePath(), uploadListFile.getAbsolutePath()));
                        continue;
                    }

                    writer.write("    <input-file path=\"" + mapping.getSourceFile().getAbsolutePath() + "\" name=\"" + mapping.getDestinationName() + "\" type=\"" + mimeType + "\" overwrite=\"true\"/>\n");
                }

                writer.write("  </input-files>\n");
                writer.write("  <wordpress-xmlrpc-url>" + wordpressXMLRPCURL + "</wordpress-xmlrpc-url>\n");
                writer.write("  <wordpress-user-name value=\"" + wordpressUserName + "\"/>\n");
                writer.write("  <wordpress-user-password value=\"" + wordpressUserPassword + "\"/>\n");

                if (httpsAcceptHostForCertificateMismatch != null)
                {
                    writer.write("  <!--\n");
                    writer.write("    This setting for accepting any server certificate for\n");
                    writer.write("    the host configured here BREAKS YOUR SECURITY!\n");
                    writer.write("  -->\n");
                    writer.write("  <https-accept-host-for-certificate-mismatch>" + httpsAcceptHostForCertificateMismatch + "</https-accept-host-for-certificate-mismatch>\n");
                }

                if (wordpressXMLRPCHeaderFieldAuthorization != null)
                {
                    writer.write("  <http-header-field-authorization>" + wordpressXMLRPCHeaderFieldAuthorization + "</http-header-field-authorization>\n");
                }

                writer.write("</wordpress-media-library-file-uploader-1-workflow-job>\n");

                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowJobFileWritingError", true, ex, null, wordpressMediaLibraryFileUploader1WorkflowJobFile.getAbsolutePath()));
                continue;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowJobFileWritingError", true, ex, null, wordpressMediaLibraryFileUploader1WorkflowJobFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowJobFileWritingError", true, ex, null, wordpressMediaLibraryFileUploader1WorkflowJobFile.getAbsolutePath()));
                continue;
            }

            builder = new ProcessBuilder("java", "wordpress_media_library_file_uploader_1", wordpressMediaLibraryFileUploader1WorkflowJobFile.getAbsolutePath(), wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath());
            builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + "wordpress_media_library_file_uploader" + File.separator + "wordpress_media_library_file_uploader_1"));
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
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowErrorWhileReadingOutput", true, ex, null, wordpressMediaLibraryFileUploader1WorkflowJobFile.getAbsolutePath()));
                continue;
            }

            try
            {
                boolean deleteSuccessful = wordpressMediaLibraryFileUploader1WorkflowJobFile.delete();

                if (deleteSuccessful == false)
                {
                    this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowJobFileCouldntDeleteAPIKeysExposed", true, null, null, wordpressMediaLibraryFileUploader1WorkflowJobFile.getAbsolutePath()));
                }
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowJobFileCouldntDeleteAPIKeysExposed", true, ex, null, wordpressMediaLibraryFileUploader1WorkflowJobFile.getAbsolutePath()));
            }

            if (wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowResultInfoFileDoesntExist", true, null, null, wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowResultInfoPathExistsButIsntAFile", true, null, null, wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowResultInfoFileIsntReadable", true, null, null, wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath()));
                continue;
            }

            mediaLinks.put(i, new ArrayList<MediaLink>());
            wasSuccess = false;

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(wordpressMediaLibraryFileUploader1WorkflowResultInfoFile);
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
                        else if (tagName.equals("media-link") == true)
                        {
                            StartElement elementMediaLink = event.asStartElement();
                            Attribute attributeInputName = elementMediaLink.getAttributeByName(new QName("input-name"));
                            Attribute attributeLink = elementMediaLink.getAttributeByName(new QName("link"));

                            if (attributeInputName == null)
                            {
                                throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowResultInfoFileIsMissingAnAttribute", null, null, wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath(), tagName, "input-name");
                            }

                            if (attributeLink == null)
                            {
                                throw constructTermination("messageWordpressMediaLibraryFileUploader1WorkflowResultInfoFileIsMissingAnAttribute", null, null, wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath(), tagName, "link");
                            }

                            mediaLinks.get(i).add(new MediaLink(attributeInputName.getValue(), attributeLink.getValue()));
                        }
                    }
                }
            }
            catch (XMLStreamException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowResultInfoFileErrorWhileReading", true, ex, null, wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowResultInfoFileErrorWhileReading", true, ex, null, wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowResultInfoFileErrorWhileReading", true, ex, null, wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (wasSuccess != true)
            {
                this.infoMessages.add(constructInfoMessage("messageWordpressMediaLibraryFileUploader1WorkflowCallWasntSuccessful", true, null, null, wordpressMediaLibraryFileUploader1WorkflowResultInfoFile.getAbsolutePath()));
                continue;
            }
        }

        for (int i = 0; i < max; i++)
        {
            File onixPrepareForJson1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_onix_prepare_for_json_1.xml");

            if (onixPrepareForJson1JobFile.exists() == true)
            {
                if (onixPrepareForJson1JobFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = onixPrepareForJson1JobFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (onixPrepareForJson1JobFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1JobFileExistsButIsntWritable", true, null, null, onixPrepareForJson1JobFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1JobPathExistsButIsntAFile", true, null, null, onixPrepareForJson1JobFile.getAbsolutePath()));
                    continue;
                }
            }

            File onixPrepareForJson1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_onix_prepare_for_json_1.xml");

            if (onixPrepareForJson1ResultInfoFile.exists() == true)
            {
                if (onixPrepareForJson1ResultInfoFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = onixPrepareForJson1ResultInfoFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (onixPrepareForJson1ResultInfoFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1ResultInfoFileExistsButIsntWritable", true, null, null, onixPrepareForJson1ResultInfoFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1ResultInfoPathExistsButIsntAFile", true, null, null, onixPrepareForJson1ResultInfoFile.getAbsolutePath()));
                    continue;
                }
            }

            File onixInputPreparedFile = new File(tempDirectory.getAbsolutePath() + File.separator + "input_prepared_" + i + ".xml");

            if (onixInputPreparedFile.exists() == true)
            {
                if (onixInputPreparedFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = onixInputPreparedFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (onixInputPreparedFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageOnixInputPreparedFileExistsButIsntWritable", true, null, null, onixInputPreparedFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageOnixInputPreparedPathExistsButIsntAFile", true, null, null, onixInputPreparedFile.getAbsolutePath()));
                    continue;
                }
            }

            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(onixPrepareForJson1JobFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by onix_to_woocommerce_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<onix-prepare-for-json-1-jobfile>\n");
                writer.write("  <input-file path=\"" + inputONIXFiles.get(i).getAbsolutePath() + "\"/>\n");
                writer.write("  <output-file path=\"" + onixInputPreparedFile.getAbsolutePath() + "\"/>\n");
                writer.write("</onix-prepare-for-json-1-jobfile>\n");

                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), onixPrepareForJson1JobFile.getAbsolutePath()));
                continue;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), onixPrepareForJson1JobFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), onixPrepareForJson1JobFile.getAbsolutePath()));
                continue;
            }


            ProcessBuilder builder = new ProcessBuilder("java", "onix_prepare_for_json_1", onixPrepareForJson1JobFile.getAbsolutePath(), onixPrepareForJson1ResultInfoFile.getAbsolutePath());
            builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "onix_prepare_for_json" + File.separator + "onix_prepare_for_json_1"));
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
                this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1ErrorWhileReadingOutput", true, ex, null, inputONIXFiles.get(i).getAbsolutePath()));
                continue;
            }

            if (onixPrepareForJson1ResultInfoFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1ResultInfoFileDoesntExistButShould", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), onixPrepareForJson1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (onixPrepareForJson1ResultInfoFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1ResultInfoPathExistsButIsntAFile", true, null, null, onixPrepareForJson1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (onixPrepareForJson1ResultInfoFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1ResultInfoFileIsntReadable", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), onixPrepareForJson1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            boolean wasSuccess = false;

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(onixPrepareForJson1ResultInfoFile);
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
                this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1ResultInfoFileErrorWhileReading", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), onixPrepareForJson1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1ResultInfoFileErrorWhileReading", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), onixPrepareForJson1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1ResultInfoFileErrorWhileReading", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), onixPrepareForJson1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (wasSuccess != true)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixPrepareForJson1CallWasntSuccessful", true, null, null, inputONIXFiles.get(i).getAbsolutePath()));
                continue;
            }

            if (onixInputPreparedFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixInputPreparedFileDoesntExistButShould", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), onixInputPreparedFile.getAbsolutePath()));
                continue;
            }

            if (onixInputPreparedFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixInputPreparedPathExistsButIsntAFile", true, null, null, onixInputPreparedFile.getAbsolutePath()));
                continue;
            }

            if (onixInputPreparedFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageOnixInputPreparedFileIsntReadable", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), onixInputPreparedFile.getAbsolutePath()));
                continue;
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
                            this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileExistsButIsntWritable", true, null, null, xmlXsltTransformator1JobFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobPathExistsButIsntAFile", true, null, null, xmlXsltTransformator1JobFile.getAbsolutePath()));
                    continue;
                }
            }

            File requestJSONFile = new File(tempDirectory.getAbsolutePath() + File.separator + "request_" + i + ".json");

            if (requestJSONFile.exists() == true)
            {
                if (requestJSONFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = requestJSONFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (requestJSONFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageJSONRequestFileExistsButIsntWritable", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), requestJSONFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageJSONRequestPathExistsButIsntAFile", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), requestJSONFile.getAbsolutePath()));
                    continue;
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
                            this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileExistsButIsntWritable", true, null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoPathExistsButIsntAFile", true, null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                    continue;
                }
            }


            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(xmlXsltTransformator1JobFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by onix_to_woocommerce_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<xml-xslt-transformator-1-jobfile>\n");
                writer.write("  <job input-file=\"" + onixInputPreparedFile.getAbsolutePath() + "\" entities-resolver-config-file=\"" + programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1" + File.separator + "entities" + File.separator + "config_onix_2_1_3_short.xml\" stylesheet-file=\"" + programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "onix_to_woocommerce" + File.separator + "onix_to_woocommerce_1" + File.separator + "onix_to_woocommerce_1.xsl\" output-file=\"" + requestJSONFile.getAbsolutePath() + "\"/>\n");
                writer.write("</xml-xslt-transformator-1-jobfile>\n");

                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
                continue;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
                continue;
            }

            builder = new ProcessBuilder("java", "xml_xslt_transformator_1", xmlXsltTransformator1JobFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
            builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1"));
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
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ErrorWhileReadingOutput", true, ex, null, inputONIXFiles.get(i).getAbsolutePath()));
                continue;
            }

            if (xmlXsltTransformator1ResultInfoFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileDoesntExistButShould", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (xmlXsltTransformator1ResultInfoFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoPathExistsButIsntAFile", true, null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (xmlXsltTransformator1ResultInfoFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileIsntReadable", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
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
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (wasSuccess != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1CallWasntSuccessful", true, null, null, inputONIXFiles.get(i).getAbsolutePath()));
                continue;
            }

            if (requestJSONFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageJSONRequestFileDoesntExistButShould", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), requestJSONFile.getAbsolutePath()));
                continue;
            }

            if (requestJSONFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageJSONRequestPathExistsButIsntAFile", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), requestJSONFile.getAbsolutePath()));
                continue;
            }

            if (requestJSONFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageJSONRequestFileIsntReadable", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), requestJSONFile.getAbsolutePath()));
                continue;
            }

            if (requestJSONFile.canWrite() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageJSONRequestFileIsntWritable", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), requestJSONFile.getAbsolutePath()));
                continue;
            }

            if (mediaLinks.containsKey(i) == true)
            {
                if (mediaLinks.get(i).size() > 0)
                {
                    File txtreplace1ReplacementDictionary = new File(tempDirectory + File.separator + "txtreplace1_replacement_dictionary.xml");

                    if (txtreplace1ReplacementDictionary.exists() == true)
                    {
                        if (txtreplace1ReplacementDictionary.isFile() == true)
                        {
                            boolean deleteSuccessful = false;

                            try
                            {
                                deleteSuccessful = txtreplace1ReplacementDictionary.delete();
                            }
                            catch (SecurityException ex)
                            {

                            }

                            if (deleteSuccessful != true)
                            {
                                if (txtreplace1ReplacementDictionary.canWrite() != true)
                                {
                                    this.infoMessages.add(constructInfoMessage("messageTxtreplace1ReplacementDictionaryFileIsntWritable", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), txtreplace1ReplacementDictionary.getAbsolutePath()));
                                    continue;
                                }
                            }
                        }
                        else
                        {
                            this.infoMessages.add(constructInfoMessage("messageTxtreplace1ReplacementDictionaryPathIsntAFile", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), txtreplace1ReplacementDictionary.getAbsolutePath()));
                            continue;
                        }
                    }

                    try
                    {
                        BufferedWriter writer = new BufferedWriter(
                                                new OutputStreamWriter(
                                                new FileOutputStream(txtreplace1ReplacementDictionary),
                                                "UTF-8"));

                        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                        writer.write("<!-- This file was created by onix_to_woocommerce_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                        writer.write("<txtreplace1-replacement-dictionary>\n");

                        for (MediaLink mediaLink : mediaLinks.get(i))
                        {
                            String inputName = mediaLink.GetInputName();
                            String link = mediaLink.GetLink();

                            inputName = inputName.replaceAll("&", "&amp;");
                            inputName = inputName.replaceAll("<", "&lt;");
                            inputName = inputName.replaceAll(">", "&gt;");

                            link = link.replaceAll("&", "&amp;");
                            link = link.replaceAll("/", "\\/");

                            writer.write("  <replace>\n");
                            writer.write("    <pattern>wordpress-media-library-file-uploader-1-workflow-media-link-" + inputName + "</pattern>\n");
                            writer.write("    <replacement>" + link + "</replacement>\n");
                            writer.write("  </replace>\n");
                        }

                        writer.write("</txtreplace1-replacement-dictionary>\n");

                        writer.flush();
                        writer.close();
                    }
                    catch (FileNotFoundException ex)
                    {
                        this.infoMessages.add(constructInfoMessage("messageTxtreplace1ReplacementDictionaryFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), txtreplace1ReplacementDictionary.getAbsolutePath()));
                        continue;
                    }
                    catch (UnsupportedEncodingException ex)
                    {
                        this.infoMessages.add(constructInfoMessage("messageTxtreplace1ReplacementDictionaryFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), txtreplace1ReplacementDictionary.getAbsolutePath()));
                        continue;
                    }
                    catch (IOException ex)
                    {
                        this.infoMessages.add(constructInfoMessage("messageTxtreplace1ReplacementDictionaryFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), txtreplace1ReplacementDictionary.getAbsolutePath()));
                        continue;
                    }

                    builder = new ProcessBuilder("java", "txtreplace1", requestJSONFile.getAbsolutePath(), txtreplace1ReplacementDictionary.getAbsolutePath(), requestJSONFile.getAbsolutePath());
                    builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "automated_digital_publishing" + File.separator + "txtreplace" + File.separator + "txtreplace1"));
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
                        this.infoMessages.add(constructInfoMessage("messageTxtreplace1ErrorWhileReadingOutput", true, ex, null, inputONIXFiles.get(i).getAbsolutePath()));
                        continue;
                    }
                }
            }


            File httpsClient1JobFile = new File(tempDirectory + File.separator + "jobfile_https_client_1.xml");

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
                            this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileIsntWritable", true, null, null, httpsClient1JobFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobPathIsntAFile", true, null, null, httpsClient1JobFile.getAbsolutePath()));
                    continue;
                }
            }

            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(httpsClient1JobFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by onix_to_woocommerce_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<https-client-1-jobfile>\n");
                writer.write("  <request url=\"" + wooCommerceRESTAPIProductsURL + "\" method=\"POST\">\n");
                writer.write("    <header>\n");
                writer.write("      <field name=\"Authorization\" value=\"Basic " + httpBasicAuth + "\"/>\n");
                writer.write("      <field name=\"Content-Type\" value=\"application/json\"/>\n");
                writer.write("      <field name=\"charset\" value=\"utf-8\"/>\n");
                writer.write("    </header>\n");
                writer.write("    <data source=\"" + requestJSONFile.getAbsolutePath() + "\"/>\n");

                if (httpsAcceptHostForCertificateMismatch != null)
                {
                    writer.write("    <!--\n");
                    writer.write("      This setting for accepting any server certificate for\n");
                    writer.write("      the host configured here BREAKS YOUR SECURITY!\n");
                    writer.write("    -->\n");
                    writer.write("    <accept-host-for-certificate-mismatch host=\"" + httpsAcceptHostForCertificateMismatch + "\"/>\n");
                }

                writer.write("  </request>\n");
                writer.write("  <response destination=\"" + outputDirectory.getAbsolutePath() + File.separator + "response_" + i + ".json\"/>\n");
                writer.write("</https-client-1-jobfile>\n");

                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), httpsClient1JobFile.getAbsolutePath()));
                continue;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), httpsClient1JobFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileWritingError", true, ex, null, inputONIXFiles.get(i).getAbsolutePath(), httpsClient1JobFile.getAbsolutePath()));
                continue;
            }

            File httpsClient1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_https_client_1.xml");

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
                            this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileExistsButIsntWritable", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoPathExistsButIsntAFile", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                    continue;
                }
            }

            builder = new ProcessBuilder("java", "https_client_1", httpsClient1JobFile.getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath());
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
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ErrorWhileReadingOutput", true, ex, null, inputONIXFiles.get(i).getAbsolutePath()));
                continue;
            }

            try
            {
                boolean deleteSuccessful = httpsClient1JobFile.delete();

                if (deleteSuccessful == false)
                {
                    this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileCouldntDeleteAPIKeysExposed", true, null, null, httpsClient1JobFile.getAbsolutePath()));
                }
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileCouldntDeleteAPIKeysExposed", true, ex, null, httpsClient1JobFile.getAbsolutePath()));
            }

            if (httpsClient1ResultInfoFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileDoesntExistButShould", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (httpsClient1ResultInfoFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoPathExistsButIsntAFile", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (httpsClient1ResultInfoFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileIsntReadable", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            wasSuccess = false;

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
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileErrorWhileReading", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileErrorWhileReading", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1ResultInfoFileErrorWhileReading", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (wasSuccess == true)
            {
                successCount++;
            }
            else
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1CallWasntSuccessful", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath()));
                continue;
            }
        }

        this.infoMessages.add(constructInfoMessage("messageResult", true, null, null, successCount, max));

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
                message = "onix_to_woocommerce_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "onix_to_woocommerce_1 workflow: " + getI10nStringFormatted(id, arguments);
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
                message = "onix_to_woocommerce_1 workflow: " + getI10nString(id);
            }
            else
            {
                message = "onix_to_woocommerce_1 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (onix_to_woocommerce_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(onix_to_woocommerce_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by onix_to_woocommerce_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<onix-to-woocommerce-1-workflow-result-information>\n");

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

                writer.write("</onix-to-woocommerce-1-workflow-result-information>\n");
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

        onix_to_woocommerce_1.resultInfoFile = null;

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

    private static onix_to_woocommerce_1 onix_to_woocommerce_1Instance;

    public static File resultInfoFile = null;
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nOnixToWoocommerce1WorkflowConsole";
    private ResourceBundle l10nConsole;
}

