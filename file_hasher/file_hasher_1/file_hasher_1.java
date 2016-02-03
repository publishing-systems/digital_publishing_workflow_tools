/* Copyright (C) 2016 Stephan Kreutzer
 *
 * This file is part of file_hasher_1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * file_hasher_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * file_hasher_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with file_hasher_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/file_hasher/file_hasher_1/file_hasher_1.java
 * @brief Wrapper to call Java hash algorithms on files.
 * @author Stephan Kreutzer
 * @since 2016-01-31
 */



import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
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
import java.security.MessageDigest;
import java.security.DigestInputStream;
import java.security.NoSuchAlgorithmException;



public class file_hasher_1
{
    protected file_hasher_1()
    {
        // Singleton to protect file_hasher_1.resultInfoFile from conflicting use.
    }

    public static file_hasher_1 getInstance()
    {
        if (file_hasher_1.file_hasher_1Instance == null)
        {
            file_hasher_1.file_hasher_1Instance = new file_hasher_1();
        }

        return file_hasher_1.file_hasher_1Instance;
    }

    public static void main(String args[])
    {
        System.out.print("file_hasher_1 Copyright (C) 2016 Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");

        file_hasher_1 hasher = file_hasher_1.getInstance();
        hasher.getResults().clear();
        hasher.getInfoMessages().clear();

        try
        {
            hasher.hash(args);
        }
        catch (ProgramTerminationException ex)
        {
            hasher.handleTermination(ex);
        }

        if (hasher.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(hasher.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by file_hasher_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<file-hasher-1-result-information>\n");
                writer.write("  <success>\n");

                if (hasher.getResults() != null)
                {
                    if (hasher.getResults().isEmpty() != true)
                    {
                        for (Map.Entry<String, String> entry : hasher.getResults().entrySet())
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

                if (hasher.getInfoMessages().size() > 0)
                {
                    writer.write("    <info-messages>\n");

                    for (int i = 0, max = hasher.getInfoMessages().size(); i < max; i++)
                    {
                        InfoMessage infoMessage = hasher.getInfoMessages().get(i);

                        writer.write("      <info-message number=\"" + i + "\">\n");

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
                                    writer.write("            <class></className>\n");
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
                                writer.write("            <class>" + className + "</className>\n");
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

                writer.write("  </success>\n");
                writer.write("</file-hasher-1-result-information>\n");
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

        hasher.getInfoMessages().clear();
        hasher.getResults().clear();
        hasher.resultInfoFile = null;
    }

    public int hash(String args[]) throws ProgramTerminationException
    {
        this.resultInfoFile = null;
        this.getResults().clear();
        this.getInfoMessages().clear();

        if (args.length < 2)
        {
            throw constructTermination("messageArgumentsMissing", null, getI10nString("messageArgumentsMissingUsage") + "\n\tfile_hasher_1 " + getI10nString("messageParameterList") + "\n");
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

        file_hasher_1.resultInfoFile = resultInfoFile;

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

        System.out.println("file_hasher_1: " + getI10nStringFormatted("messageCallDetails", jobFile.getAbsolutePath(), resultInfoFile.getAbsolutePath()));


        List<File> inputFiles = new ArrayList<File>();
        File resultFile = null;
        String hashAlgorithm = null;

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

                    if (tagName.equals("file") == true)
                    {
                        StartElement fileElement = event.asStartElement();
                        Attribute pathAttribute = fileElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "file", "path");
                        }

                        String filePath = pathAttribute.getValue();

                        if (filePath.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "file", "path");
                        }

                        File inputFile = new File(filePath);

                        try
                        {
                            inputFile = inputFile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageInputFileCantGetCanonicalPath", ex, null, inputFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageInputFileCantGetCanonicalPath", ex, null, inputFile.getAbsolutePath());
                        }

                        if (inputFile.exists() != true)
                        {
                            throw constructTermination("messageInputFileDoesntExist", null, null, jobFile.getAbsolutePath(), inputFile.getAbsolutePath());
                        }

                        if (inputFile.isFile() != true)
                        {
                            throw constructTermination("messageInputPathIsntAFile", null, null, jobFile.getAbsolutePath(), inputFile.getAbsolutePath());
                        }

                        if (inputFile.canRead() != true)
                        {
                            throw constructTermination("messageInputFileIsntReadable", null, null, jobFile.getAbsolutePath(), inputFile.getAbsolutePath());
                        }

                        /** @todo Check if file is specified multiple times. */

                        inputFiles.add(inputFile);
                    }
                    else if (tagName.equals("hash-algorithm") == true)
                    {
                        StartElement hashAlgorithmElement = event.asStartElement();
                        Attribute useAttribute = hashAlgorithmElement.getAttributeByName(new QName("use"));

                        if (useAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "hash-algorithm", "use");
                        }

                        if (hashAlgorithm != null)
                        {
                            throw constructTermination("messageJobFileHashAlgorithmAlreadySpecified", null, null, jobFile.getAbsolutePath());
                        }

                        hashAlgorithm = useAttribute.getValue();

                        if (hashAlgorithm.equals("MD5") != true &&
                            hashAlgorithm.equals("SHA-1") != true &&
                            hashAlgorithm.equals("SHA-256") != true)
                        {
                            throw constructTermination("messageJobFileHashAlgorithmNotSupported", null, null, jobFile.getAbsolutePath(), hashAlgorithm);
                        }
                    }
                    if (tagName.equals("result-file") == true)
                    {
                        StartElement resultFileElement = event.asStartElement();
                        Attribute pathAttribute = resultFileElement.getAttributeByName(new QName("path"));

                        if (pathAttribute == null)
                        {
                            throw constructTermination("messageJobFileEntryIsMissingAnAttribute", null, null, jobFile.getAbsolutePath(), "result-file", "path");
                        }

                        String resultFilePath = pathAttribute.getValue();

                        if (resultFilePath.isEmpty() == true)
                        {
                            throw constructTermination("messageJobFileAttributeValueIsEmpty", null, null, jobFile.getAbsolutePath(), "result-file", "path");
                        }

                        resultFile = new File(resultFilePath);

                        try
                        {
                            resultFile = resultFile.getCanonicalFile();
                        }
                        catch (SecurityException ex)
                        {
                            throw constructTermination("messageResultFileCantGetCanonicalPath", ex, null, resultFile.getAbsolutePath());
                        }
                        catch (IOException ex)
                        {
                            throw constructTermination("messageResultFileCantGetCanonicalPath", ex, null, resultFile.getAbsolutePath());
                        }

                        if (resultFile.exists() == true)
                        {
                            if (resultFile.isFile() == true)
                            {
                                if (resultFile.canWrite() != true)
                                {
                                    throw constructTermination("messageResultFileIsntWritable", null, null, resultFile.getAbsolutePath());
                                }
                            }
                            else
                            {
                                throw constructTermination("messageResultPathIsntFile", null, null, resultFile.getAbsolutePath());
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

        if (inputFiles.size() <= 0)
        {
            this.infoMessages.add(constructInfoMessage("messageJobFileNoInputFiles", true, null, null, jobFile.getAbsolutePath()));
            return 0;
        }

        if (hashAlgorithm == null)
        {
            throw constructTermination("messageJobFileNoHashAlgorithm", null, null, jobFile.getAbsolutePath());
        }

        if (resultFile == null)
        {
            throw constructTermination("messageJobFileNoResultFileConfigured", null, null, jobFile.getAbsolutePath());
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(
                                    new FileOutputStream(resultFile),
                                    "UTF-8"));

            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!-- This file was created by file_hasher_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
            writer.write("<hashes>\n");

            byte[] buffer = new byte[1024];

            for (File inputFile : inputFiles)
            {
                if (hashAlgorithm.equals("MD5") == true ||
                    hashAlgorithm.equals("SHA-1") == true ||
                    hashAlgorithm.equals("SHA-256") == true)
                {
                    StringBuffer hexString = new StringBuffer();
                    MessageDigest md = null;

                    try
                    {
                        md = MessageDigest.getInstance(hashAlgorithm);
                    }
                    catch (NoSuchAlgorithmException ex)
                    {
                        throw constructTermination("messageJobFileHashAlgorithmNotSupported", ex, null, jobFile.getAbsolutePath(), hashAlgorithm);
                    }

                    try
                    {
                        InputStream in = new FileInputStream(inputFile);
                        DigestInputStream reader = new DigestInputStream(in, md);

                        int charactersRead = reader.read(buffer, 0, buffer.length);

                        while (charactersRead > 0)
                        {
                            // Look at all bytes of the file.
                            charactersRead = reader.read(buffer, 0, buffer.length);
                        }

                        reader.close();
                    }
                    catch (FileNotFoundException ex)
                    {
                        throw constructTermination("messageInputFileReadingError", ex, null, inputFile.getAbsolutePath());
                    }
                    catch (IOException ex)
                    {
                        throw constructTermination("messageInputFileReadingError", ex, null, inputFile.getAbsolutePath());
                    }

                    byte[] digest = md.digest();

                    for (int i = 0; i < digest.length; i++)
                    {
                        String hex = Integer.toHexString(0xFF & digest[i]);

                        if (hex.length() == 1)
                        {
                            hexString.append('0');
                        }

                        hexString.append(hex);
                    }

                    writer.write("  <hash value=\"" + hexString + "\" algorithm=\"" + hashAlgorithm + "\"/>\n");
                }
                else
                {
                    throw constructTermination("messageJobFileHashAlgorithmNotSupported", null, null, jobFile.getAbsolutePath(), hashAlgorithm);
                }
            }

            writer.write("</hashes>\n");

            writer.flush();
            writer.close();
        }
        catch (FileNotFoundException ex)
        {
            throw constructTermination("messageResultFileWritingError", ex, null, resultFile.getAbsolutePath());
        }
        catch (UnsupportedEncodingException ex)
        {
            throw constructTermination("messageResultFileWritingError", ex, null, resultFile.getAbsolutePath());
        }
        catch (IOException ex)
        {
            throw constructTermination("messageResultFileWritingError", ex, null, resultFile.getAbsolutePath());
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
                message = "file_hasher_1: " + getI10nString(id);
            }
            else
            {
                message = "file_hasher_1: " + getI10nStringFormatted(id, arguments);
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
                message = "file_hasher_1: " + getI10nString(id);
            }
            else
            {
                message = "file_hasher_1: " + getI10nStringFormatted(id, arguments);
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

        if (file_hasher_1.resultInfoFile != null)
        {
            try
            {
                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(
                                        new FileOutputStream(file_hasher_1.resultInfoFile),
                                        "UTF-8"));

                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<!-- This file was created by file_hasher_1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->\n");
                writer.write("<file_hasher-1-result-information>\n");
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
                writer.write("</file-hasher-1-result-information>\n");
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

        file_hasher_1.resultInfoFile = null;

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

    private static file_hasher_1 file_hasher_1Instance;

    public static File resultInfoFile = null;
    protected Map<String, String> results = new HashMap<String, String>();
    protected List<InfoMessage> infoMessages = new ArrayList<InfoMessage>();

    private static final String L10N_BUNDLE = "l10n.l10nFileHasher1Console";
    private ResourceBundle l10nConsole;
}
