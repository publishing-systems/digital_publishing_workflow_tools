/* Copyright (C) 2016 Stephan Kreutzer
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
 * @file $/https_client/https_client_1/ResponseHeaderField.java
 * @brief Represents a HTTP response header field.
 * @author Stephan Kreutzer
 * @since 2016-02-05
 */



import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



class ResponseHeaderField
{
    public ResponseHeaderField(int number,
                               String name,
                               String value)
    {
        this.number = number;
        this.name = name;
        this.value = value;
    }

    public int getNumber()
    {
        return this.number;
    }

    public String getName()
    {
        return this.name;
    }

    public String getValue()
    {
        return this.value;
    }

    protected int number;
    protected String name;
    protected String value;
}
