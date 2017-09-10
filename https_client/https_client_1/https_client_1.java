/* Copyright (C) 2015-2017 Stephan Kreutzer
 *
 * This file is part of https_client_1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * https_client_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * https_client_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with https_client_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/https_client/https_client_1/https_client_1.java
 * @brief A HTTPS client.
 * @author Stephan Kreutzer
 * @since 2015-12-30
 */



import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.File;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.security.cert.Certificate;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.util.List;
import java.util.ArrayList;
import java.net.URLDecoder;



public class https_client_1
{
    protected https_client_1()
    {
        // Singleton to protect https_client_1.resultInfoFile from conflicting use.

        https_client_1.resultCertificates = null;
    }

    public static https_client_1 getInstance()
    {
        if (https_client_1.https_client_1Instance == null)
        {
            https_client_1.https_client_1Instance = new https_client_1();
        }

        return https_client_1.https_client_1Instance;
    }

    public static void main(String args[])
    {
        System.out.print("https_client_1 Copyright (C) 2015-2017 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");

        https_client_1 client = https_client_1.getInstance();
        client.getResults().clear();
        client.getInfoMessages().clear();
        client.setResultCertificates(null);
        client.getResponseHeaderFields().clear();

        try
        {
            client.request(args);
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
                writer.write("<!-- This file was created by https_client_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<https-client-1-result-information>\n");
                writer.write("  <success>\n");

                if (client.getResults() != null)
                {
                    if (client.getResults().isEmpty() != true)
                    {
                        for (Map.Entry<String, String> entry : client.getResults().entrySet())
                        {
                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            String value = entry.getValue();
                            value = value.replaceAll("&", "&amp;");
                            value = value.replaceAll("<", "&lt;");
                            value = value.replaceAll(">", "&gt;");

                            writer.write("    <" + entry.getKey() + ">" + value + "</" + entry.getKey() + ">\n");
                        }
                    }
                }

                if (client.getInfoMessages().size() > 0)
                {
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
                }

                if (client.getResultCertificates() != null)
                {
                    writer.write("    <certificates>\n");

                    for (Certificate certificate : client.getResultCertificates())
                    {
                        writer.write("      <certificate");
                        writer.write(" type=\"" + certificate.getType() + "\"");
                        writer.write(" hash=\"" + certificate.hashCode() + "\"");
                        writer.write(" public-key-algorithm=\"" + certificate.getPublicKey().getAlgorithm() + "\"");
                        writer.write(" public-key-format=\"" + certificate.getPublicKey().getFormat() + "\">");
                        writer.write(Base64Coder.encode(certificate.getPublicKey().getEncoded()));
                        writer.write("</certificate>\n");
                    }

                    writer.write("    </certificates>\n");
                }

                if (client.getResponseHeaderFields() != null)
                {
                    if (client.getResponseHeaderFields().isEmpty() != true)
                    {
                        writer.write("    <response-header-fields>\n");

                        for (ResponseHeaderField field : client.responseHeaderFields)
                        {
                            String fieldNumber = Integer.toString(field.getNumber());
                            String fieldName = field.getName();

                            if (fieldName != null)
                            {
                                // Ampersand needs to be the first, otherwise it would double-encode
                                // other entities.
                                fieldName = fieldName.replaceAll("&", "&amp;");
                                fieldName = fieldName.replaceAll("<", "&lt;");
                                fieldName = fieldName.replaceAll(">", "&gt;");
                                fieldName = fieldName.replaceAll("\"", "&quot;");
                                fieldName = fieldName.replaceAll("'", "&apos;");
                            }

                            String fieldValue = field.getValue();

                            if (fieldValue != null)
                            {
                                // Ampersand needs to be the first, otherwise it would double-encode
                                // other entities.
                                fieldValue = fieldValue.replaceAll("&", "&amp;");
                                fieldValue = fieldValue.replaceAll("<", "&lt;");
                                fieldValue = fieldValue.replaceAll(">", "&gt;");
                                fieldValue = fieldValue.replaceAll("\"", "&quot;");
                                fieldValue = fieldValue.replaceAll("'", "&apos;");
                            }
                            else
                            {
                                fieldValue = "";
                            }

                            writer.write("      <field number=\"" + fieldNumber + "\"");

                            if (fieldName != null)
                            {
                                writer.write(" name=\"" + fieldName + "\"");
                            }

                            writer.write(">" + fieldValue + "</field>\n");
                        }

                        writer.write("    </response-header-fields>\n");
                    }
                }

                writer.write("  </success>\n");
                writer.write("</https-client-1-result-information>\n");
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

        client.resultInfoFile = null;
        client.getResults().clear();
        client.getInfoMessages().clear();
        client.setResultCertificates(null);
        client.getResponseHeaderFields().clear();
    }

    public int request(String args[]) throws ProgramTerminationException
    {
        this.getResults().clear();
        this.getInfoMessages().clear();
        this.setResultCertificates(null);
        this.getResponseHeaderFields().clear();

        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\thttps_client_1 " + getI10nString("messageParameterList") + "\n");
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

        https_client_1.resultInfoFile = resultInfoFile;

        String programPath = https_client_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

        System.out.println("https_client_1: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        String requestURL = null;
        String requestMethod = null;
        Map<String, String> requestHeaderFields = null;
        File requestDataSourceFile = null;
        boolean ownsRequestDataSourceFile = false;
        String requestAcceptHostForCertificateMismatch = null;
        File responseDataFile = null;
        boolean responseOmitCertificatesInfo = false;

        try
        {
            boolean inRequest = false;
            boolean inHeader = false;

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
                        if (inRequest == true)
                        {
                            throw constructTermination("messageJobFileTagNested", null, null, jobFile.getAbsolutePath(), "request", "request");
                        }

                        inRequest = true;

                        StartElement requestElement = event.asStartElement();
                        Attribute urlAttribute = requestElement.getAttributeByName(new QName("url"));

                        if (urlAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "request", "url");
                        }

                        if (requestURL != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), "request", "url");
                        }

                        requestURL = urlAttribute.getValue();

                        if (requestURL.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "request", "url");
                        }

                        if (requestURL.startsWith("https://") != true)
                        {
                            throw constructTermination("messageJobFileRequestURLIsntHTTPS", null, null, jobFile.getAbsolutePath(), "request", "url", "https://");
                        }

                        Attribute methodAttribute = requestElement.getAttributeByName(new QName("method"));

                        if (methodAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "request", "method");
                        }

                        if (requestMethod != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), "request", "method");
                        }

                        requestMethod = methodAttribute.getValue().toUpperCase();

                        if (requestURL.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "request", "method");
                        }
                    }
                    else if (tagName.equals("header") == true &&
                             inRequest == true)
                    {
                        if (inHeader == true)
                        {
                            throw constructTermination("messageJobFileTagNested", null, null, jobFile.getAbsolutePath(), "header", "header");
                        }

                        inHeader = true;

                        requestHeaderFields = new HashMap<String, String>();
                    }
                    else if (tagName.equals("field") == true &&
                             inHeader == true)
                    {
                        StartElement fieldElement = event.asStartElement();
                        Attribute nameAttribute = fieldElement.getAttributeByName(new QName("name"));

                        if (nameAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "field", "name");
                        }

                        String fieldName = nameAttribute.getValue();

                        if (fieldName.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "field", "name");
                        }

                        if (fieldName.equalsIgnoreCase("User-Agent") == true)
                        {
                            throw constructTermination("messageJobFileCantSetHeaderField", null, null, jobFile.getAbsolutePath(), fieldName);
                        }

                        if (requestHeaderFields.containsKey(fieldName) == true)
                        {
                            throw constructTermination("messageJobFileHeaderFieldSpecifiedMoreThanOnce", null, null, jobFile.getAbsolutePath(), fieldName);
                        }

                        Attribute valueAttribute = fieldElement.getAttributeByName(new QName("value"));

                        if (valueAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "field", "value");
                        }

                        requestHeaderFields.put(fieldName, valueAttribute.getValue());
                    }
                    else if (tagName.equals("data") == true &&
                             inRequest == true)
                    {
                        if (requestDataSourceFile != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), "data", "source");
                        }

                        StartElement dataElement = event.asStartElement();
                        Attribute sourceAttribute = dataElement.getAttributeByName(new QName("source"));

                        if (sourceAttribute == null)
                        {
                            try
                            {
                                BufferedWriter writer = null;

                                while (eventReader.hasNext() == true)
                                {
                                    event = eventReader.nextEvent();

                                    if (event.isCharacters() == true)
                                    {
                                        if (requestDataSourceFile == null)
                                        {
                                            requestDataSourceFile = new File(tempDirectory.getAbsolutePath() + File.separator + "body.dat");

                                            if (requestDataSourceFile.exists() == true)
                                            {
                                                if (requestDataSourceFile.isFile() == true)
                                                {
                                                    boolean deleteSuccessful = false;

                                                    try
                                                    {
                                                        deleteSuccessful = requestDataSourceFile.delete();
                                                    }
                                                    catch (SecurityException ex)
                                                    {

                                                    }

                                                    if (deleteSuccessful != true)
                                                    {
                                                        if (requestDataSourceFile.canWrite() != true)
                                                        {
                                                            throw constructTermination("messageTemporaryRequestDataSourceFileExistsButIsntWritable", null, null, requestDataSourceFile.getAbsolutePath());
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    throw constructTermination("messageTemporaryRequestDataSourcePathExistsButIsntAFile", null, null, requestDataSourceFile.getAbsolutePath());
                                                }
                                            }

                                            ownsRequestDataSourceFile = true;

                                            writer = new BufferedWriter(
                                                    new OutputStreamWriter(
                                                    new FileOutputStream(requestDataSourceFile),
                                                    "UTF-8"));
                                        }

                                        event.writeAsEncodedUnicode(writer);
                                    }
                                    else if (event.isEndElement() == true)
                                    {
                                        if (event.asEndElement().getName().getLocalPart().equals("data") == true)
                                        {
                                            if (requestDataSourceFile == null)
                                            {
                                                throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "data", "source");
                                            }

                                            if (writer == null)
                                            {

                                            }

                                            writer.close();
                                            break;
                                        }
                                    }
                                }
                            }
                            catch (FileNotFoundException ex)
                            {
                                throw constructTermination("messageTemporaryRequestDataSourceFileWritingError", ex, null, requestDataSourceFile.getAbsolutePath());
                            }
                            catch (UnsupportedEncodingException ex)
                            {
                                throw constructTermination("messageTemporaryRequestDataSourceFileWritingError", ex, null, requestDataSourceFile.getAbsolutePath());
                            }
                            catch (IOException ex)
                            {
                                throw constructTermination("messageTemporaryRequestDataSourceFileWritingError", ex, null, requestDataSourceFile.getAbsolutePath());
                            }
                        }
                        else
                        {
                            String requestDataSource = sourceAttribute.getValue();

                            if (requestDataSource.isEmpty() == true)
                            {
                                throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "data", "source");
                            }

                            requestDataSourceFile = new File(requestDataSource);

                            if (requestDataSourceFile.isAbsolute() != true)
                            {
                                requestDataSourceFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + requestDataSource);
                            }
                        }
                    }
                    else if (tagName.equals("accept-host-for-certificate-mismatch") == true &&
                             inRequest == true)
                    {
                        StartElement acceptHostElement = event.asStartElement();
                        Attribute hostAttribute = acceptHostElement.getAttributeByName(new QName("host"));

                        if (hostAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "accept-host-for-certificate-mismatch", "host");
                        }

                        if (requestAcceptHostForCertificateMismatch != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), "accept-host-for-certificate-mismatch", "host");
                        }

                        requestAcceptHostForCertificateMismatch = hostAttribute.getValue();

                        if (requestAcceptHostForCertificateMismatch.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "accept-host-for-certificate-mismatch", "host");
                        }
                    }
                    else if (tagName.equals("response") == true)
                    {
                        StartElement responseElement = event.asStartElement();
                        Attribute destinationAttribute = responseElement.getAttributeByName(new QName("destination"));

                        if (destinationAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "response", "destination");
                        }

                        if (responseDataFile != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), "response", "destination");
                        }

                        String responseDataDestination = destinationAttribute.getValue();

                        if (responseDataDestination.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "response", "destination");
                        }

                        responseDataFile = new File(responseDataDestination);

                        if (responseDataFile.isAbsolute() != true)
                        {
                            responseDataFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + responseDataDestination);
                        }

                        Attribute omitCertificatesInfoAttribute = responseElement.getAttributeByName(new QName("omit-certificates-info"));

                        if (omitCertificatesInfoAttribute != null)
                        {
                            if (omitCertificatesInfoAttribute.getValue().equals("true") == true)
                            {
                                responseOmitCertificatesInfo = true;
                            }
                        }
                    }
                }
                else if (event.isEndElement() == true)
                {
                    String tagName = event.asEndElement().getName().getLocalPart();

                    if (tagName.equals("request") == true)
                    {
                        if (inRequest == false)
                        {
                            throw constructTermination("messageJobFileTagEndWithoutStart", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        inRequest = false;
                    }
                    else if (tagName.equals("header") == true &&
                             inRequest == true)
                    {
                        if (inHeader == false)
                        {
                            throw constructTermination("messageJobFileTagEndWithoutStart", null, null, jobFile.getAbsolutePath(), tagName);
                        }

                        inHeader = false;
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


        if (requestURL == null)
        {
            throw constructTermination("messageJobFileRequestURLIsntConfigured", null, null, jobFile.getAbsolutePath(), "request");
        }

        if (responseDataFile == null)
        {
            throw constructTermination("messageJobFileResponseDataFileIsntConfigured", null, null, jobFile.getAbsolutePath(), "response");
        }

        if (responseDataFile.exists() == true)
        {
            if (responseDataFile.isFile() == true)
            {
                if (responseDataFile.canWrite() != true)
                {
                    throw constructTermination("messageResponseDataFileIsntWritable", null, null, responseDataFile.getAbsolutePath());
                }
            }
            else
            {
                throw constructTermination("messageResponseDataPathIsntAFile", null, null, responseDataFile.getAbsolutePath());
            }
        }


        final String requestAcceptHostForCertificateMismatchFinal = requestAcceptHostForCertificateMismatch;

        if (requestAcceptHostForCertificateMismatchFinal != null)
        {
            javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

                public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession)
                {
                    if (hostname.equals(requestAcceptHostForCertificateMismatchFinal) == true)
                    {
                        return true;
                    }

                    return false;
                }
            });
        }


        HttpsURLConnection connection = null;

        try
        {
            URL url = new URL(requestURL);

            connection = (HttpsURLConnection)url.openConnection();

            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod(requestMethod);
            connection.setUseCaches(false);

            if (requestHeaderFields != null)
            {
                if (requestHeaderFields.isEmpty() != true)
                {
                    for (Map.Entry<String, String> field : requestHeaderFields.entrySet())
                    {
                        connection.setRequestProperty(field.getKey(), field.getValue());
                    }
                }
            }

            connection.setRequestProperty("User-Agent", "https_client_1 of digital_publishing_workflow_tools (publishing-systems.org)");

            if (requestMethod.equalsIgnoreCase("TRACE") != true &&
                requestDataSourceFile != null)
            {
                if (requestDataSourceFile.exists() != true)
                {
                    throw constructTermination("messageRequestDataFileDoesntExist", null, null, jobFile.getAbsolutePath(), requestDataSourceFile.getAbsolutePath());
                }

                if (requestDataSourceFile.isFile() != true)
                {
                    throw constructTermination("messageRequestDataPathIsntAFile", null, null, jobFile.getAbsolutePath(), requestDataSourceFile.getAbsolutePath());
                }

                if (requestDataSourceFile.canRead() != true)
                {
                    throw constructTermination("messageRequestDataFileIsntReadable", null, null, jobFile.getAbsolutePath(), requestDataSourceFile.getAbsolutePath());
                }

                boolean exception = false;

                byte[] data = new byte[1024];

                FileInputStream reader = null;
                DataOutputStream writer = null;

                try
                {
                    reader = new FileInputStream(requestDataSourceFile);
                    writer = new DataOutputStream(connection.getOutputStream());

                    int bytesRead = reader.read(data, 0, data.length);

                    while (bytesRead > 0)
                    {
                        writer.write(data, 0, bytesRead);
                        bytesRead = reader.read(data, 0, data.length);
                    }
                }
                catch (FileNotFoundException ex)
                {
                    exception = true;
                    throw constructTermination("messageErrorWhileHandlingRequestData", ex, null);
                }
                catch (IOException ex)
                {
                    exception = true;
                    throw constructTermination("messageErrorWhileHandlingRequestData", ex, null);
                }
                finally
                {
                    if (writer != null)
                    {
                        try
                        {
                            writer.close();
                            writer = null;
                        }
                        catch (IOException ex)
                        {
                            if (exception == false)
                            {
                                exception = true;
                                throw constructTermination("messageErrorWhileHandlingRequestData", ex, null);
                            }
                        }
                        finally
                        {
                            writer = null;

                            if (reader != null)
                            {
                                try
                                {
                                    reader.close();
                                    reader = null;
                                }
                                catch (IOException ex)
                                {
                                    if (exception == false)
                                    {
                                        exception = true;
                                        throw constructTermination("messageErrorWhileHandlingRequestData", ex, null);
                                    }
                                }
                                finally
                                {
                                    reader = null;
                                }
                            }
                        }
                    }

                    if (reader != null)
                    {
                        try
                        {
                            reader.close();
                            reader = null;
                        }
                        catch (IOException ex)
                        {
                            if (exception == false)
                            {
                                exception = true;
                                throw constructTermination("messageErrorWhileHandlingRequestData", ex, null);
                            }
                        }
                        finally
                        {
                            reader = null;
                        }
                    }
                }
            }

            connection.connect();
        }
        catch (MalformedURLException ex)
        {
            throw constructTermination("messageErrorWhileSettingUpConnection", ex, null);
        }
        catch (IOException ex)
        {
            throw constructTermination("messageErrorWhileSettingUpConnection", ex, null);
        }

        if (connection != null)
        {
            if (responseOmitCertificatesInfo == false)
            {
                this.getResults().put("cipher-suite", connection.getCipherSuite());

                try
                {
                    https_client_1.resultCertificates = connection.getServerCertificates();
                }
                catch (SSLPeerUnverifiedException ex)
                {
                    throw constructTermination("messageResponseCantGetCertificates", ex, null);
                }
            }

            try
            {
                this.getResults().put("http-status-code", Integer.toString(connection.getResponseCode()));
            }
            catch (IOException ex)
            {
                throw constructTermination("messageErrorWhileReadingResponse", ex, null);
            }

            for (int i = 0; true; i++)
            {
                String fieldName = connection.getHeaderFieldKey(i);
                String fieldValue = connection.getHeaderField(i);

                if (fieldName != null ||
                    fieldValue != null)
                {
                    this.responseHeaderFields.add(new ResponseHeaderField(i, fieldName, fieldValue));
                }
                else
                {
                    break;
                }
            }

            byte[] data = new byte[1024];

            DataInputStream reader = null;
            FileOutputStream writer = null;

            boolean error = false;
            boolean exception = false;

            try
            {
                reader = new DataInputStream(connection.getInputStream());
            }
            catch (IOException ex)
            {
                error = true;
            }

            if (error == true)
            {
                reader = new DataInputStream(connection.getErrorStream());
                error = false;
            }

            if (error == false)
            {
                try
                {
                    writer = new FileOutputStream(responseDataFile);

                    int bytesRead = reader.read(data, 0, data.length);

                    while (bytesRead > 0)
                    {
                        writer.write(data, 0, bytesRead);
                        bytesRead = reader.read(data, 0, data.length);
                    }
                }
                catch (FileNotFoundException ex)
                {
                    error = true;
                    exception = true;
                    throw constructTermination("messageErrorWhileHandlingResponseData", ex, null);
                }
                catch (IOException ex)
                {
                    error = true;
                    exception = true;
                    throw constructTermination("messageErrorWhileHandlingResponseData", ex, null);
                }
                finally
                {
                    if (writer != null)
                    {
                        try
                        {
                            writer.close();
                            writer = null;
                        }
                        catch (IOException ex)
                        {
                            if (exception == false)
                            {
                                exception = true;
                                throw constructTermination("messageErrorWhileHandlingResponseData", ex, null);
                            }
                        }
                        finally
                        {
                            writer = null;

                            if (reader != null)
                            {
                                try
                                {
                                    reader.close();
                                    reader = null;
                                }
                                catch (IOException ex)
                                {
                                    if (exception == false)
                                    {
                                        exception = true;
                                        throw constructTermination("messageErrorWhileHandlingResponseData", ex, null);
                                    }
                                }
                                finally
                                {
                                    reader = null;
                                }
                            }
                        }
                    }

                    if (reader != null)
                    {
                        try
                        {
                            reader.close();
                            reader = null;
                        }
                        catch (IOException ex)
                        {
                            if (exception == false)
                            {
                                exception = true;
                                throw constructTermination("messageErrorWhileHandlingResponseData", ex, null);
                            }
                        }
                        finally
                        {
                            reader = null;
                        }
                    }
                }
            }

            if (error == false)
            {
                try
                {
                    responseDataFile = responseDataFile.getCanonicalFile();
                }
                catch (IOException ex)
                {

                }

               this.getResults().put("response-data-file", responseDataFile.getAbsolutePath());
            }
        }

        if (requestDataSourceFile != null &&
            ownsRequestDataSourceFile == true)
        {
            try
            {
                requestDataSourceFile.delete();
            }
            catch (SecurityException ex)
            {
                throw constructTermination("messageTemporaryRequestDataSourceFileIsntDeletable", null, null, requestDataSourceFile.getAbsolutePath());
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
                message = "https_client_1: " + getI10nString(id);
            }
            else
            {
                message = "https_client_1: " + getI10nStringFormatted(id, arguments);
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
                message = "https_client_1: " + getI10nString(id);
            }
            else
            {
                message = "https_client_1: " + getI10nStringFormatted(id, arguments);
            }
        }

        return new ProgramTerminationException(id, cause, message, L10N_BUNDLE, arguments);
    }

    public int handleTermination(ProgramTerminationException ex)
    {
        String timestamp = ex.getTimestamp();
        String message = ex.getMessage();
        String id = ex.getId();
        String bundle = ex.getBundle();
        Object[] arguments = ex.getArguments();

        if (message != null)
        {
            System.err.println(message);
        }

        Throwable innerException = ex.getCause();

        if (innerException != null)
        {
            innerException.printStackTrace();
        }

        if (https_client_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(https_client_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by https_client_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<https-client-1-result-information>\n");
                writer.write("  <failure>\n");
                writer.write("    <timestamp>" + timestamp + "</timestamp>\n");

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
                    ex.printStackTrace(printWriter);
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

                writer.write("  </failure>\n");
                writer.write("</https-client-1-result-information>\n");
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

        https_client_1.resultInfoFile = null;

        System.exit(-1);
        return -1;
    }

    public Map<String, String> getResults()
    {
        return this.results;
    }

    public List<InfoMessage> getInfoMessages()
    {
        return this.infoMessages;
    }

    public Certificate[] getResultCertificates()
    {
        return this.resultCertificates;
    }

    public void setResultCertificates(Certificate[] resultCertificates)
    {
        this.resultCertificates = resultCertificates;
    }

    public List<ResponseHeaderField> getResponseHeaderFields()
    {
        return this.responseHeaderFields;
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

    private static https_client_1 https_client_1Instance;

    public static File resultInfoFile = null;
    protected static Map<String, String> results = new HashMap<String, String>();
    protected static List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();
    protected static Certificate[] resultCertificates = null;
    protected static List<ResponseHeaderField> responseHeaderFields = new ArrayList<ResponseHeaderField>();

    private static final String L10N_BUNDLE = "l10n.l10nHttpsClient1Console";
    private ResourceBundle l10nConsole;
}
