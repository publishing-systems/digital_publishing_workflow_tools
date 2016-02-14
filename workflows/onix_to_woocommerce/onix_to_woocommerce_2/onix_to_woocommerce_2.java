/* Copyright (C) 2016  Stephan Kreutzer
 *
 * This file is part of onix_to_woocommerce_2 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * onix_to_woocommerce_2 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * onix_to_woocommerce_2 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with onix_to_woocommerce_2 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/onix_to_woocommerce/onix_to_woocommerce_2/onix_to_woocommerce_2.java
 * @brief Workflow to automatically find and upload/update products from ONIX input files,
 *     found in directories.
 * @author Stephan Kreutzer
 * @since 2016-02-05
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
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator; 



public class onix_to_woocommerce_2
{
    protected onix_to_woocommerce_2()
    {
        // Singleton to protect onix_to_woocommerce_2.resultInfoFile from conflicting use.
    }

    public static onix_to_woocommerce_2 getInstance()
    {
        if (onix_to_woocommerce_2.onix_to_woocommerce_2Instance == null)
        {
            onix_to_woocommerce_2.onix_to_woocommerce_2Instance = new onix_to_woocommerce_2();
        }

        return onix_to_woocommerce_2.onix_to_woocommerce_2Instance;
    }

    public static void main(String args[])
    {
        System.out.print("onix_to_woocommerce_2 workflow Copyright (C) 2016 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");


        onix_to_woocommerce_2 client = onix_to_woocommerce_2.getInstance();

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
                writer.write("<!-- This file was created by onix_to_woocommerce_2 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<onix-to-woocommerce-2-workflow-result-information>\n");

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

                writer.write("</onix-to-woocommerce-2-workflow-result-information>\n");
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
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\tonix_to_woocommerce_2 " + getI10nString("messageParameterList") + "\n");
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

        onix_to_woocommerce_2.resultInfoFile = resultInfoFile;


        String programPath = onix_to_woocommerce_2.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

        System.out.println("onix_to_woocommerce_2 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        File fileDiscovery1Jobfile = null;
        File onixToWoocommerce1WorkflowJobfile = null;

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

                    if (tagName.equals("file-discovery-1-jobfile") == true)
                    {
                        StartElement fileDiscovery1JobfileElement = event.asStartElement();
                        Attribute pathAttribute = fileDiscovery1JobfileElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        String fileDiscovery1JobfilePath = pathAttribute.getValue();

                        if (fileDiscovery1JobfilePath.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        fileDiscovery1Jobfile = new File(fileDiscovery1JobfilePath);

                        if (fileDiscovery1Jobfile.isAbsolute() != true)
                        {
                            fileDiscovery1Jobfile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + fileDiscovery1JobfilePath);
                        }

                        try
                        {
                            fileDiscovery1Jobfile = fileDiscovery1Jobfile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageFileDiscovery1JobfileCantGetCanonicalPath", ex, null, new File(fileDiscovery1JobfilePath).getAbsolutePath(), jobFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageFileDiscovery1JobfileCantGetCanonicalPath", ex, null, new File(fileDiscovery1JobfilePath).getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (fileDiscovery1Jobfile.exists() != true)
                        {
                            throw constructTermination("messageFileDiscovery1JobfileDoesntExist", null, null, fileDiscovery1Jobfile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (fileDiscovery1Jobfile.isFile() != true)
                        {
                            throw constructTermination("messageFileDiscovery1JobfilePathIsntAFile", null, null, fileDiscovery1Jobfile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (fileDiscovery1Jobfile.canRead() != true)
                        {
                            throw constructTermination("messageFileDiscovery1JobfileIsntReadable", null, null, fileDiscovery1Jobfile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }
                    }
                    else if (tagName.equals("onix-to-woocommerce-1-workflow-jobfile") == true)
                    {
                        StartElement onixToWoocommerce1WorkflowJobfileElement = event.asStartElement();
                        Attribute pathAttribute = onixToWoocommerce1WorkflowJobfileElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        String onixToWoocommerce1WorkflowJobfilePath = pathAttribute.getValue();

                        if (onixToWoocommerce1WorkflowJobfilePath.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        onixToWoocommerce1WorkflowJobfile = new File(onixToWoocommerce1WorkflowJobfilePath);

                        if (onixToWoocommerce1WorkflowJobfile.isAbsolute() != true)
                        {
                            onixToWoocommerce1WorkflowJobfile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + onixToWoocommerce1WorkflowJobfilePath);
                        }

                        try
                        {
                            onixToWoocommerce1WorkflowJobfile = onixToWoocommerce1WorkflowJobfile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobfileCantGetCanonicalPath", ex, null, new File(onixToWoocommerce1WorkflowJobfilePath).getAbsolutePath(), jobFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobfileCantGetCanonicalPath", ex, null, new File(onixToWoocommerce1WorkflowJobfilePath).getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (onixToWoocommerce1WorkflowJobfile.exists() != true)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobfileDoesntExist", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (onixToWoocommerce1WorkflowJobfile.isFile() != true)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobfilePathIsntAFile", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (onixToWoocommerce1WorkflowJobfile.canRead() != true)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobfileIsntReadable", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), jobFile.getAbsolutePath());
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


        if (fileDiscovery1Jobfile == null)
        {
            throw constructTermination("messageJobFileNoFileDiscovery1Jobfile", null, null, jobFile.getAbsolutePath());
        }

        if (onixToWoocommerce1WorkflowJobfile == null)
        {
            throw constructTermination("messageJobFileNoOnixToWoocommerce1WorkflowJobfile", null, null, jobFile.getAbsolutePath());
        }


        String wooCommerceRESTAPIProductsURL = null;
        /**
         * @todo Always delete all temporary files which contain authentication credentials.
         */
        String wooCommerceRESTAPIPublicKey = null;
        String wooCommerceRESTAPISecretKey = null;
        String httpsAcceptHostForCertificateMismatch = null;

        try
        {
            boolean inSettings = false;

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(onixToWoocommerce1WorkflowJobfile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equals("settings") == true)
                    {
                        if (inSettings == true)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileTagNested", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, tagName);
                        }

                        inSettings = true;
                    }
                    else if (tagName.equals("woocommerce-rest-api-products-url") == true &&
                             inSettings == true)
                    {
                        StartElement wooCommerceRESTAPIProductsURLElement = event.asStartElement();
                        Attribute urlAttribute = wooCommerceRESTAPIProductsURLElement.getAttributeByName(new QName("url"));

                        if (urlAttribute == null)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileEntryIsMissingAnAttribute", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "url");
                        }

                        if (wooCommerceRESTAPIProductsURL != null)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileSettingConfiguredMoreThanOnce", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "url");
                        }

                        wooCommerceRESTAPIProductsURL = urlAttribute.getValue();

                        if (wooCommerceRESTAPIProductsURL.isEmpty() == true)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileAttributeValueIsEmpty", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "url");
                        }

                        if (wooCommerceRESTAPIProductsURL.startsWith("https://") != true)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileWooCommerceRESTAPIProductsURLIsntHTTPS", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "url", "https://");
                        }
                    }
                    else if (tagName.equals("woocommerce-rest-api-public-key") == true &&
                             inSettings == true)
                    {
                        StartElement wooCommerceRESTAPIPublicKeyElement = event.asStartElement();
                        Attribute keyAttribute = wooCommerceRESTAPIPublicKeyElement.getAttributeByName(new QName("key"));

                        if (keyAttribute == null)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileEntryIsMissingAnAttribute", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "key");
                        }

                        if (wooCommerceRESTAPIPublicKey != null)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileSettingConfiguredMoreThanOnce", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "key");
                        }

                        wooCommerceRESTAPIPublicKey = keyAttribute.getValue();

                        if (wooCommerceRESTAPIPublicKey.isEmpty() == true)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileAttributeValueIsEmpty", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "key");
                        }
                    }
                    else if (tagName.equals("woocommerce-rest-api-secret-key") == true &&
                             inSettings == true)
                    {
                        StartElement wooCommerceRESTAPISecretKeyElement = event.asStartElement();
                        Attribute keyAttribute = wooCommerceRESTAPISecretKeyElement.getAttributeByName(new QName("key"));

                        if (keyAttribute == null)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileEntryIsMissingAnAttribute", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "key");
                        }

                        if (wooCommerceRESTAPISecretKey != null)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileSettingConfiguredMoreThanOnce", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "key");
                        }

                        wooCommerceRESTAPISecretKey = keyAttribute.getValue();

                        if (wooCommerceRESTAPISecretKey.isEmpty() == true)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileAttributeValueIsEmpty", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "key");
                        }
                    }
                    else if (tagName.equals("accept-host-for-certificate-mismatch") == true &&
                             inSettings == true)
                    {
                        StartElement acceptHostForCertificateMismatchElement = event.asStartElement();
                        Attribute hostAttribute = acceptHostForCertificateMismatchElement.getAttributeByName(new QName("host"));

                        if (hostAttribute == null)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileEntryIsMissingAnAttribute", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "host");
                        }

                        if (httpsAcceptHostForCertificateMismatch != null)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileSettingConfiguredMoreThanOnce", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "host");
                        }

                        httpsAcceptHostForCertificateMismatch = hostAttribute.getValue();

                        if (httpsAcceptHostForCertificateMismatch.isEmpty() == true)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileAttributeValueIsEmpty", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), tagName, "host");
                        }
                    }
                }
                else if (event.isEndElement() == true)
                {
                    String tagName = event.asEndElement().getName().getLocalPart();

                    if (tagName.equals("settings") == true &&
                        inSettings == true)
                    {
                        inSettings = false;
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileErrorWhileReading", ex, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileErrorWhileReading", ex, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileErrorWhileReading", ex, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath());
        }

        if (wooCommerceRESTAPIProductsURL == null)
        {
            throw constructTermination("messageeWooCommerceRESTAPIProductsURLIsntConfigured", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), "woocommerce-rest-api-products-url", "url");
        }

        if (wooCommerceRESTAPIPublicKey == null)
        {
            throw constructTermination("messageWooCommerceRESTAPIPublicKeyIsntConfigured", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), "woocommerce-rest-api-public-key", "key");
        }

        if (wooCommerceRESTAPISecretKey == null)
        {
            throw constructTermination("messageWooCommerceRESTAPISecretKeyIsntConfigured", null, null, onixToWoocommerce1WorkflowJobfile.getAbsolutePath(), "woocommerce-rest-api-secret-key", "key");
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


        List<File> inputFiles = new ArrayList<File>();

        {
            File fileDiscovery1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_file_discovery_1.xml");

            if (fileDiscovery1ResultInfoFile.exists() == true)
            {
                if (fileDiscovery1ResultInfoFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = fileDiscovery1ResultInfoFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (fileDiscovery1ResultInfoFile.canWrite() != true)
                        {
                            throw constructTermination("messageFileDiscovery1ResultInfoFileExistsButIsntWritable", null, null, fileDiscovery1ResultInfoFile.getAbsolutePath());
                        }
                    }
                }
                else
                {
                    throw constructTermination("messageFileDiscovery1ResultInfoPathExistsButIsntAFile", null, null, fileDiscovery1ResultInfoFile.getAbsolutePath());
                }
            }

            ProcessBuilder builder = new ProcessBuilder("java", "file_discovery_1", fileDiscovery1Jobfile.getAbsolutePath(), fileDiscovery1ResultInfoFile.getAbsolutePath());
            builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "file_discovery" + File.separator + "file_discovery_1"));
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
                throw constructTermination("messageFileDiscovery1ErrorWhileReadingOutput", ex, null);
            }


            if (fileDiscovery1ResultInfoFile.exists() != true)
            {
                throw constructTermination("messageFileDiscovery1ResultInfoFileDoesntExistButShould", null, null, fileDiscovery1ResultInfoFile.getAbsolutePath());
            }

            if (fileDiscovery1ResultInfoFile.isFile() != true)
            {
                throw constructTermination("messageFileDiscovery1ResultInfoPathExistsButIsntAFile", null, null, fileDiscovery1ResultInfoFile.getAbsolutePath());
            }

            if (fileDiscovery1ResultInfoFile.canRead() != true)
            {
                throw constructTermination("messageFileDiscovery1ResultInfoFileIsntReadable", null, null, fileDiscovery1ResultInfoFile.getAbsolutePath());
            }

            boolean wasSuccess = false;

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(fileDiscovery1ResultInfoFile);
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
                throw constructTermination("messageFileDiscovery1ResultInfoFileErrorWhileReading", null, null, fileDiscovery1ResultInfoFile.getAbsolutePath());
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageFileDiscovery1ResultInfoFileErrorWhileReading", null, null, fileDiscovery1ResultInfoFile.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageFileDiscovery1ResultInfoFileErrorWhileReading", null, null, fileDiscovery1ResultInfoFile.getAbsolutePath());
            }

            if (wasSuccess != true)
            {
                throw constructTermination("messageFileDiscovery1CallWasntSuccessful", null, null);
            }


            File fileDiscovery1ResultFile = null;

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(fileDiscovery1Jobfile);
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

                while (eventReader.hasNext() == true)
                {
                    XMLEvent event = eventReader.nextEvent();

                    if (event.isStartElement() == true)
                    {
                        String tagName = event.asStartElement().getName().getLocalPart();

                        if (tagName.equals("result-file") == true)
                        {
                            StartElement resultFileElement = event.asStartElement();
                            Attribute pathAttribute = resultFileElement.getAttributeByName(new QName("path"));

                            if (pathAttribute == null)
                            {
                                throw constructTermination("messageFileDiscovery1JobFileEntryIsMissingAnAttribute", null, null, fileDiscovery1Jobfile.getAbsolutePath(), tagName, "path");
                            }

                            String fileDiscovery1ResultFilePath = pathAttribute.getValue();

                            if (fileDiscovery1ResultFilePath.isEmpty() == true)
                            {
                                throw constructTermination("messageFileDiscovery1JobFileAttributeValueIsEmpty", null, null, fileDiscovery1Jobfile.getAbsolutePath(), tagName, "path");
                            }

                            fileDiscovery1ResultFile = new File(fileDiscovery1ResultFilePath);

                            if (fileDiscovery1ResultFile.isAbsolute() != true)
                            {
                                fileDiscovery1ResultFile = new File(fileDiscovery1Jobfile.getAbsoluteFile().getParent() + File.separator + fileDiscovery1ResultFilePath);
                            }

                            try
                            {
                                fileDiscovery1ResultFile = fileDiscovery1ResultFile.getCanonicalFile();
                            }
                            catch (SecurityException ex)
                            {
                                throw constructTermination("messageFileDiscovery1ResultFileCantGetCanonicalPath", ex, null, new File(fileDiscovery1ResultFilePath).getAbsolutePath(), fileDiscovery1Jobfile.getAbsolutePath());
                            }
                            catch (IOException ex)
                            {
                                throw constructTermination("messageFileDiscovery1ResultFileCantGetCanonicalPath", ex, null, new File(fileDiscovery1ResultFilePath).getAbsolutePath(), fileDiscovery1Jobfile.getAbsolutePath());
                            }

                            if (fileDiscovery1ResultFile.exists() != true)
                            {
                                throw constructTermination("messageFileDiscovery1ResultFileDoesntExistButShould", null, null, fileDiscovery1ResultFile.getAbsolutePath());
                            }

                            if (fileDiscovery1ResultFile.isFile() != true)
                            {
                                throw constructTermination("messageFileDiscovery1ResultPathExistsButIsntAFile", null, null, fileDiscovery1ResultFile.getAbsolutePath());
                            }

                            if (fileDiscovery1ResultFile.canRead() != true)
                            {
                                throw constructTermination("messageFileDiscovery1ResultFileIsntReadable", null, null, fileDiscovery1ResultFile.getAbsolutePath());
                            }
                        }
                    }
                }
            }
            catch (XMLStreamException ex)
            {
                throw constructTermination("messageFileDiscovery1JobFileErrorWhileReading", ex, null, fileDiscovery1Jobfile.getAbsolutePath());
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageFileDiscovery1JobFileErrorWhileReading", ex, null, fileDiscovery1Jobfile.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageFileDiscovery1JobFileErrorWhileReading", ex, null, fileDiscovery1Jobfile.getAbsolutePath());
            }

            if (fileDiscovery1ResultFile == null)
            {
                throw constructTermination("messageFileDiscovery1JobFileNoResultFile", null, null, fileDiscovery1Jobfile.getAbsolutePath());
            }


            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(fileDiscovery1ResultFile);
                XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

                while (eventReader.hasNext() == true)
                {
                    XMLEvent event = eventReader.nextEvent();

                    if (event.isStartElement() == true)
                    {
                        String tagName = event.asStartElement().getName().getLocalPart();

                        if (tagName.equals("entry") == true)
                        {
                            StartElement entryElement = event.asStartElement();
                            Attribute typeAttribute = entryElement.getAttributeByName(new QName("type"));
                            Attribute pathAttribute = entryElement.getAttributeByName(new QName("path"));

                            if (typeAttribute == null)
                            {
                                throw constructTermination("messageFileDiscovery1ResultFileEntryIsMissingAnAttribute", null, null, fileDiscovery1ResultFile.getAbsolutePath(), tagName, "type");
                            }

                            String type = typeAttribute.getValue();

                            if (type.equals("file") != true)
                            {
                                continue;
                            }

                            if (pathAttribute == null)
                            {
                                throw constructTermination("messageFileDiscovery1ResultFileEntryIsMissingAnAttribute", null, null, fileDiscovery1ResultFile.getAbsolutePath(), tagName, "path");
                            }

                            String path = pathAttribute.getValue();

                            if (path.isEmpty() == true)
                            {
                                throw constructTermination("messageFileDiscovery1ResultFileAttributeValueIsEmpty", null, null, fileDiscovery1ResultFile.getAbsolutePath(), tagName, "path");
                            }

                            File inputFile = new File(path);

                            if (inputFile.isAbsolute() != true)
                            {
                                inputFile = new File(fileDiscovery1ResultFile.getAbsoluteFile().getParent() + File.separator + path);
                            }

                            try
                            {
                                inputFile = inputFile.getCanonicalFile();
                            }
                            catch (SecurityException ex)
                            {
                                this.infoMessages.add(constructInfoMessage("messageInputFileCantGetCanonicalPath", true, ex, null, new File(path).getAbsolutePath(), fileDiscovery1ResultFile.getAbsolutePath()));
                                continue;
                            }
                            catch (IOException ex)
                            {
                                this.infoMessages.add(constructInfoMessage("messageInputFileCantGetCanonicalPath", true, ex, null, new File(path).getAbsolutePath(), fileDiscovery1ResultFile.getAbsolutePath()));
                                continue;
                            }

                            if (inputFile.exists() != true)
                            {
                                this.infoMessages.add(constructInfoMessage("messageInputFileDoesntExist", true, null, null, inputFile.getAbsolutePath(), fileDiscovery1ResultFile.getAbsolutePath()));
                                continue;
                            }

                            if (inputFile.isFile() != true)
                            {
                                this.infoMessages.add(constructInfoMessage("messageInputPathIsntAFile", true, null, null, inputFile.getAbsolutePath()));
                                continue;
                            }

                            if (inputFile.canRead() != true)
                            {
                                this.infoMessages.add(constructInfoMessage("messageInputFileIsntReadable", true, null, null, inputFile.getAbsolutePath()));
                                continue;
                            }

                            /** @todo Check if files get inserted multiple times. */
                            inputFiles.add(inputFile);
                        }
                    }
                }
            }
            catch (XMLStreamException ex)
            {
                throw constructTermination("messageFileDiscovery1ResultFileErrorWhileReading", ex, null, fileDiscovery1ResultFile.getAbsolutePath());
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageFileDiscovery1ResultFileErrorWhileReading", ex, null, fileDiscovery1ResultFile.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageFileDiscovery1ResultFileErrorWhileReading", ex, null, fileDiscovery1ResultFile.getAbsolutePath());
            }
        }

        if (inputFiles.size() <= 0)
        {
            ProgramTerminationException ex = constructTermination("messageNoInputFiles", null, null);
            ex.setNormalTermination(true);
            throw ex;
        }

        /**
         * @todo This should be a separate ONIX file type identifier tool, which may have a
         *     ONIXFileInfo class that takes ONIX version, short or long version, message or
         *     update format, identified by DOCTYPE or XML namespace.
         */

        List<File> onixFiles = new ArrayList<File>();

        {
            String doctypeDeclaration = new String("<!DOCTYPE");
            String doctypeDeclarationName = new String("ONIXmessage");
            String doctypeIdentifier = new String("\"http://www.editeur.org/onix/2.1/short/onix-international.dtd\"");
            String xmlNamespaceTag = new String("<ONIXmessage");
            String xmlNamespaceIdentifier = new String("xmlns=\"http://www.editeur.org/onix/2.1/short\"");

            for (File inputFile : inputFiles)
            {
                boolean isONIX_2_1_short_message_doctype = false;
                boolean isONIX_2_1_short_message_namespace = false;

                int doctypeDeclarationPosMatching = 0;
                int doctypeDeclarationNamePosMatching = 0;
                int doctypeIdentifierPosMatching = 0;
                int xmlNamespaceTagPosMatching = 0;
                int xmlNamespaceIdentifierPosMatching = 0;

                try
                {
                    FileInputStream in = new FileInputStream(inputFile);

                    int currentByte = 0;

                    do
                    {
                        currentByte = in.read();

                        if (currentByte < 0 ||
                            currentByte > 255)
                        {
                            break;
                        }

                        char currentByteCharacter = (char) currentByte;

                        if (doctypeDeclarationPosMatching < doctypeDeclaration.length())
                        {
                            if (currentByteCharacter == doctypeDeclaration.charAt(doctypeDeclarationPosMatching))
                            {
                                doctypeDeclarationPosMatching++;
                            }
                            else
                            {
                                doctypeDeclarationPosMatching = 0;
                            }
                        }

                        if (xmlNamespaceTagPosMatching < xmlNamespaceTag.length())
                        {
                            if (currentByteCharacter == xmlNamespaceTag.charAt(xmlNamespaceTagPosMatching))
                            {
                                xmlNamespaceTagPosMatching++;
                            }
                            else
                            {
                                xmlNamespaceTagPosMatching = 0;
                            }
                        }

                        if (doctypeDeclarationPosMatching < doctypeDeclaration.length() &&
                            xmlNamespaceTagPosMatching < xmlNamespaceTag.length())
                        {
                            continue;
                        }

                        if (currentByteCharacter == '>')
                        {
                            // Both end with '>'.
                            doctypeDeclarationPosMatching = 0;
                            xmlNamespaceTagPosMatching = 0;
                            continue;
                        }

                        if (doctypeDeclarationPosMatching >= doctypeDeclaration.length())
                        {
                            xmlNamespaceTagPosMatching = 0;

                            if (doctypeDeclarationNamePosMatching < doctypeDeclarationName.length())
                            {
                                if (currentByteCharacter == doctypeDeclarationName.charAt(doctypeDeclarationNamePosMatching))
                                {
                                    doctypeDeclarationNamePosMatching++;
                                }
                                else
                                {
                                    doctypeDeclarationNamePosMatching = 0;
                                }
                            }

                            if (doctypeDeclarationNamePosMatching < doctypeDeclarationName.length())
                            {
                                continue;
                            }

                            if (doctypeIdentifierPosMatching < doctypeIdentifier.length())
                            {
                                if (currentByteCharacter == doctypeIdentifier.charAt(doctypeIdentifierPosMatching))
                                {
                                    doctypeIdentifierPosMatching++;
                                }
                                else
                                {
                                    doctypeIdentifierPosMatching = 0;
                                }
                            }

                            if (doctypeIdentifierPosMatching < doctypeIdentifier.length())
                            {
                                continue;
                            }
                            else
                            {
                                isONIX_2_1_short_message_doctype = true;
                                break;
                            }
                        }
                        else if (xmlNamespaceTagPosMatching >= xmlNamespaceTag.length())
                        {
                            doctypeDeclarationPosMatching = 0;

                            if (xmlNamespaceIdentifierPosMatching < xmlNamespaceIdentifier.length())
                            {
                                if (currentByteCharacter == xmlNamespaceIdentifier.charAt(xmlNamespaceIdentifierPosMatching))
                                {
                                    xmlNamespaceIdentifierPosMatching++;
                                }
                                else
                                {
                                    xmlNamespaceIdentifierPosMatching = 0;
                                }
                            }

                            if (xmlNamespaceIdentifierPosMatching < xmlNamespaceIdentifier.length())
                            {
                                continue;
                            }
                            else
                            {
                                isONIX_2_1_short_message_namespace = true;
                                break;
                            }
                        }

                    } while (true);
                }
                catch (FileNotFoundException ex)
                {
                    throw constructTermination("messageInputFileErrorWhileReading", ex, null, inputFile.getAbsolutePath());
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageInputFileErrorWhileReading", ex, null, inputFile.getAbsolutePath());
                }

                if (isONIX_2_1_short_message_doctype == true)
                {
                    onixFiles.add(inputFile);
                }
                else if (isONIX_2_1_short_message_namespace == true)
                {
                    onixFiles.add(inputFile);
                }
                else
                {
                    if (inputFile.getName().endsWith(".xml") == true)
                    {
                        this.infoMessages.add(constructInfoMessage("messageInputFileXMLNotRecognizedAsSupportedONIXFormat", true, null, null, inputFile.getAbsolutePath()));
                    }
                    else
                    {
                        // Not ONIX related.
                    }
                }
            }
        }

        Map<String, ONIXFileInfo> onixFileInfos = new HashMap<String, ONIXFileInfo>();

        for (int i = 0; i < onixFiles.size(); i++)
        {
            File onixFile = onixFiles.get(i);
            File xmlXsltTransformator1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_xml_xslt_transformator_1_job_" + i + ".xml");
            File xmlXsltTransformator1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_xml_xslt_transformator_1_job_" + i + ".xml");
            File onixExtractedISBNFile = new File(tempDirectory.getAbsolutePath() + File.separator + "onix_extracted_isbn_job_" + i + ".txt");

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
                            this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileExistsButIsntWritable", true, null, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobPathExistsButIsntAFile", true, null, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
                    continue;
                }
            }

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
                            this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileExistsButIsntWritable", true, null, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoPathExistsButIsntAFile", true, null, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                    continue;
                }
            }

            if (onixExtractedISBNFile.exists() == true)
            {
                if (onixExtractedISBNFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = onixExtractedISBNFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (onixExtractedISBNFile.canWrite() != true)
                        {
                            this.infoMessages.add(constructInfoMessage("messageExtractedISBNFileExistsButIsntWritable", true, null, null, i, onixFile.getAbsolutePath(), onixExtractedISBNFile.getAbsolutePath()));
                            continue;
                        }
                    }
                }
                else
                {
                    this.infoMessages.add(constructInfoMessage("messageExtractedISBNPathExistsButIsntAFile", true, null, null, i, onixFile.getAbsolutePath(), onixExtractedISBNFile.getAbsolutePath()));
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
                writer.write("<!-- This file was created by onix_to_woocommerce_2 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<xml-xslt-transformator-1-jobfile>\n");
                writer.write("  <job input-file=\"" + onixFile.getAbsolutePath() + "\" entities-resolver-config-file=\"" + programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1" + File.separator + "entities" + File.separator + "config_onix_2_1_3_short.xml\" stylesheet-file=\"" + programPath + "onix_2_1_short_message_extract_isbn.xsl\" output-file=\"" + onixExtractedISBNFile.getAbsolutePath() + "\"/>\n");
                writer.write("</xml-xslt-transformator-1-jobfile>\n");

                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileWritingError", true, ex, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
                continue;
            }
            catch (UnsupportedEncodingException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileWritingError", true, ex, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1JobFileWritingError", true, ex, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
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
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ErrorWhileReadingOutput", true, ex, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1JobFile.getAbsolutePath()));
                continue;
            }

            if (xmlXsltTransformator1ResultInfoFile.exists() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileDoesntExistButShould", true, null, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (xmlXsltTransformator1ResultInfoFile.isFile() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoPathExistsButIsntAFile", true, null, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (xmlXsltTransformator1ResultInfoFile.canRead() != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileIsntReadable", true, null, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
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
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", true, null, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", true, null, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1ResultInfoFileErrorWhileReading", true, null, null, i, onixFile.getAbsolutePath(), xmlXsltTransformator1ResultInfoFile.getAbsolutePath()));
                continue;
            }

            if (wasSuccess != true)
            {
                this.infoMessages.add(constructInfoMessage("messageXmlXsltTransformator1CallWasntSuccessful", true, null, null, i, onixFile.getAbsolutePath()));
                continue;
            }


            String isbn = null;

            try
            {
                StringBuilder sbISBN = new StringBuilder();

                BufferedReader reader = new BufferedReader(
                                        new FileReader(onixExtractedISBNFile));

                isbn = reader.readLine();

                while (isbn != null)
                {
                    sbISBN.append(isbn);
                    isbn = reader.readLine();
                }

                reader.close();

                isbn = sbISBN.toString();
            }
            catch (FileNotFoundException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageExtractedISBNFileReadingError", true, null, null, i, onixFile.getAbsolutePath(), onixExtractedISBNFile.getAbsolutePath()));
                continue;
            }
            catch (IOException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageExtractedISBNFileReadingError", true, null, null, i, onixFile.getAbsolutePath(), onixExtractedISBNFile.getAbsolutePath()));
                continue;
            }

            if (isbn.isEmpty() == true)
            {
                this.infoMessages.add(constructInfoMessage("messageNoExtractedISBN", true, null, null, i, onixFile.getAbsolutePath()));
                continue;
            }

            if (onixFileInfos.containsKey(isbn) != true)
            {
                onixFileInfos.put(isbn, new ONIXFileInfo(onixFile, isbn));
            }
            else
            {
                throw constructTermination("messageONIXFileISBNFoundMoreThanOnce", null, null, i, onixFile.getAbsolutePath(), isbn, onixFileInfos.get(isbn).getFile().getAbsolutePath());
            }
        }

        if (onixFileInfos.isEmpty() == true)
        {
            ProgramTerminationException ex = constructTermination("messageNoONIXInputFilesWithISBNsExtracted", null, null);
            ex.setNormalTermination(true);
            throw ex;
        }

        List<ONIXFileInfo> uploadCandidates = new ArrayList<ONIXFileInfo>();

        {
            Iterator iterOnixFileInfo = onixFileInfos.entrySet().iterator();

            for (int i = 0; iterOnixFileInfo.hasNext() == true; i++)
            {
                Map.Entry entry = (Map.Entry) iterOnixFileInfo.next();
                ONIXFileInfo onixFileInfo = (ONIXFileInfo) entry.getValue();

                File httpsClient1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_https_client_1_product_existence_isbn_lookup_job_" + i + ".xml");
                File httpsClient1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_https_client_1_product_existence_isbn_lookup_job_" + i + ".xml");
                File productISBNLookupJSONResultFile = new File(tempDirectory.getAbsolutePath() + File.separator + "result_product_existence_isbn_lookup_job_" + i + ".json");

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
                                throw constructTermination("messageHttpsClient1JobFileForProductExistenceISBNLookupExistsButIsntWritable", null, null, httpsClient1JobFile.getAbsolutePath());
                            }
                        }
                    }
                    else
                    {
                        throw constructTermination("messageHttpsClient1JobFileForProductExistenceISBNLookupJobPathExistsButIsntAFile", null, null, httpsClient1JobFile.getAbsolutePath());
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
                                throw constructTermination("messageHttpsClient1ResultInfoFileForProductExistenceISBNLookupExistsButIsntWritable", null, null, httpsClient1ResultInfoFile.getAbsolutePath());
                            }
                        }
                    }
                    else
                    {
                        throw constructTermination("messageHttpsClient1ResultInfoPathForProductExistenceISBNLookupExistsButIsntAFile", null, null, httpsClient1ResultInfoFile.getAbsolutePath());
                    }
                }

                if (productISBNLookupJSONResultFile.exists() == true)
                {
                    if (productISBNLookupJSONResultFile.isFile() == true)
                    {
                        boolean deleteSuccessful = false;

                        try
                        {
                            deleteSuccessful = productISBNLookupJSONResultFile.delete();
                        }
                        catch (SecurityException ex)
                        {

                        }

                        if (deleteSuccessful != true)
                        {
                            if (productISBNLookupJSONResultFile.canWrite() != true)
                            {
                                throw constructTermination("messageHttpsClient1ResultFileForProductExistenceISBNLookupExistsButIsntWritable", null, null, productISBNLookupJSONResultFile.getAbsolutePath());
                            }
                        }
                    }
                    else
                    {
                        throw constructTermination("messageHttpsClient1ResultFileForProductExistenceISBNLookupJobPathExistsButIsntAFile", null, null, productISBNLookupJSONResultFile.getAbsolutePath());
                    }
                }

                try
                {
                    BufferedWriter writer = new BufferedWriter(
                                            new OutputStreamWriter(
                                            new FileOutputStream(httpsClient1JobFile),
                                            "UTF-8"));

                    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                    writer.write("<!-- This file was created by onix_to_woocommerce_2 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                    writer.write("<https-client-1-jobfile>\n");
                    writer.write("  <request url=\"" + wooCommerceRESTAPIProductsURL + "?filter[sku]=" + onixFileInfo.getISBN() + "\" method=\"GET\">\n");
                    writer.write("    <header>\n");
                    writer.write("      <field name=\"Authorization\" value=\"Basic " + httpBasicAuth + "\"/>\n");
                    writer.write("      <field name=\"Content-Type\" value=\"text/xml\"/>\n");
                    writer.write("      <field name=\"charset\" value=\"utf-8\"/>\n");
                    writer.write("    </header>\n");

                    if (httpsAcceptHostForCertificateMismatch != null)
                    {
                        writer.write("    <!--\n");
                        writer.write("      This setting for accepting any server certificate for\n");
                        writer.write("      the host configured here BREAKS YOUR SECURITY!\n");
                        writer.write("    -->\n");
                        writer.write("    <accept-host-for-certificate-mismatch host=\"" + httpsAcceptHostForCertificateMismatch + "\"/>\n");
                    }

                    writer.write("  </request>\n");
                    writer.write("  <response destination=\"" + productISBNLookupJSONResultFile.getAbsolutePath() + "\"/>\n");
                    writer.write("</https-client-1-jobfile>\n");

                    writer.flush();
                    writer.close();
                }
                catch (FileNotFoundException ex)
                {
                    throw constructTermination("messageHttpsClient1JobFileForProductExistenceISBNLookupWritingError", ex, null, i, onixFileInfo.getFile().getAbsolutePath(), httpsClient1JobFile.getAbsolutePath());
                }
                catch (UnsupportedEncodingException ex)
                {
                    throw constructTermination("messageHttpsClient1JobFileForProductExistenceISBNLookupWritingError", ex, null, i, onixFileInfo.getFile().getAbsolutePath(), httpsClient1JobFile.getAbsolutePath());
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageHttpsClient1JobFileForProductExistenceISBNLookupWritingError", ex, null, i, onixFileInfo.getFile().getAbsolutePath(), httpsClient1JobFile.getAbsolutePath());
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
                    throw constructTermination("messageHttpsClient1ForProductExistenceISBNLookupErrorWhileReadingOutput", ex, null, i, onixFileInfo.getFile().getAbsolutePath(), httpsClient1JobFile.getAbsolutePath());
                }

                if (httpsClient1ResultInfoFile.exists() != true)
                {
                    throw constructTermination("messageHttpsClient1ResultInfoFileForProductExistenceISBNLookupDoesntExistButShould", null, null, i, onixFileInfo.getFile().getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath());
                }

                if (httpsClient1ResultInfoFile.isFile() != true)
                {
                    throw constructTermination("messageHttpsClient1ResultInfoPathForProductExistenceISBNLookupExistsButIsntAFile", null, null, httpsClient1ResultInfoFile.getAbsolutePath());
                }

                if (httpsClient1ResultInfoFile.canRead() != true)
                {
                    throw constructTermination("messageHttpsClient1ResultInfoFileForProductExistenceISBNLookupIsntReadable", null, null, i, onixFileInfo.getFile().getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath());
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
                    throw constructTermination("messageHttpsClient1ResultInfoFileForProductExistenceISBNLookupErrorWhileReading", null, null, i, onixFileInfo.getFile().getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath());
                }
                catch (SecurityException ex)
                {
                    throw constructTermination("messageHttpsClient1ResultInfoFileForProductExistenceISBNLookupErrorWhileReading", null, null, i, onixFileInfo.getFile().getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath());
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageHttpsClient1ResultInfoFileForProductExistenceISBNLookupErrorWhileReading", null, null, i, onixFileInfo.getFile().getAbsolutePath(), httpsClient1ResultInfoFile.getAbsolutePath());
                }

                if (wasSuccess != true)
                {
                    throw constructTermination("messageHttpsClient1CallForProductExistenceISBNLookupWasntSuccessful", null, null, i, onixFileInfo.getFile().getAbsolutePath(), httpsClient1JobFile.getAbsolutePath());
                }


                /*

                Might be of use if the workflow has to distinguish between new products and product updates. If this block gets used, it still needs l10n.

                File jsonToXml1JobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_json_to_xml_1_product_existence_isbn_lookup_job_" + i + ".xml");
                File jsonToXml1ResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_json_to_xml_1_product_existence_isbn_lookup_job_" + i + ".xml");
                File productISBNLookupXMLResultFile = new File(tempDirectory.getAbsolutePath() + File.separator + "result_product_existence_isbn_lookup_job_" + i + ".xml");

                if (jsonToXml1JobFile.exists() == true)
                {
                    if (jsonToXml1JobFile.isFile() == true)
                    {
                        boolean deleteSuccessful = false;

                        try
                        {
                            deleteSuccessful = jsonToXml1JobFile.delete();
                        }
                        catch (SecurityException ex)
                        {

                        }

                        if (deleteSuccessful != true)
                        {
                            if (jsonToXml1JobFile.canWrite() != true)
                            {
                                throw constructTermination("messageJsonToXml1JobFileForProductExistenceISBNLookupExistsButIsntWritable", null, null, jsonToXml1JobFile.getAbsolutePath());
                            }
                        }
                    }
                    else
                    {
                        throw constructTermination("messageJsonToXml1JobFileForProductExistenceISBNLookupJobPathExistsButIsntAFile", null, null, jsonToXml1JobFile.getAbsolutePath());
                    }
                }

                if (jsonToXml1ResultInfoFile.exists() == true)
                {
                    if (jsonToXml1ResultInfoFile.isFile() == true)
                    {
                        boolean deleteSuccessful = false;

                        try
                        {
                            deleteSuccessful = jsonToXml1ResultInfoFile.delete();
                        }
                        catch (SecurityException ex)
                        {

                        }

                        if (deleteSuccessful != true)
                        {
                            if (jsonToXml1ResultInfoFile.canWrite() != true)
                            {
                                throw constructTermination("messageJsonToXml1ResultInfoFileForProductExistenceISBNLookupExistsButIsntWritable", null, null, jsonToXml1ResultInfoFile.getAbsolutePath());
                            }
                        }
                    }
                    else
                    {
                        throw constructTermination("messageJsonToXml1ResultInfoPathForProductExistenceISBNLookupExistsButIsntAFile", null, null, jsonToXml1ResultInfoFile.getAbsolutePath());
                    }
                }

                if (productISBNLookupXMLResultFile.exists() == true)
                {
                    if (productISBNLookupXMLResultFile.isFile() == true)
                    {
                        boolean deleteSuccessful = false;

                        try
                        {
                            deleteSuccessful = productISBNLookupXMLResultFile.delete();
                        }
                        catch (SecurityException ex)
                        {

                        }

                        if (deleteSuccessful != true)
                        {
                            if (productISBNLookupXMLResultFile.canWrite() != true)
                            {
                                throw constructTermination("messageJsonToXml1ResultFileForProductExistenceISBNLookupExistsButIsntWritable", null, null, productISBNLookupXMLResultFile.getAbsolutePath());
                            }
                        }
                    }
                    else
                    {
                        throw constructTermination("messageJsonToXml1ResultFileForProductExistenceISBNLookupJobPathExistsButIsntAFile", null, null, productISBNLookupXMLResultFile.getAbsolutePath());
                    }
                }

                try
                {
                    BufferedWriter writer = new BufferedWriter(
                                            new OutputStreamWriter(
                                            new FileOutputStream(jsonToXml1JobFile),
                                            "UTF-8"));

                    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                    writer.write("<!-- This file was created by onix_to_woocommerce_2 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                    writer.write("<json-to-xml-1-jobfile>\n");
                    writer.write("  <json-input-file path=\"" + productISBNLookupJSONResultFile.getAbsolutePath() + "\"/>\n");
                    writer.write("  <xml-output-file path=\"" + productISBNLookupXMLResultFile.getAbsolutePath() + "\"/>\n");
                    writer.write("</json-to-xml-1-jobfile>\n");

                    writer.flush();
                    writer.close();
                }
                catch (FileNotFoundException ex)
                {
                    throw constructTermination("messageJsonToXml1JobFileForProductExistenceISBNLookupWritingError", ex, null, i, onixFileInfo.getFile().getAbsolutePath(), jsonToXml1JobFile.getAbsolutePath());
                }
                catch (UnsupportedEncodingException ex)
                {
                    throw constructTermination("messageJsonToXml1JobFileForProductExistenceISBNLookupWritingError", ex, null, i, onixFileInfo.getFile().getAbsolutePath(), jsonToXml1JobFile.getAbsolutePath());
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageJsonToXml1JobFileForProductExistenceISBNLookupWritingError", ex, null, i, onixFileInfo.getFile().getAbsolutePath(), jsonToXml1JobFile.getAbsolutePath());
                }

                builder = new ProcessBuilder("java", "json_to_xml_1", jsonToXml1JobFile.getAbsolutePath(), jsonToXml1ResultInfoFile.getAbsolutePath());
                builder.directory(new File(programPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + File.separator + "json_to_xml" + File.separator + "json_to_xml_1"));
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
                    throw constructTermination("messageJsonToXml1ForProductExistenceISBNLookupErrorWhileReadingOutput", ex, null, i, onixFileInfo.getFile().getAbsolutePath(), jsonToXml1JobFile.getAbsolutePath());
                }

                if (jsonToXml1ResultInfoFile.exists() != true)
                {
                    throw constructTermination("messageJsonToXml1ResultInfoFileForProductExistenceISBNLookupDoesntExistButShould", null, null, i, onixFileInfo.getFile().getAbsolutePath(), jsonToXml1ResultInfoFile.getAbsolutePath());
                }

                if (jsonToXml1ResultInfoFile.isFile() != true)
                {
                    throw constructTermination("messageJsonToXml1ResultInfoPathForProductExistenceISBNLookupExistsButIsntAFile", null, null, jsonToXml1ResultInfoFile.getAbsolutePath());
                }

                if (jsonToXml1ResultInfoFile.canRead() != true)
                {
                    throw constructTermination("messageJsonToXml1ResultInfoFileForProductExistenceISBNLookupIsntReadable", null, null, i, onixFileInfo.getFile().getAbsolutePath(), jsonToXml1ResultInfoFile.getAbsolutePath());
                }

                wasSuccess = false;

                try
                {
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    InputStream in = new FileInputStream(jsonToXml1ResultInfoFile);
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
                    throw constructTermination("messageJsonToXml1ResultInfoFileForProductExistenceISBNLookupErrorWhileReading", null, null, i, onixFileInfo.getFile().getAbsolutePath(), jsonToXml1ResultInfoFile.getAbsolutePath());
                }
                catch (SecurityException ex)
                {
                    throw constructTermination("messageJsonToXml1ResultInfoFileForProductExistenceISBNLookupErrorWhileReading", null, null, i, onixFileInfo.getFile().getAbsolutePath(), jsonToXml1ResultInfoFile.getAbsolutePath());
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageJsonToXml1ResultInfoFileForProductExistenceISBNLookupErrorWhileReading", null, null, i, onixFileInfo.getFile().getAbsolutePath(), jsonToXml1ResultInfoFile.getAbsolutePath());
                }

                if (wasSuccess != true)
                {
                    throw constructTermination("messageJsonToXml1CallForProductExistenceISBNLookupWasntSuccessful", null, null, i, onixFileInfo.getFile().getAbsolutePath(), jsonToXml1JobFile.getAbsolutePath());
                }
                */

                String jsonFirstLine = null;

                try
                {
                    BufferedReader reader = new BufferedReader(
                                            new FileReader(productISBNLookupJSONResultFile));

                    jsonFirstLine = reader.readLine();
                    reader.close();
                }
                catch (FileNotFoundException ex)
                {
                    throw constructTermination("messageHttpsClient1ResultJSONFileForProductExistenceISBNLookupErrorWhileReading", ex, null, i, onixFileInfo.getFile().getAbsolutePath(), productISBNLookupJSONResultFile.getAbsolutePath());
                }
                catch (IOException ex)
                {
                    throw constructTermination("messageHttpsClient1ResultJSONFileForProductExistenceISBNLookupErrorWhileReading", ex, null, i, onixFileInfo.getFile().getAbsolutePath(), productISBNLookupJSONResultFile.getAbsolutePath());
                }

                if (jsonFirstLine.equals("{\"products\":[]}") != true)
                {
                    this.infoMessages.add(constructInfoMessage("messageProductSeemsToExistAlready", true, null, null, i, onixFileInfo.getFile().getAbsolutePath(), productISBNLookupJSONResultFile.getAbsolutePath()));
                    continue;
                }

                uploadCandidates.add(new ONIXFileInfo( onixFileInfo.getFile(), onixFileInfo.getISBN()));
            }
        }

        if (uploadCandidates.isEmpty() == true)
        {
            ProgramTerminationException ex = constructTermination("messageNoNewONIXInputFilesToUpload", null, null);
            ex.setNormalTermination(true);
            throw ex;
        }

        {
            File onixToWoocommerce1WorkflowJobFile = new File(tempDirectory.getAbsolutePath() + File.separator + "jobfile_onix_to_woocommerce_1_workflow.xml");
            File onixToWoocommerce1WorkflowResultInfoFile = new File(tempDirectory.getAbsolutePath() + File.separator + "resultinfo_onix_to_woocommerce_1_workflow.xml");
            File onixToWoocommerce1WorkflowResultDirectory = new File(tempDirectory.getAbsolutePath() + File.separator + "result");

            if (onixToWoocommerce1WorkflowJobFile.exists() == true)
            {
                if (onixToWoocommerce1WorkflowJobFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = onixToWoocommerce1WorkflowJobFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (onixToWoocommerce1WorkflowJobFile.canWrite() != true)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileExistsButIsntWritable", null, null, onixToWoocommerce1WorkflowJobFile.getAbsolutePath());
                        }
                    }
                }
                else
                {
                    throw constructTermination("messageOnixToWoocommerce1WorkflowJobPathExistsButIsntAFile", null, null, onixToWoocommerce1WorkflowJobFile.getAbsolutePath());
                }
            }

            if (onixToWoocommerce1WorkflowResultInfoFile.exists() == true)
            {
                if (onixToWoocommerce1WorkflowResultInfoFile.isFile() == true)
                {
                    boolean deleteSuccessful = false;

                    try
                    {
                        deleteSuccessful = onixToWoocommerce1WorkflowResultInfoFile.delete();
                    }
                    catch (SecurityException ex)
                    {

                    }

                    if (deleteSuccessful != true)
                    {
                        if (onixToWoocommerce1WorkflowResultInfoFile.canWrite() != true)
                        {
                            throw constructTermination("messageOnixToWoocommerce1WorkflowResultInfoFileExistsButIsntWritable", null, null, onixToWoocommerce1WorkflowResultInfoFile.getAbsolutePath());
                        }
                    }
                }
                else
                {
                    throw constructTermination("messageOnixToWoocommerce1WorkflowResultInfoPathExistsButIsntAFile", null, null, onixToWoocommerce1WorkflowResultInfoFile.getAbsolutePath());
                }
            }

            if (onixToWoocommerce1WorkflowResultDirectory.exists() == true)
            {
                deleteFilesRecursively(onixToWoocommerce1WorkflowResultDirectory);
            }

            try
            {
                onixToWoocommerce1WorkflowResultDirectory.mkdirs();
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageOnixToWoocommerce1WorkflowResultDirectoryCantCreate", null, null, onixToWoocommerce1WorkflowResultDirectory.getAbsolutePath());
            }

            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(onixToWoocommerce1WorkflowJobFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by onix_to_woocommerce_2 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<onix-to-woocommerce-1-workflow-jobfile>\n");
                writer.write("  <input>\n");

                for (ONIXFileInfo info : uploadCandidates)
                {
                    writer.write("    <onix-file source=\"" + info.getFile().getAbsolutePath() + "\"/>\n");
                }

                writer.write("  </input>\n");
                writer.write("  <settings>\n");
                writer.write("    <woocommerce-rest-api-products-url url=\"" + wooCommerceRESTAPIProductsURL + "\"/>\n");
                writer.write("    <woocommerce-rest-api-public-key key=\"" + wooCommerceRESTAPIPublicKey + "\"/>\n");
                writer.write("    <woocommerce-rest-api-secret-key key=\"" + wooCommerceRESTAPISecretKey + "\"/>\n");

                if (httpsAcceptHostForCertificateMismatch != null)
                {
                    writer.write("    <!--\n");
                    writer.write("      This setting for accepting any server certificate for\n");
                    writer.write("      the host configured here BREAKS YOUR SECURITY!\n");
                    writer.write("    -->\n");
                    writer.write("    <accept-host-for-certificate-mismatch host=\"" + httpsAcceptHostForCertificateMismatch + "\"/>\n");
                }

                writer.write("  </settings>\n");
                writer.write("  <output>\n");
                writer.write("    <response-directory destination=\"" + onixToWoocommerce1WorkflowResultDirectory.getAbsolutePath() + "\"/>\n");
                writer.write("  </output>\n");
                writer.write("</onix-to-woocommerce-1-workflow-jobfile>\n");

                writer.flush();
                writer.close();
            }
            catch (FileNotFoundException ex)
            {
                throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileWritingError", ex, null, onixToWoocommerce1WorkflowJobFile.getAbsolutePath());
            }
            catch (UnsupportedEncodingException ex)
            {
                throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileWritingError", ex, null, onixToWoocommerce1WorkflowJobFile.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageOnixToWoocommerce1WorkflowJobFileWritingError", ex, null, onixToWoocommerce1WorkflowJobFile.getAbsolutePath());
            }

            ProcessBuilder builder = new ProcessBuilder("java", "onix_to_woocommerce_1", onixToWoocommerce1WorkflowJobFile.getAbsolutePath(), onixToWoocommerce1WorkflowResultInfoFile.getAbsolutePath());
            builder.directory(new File(programPath + File.separator + ".." + File.separator + "onix_to_woocommerce_1"));
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
                throw constructTermination("messageOnixToWoocommerce1WorkflowErrorWhileReadingOutput", ex, null, onixToWoocommerce1WorkflowJobFile.getAbsolutePath());
            }

            if (onixToWoocommerce1WorkflowResultInfoFile.exists() != true)
            {
                throw constructTermination("messageOnixToWoocommerce1WorkflowResultInfoFileDoesntExistButShould", null, null, onixToWoocommerce1WorkflowResultInfoFile.getAbsolutePath());
            }

            if (onixToWoocommerce1WorkflowResultInfoFile.isFile() != true)
            {
                throw constructTermination("messageOnixToWoocommerce1WorkflowResultInfoPathExistsButIsntAFile", null, null, onixToWoocommerce1WorkflowResultInfoFile.getAbsolutePath());
            }

            if (onixToWoocommerce1WorkflowResultInfoFile.canRead() != true)
            {
                throw constructTermination("messageOnixToWoocommerce1WorkflowResultInfoFileIsntReadable", null, null, onixToWoocommerce1WorkflowResultInfoFile.getAbsolutePath());
            }

            boolean wasSuccess = false;

            try
            {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                InputStream in = new FileInputStream(onixToWoocommerce1WorkflowResultInfoFile);
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
                throw constructTermination("messageOnixToWoocommerce1WorkflowResultInfoFileErrorWhileReading", null, null, onixToWoocommerce1WorkflowResultInfoFile.getAbsolutePath());
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageOnixToWoocommerce1WorkflowResultInfoFileErrorWhileReading", null, null, onixToWoocommerce1WorkflowResultInfoFile.getAbsolutePath());
            }
            catch (IOException ex)
            {
                throw constructTermination("messageOnixToWoocommerce1WorkflowResultInfoFileErrorWhileReading", null, null, onixToWoocommerce1WorkflowResultInfoFile.getAbsolutePath());
            }

            if (wasSuccess != true)
            {
                throw constructTermination("messageOnixToWoocommerce1WorkflowCallWasntSuccessful", null, null, onixToWoocommerce1WorkflowJobFile.getAbsolutePath());
            }
        }

        return 0;
    }

    public int deleteFilesRecursively(File file)
    {
        try
        {
            file = file.getCanonicalFile();
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageDeleteFileCantGetCanonicalPath", ex, null, file.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageDeleteFileCantGetCanonicalPath", ex, null, file.getAbsolutePath());
        }

        if (file.isDirectory() == true)
        {
            File[] children = file.listFiles();

            if (children != null)
            {
                for (File child : file.listFiles())
                {
                    if (this.deleteFilesRecursively(child) != 0)
                    {
                        return -1;
                    }
                }
            }
            else
            {
                throw constructTermination("messageDeleteFileUnableToDetermineChildren", null, null, file.getAbsolutePath());
            }
        }

        if (file.delete() != true)
        {
            throw constructTermination("messageDeleteFileError", null, null, file.getAbsolutePath());
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
                message = "onix_to_woocommerce_2 workflow: " + getI10nString(id);
            }
            else
            {
                message = "onix_to_woocommerce_2 workflow: " + getI10nStringFormatted(id, arguments);
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
                message = "onix_to_woocommerce_2 workflow: " + getI10nString(id);
            }
            else
            {
                message = "onix_to_woocommerce_2 workflow: " + getI10nStringFormatted(id, arguments);
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

        if (onix_to_woocommerce_2.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(onix_to_woocommerce_2.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by onix_to_woocommerce_2 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<onix-to-woocommerce-2-workflow-result-information>\n");

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

                writer.write("</onix-to-woocommerce-2-workflow-result-information>\n");
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

        onix_to_woocommerce_2.resultInfoFile = null;

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

    private static onix_to_woocommerce_2 onix_to_woocommerce_2Instance;

    public static File resultInfoFile = null;
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nOnixToWoocommerce2WorkflowConsole";
    private ResourceBundle l10nConsole;
}

