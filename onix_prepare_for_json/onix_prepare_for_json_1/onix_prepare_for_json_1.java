/* Copyright (C) 2015-2016  Stephan Kreutzer
 *
 * This file is part of onix_prepare_for_json_1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * onix_prepare_for_json_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * onix_prepare_for_json_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with onix_prepare_for_json_1. If not, see <http://www.gnu.org/licenses/>.
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
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartDocument;
import java.util.Iterator;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;



public class onix_prepare_for_json_1
{
    public static void main(String args[])
    {
        System.out.print("onix_prepare_for_json_1 Copyright (C) 2015-2016 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");


        onix_prepare_for_json_1 preparer = new onix_prepare_for_json_1();

        preparer.getInfoMessages().clear();

        try
        {
            preparer.prepare(args);
        }
        catch (ProgramTerminationException ex)
        {
            preparer.handleTermination(ex);
        }

        if (preparer.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(preparer.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by onix_prepare_for_json_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<onix-prepare-for-json-1-result-information>\n");

                if (preparer.getInfoMessages().size() <= 0)
                {
                    writer.write("  <success/>\n");
                }
                else
                {
                    writer.write("  <success>\n");
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = preparer.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = preparer.getInfoMessages().get(i);

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

                writer.write("</onix-prepare-for-json-1-result-information>\n");
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

        preparer.getInfoMessages().clear();
        preparer.resultInfoFile = null;
    }

    public int prepare(String args[]) throws ProgramTerminationException
    {
        this.getInfoMessages().clear();

        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\tonix_prepare_for_json_1 " + getI10nString("messageParameterList") + "\n");
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

        onix_prepare_for_json_1.resultInfoFile = resultInfoFile;


        String programPath = onix_prepare_for_json_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

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

        System.out.println("onix_prepare_for_json_1: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        File inFile = null;
        File outFile = null;

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

                    if (tagName.equals("input-file") == true)
                    {
                        StartElement inputFileElement = event.asStartElement();
                        Attribute pathAttribute = inputFileElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        String inputFilePath = pathAttribute.getValue();

                        inFile = new File(inputFilePath);

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
                    }
                    else if (tagName.equals("output-file") == true)
                    {
                        StartElement outputFileElement = event.asStartElement();
                        Attribute pathAttribute = outputFileElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), tagName, "path");
                        }

                        String outputFilePath = pathAttribute.getValue();

                        outFile = new File(outputFilePath);

                        if (outFile.isAbsolute() != true)
                        {
                            outFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + outputFilePath);
                        }

                        try
                        {
                            outFile = outFile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageOutputFileCantGetCanonicalPath", ex, null, new File(outputFilePath).getAbsolutePath(), jobFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageOutputFileCantGetCanonicalPath", ex, null, new File(outputFilePath).getAbsolutePath(), jobFile.getAbsolutePath());
                        }

                        if (outFile.exists() == true)
                        {
                            if (outFile.isFile() == true)
                            {
                                if (outFile.canWrite() != true)
                                {
                                    throw constructTermination("messageOutputFileIsntWritable", null, null, outFile.getAbsolutePath(), jobFile.getAbsolutePath());
                                }
                            }
                            else
                            {
                                throw constructTermination("messageOutputPathIsntAFile", null, null, outFile.getAbsolutePath(), jobFile.getAbsolutePath());
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

        if (inFile == null)
        {
            throw constructTermination("messageJobFileNoInputFile", null, null, jobFile.getAbsolutePath());
        }

        if (outFile == null)
        {
            throw constructTermination("messageJobFileNoOutputFile", null, null, jobFile.getAbsolutePath());
        }


        File tempFile = new File(tempDirectory.getAbsolutePath() + File.separator + "temp.xml");

        if (tempFile.exists() == true)
        {
            if (tempFile.isFile() == true)
            {
                if (tempFile.canWrite() != true)
                {
                    throw constructTermination("messageTempFileIsntWritable", null, null, tempFile.getAbsolutePath());
                }
            }
            else
            {
                throw constructTermination("messageTempPathIsntAFile", null, null, tempFile.getAbsolutePath());
            }
        }


        /** @todo Identify ONIX version like onix_to_woocommerce_2 workflow, but as separate tool. */
        XMLResolverLocal entityResolver = new XMLResolverLocal(new File(programPath + "entities" + File.separator + "config_onix_2_1_3_short.xml"));

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setXMLResolver(entityResolver);

            InputStream in = new FileInputStream(inFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in, "UTF-8");

            XMLEvent event = null;

            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(tempFile),
                                    "UTF-8"));

            StringBuilder d102 = null;
            StringBuilder descriptionTextStart = null;
            StringBuilder descriptionText = null;
            StringBuilder descriptionTextEnd = null;

            boolean isInOnixMessage = false;
            boolean isInProduct = false;
            boolean isInOthertext = false;
            boolean isInD102 = false;
            boolean isInD104 = false;

            while (eventReader.hasNext() == true)
            {
                event = eventReader.nextEvent();

                if (event.isStartDocument() == true)
                {
                    StartDocument startDocument = (StartDocument)event;

                    writer.write("<?xml version=\"" + startDocument.getVersion() + "\"");

                    if (startDocument.encodingSet() == true)
                    {
                        writer.write(" encoding=\"" + startDocument.getCharacterEncodingScheme() + "\"");
                    }

                    if (startDocument.standaloneSet() == true)
                    {
                        writer.write(" standalone=\"");

                        if (startDocument.isStandalone() == true)
                        {
                            writer.write("yes");
                        }
                        else
                        {
                            writer.write("no");
                        }

                        writer.write("\"");
                    }

                    writer.write("?>\n");
                }
                else if (event.isStartElement() == true)
                {
                    QName elementName = event.asStartElement().getName();
                    String fullElementName = elementName.getLocalPart();

                    if (elementName.getPrefix().isEmpty() != true)
                    {
                        fullElementName = elementName.getPrefix() + ":" + fullElementName;
                    }

                    if (fullElementName.equals("ONIXmessage") == true)
                    {
                        if (isInOnixMessage == true)
                        {
                            throw constructTermination("messageInputFileNestedTag", null, null, fullElementName, inFile.getAbsolutePath());
                        }

                        isInOnixMessage = true;
                    }
                    else if (fullElementName.equals("product") == true &&
                             isInOnixMessage == true)
                    {
                        if (isInProduct == true)
                        {
                            throw constructTermination("messageInputFileNestedTag", null, null, fullElementName, inFile.getAbsolutePath());
                        }

                        isInProduct = true;
                    }
                    else if (fullElementName.equals("othertext") == true &&
                             isInOnixMessage == true &&
                             isInProduct == true)
                    {
                        if (isInOthertext == true)
                        {
                            throw constructTermination("messageInputFileNestedTag", null, null, fullElementName, inFile.getAbsolutePath());
                        }

                        isInOthertext = true;

                        d102 = new StringBuilder();
                        descriptionTextStart = new StringBuilder();
                        descriptionText = new StringBuilder();
                        descriptionTextEnd = new StringBuilder();
                    }
                    else if (fullElementName.equals("d102") == true &&
                             isInOnixMessage == true &&
                             isInProduct == true &&
                             isInOthertext == true)
                    {
                        if (isInD102 == true)
                        {
                            throw constructTermination("messageInputFileNestedTag", null, null, fullElementName, inFile.getAbsolutePath());
                        }

                        isInD102 = true;
                    }
                    else if (fullElementName.equals("d104") == true &&
                             isInOnixMessage == true &&
                             isInProduct == true &&
                             isInOthertext == true)
                    {
                        if (isInD104 == true)
                        {
                            throw constructTermination("messageInputFileNestedTag", null, null, fullElementName, inFile.getAbsolutePath());
                        }

                        isInD104 = true;
                    }

                    if (isInD104 != true)
                    {
                        writer.write("<" + fullElementName);

                        // http://coding.derkeiler.com/Archive/Java/comp.lang.java.help/2008-12/msg00090.html
                        @SuppressWarnings("unchecked")
                        Iterator<Attribute> attributes = (Iterator<Attribute>)event.asStartElement().getAttributes();

                        while (attributes.hasNext() == true)
                        {
                            Attribute attribute = attributes.next();
                            QName attributeName = attribute.getName();
                            String fullAttributeName = attributeName.getLocalPart();

                            if (attributeName.getPrefix().length() > 0)
                            {
                                fullAttributeName = attributeName.getPrefix() + ":" + fullAttributeName;
                            }

                            String attributeValue = attribute.getValue();

                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            attributeValue = attributeValue.replaceAll("&", "&amp;");
                            attributeValue = attributeValue.replaceAll("\"", "&quot;");
                            attributeValue = attributeValue.replaceAll("'", "&apos;");
                            attributeValue = attributeValue.replaceAll("<", "&lt;");
                            attributeValue = attributeValue.replaceAll(">", "&gt;");

                            writer.write(" " + fullAttributeName + "=\"" + attributeValue + "\"");
                        }

                        writer.write(">");
                    }
                    else
                    {
                        descriptionTextStart.append("<" + fullElementName);

                        // http://coding.derkeiler.com/Archive/Java/comp.lang.java.help/2008-12/msg00090.html
                        @SuppressWarnings("unchecked")
                        Iterator<Attribute> attributes = (Iterator<Attribute>)event.asStartElement().getAttributes();

                        while (attributes.hasNext() == true)
                        {
                            Attribute attribute = attributes.next();
                            QName attributeName = attribute.getName();
                            String fullAttributeName = attributeName.getLocalPart();

                            if (attributeName.getPrefix().length() > 0)
                            {
                                fullAttributeName = attributeName.getPrefix() + ":" + fullAttributeName;
                            }

                            String attributeValue = attribute.getValue();

                            // Ampersand needs to be the first, otherwise it would double-encode
                            // other entities.
                            attributeValue = attributeValue.replaceAll("&", "&amp;");
                            attributeValue = attributeValue.replaceAll("\"", "&quot;");
                            attributeValue = attributeValue.replaceAll("'", "&apos;");
                            attributeValue = attributeValue.replaceAll("<", "&lt;");
                            attributeValue = attributeValue.replaceAll(">", "&gt;");

                            descriptionTextStart.append(" " + fullAttributeName + "=\"" + attributeValue + "\"");
                        }

                        descriptionTextStart.append(">");
                    }
                }
                else if (event.isEndElement() == true)
                {
                    boolean output = true;

                    QName elementName = event.asEndElement().getName();
                    String fullElementName = elementName.getLocalPart();

                    if (elementName.getPrefix().isEmpty() != true)
                    {
                        fullElementName = elementName.getPrefix() + ":" + fullElementName;
                    }

                    if (isInOnixMessage == true &&
                        isInProduct == true &&
                        isInOthertext == true &&
                        isInD104 == true &&
                        fullElementName.equals("d104") == true)
                    {
                        isInD104 = false;
                        output = false;
                        descriptionTextEnd.append("</" + fullElementName + ">");
                    }
                    else if (isInOnixMessage == true &&
                             isInProduct == true &&
                             isInOthertext == true &&
                             isInD102 == true &&
                             fullElementName.equals("d102") == true)
                    {
                        isInD102 = false;
                    }
                    else if (isInOnixMessage == true &&
                             isInProduct == true &&
                             isInOthertext == true &&
                             fullElementName.equals("othertext") == true)
                    {
                        if (isInD104 == true)
                        {
                            throw constructTermination("messageInputFileMissingTagWhileEncounteringEndTag", null, null, fullElementName, "d104", inFile.getAbsolutePath());
                        }

                        if (isInD102 == true)
                        {
                            throw constructTermination("messageInputFileMissingTagWhileEncounteringEndTag", null, null, fullElementName, "d102", inFile.getAbsolutePath());
                        }

                        writer.write(descriptionTextStart.toString());

                        if (d102.toString().equals("01") != true)
                        {
                            writer.write(descriptionText.toString());
                        }
                        else
                        {
                            String text = descriptionText.toString();

                            int count = text.length();

                            for (int i = 0; i < count; i++)
                            {
                                jsonStringEscapeChar(text.charAt(i), writer);
                            }
                        }

                        writer.write(descriptionTextEnd.toString());

                        isInOthertext = false;

                        d102 = null;
                        descriptionTextStart = null;
                        descriptionText = null;
                        descriptionTextEnd = null;
                    }
                    else if (isInProduct == true &&
                             fullElementName.equals("product") == true)
                    {
                        if (isInD104 == true)
                        {
                            throw constructTermination("messageInputFileMissingTagWhileEncounteringEndTag", null, null, fullElementName, "d104", inFile.getAbsolutePath());
                        }

                        if (isInD102 == true)
                        {
                            throw constructTermination("messageInputFileMissingTagWhileEncounteringEndTag", null, null, fullElementName, "d102", inFile.getAbsolutePath());
                        }

                        if (isInOthertext == true)
                        {
                            throw constructTermination("messageInputFileMissingTagWhileEncounteringEndTag", null, null, fullElementName, "othertext", inFile.getAbsolutePath());
                        }

                        isInProduct = false;
                    }
                    else if (isInOnixMessage == true &&
                             fullElementName.equals("ONIXmessage") == true)
                    {
                        if (isInD104 == true)
                        {
                            throw constructTermination("messageInputFileMissingTagWhileEncounteringEndTag", null, null, fullElementName, "d104", inFile.getAbsolutePath());
                        }

                        if (isInD102 == true)
                        {
                            throw constructTermination("messageInputFileMissingTagWhileEncounteringEndTag", null, null, fullElementName, "d102", inFile.getAbsolutePath());
                        }

                        if (isInOthertext == true)
                        {
                            throw constructTermination("messageInputFileMissingTagWhileEncounteringEndTag", null, null, fullElementName, "othertext", inFile.getAbsolutePath());
                        }

                        if (isInProduct == true)
                        {
                            throw constructTermination("messageInputFileMissingTagWhileEncounteringEndTag", null, null, fullElementName, "product", inFile.getAbsolutePath());
                        }

                        isInOnixMessage = false;
                    }

                    if (output == true)
                    {
                        writer.write("</" + fullElementName + ">");
                    }
                }
                else if (event.isCharacters() == true)
                {
                    if (isInD102 == true)
                    {
                        d102.append(event.asCharacters().getData());
                        event.writeAsEncodedUnicode(writer); 
                    }
                    else if (isInD104 == true)
                    {
                        descriptionText.append(event.asCharacters().getData());
                    }
                    else
                    {
                        event.writeAsEncodedUnicode(writer); 
                    }
                }
                else if (event.getEventType() == XMLStreamConstants.COMMENT)
                {
                    writer.write("<!--" + ((Comment)event).getText() + "-->");
                }
                else if (event.getEventType() == XMLStreamConstants.DTD)
                {
                    DTD dtd = (DTD) event;

                    if (dtd != null)
                    {
                        writer.write(dtd.getDocumentTypeDeclaration());
                    }
                }
                else if (event.isEndDocument() == true)
                {

                }
                else
                {
                    throw constructTermination("messageUnsupportedXMLEventType", null, null, event.getEventType(), "javax.xml.stream.XMLStreamConstants", inFile.getAbsolutePath());
                }
            }

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageInputFileErrorWhileReading", ex, null, inFile.getAbsolutePath());
        }
        catch (XMLStreamException ex)
        {
            throw constructTermination("messageInputFileErrorWhileReading", ex, null, inFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageInputFileErrorWhileReading", ex, null, inFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageInputFileErrorWhileReading", ex, null, inFile.getAbsolutePath());
        }

        this.infoMessages.addAll(entityResolver.getInfoMessages());

        if (CopyFileBinary(tempFile, outFile) != 0)
        {
            System.exit(-1);
        }

        if (tempFile.delete() != true)
        {
            throw constructTermination("messageCantDeleteTemporaryFile", null, null, tempFile.getAbsolutePath());
        }

        return 0;
    }

    public void jsonStringEscapeChar(char input, BufferedWriter writer) throws IOException
    {
        if (input == '"')
        {
            writer.write("\\\"");
        }
        else if (input == '\\')
        {
            writer.write("\\\\");
        }
        else if (input == '/')
        {
            writer.write("\\/");
        }
        else if (input == '\n')
        {
            writer.write("\\n");
        }
        else if (input == '\r')
        {
            writer.write("\\r");
        }
        else
        {
            writer.write(input);
        }
    }

    public int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            throw constructTermination("messageCantCopyBecauseFromDoesntExist", null, null, from.getAbsolutePath(), to.getAbsolutePath());
        }

        if (from.isFile() != true)
        {
            throw constructTermination("messageCantCopyBecauseFromIsntAFile", null, null, from.getAbsolutePath(), to.getAbsolutePath());
        }

        if (from.canRead() != true)
        {
            throw constructTermination("messageCantCopyBecauseFromIsntReadable", null, null, from.getAbsolutePath(), to.getAbsolutePath());
        }

        if (to.exists() == true)
        {
            if (to.isFile() == true)
            {
                if (to.canWrite() != true)
                {
                    throw constructTermination("messageCantCopyBecauseToIsntWritable", null, null, from.getAbsolutePath(), to.getAbsolutePath());
                }
            }
            else
            {
                throw constructTermination("messageCantCopyBecauseToIsntAFile", null, null, from.getAbsolutePath(), to.getAbsolutePath());
            }
        }


        Exception exception = null;

        byte[] buffer = new byte[1024];

        FileInputStream reader = null;
        FileOutputStream writer = null;

        try
        {
            to.createNewFile();

            reader = new FileInputStream(from);
            writer = new FileOutputStream(to);

            int bytesRead = reader.read(buffer, 0, buffer.length);

            while (bytesRead > 0)
            {
                writer.write(buffer, 0, bytesRead);
                bytesRead = reader.read(buffer, 0, buffer.length);
            }

            writer.close();
            reader.close();
        }
        catch (FileNotFoundException ex)
        {
            exception = ex;
        }
        catch (IOException ex)
        {
            exception = ex;
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
                    if (exception == null)
                    {
                        exception = ex;
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
                    if (exception == null)
                    {
                        exception = ex;
                    }
                }
            }
        }

        if (exception != null)
        {
            throw constructTermination("messageErrorWhileCopying", exception, null, from.getAbsolutePath(), to.getAbsolutePath());
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
                message = "onix_prepare_for_json_1: " + getI10nString(id);
            }
            else
            {
                message = "onix_prepare_for_json_1: " + getI10nStringFormatted(id, arguments);
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
                message = "onix_prepare_for_json_1: " + getI10nString(id);
            }
            else
            {
                message = "onix_prepare_for_json_1: " + getI10nStringFormatted(id, arguments);
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

        if (onix_prepare_for_json_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(onix_prepare_for_json_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by onix_prepare_for_json_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<onix-prepare-for-json-1-result-information>\n");

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

                writer.write("</onix-prepare-for-json-1-result-information>\n");
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

        onix_prepare_for_json_1.resultInfoFile = null;

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

    private static final String L10N_BUNDLE = "l10n.l10nOnixPrepareForJson1Console";
    private ResourceBundle l10nConsole;
}
