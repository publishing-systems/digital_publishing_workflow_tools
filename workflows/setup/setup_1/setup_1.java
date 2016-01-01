/* Copyright (C) 2016 Stephan Kreutzer
 *
 * This file is part of setup_1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * setup_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * setup_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with setup_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/setup/setup_1/setup_1.java
 * @brief Sets up the various tools that get called by the processing workflows.
 * @author Stephan Kreutzer
 * @since 2016-01-01
 */



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;



public class setup_1
{
    public static void main(String args[])
    {
        System.out.print("setup_1 Copyright (C) 2016  Stephan Kreutzer\n" +
                         "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                         "This is free software, and you are welcome to redistribute it\n" +
                         "under certain conditions. See the GNU Affero General Public License 3\n" +
                         "or any later version for details. Also, see the source code repository\n" +
                         "https://github.com/publishing-systems/digital_publishing_workflow_tools/ and\n" +
                         "the project website http://www.publishing-systems.org.\n\n");

        setup_1 setup = new setup_1();
        setup.setup();
    }

    public int setup()
    {
        String programPath = setup_1.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        try
        {
            programPath = new File(programPath).getCanonicalPath() + File.separator;
            programPath = URLDecoder.decode(programPath, "UTF-8");
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


        CopyEntitiesONIX_2_1_3_Short(programPath, ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1" + File.separator + "entities");

        CopyEntitiesXHTML(programPath, ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1" + File.separator + "entities");

        CopyEntitiesUnicode(programPath, ".." + File.separator + ".." + File.separator + ".." + File.separator + "xml_xslt_transformator" + File.separator + "xml_xslt_transformator_1" + File.separator + "entities");

        return 0;
    }


    public int CopyEntitiesONIX_2_1_3_Short(String programPath, String to)
    {
        if (programPath.endsWith("/") != true &&
            programPath.endsWith("\\") != true &&
            programPath.endsWith(File.separator) != true)
        {
            programPath += File.separator;
        }

        if (to.startsWith("/") == true)
        {
            to = to.substring(0, new String("/").length());
        }
        else if (to.startsWith("\\") == true)
        {
            to = to.substring(0, new String("\\").length());
        }
        else if (to.startsWith(File.separator) == true)
        {
            to = to.substring(0, new String(File.separator).length());
        }

        if (to.endsWith("/") != true &&
            to.endsWith("\\") != true &&
            to.endsWith(File.separator) != true)
        {
            to += File.separator;
        }

        to += "org.editeur" + File.separator + "onix_2_1_3_short" + File.separator;

        new File(to).mkdirs();

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.editeur" + File.separator + "onix_2_1_3_short" + File.separator + "onix-international.dtd"), 
                            new File(programPath + to + "onix-international.dtd")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.editeur" + File.separator + "onix_2_1_3_short" + File.separator + "short.elt"), 
                            new File(programPath + to + "short.elt")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.editeur" + File.separator + "onix_2_1_3_short" + File.separator + "onix-xhtml.elt"), 
                            new File(programPath + to + "onix-xhtml.elt")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.editeur" + File.separator + "EDItEUR_IPR_licence_01-06-15.pdf"), 
                            new File(programPath + to + "EDItEUR_IPR_licence_01-06-15.pdf")) != 0)
        {
            System.exit(-1);
        }

        return 0;
    }

    /**
     * @brief For general XHTML, not for a specific version.
     */
    public int CopyEntitiesXHTML(String programPath, String to)
    {
        if (programPath.endsWith("/") != true &&
            programPath.endsWith("\\") != true &&
            programPath.endsWith(File.separator) != true)
        {
            programPath += File.separator;
        }

        if (to.startsWith("/") == true)
        {
            to = to.substring(0, new String("/").length());
        }
        else if (to.startsWith("\\") == true)
        {
            to = to.substring(0, new String("\\").length());
        }
        else if (to.startsWith(File.separator) == true)
        {
            to = to.substring(0, new String(File.separator).length());
        }

        if (to.endsWith("/") != true &&
            to.endsWith("\\") != true &&
            to.endsWith(File.separator) != true)
        {
            to += File.separator;
        }

        to += "org.w3c" + File.separator + "xhtml" + File.separator;

        new File(to).mkdirs();

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.w3c" + File.separator + "xhtml" + File.separator + "xhtml-symbol.ent"), 
                            new File(programPath + to + "xhtml-symbol.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.w3c" + File.separator + "xhtml" + File.separator + "xhtml-special.ent"), 
                            new File(programPath + to + "xhtml-special.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.w3c" + File.separator + "xhtml" + File.separator + "xhtml-lat1.ent"), 
                            new File(programPath + to + "xhtml-lat1.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.w3c" + File.separator + "LICENSE"), 
                            new File(programPath + to + "LICENSE")) != 0)
        {
            System.exit(-1);
        }

        return 0;
    }

    public int CopyEntitiesUnicode(String programPath, String to)
    {
        if (programPath.endsWith("/") != true &&
            programPath.endsWith("\\") != true &&
            programPath.endsWith(File.separator) != true)
        {
            programPath += File.separator;
        }

        if (to.startsWith("/") == true)
        {
            to = to.substring(0, new String("/").length());
        }
        else if (to.startsWith("\\") == true)
        {
            to = to.substring(0, new String("\\").length());
        }
        else if (to.startsWith(File.separator) == true)
        {
            to = to.substring(0, new String(File.separator).length());
        }

        if (to.endsWith("/") != true &&
            to.endsWith("\\") != true &&
            to.endsWith(File.separator) != true)
        {
            to += File.separator;
        }

        to += "org.iso" + File.separator + "unicode" + File.separator;

        new File(to).mkdirs();

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-amsa.ent"), 
                            new File(programPath + to + "iso-amsa.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-amsb.ent"), 
                            new File(programPath + to + "iso-amsb.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-amsc.ent"), 
                            new File(programPath + to + "iso-amsc.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-amsn.ent"), 
                            new File(programPath + to + "iso-amsn.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-amso.ent"), 
                            new File(programPath + to + "iso-amso.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-amsr.ent"), 
                            new File(programPath + to + "iso-amsr.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-box.ent"), 
                            new File(programPath + to + "iso-box.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-cyr1.ent"), 
                            new File(programPath + to + "iso-cyr1.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-cyr2.ent"), 
                            new File(programPath + to + "iso-cyr2.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-dia.ent"), 
                            new File(programPath + to + "iso-dia.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-grk3.ent"), 
                            new File(programPath + to + "iso-grk3.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-lat1.ent"), 
                            new File(programPath + to + "iso-lat1.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-lat2.ent"), 
                            new File(programPath + to + "iso-lat2.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-mfrk.ent"), 
                            new File(programPath + to + "iso-mfrk.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-mopf.ent"), 
                            new File(programPath + to + "iso-mopf.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-mscr.ent"), 
                            new File(programPath + to + "iso-mscr.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-num.ent"), 
                            new File(programPath + to + "iso-num.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-pub.ent"), 
                            new File(programPath + to + "iso-pub.ent")) != 0)
        {
            System.exit(-1);
        }

        if (CopyFileBinary(new File(programPath + ".." + File.separator + ".." + File.separator + ".." + File.separator + "resources" + File.separator + "org.iso" + File.separator + "unicode" + File.separator + "iso-tech.ent"), 
                            new File(programPath + to + "iso-tech.ent")) != 0)
        {
            System.exit(-1);
        }

        return 0;
    }


    public int CopyFileBinary(File from, File to)
    {
        if (from.exists() != true)
        {
            System.out.println("setup_1: " + getI10nStringFormatted("messageCantCopyBecauseFromDoesntExist", from.getAbsolutePath(), to.getAbsolutePath()));
            return -1;
        }

        if (from.isFile() != true)
        {
            System.out.println("setup_1: " + getI10nStringFormatted("messageCantCopyBecauseFromIsntAFile", from.getAbsolutePath(), to.getAbsolutePath()));
            return -2;
        }

        if (from.canRead() != true)
        {
            System.out.println("setup_1: " + getI10nStringFormatted("messageCantCopyBecauseFromIsntReadable", from.getAbsolutePath(), to.getAbsolutePath()));
            return -3;
        }

        if (to.exists() == true)
        {
            if (to.isDirectory() == true)
            {
                System.out.println("setup_1: " + getI10nStringFormatted("messageCantCopyBecauseToIsDirectory", from.getAbsolutePath(), to.getAbsolutePath()));
                return -4;
            }
            else
            {
                if (to.canWrite() != true)
                {
                    System.out.println("setup_1: " + getI10nStringFormatted("messageCantCopyBecauseToIsntWritable", from.getAbsolutePath(), to.getAbsolutePath()));
                    return -5;
                }
            }
        }


        boolean exception = false;

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
            exception = true;
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            exception = true;
            ex.printStackTrace();
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
                        exception = true;
                        ex.printStackTrace();
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
                        exception = true;
                        ex.printStackTrace();
                    }
                }
            }
        }

        if (exception != false)
        {
            System.exit(-1);
        }

        return 0;
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

    private static final String L10N_BUNDLE = "l10n.l10nSetup1Console";
    private ResourceBundle l10nConsole;
}
