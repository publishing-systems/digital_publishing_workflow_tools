/* Copyright (C) 2014-2015 Stephan Kreutzer
 *
 * This file is part of xsltransformator1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * xsltransformator1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * xsltransformator1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with xsltransformator1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/xsltransformator/xsltransformator1/xsltransformator1.java
 * @brief Wrapper for using a Java XSLT processor from the command line.
 * @author Stephan Kreutzer
 * @since 2014-03-29
 */



import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.UnsupportedEncodingException;
import javax.xml.stream.XMLInputFactory;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Attribute;
import javax.xml.namespace.QName;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import javax.xml.parsers.SAXParser;
import org.xml.sax.XMLReader;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;



public class xsltransformator1
{
    protected xsltransformator1()
    {
        // Singleton to protect this.resultInfoFile from conflicting use.
    }

    public static xsltransformator1 getInstance()
    {
        if (xsltransformator1.xsltransformator1Instance == null)
        {
            xsltransformator1.xsltransformator1Instance = new xsltransformator1();
        }

        return xsltransformator1.xsltransformator1Instance;
    }

    public static void main(String args[])
    {
        System.out.print("xsltransformator1 Copyright (C) 2014-2015 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");

        xsltransformator1 transformator1 = xsltransformator1.getInstance();

        try
        {
            transformator1.transform(args);
        }
        catch (ProgramTerminationException ex)
        {
            transformator1.handleTermination(ex);
        }

        if (xsltransformator1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(xsltransformator1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by xsltransformator1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<xsltransformator1-result-information>\n");
                writer.write("  <success/>\n");
                writer.write("</xsltransformator1-result-information>\n");
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

        xsltransformator1.resultInfoFile = null;
    }

    public int transform(String args[]) throws ProgramTerminationException
    {
        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\txsltransformator1 " + getI10nString("messageParameterList") + "\n");
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
                throw constructTermination("messageResultInfoPathIsADirectory", null, resultInfoFile.getAbsolutePath());
            }

            if (resultInfoFile.canWrite() != true)
            {
                throw constructTermination("messageResultInfoFileIsntWritable", null, null, resultInfoFile.getAbsolutePath());
            }
        }

        xsltransformator1.resultInfoFile = resultInfoFile;

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

        System.out.println("xsltransformator1: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        List<JobDefinition> jobs = new ArrayList<JobDefinition>();

        try
        {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(jobFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            int jobEntryNumber = 0;

            while (eventReader.hasNext() == true)
            {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement() == true)
                {
                    String tagName = event.asStartElement().getName().getLocalPart();

                    if (tagName.equals("job") == true)
                    {
                        jobEntryNumber++;

                        StartElement jobElement = event.asStartElement();

                        Attribute inputFileAttribute = jobElement.getAttributeByName(new QName("input-file"));

                        if (inputFileAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), jobEntryNumber, "input-file");
                        }

                        Attribute entitiesResolverConfigFileAttribute = jobElement.getAttributeByName(new QName("entities-resolver-config-file"));

                        if (entitiesResolverConfigFileAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), jobEntryNumber, "entities-resolver-config-file");
                        }

                        Attribute stylesheetFileAttribute = jobElement.getAttributeByName(new QName("stylesheet-file"));

                        if (stylesheetFileAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), jobEntryNumber, "stylesheet-file");
                        }

                        Attribute outputFileAttribute = jobElement.getAttributeByName(new QName("output-file"));

                        if (outputFileAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), jobEntryNumber, "output-file");
                        }


                        File inFile = new File(inputFileAttribute.getValue());

                        if (inFile.isAbsolute() != true)
                        {
                            inFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + inputFileAttribute.getValue());
                        }

                        inFile = inFile.getCanonicalFile();

                        if (inFile.exists() != true)
                        {
                            throw constructTermination("messageJobFileEntryInputFileDoesntExist", null, null, jobFile.getAbsolutePath(), jobEntryNumber, inFile.getAbsolutePath());
                        }

                        if (inFile.isFile() != true)
                        {
                            throw constructTermination("messageJobFileEntryInputPathIsntAFile", null, null, jobFile.getAbsolutePath(), jobEntryNumber, inFile.getAbsolutePath());
                        }

                        if (inFile.canRead() != true)
                        {
                            throw constructTermination("messageJobFileEntryInputFileIsntReadable", null, null, jobFile.getAbsolutePath(), jobEntryNumber, inFile.getAbsolutePath());
                        }

                        File entitiesResolverConfigFile = new File(entitiesResolverConfigFileAttribute.getValue());

                        if (entitiesResolverConfigFile.isAbsolute() != true)
                        {
                            entitiesResolverConfigFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + entitiesResolverConfigFileAttribute.getValue());
                        }

                        entitiesResolverConfigFile = entitiesResolverConfigFile.getCanonicalFile();

                        if (entitiesResolverConfigFile.exists() != true)
                        {
                            throw constructTermination("messageJobFileEntryEntitiesResolverConfigFileDoesntExist", null, null, jobFile.getAbsolutePath(), jobEntryNumber, entitiesResolverConfigFile.getAbsolutePath());
                        }

                        if (entitiesResolverConfigFile.isFile() != true)
                        {
                            throw constructTermination("messageJobFileEntryEntitiesResolverConfigPathIsntAFile", null, null, jobFile.getAbsolutePath(), jobEntryNumber, entitiesResolverConfigFile.getAbsolutePath());
                        }

                        if (entitiesResolverConfigFile.canRead() != true)
                        {
                            throw constructTermination("messageJobFileEntryEntitiesResolverConfigFileIsntReadable", null, null, jobFile.getAbsolutePath(), jobEntryNumber, entitiesResolverConfigFile.getAbsolutePath());
                        }

                        File stylesheetFile = new File(stylesheetFileAttribute.getValue());

                        if (stylesheetFile.isAbsolute() != true)
                        {
                            stylesheetFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + stylesheetFileAttribute.getValue());
                        }

                        stylesheetFile = stylesheetFile.getCanonicalFile();

                        if (stylesheetFile.exists() != true)
                        {
                            throw constructTermination("messageJobFileEntryStylesheetFileDoesntExist", null, null, jobFile.getAbsolutePath(), jobEntryNumber, stylesheetFile.getAbsolutePath());
                        }

                        if (stylesheetFile.isFile() != true)
                        {
                            throw constructTermination("messageJobFileEntryStylesheetPathIsntAFile", null, null, jobFile.getAbsolutePath(), jobEntryNumber, stylesheetFile.getAbsolutePath());
                        }

                        if (stylesheetFile.canRead() != true)
                        {
                            throw constructTermination("messageJobFileEntryStylesheetFileIsntReadable", null, null, jobFile.getAbsolutePath(), jobEntryNumber, stylesheetFile.getAbsolutePath());
                        }

                        File outFile = new File(outputFileAttribute.getValue());

                        if (outFile.isAbsolute() != true)
                        {
                            outFile = new File(jobFile.getAbsoluteFile().getParent() + File.separator + outputFileAttribute.getValue());
                        }

                        outFile = outFile.getCanonicalFile();

                        if (outFile.exists() == true)
                        {
                            if (outFile.isDirectory() == true)
                            {
                                throw constructTermination("messageJobFileEntryOutputPathIsADirectory", null, null, jobFile.getAbsolutePath(), jobEntryNumber, outFile.getAbsolutePath());
                            }

                            if (outFile.canWrite() != true)
                            {
                                throw constructTermination("messageJobFileEntryOutputFileIsntWritable", null, null, jobFile.getAbsolutePath(), jobEntryNumber, outFile.getAbsolutePath());
                            }
                        }

                        jobs.add(new JobDefinition(inFile,
                                                   entitiesResolverConfigFile,
                                                   stylesheetFile,
                                                   outFile));
                    }
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageJobFileErrorWhileReading", ex, null, jobFile.getAbsolutePath());
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

        int jobCount = jobs.size();

        if (jobCount <= 0)
        {
            throw constructTermination("messageJobFileNoEntriesConfigured", null, null, jobFile.getAbsolutePath());
        }

        for (int i = 0; i < jobCount; i++)
        {
            JobDefinition jobDefinition = jobs.get(i);

            try
            {
                SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                parserFactory.setValidating(false); 
                parserFactory.setNamespaceAware(true);

                EntityResolverLocal localResolver = null;
                EntityResolverLocal2 localResolver2 = null;

                try
                {
                    parserFactory.setFeature("http://xml.org/sax/features/use-entity-resolver2", true);
                    localResolver2 = new EntityResolverLocal2(jobDefinition.GetEntitiesResolverConfigFile());
                } 
                catch (SAXException ex)
                {
                    if (i == 0)
                    {
                        System.out.println("xsltransformator1: " + getI10nStringFormatted("messageCantUseEntityResolver2", jobDefinition.GetEntitiesResolverConfigFile().getAbsolutePath()));
                    }

                    localResolver = new EntityResolverLocal(jobDefinition.GetEntitiesResolverConfigFile());
                }

                SAXParser parser = parserFactory.newSAXParser();
                XMLReader reader = parser.getXMLReader();
                //reader.setErrorHandler(this);

                if (localResolver != null)
                {
                    reader.setEntityResolver(localResolver);
                }
                else if (localResolver2 != null)
                {
                    reader.setEntityResolver(localResolver2);
                }
                else
                {
                    if (i == 0)
                    {
                        System.out.println("xsltransformator1: " + getI10nStringFormatted("messageNoEntityResolver"));
                    }
                }
                
                SAXSource inSource = new SAXSource(reader, new InputSource(jobDefinition.GetInFile().getAbsolutePath()));
                Source stylesheetSource = new StreamSource(jobDefinition.GetStylesheetFile());
                
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer(stylesheetSource);
                transformer.setErrorListener(new TransformerErrorListener());
                /** @todo transformer.setOutputProperty(): http://docs.oracle.com/javase/7/docs/api/javax/xml/transform/Transformer.html#setOutputProperty%28java.lang.String,%20java.lang.String%29 */
                
                transformer.transform(inSource, new StreamResult(jobDefinition.GetOutFile()));
            }
            catch (ParserConfigurationException ex)
            {
                throw constructTermination("messageErrorWhileTransforming", ex, null, jobFile.getAbsolutePath(), i + 1);
            }
            catch (SAXException ex)
            {
                throw constructTermination("messageErrorWhileTransforming", ex, null, jobFile.getAbsolutePath(), i + 1);
            }
            catch (TransformerConfigurationException ex)
            {
                throw constructTermination("messageErrorWhileTransforming", ex, null, jobFile.getAbsolutePath(), i + 1);
            }
            catch (TransformerException ex)
            {
                Throwable cause = ex.getCause();

                while (cause != null)
                {
                    if (cause instanceof ProgramTerminationException)
                    {
                        ProgramTerminationException ex2 = (ProgramTerminationException)cause;
                        throw ex2;
                    }
                    else
                    {
                        cause = cause.getCause();
                    }
                }

                throw constructTermination("messageErrorWhileTransforming", ex, null, jobFile.getAbsolutePath(), i + 1);
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
                message = "xsltransformator1: " + getI10nString(id);
            }
            else
            {
                message = "xsltransformator1: " + getI10nStringFormatted(id, arguments);
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

        if (xsltransformator1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(xsltransformator1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by xsltransformator1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<xsltransformator1-result-information>\n");
                writer.write("  <error>\n");

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

                writer.write("  </error>\n");
                writer.write("</xsltransformator1-result-information>\n");
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

        xsltransformator1.resultInfoFile = null;

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

    private static xsltransformator1 xsltransformator1Instance;

    public static File resultInfoFile;

    private static final String L10N_BUNDLE = "l10n.l10nXsltransformator1Console";
    private ResourceBundle l10nConsole;
}

