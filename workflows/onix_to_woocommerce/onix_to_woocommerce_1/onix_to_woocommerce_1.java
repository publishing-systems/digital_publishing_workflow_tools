/* Copyright (C) 2016  Stephan Kreutzer
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

        System.out.println("onix_to_woocommerce_1 workflow: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        List<File> inputONIXFiles = new ArrayList<File>();
        String wooCommerceRESTAPIProductsURL = null;
        String wooCommerceRESTAPIPublicKey = null;
        String wooCommerceRESTAPISecretKey = null;
        String httpsAcceptHostForCertificateMismatch = null;
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
                            throw constructTermination("messageJobFileTagNested", null, null, jobFile.getAbsolutePath(), "input", "input");
                        }

                        inInput = true;
                    }
                    else if (tagName.equals("settings") == true &&
                             inInput == false &&
                             inOutput == false)
                    {
                        if (inSettings == true)
                        {
                            throw constructTermination("messageJobFileTagNested", null, null, jobFile.getAbsolutePath(), "settings", "settings");
                        }

                        inSettings = true;
                    }
                    else if (tagName.equals("output") == true &&
                             inInput == false &&
                             inSettings == false)
                    {
                        if (inOutput == true)
                        {
                            throw constructTermination("messageJobFileTagNested", null, null, jobFile.getAbsolutePath(), "output", "output");
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
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "onix-input", "source");
                        }

                        String onixFileSource = sourceAttribute.getValue();

                        if (onixFileSource.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "onix-input", "source");
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
                            throw constructTermination("messageONIXFileCantGetCanonicalPath", ex, null, onixFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageONIXFileCantGetCanonicalPath", ex, null, onixFile.getAbsolutePath());
                        }

                        if (onixFile.exists() != true)
                        {
                            throw constructTermination("messageONIXFileDoesntExist", null, null, onixFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (onixFile.isFile() != true)
                        {
                            throw constructTermination("messageONIXPathIsntAFile", null, null, onixFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (onixFile.canRead() != true)
                        {
                            throw constructTermination("messageONIXFileIsntReadable", null, null, onixFile.getAbsolutePath(), jobFile.getAbsolutePath());
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
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-products-url", "url");
                        }

                        if (wooCommerceRESTAPIProductsURL != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-products-url", "url");
                        }

                        wooCommerceRESTAPIProductsURL = urlAttribute.getValue();

                        if (wooCommerceRESTAPIProductsURL.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-products-url", "url");
                        }

                        if (wooCommerceRESTAPIProductsURL.startsWith("https://") != true)
                        {
                            throw constructTermination("messageJobFileWooCommerceRESTAPIProductsURLIsntHTTPS", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-products-url", "url", "https://");
                        }
                    }
                    else if (tagName.equals("woocommerce-rest-api-public-key") == true &&
                             inSettings == true)
                    {
                        StartElement wooCommerceRESTAPIPublicKeyElement = event.asStartElement();
                        Attribute keyAttribute = wooCommerceRESTAPIPublicKeyElement.getAttributeByName(new QName("key"));

                        if (keyAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-public-key", "key");
                        }

                        if (wooCommerceRESTAPIPublicKey != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-public-key", "key");
                        }

                        wooCommerceRESTAPIPublicKey = keyAttribute.getValue();

                        if (wooCommerceRESTAPIPublicKey.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-public-key", "key");
                        }
                    }
                    else if (tagName.equals("woocommerce-rest-api-secret-key") == true &&
                             inSettings == true)
                    {
                        StartElement wooCommerceRESTAPISecretKeyElement = event.asStartElement();
                        Attribute keyAttribute = wooCommerceRESTAPISecretKeyElement.getAttributeByName(new QName("key"));

                        if (keyAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-secret-key", "key");
                        }

                        if (wooCommerceRESTAPISecretKey != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-secret-key", "key");
                        }

                        wooCommerceRESTAPISecretKey = keyAttribute.getValue();

                        if (wooCommerceRESTAPISecretKey.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "woocommerce-rest-api-secret-key", "key");
                        }
                    }
                    else if (tagName.equals("accept-host-for-certificate-mismatch") == true &&
                             inSettings == true)
                    {
                        StartElement acceptHostForCertificateMismatchElement = event.asStartElement();
                        Attribute hostAttribute = acceptHostForCertificateMismatchElement.getAttributeByName(new QName("host"));

                        if (hostAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "accept-host-for-certificate-mismatch", "host");
                        }

                        if (httpsAcceptHostForCertificateMismatch != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), "accept-host-for-certificate-mismatch", "host");
                        }

                        httpsAcceptHostForCertificateMismatch = hostAttribute.getValue();

                        if (httpsAcceptHostForCertificateMismatch.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "accept-host-for-certificate-mismatch", "host");
                        }
                    }
                    else if (tagName.equals("response-directory") == true &&
                             inOutput == true)
                    {
                        StartElement responseDirectoryElement = event.asStartElement();
                        Attribute destinationAttribute = responseDirectoryElement.getAttributeByName(new QName("destination"));

                        if (destinationAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "response-directory", "destination");
                        }

                        if (outputDirectory != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), "response-directory", "destination");
                        }

                        if (destinationAttribute.getValue().isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "response-directory", "destination");
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
                            throw constructTermination("messageOutputDirectoryCantGetCanonicalPath", ex, null, outputDirectory.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageOutputDirectoryCantGetCanonicalPath", ex, null, outputDirectory.getAbsolutePath());
                        }

                        if (outputDirectory.exists() == true)
                        {
                            if (outputDirectory.isFile() == true)
                            {
                                throw constructTermination("messageOutputPathExistsButIsFile", null, null, jobFile.getAbsolutePath(), outputDirectory.getAbsolutePath());
                            }
                            else
                            {
                                if (outputDirectory.canWrite() != true)
                                {
                                    throw constructTermination("messageOutputDirectoryIsntWritable", null, null, jobFile.getAbsolutePath(), outputDirectory.getAbsolutePath());
                                }
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


        int successCount = 0;
        int max = inputONIXFiles.size();

        for (int i = 0; i < max; i++)
        {
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
                    this.infoMessages.add(constructInfoMessage("messageJSONRequestPathExistsButIsntFile", true, null, null, inputONIXFiles.get(i).getAbsolutePath(), requestJSONFile.getAbsolutePath()));
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
                            throw constructTermination("messageXmlXsltTransformator1ResultInfoFileExistsButIsntWritable", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
                        }
                    }
                }
                else
                {
                    throw constructTermination("messageXmlXsltTransformator1ResultInfoPathExistsButIsntAFile", null, null, xmlXsltTransformator1ResultInfoFile.getAbsolutePath());
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
                writer.write("  <job input-file=\"" + inputONIXFiles.get(i).getAbsolutePath() + "\" entities-resolver-config-file=\"" + programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1" + File.separator + "entities" + File.separator + "config_onix_2_1_3_short.xml\" stylesheet-file=\"" + programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "onix_to_woocommerce" + File.separator + "onix_to_woocommerce_1" + File.separator + "onix_to_woocommerce_1.xsl\" output-file=\"" + requestJSONFile.getAbsolutePath() + "\"/>\n");
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
                            throw constructTermination("messageHttpsClient1JobFileIsntWritable", null, null, httpsClient1JobFile.getAbsolutePath());
                        }
                    }
                }
                else
                {
                    throw constructTermination("messageHttpsClient1JobPathIsntAFile", null, null, httpsClient1JobFile.getAbsolutePath());
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
                            throw constructTermination("messageHttpsClient1ResultInfoFileExistsButIsntWritable", null, null, httpsClient1ResultInfoFile.getAbsolutePath());
                        }
                    }
                }
                else
                {
                    throw constructTermination("messageHttpsClient1ResultInfoPathExistsButIsntAFile", null, null, httpsClient1ResultInfoFile.getAbsolutePath());
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

            try
            {
                xmlXsltTransformator1JobFile.delete();
            }
            catch (SecurityException ex)
            {

            }

            /*
            try
            {
                requestJSONFile.delete();
            }
            catch (SecurityException ex)
            {

            }
            */

            try
            {
                xmlXsltTransformator1ResultInfoFile.delete();
            }
            catch (SecurityException ex)
            {

            }

            try
            {
                boolean deleteSuccessful = httpsClient1JobFile.delete();

                if (i + 1 == max &&
                    deleteSuccessful == false)
                {
                    this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileCouldntDeleteAPIKeysExposed", true, null, null, httpsClient1ResultInfoFile.getAbsolutePath()));
                }
            }
            catch (SecurityException ex)
            {
                this.infoMessages.add(constructInfoMessage("messageHttpsClient1JobFileCouldntDeleteAPIKeysExposed", true, ex, null, httpsClient1ResultInfoFile.getAbsolutePath()));
            }

            /*
            try
            {
                httpsClient1ResultInfoFile.delete();
            }
            catch (SecurityException ex)
            {

            }
            */
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

