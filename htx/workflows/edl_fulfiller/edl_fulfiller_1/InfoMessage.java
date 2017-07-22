/* Copyright (C) 2016-2017 Stephan Kreutzer
 *
 * This file is part of edl_fulfiller_1 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * edl_fulfiller_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * edl_fulfiller_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with edl_fulfiller_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/htx/workflows/edl_fulfiller/edl_fulfiller_1/InfoMessage.java
 * @brief For normal messages during edl_fulfiller_1 workflow execution.
 * @author Stephan Kreutzer
 * @since 2016-01-31
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
        this.resourceIndex = -1;

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

    public void SetResourceIndex(int resourceIndex)
    {
        this.resourceIndex = resourceIndex;
    }

    public int GetResourceIndex()
    {
        return this.resourceIndex;
    }

    protected String id;
    protected Exception exception;
    protected String message;
    protected String bundle;
    protected Object[] arguments;
    protected String timestamp;
    protected int resourceIndex;
}
