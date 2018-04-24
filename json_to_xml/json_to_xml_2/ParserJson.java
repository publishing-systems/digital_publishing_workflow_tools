/* Copyright (C) 2016-2018  Stephan Kreutzer
 *
 * This file is part of json_to_xml_2, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * json_to_xml_2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * json_to_xml_2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with json_to_xml_2. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/json_to_xml/json_to_xml_2/ParserJson.java
 * @brief Parses the tokens of a JSON file and converts them into XML.
 * @author Stephan Kreutzer
 * @since 2018-04-10
 */



import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;
import java.io.UnsupportedEncodingException;
import java.io.BufferedWriter;
import java.io.IOException;



public class ParserJson
{
    public ParserJson(List<JsonNode> nodes, List<InfoMessage> infoMessages)
    {
        this.nodes = nodes;
        this.infoMessages = infoMessages;
        this.nodeCursor = 0;
    }

    public int parse(BufferedWriter writer) throws IOException
    {
        this.writer = writer;
        this.nodeCursor = 0;

        consumeWhitespace();

        JsonNode node = nextNode();

        if (node.getToken().equals("{") == true)
        {
            this.writer.write("<object>");
            HandleObjectContent();
            this.writer.write("</object>");
        }
        else if (node.getToken().equals("[") == true)
        {
            this.writer.write("<array>");
            HandleArrayContent();
            this.writer.write("</array>");
        }
        else
        {
            throw constructTermination("messageParserUnknownStartToken", null, null, node.getToken());
        }

        return 0;
    }

    protected int HandleObjectContent() throws IOException
    {
        do
        {
            consumeWhitespace();
            match("\"");

            String name = HandleString().toString();
            // Ampersand needs to be the first, otherwise it would double-encode
            // other entities.
            name = name.replaceAll("&", "&amp;");
            name = name.replaceAll("\"", "&quot;");
            name = name.replaceAll("'", "&apos;");
            name = name.replaceAll("<", "&lt;");
            name = name.replaceAll(">", "&gt;");

            consumeWhitespace();
            match(":");
            consumeWhitespace();

            JsonNode node = nextNode();

            if (node.getToken().equals("{") == true)
            {
                this.writer.write("<object name=\"" + name.toString() + "\">");
                HandleObjectContent();
                this.writer.write("</object>");
            }
            else if (node.getToken().equals("[") == true)
            {
                this.writer.write("<array name=\"" + name.toString() + "\">");
                HandleArrayContent();
                this.writer.write("</array>");
            }
            else if (node.getToken().equals("\"") == true)
            {
                this.writer.write("<object name=\"" + name.toString() + "\">");

                String string = HandleString().toString();
                // Ampersand needs to be the first, otherwise it would double-encode
                // other entities.
                string = string.replaceAll("&", "&amp;");
                string = string.replaceAll("<", "&lt;");
                string = string.replaceAll(">", "&gt;");

                this.writer.write(string);
                this.writer.write("</object>");
            }
            else
            {
                this.writer.write("<object name=\"" + name.toString() + "\">");
                HandleText(node, JSON_TYPE.OBJECT);
                this.writer.write("</object>");
            }

            consumeWhitespace();

            node = nextNode();

            if (node.getToken().equals(",") == true)
            {
                continue;
            }
            else if (node.getToken().equals("}") == true)
            {
                return 0;
            }
            else
            {
                throw constructTermination("messageParserUnknownTokenInObject", null, null, node.getToken());
            }

        } while (true);
    }

    protected int HandleArrayContent() throws IOException
    {
        consumeWhitespace();

        JsonNode node = lookAhead();

        if (node == null)
        {
            throw constructTermination("messageParserNoMoreTokens", null, null);
        }

        if (node.getToken().equals("]") == true)
        {
            nextNode();
            return 0;
        }

        do
        {
            consumeWhitespace();
            node = nextNode();

            if (node.getToken().equals("{") == true)
            {
                this.writer.write("<object>");
                HandleObjectContent();
                this.writer.write("</object>");
            }
            else if (node.getToken().equals("\"") == true)
            {
                HandleString();
            }
            else
            {
                HandleText(node, JSON_TYPE.ARRAY);
            }

            consumeWhitespace();

            node = nextNode();

            if (node.getToken().equals(",") == true)
            {
                continue;
            }
            else if (node.getToken().equals("]") == true)
            {
                return 0;
            }
            else
            {
                throw constructTermination("messageParserUnknownTokenInArray", null, null, node.getToken());
            }

        } while (true);
    }

    protected StringBuilder HandleString()
    {
        StringBuilder sb = new StringBuilder();

        while (true)
        {
            JsonNode node = nextNode();

            if (node.getToken().equals("\"") == true)
            {
                return sb;
            }
            else if (node.getToken().equals("\\") == true)
            {
                sb.append(HandleEscapeSequence());
            }
            else
            {
                sb.append(node.getToken());
            }
        }
    }

    protected String HandleEscapeSequence()
    {
        JsonNode node = nextNode();

        // The tokenizer made sure that the next node will be
        // only one character.
        if (node.getToken().equals("\"") == true)
        {
            return "\"";
        }
        else if (node.getToken().equals("\\") == true)
        {
            return "\\";
        }
        else if (node.getToken().equals("/") == true)
        {
            return "/";
        }
        else if (node.getToken().equals("b") == true)
        {
            throw constructTermination("messageParserUnsupportedEscapeSequence", null, null, "\\b");
        }
        else if (node.getToken().equals("f") == true)
        {
            throw constructTermination("messageParserUnsupportedEscapeSequence", null, null, "\\f");
        }
        else if (node.getToken().equals("n") == true)
        {
            return "\n";
        }
        else if (node.getToken().equals("r") == true)
        {
            return "\r";
        }
        else if (node.getToken().equals("t") == true)
        {
            return "\t";
        }
        else if (node.getToken().equals("u") == true)
        {
            node = lookAhead();

            if (node.getToken().length() < 4)
            {
                throw constructTermination("messageParserEscapeSequenceIncomplete", null, null, "\\u" + node.getToken());
            }

            String hex = node.getToken().substring(0, 4);

            int codepoint = Integer.parseInt(hex, 16);
            char character = (char)codepoint;

            String restToken = node.getToken().substring(4);

            if (restToken.length() > 0)
            {
                if (this.nodeCursor > 0)
                {
                    this.nodes.set(this.nodeCursor, new JsonNode(restToken, node.isWhitespace()));
                }
                else
                {
                    throw constructTermination("messageParserEscapeSequenceAtStart", null, null, "\\u");
                }
            }
            else
            {
                // Consume the lookAhead().
                nextNode();
            }

            return new String() + (char)character;
        }
        else
        {
            throw constructTermination("messageParserUnsupportedEscapeSequence", null, null, "\\" + node.getToken());
        }
    }

    protected int HandleText(JsonNode node, JSON_TYPE type) throws IOException
    {
        // Already consumed node isn't lookAhead(), but callees must react
        // on end or consecutive elements, so decrement nodeCursor for those
        // nodes to be interpreted by the callee.
        if ((node.getToken().equals("}") == true ||
             node.getToken().equals(",") == true) &&
            type == JSON_TYPE.OBJECT)
        {
            --this.nodeCursor;
            return 0;
        }
        else if ((node.getToken().equals("]") == true ||
                  node.getToken().equals(",") == true) &&
                 type == JSON_TYPE.ARRAY)
        {
            --this.nodeCursor;
            return 0;
        }

        do
        {
            if ((node.getToken().equals("}") == true ||
                 node.getToken().equals(",") == true) &&
                type == JSON_TYPE.OBJECT)
            {
                // Not a problem, node was obtained from lookAhead(), so the
                // callee can react to this nodes.
                return 0;
            }
            else if ((node.getToken().equals("]") == true ||
                      node.getToken().equals(",") == true) &&
                     type == JSON_TYPE.ARRAY)
            {
                // Not a problem, node was obtained from lookAhead(), so the
                // callee can react to this nodes.
                return 0;
            }
            else if (node.getToken().equals("\"") == true)
            {
                throw constructTermination("messageParserDoubleQuoteInTextLiteral", null, null);
            }
            else if (node.getToken().equals("\\") == true)
            {
                String string = HandleEscapeSequence();
                // Escaping to make sure that there's no \\u encoded XML special
                // character in there. Ampersand needs to be the first, otherwise
                // it would double-encode other entities.
                string = string.replaceAll("&", "&amp;");
                string = string.replaceAll("<", "&lt;");
                string = string.replaceAll(">", "&gt;");

                this.writer.write(string);
            }
            else if (node.getToken().equals(":") == true)
            {
                this.writer.write(node.getToken());
            }
            else
            {
                String string = node.getToken();
                // Ampersand needs to be the first, otherwise it would double-encode
                // other entities.
                string = string.replaceAll("&", "&amp;");
                string = string.replaceAll("<", "&lt;");
                string = string.replaceAll(">", "&gt;");

                this.writer.write(string);
            }

            node = lookAhead();

            if (node == null)
            {
                throw constructTermination("messageParserNoMoreTokens", null, null);
            }

        } while (true);
    }

    protected boolean match(String required)
    {
        JsonNode node = nextNode();

        if (node.getToken().equals(required))
        {
            return true;
        }

        // this.infoMessages.add(constructInfoMessage("messageParsingError", true, null, null, required, node.getToken()));
        throw constructTermination("messageParsingError", null, null, required, node.getToken());
    }

    /**
     * @details As whitespace is collected into a single token,
     *     there shouldn't be two consecutive whitespace nodes
     *     anyway.
     */
    protected int consumeWhitespace() throws IOException
    {
        do
        {
            if (this.nodeCursor >= this.nodes.size())
            {
                return -1;
            }

            ++this.nodeCursor;

            JsonNode node = this.nodes.get(this.nodeCursor-1);

            if (node.isWhitespace() == true)
            {
                this.writer.write(node.getToken());
            }
            else
            {
                --this.nodeCursor;
                return 0;
            }

        } while (true);
    }

    protected JsonNode nextNode()
    {
        if (this.nodeCursor >= this.nodes.size())
        {
            throw constructTermination("messageParserNoMoreTokens", null, null);
        }

        ++this.nodeCursor;
        return this.nodes.get(this.nodeCursor-1);
    }

    protected JsonNode lookAhead()
    {
        if (this.nodeCursor >= this.nodes.size())
        {
            return null;
        }

        return this.nodes.get(this.nodeCursor);
    }

    public enum JSON_TYPE
    {
        OBJECT,
        ARRAY
    }

    protected List<JsonNode> nodes = null;
    protected int nodeCursor = 0;
    BufferedWriter writer = null;


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
                message = "json_to_xml_2 (ParserJson): " + getI10nString(id);
            }
            else
            {
                message = "json_to_xml_2 (ParserJson): " + getI10nStringFormatted(id, arguments);
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
                message = "json_to_xml_2 (ParserJson): " + getI10nString(id);
            }
            else
            {
                message = "json_to_xml_2 (ParserJson): " + getI10nStringFormatted(id, arguments);
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
        if (this.l10nParserJson == null)
        {
            this.l10nParserJson = ResourceBundle.getBundle(L10N_BUNDLE, this.getLocale());
        }

        try
        {
            return new String(this.l10nParserJson.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            return this.l10nParserJson.getString(key);
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

    private static final String L10N_BUNDLE = "l10n.l10nJsonToXml2ParserJson";
    private ResourceBundle l10nParserJson;
}
