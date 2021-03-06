/* Copyright (C) 2014-2015 Stephan Kreutzer
 *
 * This file is part of xml_xslt_transformator_1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * xml_xslt_transformator_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * xml_xslt_transformator_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with xml_xslt_transformator_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/xml_xslt_transformator/xml_xslt_transformator_1/EntityResolverLocal2.java
 * @brief Resolves external XML entities locally.
 * @author Stephan Kreutzer
 * @since 2014-04-06
 */



import org.xml.sax.ext.EntityResolver2;
import java.io.File;
import org.xml.sax.InputSource;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.UnsupportedEncodingException;



class EntityResolverLocal2 implements EntityResolver2
{
    public EntityResolverLocal2(File configFile) throws ProgramTerminationException
    {
        this.localEntities = new HashMap<String, File>();

        if (configFile.isAbsolute() != true)
        {
            throw constructTermination("messageConfigPathNotAbsolute", null, null, configFile.getAbsolutePath());
        }

        try
        {
            configFile = configFile.getCanonicalFile();
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageConfigFileCantGetCanonicalPath", ex, null, configFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageConfigFileCantGetCanonicalPath", ex, null, configFile.getAbsolutePath());
        }

        if (configFile.exists() != true)
        {
            throw constructTermination("messageConfigFileDoesntExist", null, null, configFile.getAbsolutePath());
        }

        if (configFile.isFile() != true)
        {
            throw constructTermination("messageConfigPathIsntAFile", null, null, configFile.getAbsolutePath());
        }

        if (configFile.canRead() != true)
        {
            throw constructTermination("messageConfigFileIsntReadable", null, null, configFile.getAbsolutePath());
        }

        this.configFile = configFile;


        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(this.configFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            int entityNumber = 0;
            int resolveNumber = 0;
            boolean entity = false;

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equals("entity") == true)
                    {
                        entityNumber++;

                        if (entity == true)
                        {
                            throw constructTermination("messageConfigFileNestedTags", null, null, this.configFile.getAbsolutePath(), "entity");
                        }

                        entity = true;
                    }
                    else if (tagName.equals("resolve") == true)
                    {
                        if (entity == true)
                        {
                            resolveNumber++;

                            StartElement resolveElement = event.asStartElement();

                            Attribute identifierAttribute = resolveElement.getAttributeByName(new QName("identifier"));

                            if (identifierAttribute == null)
                            {
                                throw constructTermination("messageConfigFileEntryIsMissingAnAttribute", null, null, this.configFile.getAbsolutePath(), entityNumber, resolveNumber, "identifier");
                            }

                            Attribute referenceAttribute = resolveElement.getAttributeByName(new QName("reference"));

                            if (referenceAttribute == null)
                            {
                                throw constructTermination("messageConfigFileEntryIsMissingAnAttribute", null, null, this.configFile.getAbsolutePath(), entityNumber, resolveNumber, "reference");
                            }

                            String identifier = identifierAttribute.getValue();
	                          String reference = referenceAttribute.getValue();

                            if (identifier.length() <= 0)
                            {
                                throw constructTermination("messageConfigFileEntryAttributeIsEmpty", null, null, this.configFile.getAbsolutePath(), entityNumber, resolveNumber, "identifier");
                            }
	                          
                            if (reference.length() <= 0)
                            {
                                throw constructTermination("messageConfigFileEntryAttributeIsEmpty", null, null, this.configFile.getAbsolutePath(), entityNumber, resolveNumber, "reference");
                            }
	                          
                            File referencedFile = new File(reference);
                    
                            if (referencedFile.isAbsolute() != true)
                            {
                                String relativePath = this.configFile.getParentFile().getAbsolutePath();

                                if (relativePath.substring((relativePath.length() - File.separator.length()) - new String(".").length(), relativePath.length()).equalsIgnoreCase(File.separator + "."))
                                {
                                    // Remove dot that references the local, current directory.
                                    relativePath = relativePath.substring(0, relativePath.length() - new String(".").length());
                                }
                                
                                if (relativePath.substring(relativePath.length() - File.separator.length(), relativePath.length()).equalsIgnoreCase(File.separator) != true)
                                {
                                    relativePath += File.separator;
                                }
                                
                                relativePath += reference;
                                referencedFile = new File(relativePath);
                            }

                            try
                            {
                                referencedFile = referencedFile.getCanonicalFile();
                            }
                            catch (SecurityException ex)
                            {
                                throw constructTermination("messageReferencedFileCantGetCanonicalPath", ex, null, this.configFile.getAbsolutePath(), entityNumber, resolveNumber, reference, referencedFile.getAbsolutePath());
                            }
                            catch (IOException ex)
                            {
                                throw constructTermination("messageReferencedFileCantGetCanonicalPath", ex, null, this.configFile.getAbsolutePath(), entityNumber, resolveNumber, reference, referencedFile.getAbsolutePath());
                            }

                            if (referencedFile.exists() != true)
                            {
                                throw constructTermination("messageReferencedFileDoesntExist", null, null, this.configFile.getAbsolutePath(), entityNumber, resolveNumber, reference, referencedFile.getAbsolutePath());
                            }

                            if (referencedFile.isFile() != true)
                            {
                                throw constructTermination("messageReferencedPathIsntAFile", null, null, this.configFile.getAbsolutePath(), entityNumber, resolveNumber, reference, referencedFile.getAbsolutePath());
                            }

                            if (referencedFile.canRead() != true)
                            {
                                throw constructTermination("messageReferencedFileIsntReadable", null, null, this.configFile.getAbsolutePath(), entityNumber, resolveNumber, reference, referencedFile.getAbsolutePath());
                            }
                    
                            if (this.localEntities.containsKey(identifier) != true)
                            {
                                this.localEntities.put(identifier, referencedFile);
                            }
                            else
                            {
                                throw constructTermination("messageIdentifierConfiguredTwice", null, null, this.configFile.getAbsolutePath(), identifier);
                            }
                        }
                        else
                        {
                            System.out.println("xml_xslt_transformator_1: " + getI10nStringFormatted("messageConfigFileTagFoundOutsideOfOtherTagAndIgnored", this.configFile.getAbsolutePath(), "resolve", "entity"));
                        }
                    }
                }
                else if (event.isEndElement() == true)
                {
                    String tagName = event.asEndElement().getName().getLocalPart();

                    if (tagName.equals("resolve") == true)
                    {
                        if (entity == true)
                        {
                            resolveNumber = 0;
                        }
                    }
                    else if (tagName.equals("entity") == true)
                    {
                        entity = false;
                    }
                }
            }
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageConfigFileErrorWhileReading", ex, null, configFile.getAbsolutePath());
        }
        catch (SecurityException ex)
        {
            throw constructTermination("messageConfigFileErrorWhileReading", ex, null, configFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageConfigFileErrorWhileReading", ex, null, configFile.getAbsolutePath());
        }
    }

    public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
    {
        return this.resolveEntity(publicId, systemId);
    }

    public InputSource resolveEntity(String publicId, String systemId)
    {
        if (this.configFile == null)
        {
            throw constructTermination("messageCantResolveEntitiesWithoutConfigFile", null, null);
        }

        File localEntity = null;

        if (localEntity == null)
        {
            if (this.localEntities.containsKey(publicId) == true)
            {
                localEntity = this.localEntities.get(publicId);
            }
        }

        if (localEntity == null)
        {
            if (this.localEntities.containsKey(systemId) == true)
            {
                localEntity = this.localEntities.get(systemId);
            }
        }

        if (localEntity == null)
        {
            throw constructTermination("messageCantResolveEntities", null, null, publicId, systemId, this.configFile.getAbsolutePath());                 
        }

        if (localEntity.exists() != true)
        {
            throw constructTermination("messageLocalEntitiesFileDoesntExist", null, null, localEntity.getAbsolutePath(), this.configFile.getAbsolutePath());                 
        }

        if (localEntity.isFile() != true)
        {
            throw constructTermination("messageLocalEntitiesPathIsntAFile", null, null, localEntity.getAbsolutePath(), this.configFile.getAbsolutePath());                 
        }

        if (localEntity.canRead() != true)
        {
            throw constructTermination("messageLocalEntitiesIsntReadable", null, null, localEntity.getAbsolutePath(), this.configFile.getAbsolutePath());                 
        }

        InputSource inputSource = null;

        try
        {
            inputSource = new InputSource(new BufferedReader(new FileReader(localEntity)));
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageLocalEntitiesFileErrorWhileReading", ex, null, localEntity.getAbsolutePath());
        }

        return inputSource;
    }

    public InputSource getExternalSubset(String name, String baseURI)
    {
        return null;
    }

    public ProgramTerminationException constructTermination(String id, Exception cause, String message, Object ... arguments)
    {
        if (message == null)
        {
            if (arguments == null)
            {
                message = "xml_xslt_transformator_1: " + getI10nString(id);
            }
            else
            {
                message = "xml_xslt_transformator_1: " + getI10nStringFormatted(id, arguments);
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

    protected File configFile;
    protected Map<String, File> localEntities;

    private static final String L10N_BUNDLE = "l10n.l10nEntityResolverLocal2Console";
    private ResourceBundle l10nConsole;
}

