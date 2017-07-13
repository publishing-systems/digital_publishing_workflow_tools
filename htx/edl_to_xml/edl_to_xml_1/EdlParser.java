/* Copyright (C) 2016-2017 Stephan Kreutzer
 *
 * This file is part of edl_to_xml_1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * edl_to_xml_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * edl_to_xml_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with edl_to_xml_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/htx/edl_to_xml/edl_to_xml_1/EdlParser.java
 * @brief Parses the tokens of the EDL format and converts them into XML.
 * @author Stephan Kreutzer
 * @since 2017-07-12
 */



import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.UnsupportedEncodingException;



public class EdlParser
{
    public EdlParser(List<String> tokens, List<InfoMessage> infoMessages)
    {
        this.tokens = tokens;
        this.infoMessages = infoMessages;
        this.tokenCursor = 0;
    }

    public StringBuilder parse()
    {
        StringBuilder sbResult = new StringBuilder();

        while (this.tokenCursor < this.tokens.size())
        {
            String token = nextToken();

            if (token.equals("span") == true)
            {
                StringBuilder sbSpan = span();

                if (sbSpan != null)
                {
                    sbResult.append(sbSpan.toString());
                }
                else
                {
                    return null;
                }
            }
            else if (token.equals("link") == true)
            {
                StringBuilder sbLink = link();

                if (sbLink != null)
                {
                    sbResult.append(sbLink.toString());
                }
                else
                {
                    return null;
                }
            }
            else if (token.equals("\n") == true)
            {
                continue;
            }
            else
            {
                throw constructTermination("messageParserUnknownStatement", null, null, token);
            }
        }

        return sbResult;
    }

    public StringBuilder span()
    {
        StringBuilder sbResult = new StringBuilder();

        sbResult.append("<span ");

        if (match(":") != true)
        {
            return null;
        }

        StringBuilder sbIdentifier = identifier();

        if (sbIdentifier != null)
        {
            sbResult.append("identifier=\"");
            sbResult.append(sbIdentifier.toString());
            sbResult.append("\" ");
        }
        else
        {
            return null;
        }

        if (match(",") != true ||
            match("start") != true ||
            match("=") != true)
        {
            return null;
        }

        String startPosition = nextToken();

        try
        {
            if (Integer.parseInt(startPosition) < 0)
            {
                throw constructTermination("messageParserInvalidIntegerGreaterEqualZeroExpected", null, null, startPosition);
            }
        }
        catch (NumberFormatException ex)
        {
            throw constructTermination("messageParserInvalidInteger", ex, null, startPosition);
        }

        sbResult.append("start=\"");
        sbResult.append(startPosition);
        sbResult.append("\" ");

        if (match(",") != true ||
            match("length") != true ||
            match("=") != true)
        {
            return null;
        }

        String length = nextToken();

        try
        {
            if (Integer.parseInt(length) <= 0)
            {
                throw constructTermination("messageParserInvalidIntegerGreaterZeroExpected", null, null, length);
            }
        }
        catch (NumberFormatException ex)
        {
            throw constructTermination("messageParserInvalidInteger", ex, null, length);
        }

        sbResult.append("length=\"");
        sbResult.append(length);
        sbResult.append("\" ");

        if (match("\n") != true)
        {
            return null;
        }

        sbResult.append("/>");

        return sbResult;
    }

    public StringBuilder identifier()
    {
        StringBuilder sbResult = new StringBuilder();

        while (this.tokenCursor < this.tokens.size())
        {
            if (this.tokens.get(this.tokenCursor).equals(",") == true ||
                this.tokens.get(this.tokenCursor).equals("\n") == true)
            {
                break;
            }

            sbResult.append(nextToken());
        }

        return sbResult;
    }

    public StringBuilder link()
    {
        StringBuilder sbResult = new StringBuilder();

        sbResult.append("<link ");

        if (match(":") != true)
        {
            return null;
        }

        StringBuilder sbIdentifier = identifier();

        if (sbIdentifier != null)
        {
            sbResult.append("identifier=\"");
            sbResult.append(sbIdentifier.toString());
            sbResult.append("\" ");
        }
        else
        {
            return null;
        }

        if (match("\n") != true)
        {
            return null;
        }

        sbResult.append("/>");

        return sbResult;
    }

    public boolean match(String required)
    {
        String token = nextToken();

        if (token.equals(required))
        {
            return true;
        }

        /*
        this.infoMessages.add(constructInfoMessage("messageParsingError", true, null, null, required, token));

        return false;
        */

        throw constructTermination("messageParsingError", null, null, required, token);
    }

    public String nextToken()
    {
        if (this.tokenCursor >= this.tokens.size())
        {
            throw constructTermination("messageParserNoMoreTokens", null, null);
        }

        ++this.tokenCursor;
        return this.tokens.get(this.tokenCursor-1);
    }

    protected List<String> tokens;
    protected int tokenCursor;


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
                message = "edl_to_xml_1 (EdlParser): " + getI10nString(id);
            }
            else
            {
                message = "edl_to_xml_1 (EdlParser): " + getI10nStringFormatted(id, arguments);
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
                message = "edl_to_xml_1 (EdlParser): " + getI10nString(id);
            }
            else
            {
                message = "edl_to_xml_1 (EdlParser): " + getI10nStringFormatted(id, arguments);
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
        if (this.l10nEdlParser == null)
        {
            this.l10nEdlParser = ResourceBundle.getBundle(L10N_BUNDLE, this.getLocale());
        }

        try
        {
            return new String(this.l10nEdlParser.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            return this.l10nEdlParser.getString(key);
        }
    }

    private String getI10nStringFormatted(String i10nStringName, Object ... arguments)
    {
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(this.getLocale());

        formatter.applyPattern(getI10nString(i10nStringName));
        return formatter.format(arguments);
    }

    protected List<InfoMessage> infoMessages = null;

    private static final String L10N_BUNDLE = "l10n.l10nEdlParser";
    private ResourceBundle l10nEdlParser;
}
