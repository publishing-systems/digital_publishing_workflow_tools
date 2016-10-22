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
 * @file $/workflows/wordpress_media_library_file_uploader/wordpress_media_library_file_uploader_1/JobFileProcessor.java
 * @brief Processor to read the job file.
 * @author Stephan Kreutzer
 * @since 2016-03-03
 */



import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
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
import java.io.UnsupportedEncodingException;



class JobFileProcessor
{
    public JobFileProcessor(File jobFile)
    {
        this.jobFile = jobFile;
    }

    public int Run()
    {
        this.inputFiles.clear();
        this.jobSettings.clear();
        this.infoMessages.clear();

        if (this.jobFile == null)
        {
            throw constructTermination("messageJobFileProcessorNoJobFile", null, null, this.getClass().getName());
        }

        try
        {
            this.jobFile = this.jobFile.getCanonicalFile();
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageJobFileCantGetCanonicalPath", ex, null, this.jobFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageJobFileCantGetCanonicalPath", ex, null, this.jobFile.getAbsolutePath());
        }

        if (this.jobFile.exists() != true)
        {
            throw constructTermination("messageJobFileDoesntExist", null, null, this.jobFile.getAbsolutePath());
        }

        if (this.jobFile.isFile() != true)
        {
            throw constructTermination("messageJobPathIsntAFile", null, null, this.jobFile.getAbsolutePath());
        }

        if (this.jobFile.canRead() != true)
        {
            throw constructTermination("messageJobFileIsntReadable", null, null, this.jobFile.getAbsolutePath());
        }


        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(this.jobFile);
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

                        Attribute nameAttribute = inputFileElement.getAttributeByName(new QName("name"));

                        if (nameAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "name");
                        }

                        Attribute typeAttribute = inputFileElement.getAttributeByName(new QName("type"));

                        if (typeAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "type");
                        }

                        Attribute overwriteAttribute = inputFileElement.getAttributeByName(new QName("overwrite"));

                        if (overwriteAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "overwrite");
                        }

                        String inputFilePath = pathAttribute.getValue();

                        File inFile = new File(inputFilePath);

                        if (inFile.isAbsolute() != true)
                        {
                            inFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + inputFilePath);
                        }

                        try
                        {
                            inFile = inFile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageInputFileCantGetCanonicalPath", ex, null, new File(inputFilePath).getAbsolutePath(), jobFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageInputFileCantGetCanonicalPath", ex, null, new File(inputFilePath).getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (inFile.exists() != true)
                        {
                            throw constructTermination("messageInputFileDoesntExist", null, null, inFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (inFile.isFile() != true)
                        {
                            throw constructTermination("messageInputPathIsntAFile", null, null, inFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (inFile.canRead() != true)
                        {
                            throw constructTermination("messageInputFileIsntReadable", null, null, inFile.getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        String name = nameAttribute.getValue();
                        String type = typeAttribute.getValue();
                        String overwriteString = overwriteAttribute.getValue();
                        boolean overwrite = false;

                        if (overwriteString.equals("true") == true)
                        {
                            overwrite = true;
                        }
                        else
                        {
                            overwrite = false;
                        }

                        /** @todo Check if the file is referenced more than once. */
                        this.inputFiles.add(new InputFileInfo(inFile,
                                                              name,
                                                              type,
                                                              overwrite));
                    }
                    else if (tagName.equalsIgnoreCase("wordpress-xmlrpc-url") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            if (this.jobSettings.containsKey("wordpress-xmlrpc-url") != true)
                            {
                                this.jobSettings.put("wordpress-xmlrpc-url", event.asCharacters().getData());
                            }
                            else
                            {
                                this.infoMessages.add(constructInfoMessage("messageJobFileXMLRPCURLSpecifiedMoreThanOnce", true, null, null, this.jobFile.getAbsolutePath()));
                            }
                        }
                    }
                    else if (tagName.equalsIgnoreCase("wordpress-user-name") == true)
                    {
                        StartElement wordpressUserNameElement = event.asStartElement();
                        Attribute valueAttribute = wordpressUserNameElement.getAttributeByName(new QName("value"));

                        if (valueAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "value");
                        }

                        if (this.jobSettings.containsKey("wordpress-user-name") != true)
                        {
                            this.jobSettings.put("wordpress-user-name", valueAttribute.getValue());
                        }
                        else
                        {
                            this.infoMessages.add(constructInfoMessage("messageJobFileWordpressUserNameSpecifiedMoreThanOnce", true, null, null, this.jobFile.getAbsolutePath()));
                        }
                    }
                    else if (tagName.equalsIgnoreCase("wordpress-user-password") == true)
                    {
                        StartElement wordpressUserPasswordElement = event.asStartElement();
                        Attribute valueAttribute = wordpressUserPasswordElement.getAttributeByName(new QName("value"));

                        if (valueAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "value");
                        }

                        if (this.jobSettings.containsKey("wordpress-user-password") != true)
                        {
                            this.jobSettings.put("wordpress-user-password", valueAttribute.getValue());
                        }
                        else
                        {
                            this.infoMessages.add(constructInfoMessage("messageJobFileWordpressUserPasswordSpecifiedMoreThanOnce", true, null, null, this.jobFile.getAbsolutePath()));
                        }
                    }
                    else if (tagName.equalsIgnoreCase("http-header-field-authorization") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            if (this.jobSettings.containsKey("http-header-field-authorization") != true)
                            {
                                this.jobSettings.put("http-header-field-authorization", event.asCharacters().getData());
                            }
                            else
                            {
                                this.infoMessages.add(constructInfoMessage("messageJobFileHttpHeaderFieldAuthorizationSpecifiedMoreThanOnce", true, null, null, this.jobFile.getAbsolutePath()));
                            }
                        }
                    }
                    else if (tagName.equalsIgnoreCase("https-accept-host-for-certificate-mismatch") == true)
                    {
                        event = eventReader.nextEvent();

                        if (event.isCharacters() == true)
                        {
                            if (this.jobSettings.containsKey("https-accept-host-for-certificate-mismatch") != true)
                            {
                                this.jobSettings.put("https-accept-host-for-certificate-mismatch", event.asCharacters().getData());
                            }
                            else
                            {
                                this.infoMessages.add(constructInfoMessage("messageJobFileHttpsAcceptHostForCertificateMismatchSpecifiedMoreThanOnce", true, null, null, tagName, this.jobFile.getAbsolutePath()));
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

        return 0;
    }

    public List<InputFileInfo> getInputFiles()
    {
        return this.inputFiles;
    }

    public Map<String, String> getJobSettings()
    {
        return this.jobSettings;
    }

    public List<InfoMessage> getInfoMessages()
    {
        return this.infoMessages;
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

    private File jobFile = null;
    private List<InputFileInfo> inputFiles = new ArrayList<InputFileInfo>();
    private Map<String, String> jobSettings = new HashMap<String, String>();
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nJobFileProcessorConsole";
    private ResourceBundle l10nConsole = null;
}
