/* Copyright (C) 2018  Stephan Kreutzer
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
 * @file $/json_to_xml/json_to_xml_2/JsonNode.java
 * @brief Carries information about a JSON token.
 * @author Stephan Kreutzer
 * @since 2018-04-13
 */


 
public class JsonNode
{
    public JsonNode(String token, boolean isWhitespace)
    {
        this.token = token;
        this.isWhitespace = isWhitespace;
    }

    public String getToken()
    {
        return this.token;
    }

    public boolean isWhitespace()
    {
        return this.isWhitespace;
    }

    protected String token;
    protected boolean isWhitespace;
}
