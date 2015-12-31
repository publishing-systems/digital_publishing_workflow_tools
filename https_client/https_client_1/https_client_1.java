/* Copyright (C) 2015 Stephan Kreutzer
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.security.cert.Certificate;
import javax.net.ssl.SSLPeerUnverifiedException;



public class https_client_1
{
    protected https_client_1()
    {
        // Singleton to protect https_client_1.resultInfoFile from conflicting use.

        https_client_1.result = new HashMap<String, String>();
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
        System.out.print("https_client_1 Copyright (C) 2015 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");

        https_client_1 client = https_client_1.getInstance();
        client.result.clear();
        client.resultCertificates = null;

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

                for (Map.Entry<String, String> entry : client.result.entrySet())
                {
                    // Ampersand needs to be the first, otherwise it would double-encode
                    // other entities.
                    String value = entry.getValue();
                    value = value.replaceAll("&", "&amp;");
                    value = value.replaceAll("<", "&lt;");
                    value = value.replaceAll(">", "&gt;");

                    writer.write("    <" + entry.getKey() + ">" + value + "</" + entry.getKey() + ">\n");
                }

                if (client.resultCertificates != null)
                {
                    writer.write("    <certificates>\n");

                    for (Certificate certificate : client.resultCertificates)
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
        client.result.clear();
        client.resultCertificates = null;
    }

    public int request(String args[]) throws ProgramTerminationException
    {
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
            if (resultInfoFile.isDirectory() == true)
            {
                throw constructTermination("messageResultInfoPathIsADirectory", null, null, resultInfoFile.getAbsolutePath());
            }

            if (resultInfoFile.canWrite() != true)
            {
                throw constructTermination("messageResultInfoFileIsntWritable", null, null, resultInfoFile.getAbsolutePath());
            }
        }

        https_client_1.resultInfoFile = resultInfoFile;

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
                            throw constructTermination("messageJobFileCantSetHeaderField", null, null, jobFile.getAbsolutePath(), "User-Agent");
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
                        StartElement dataElement = event.asStartElement();
                        Attribute sourceAttribute = dataElement.getAttributeByName(new QName("source"));

                        if (sourceAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "data", "source");
                        }

                        if (requestDataSourceFile != null)
                        {
                            throw constructTermination("messageJobFileSettingConfiguredMoreThanOnce", null, null, jobFile.getAbsolutePath(), "data", "source");
                        }

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
                            throw constructTermination("messageJobFileTagEndWithoutStart", null, null, jobFile.getAbsolutePath(), "request");
                        }

                        inRequest = false;
                    }
                    else if (tagName.equals("header") == true &&
                             inRequest == true)
                    {
                        if (inHeader == false)
                        {
                            throw constructTermination("messageJobFileTagEndWithoutStart", null, null, jobFile.getAbsolutePath(), "header");
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
            if (responseDataFile.isDirectory() == true)
            {
                throw constructTermination("messageResponseDataPathIsADirectory", null, null, responseDataFile.getAbsolutePath());
            }

            if (responseDataFile.canWrite() != true)
            {
                throw constructTermination("messageResponseDataFileIsntWritable", null, null, responseDataFile.getAbsolutePath());
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

            for (Map.Entry<String, String> field : requestHeaderFields.entrySet())
            {
                connection.setRequestProperty(field.getKey(), field.getValue());
            }

            connection.setRequestProperty("User-Agent", "https_client_1 of digital_publishing_workflow_tools (publishing-systems.org)");

            if (requestMethod.equalsIgnoreCase("POST") == true &&
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
                        }
                        catch (IOException ex)
                        {
                            if (exception == false)
                            {
                                throw constructTermination("messageErrorWhileHandlingRequestData", ex, null);
                            }
                        }
                    }

                    if (reader != null)
                    {
                        try
                        {
                            reader.close();
                        }
                        catch (IOException ex)
                        {
                            if (exception == false)
                            {
                                throw constructTermination("messageErrorWhileHandlingRequestData", ex, null);
                            }
                        }
                    }
                }
            }
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
                https_client_1.result.put("cipher-suite", connection.getCipherSuite());

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
                https_client_1.result.put("http-status-code", Integer.toString(connection.getResponseCode()));
            }
            catch (IOException ex)
            {
                throw constructTermination("messageErrorWhileReadingResponse", ex, null);
            }

            boolean error = false;

            byte[] data = new byte[1024];

            DataInputStream reader = null;
            FileOutputStream writer = null;

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
                boolean exception = false;

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
                    exception = true;
                    throw constructTermination("messageErrorWhileHandlingResponseData", ex, null);
                }
                catch (IOException ex)
                {
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
                        }
                        catch (IOException ex)
                        {
                            if (exception == false)
                            {
                                throw constructTermination("messageErrorWhileHandlingResponseData", ex, null);
                            }
                        }
                    }

                    if (reader != null)
                    {
                        try
                        {
                            reader.close();
                        }
                        catch (IOException ex)
                        {
                            if (exception == false)
                            {
                                throw constructTermination("messageErrorWhileHandlingResponseData", ex, null);
                            }
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

                https_client_1.result.put("response-data-file", responseDataFile.getAbsolutePath());
            }
        }

        return 0;
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
                            writer.write("        <class></className>\n");
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
                        writer.write("        <class>" + className + "</className>\n");
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
    public static Map<String, String> result = null;
    public static Certificate[] resultCertificates = null;

    private static final String L10N_BUNDLE = "l10n.l10nHttpsClient1Console";
    private ResourceBundle l10nConsole;
}
