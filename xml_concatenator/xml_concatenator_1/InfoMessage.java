/* Copyright (C) 2016-2018 Stephan Kreutzer
 *
 * This file is part of xml_concatenator_1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * xml_concatenator_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * xml_concatenator_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with xml_concatenator_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/xml_concatenator/xml_concatenator_1/InfoMessage.java
 * @brief For normal messages during xml_concatenator_1 concatenation.
 * @author Stephan Kreutzer
 * @since 2016-01-04
 */



import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



class InfoMessage
{
    public InfoMessage(String id,
                       Exception exception,
                       String message,
                       String bundle,
                       Object ... arguments)
    {
        this.id = id;
        this.exception = exception;
        this.message = message;
        this.bundle = bundle;
        this.arguments = arguments;

        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(timeZone);
        this.timestamp = dateFormat.format(new Date());
    }

    public String getId()
    {
        return this.id;
    }

    public Exception getException()
    {
        return this.exception;
    }

    public String getMessage()
    {
        return this.message;
    }

    public String getBundle()
    {
        return this.bundle;
    }

    public Object[] getArguments()
    {
        return this.arguments;
    }

    public String getTimestamp()
    {
        return this.timestamp;
    }

    protected String id;
    protected Exception exception;
    protected String message;
    protected String bundle;
    protected Object[] arguments;
    protected String timestamp;
}
