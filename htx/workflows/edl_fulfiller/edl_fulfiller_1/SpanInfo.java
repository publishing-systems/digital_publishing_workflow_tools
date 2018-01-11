/* Copyright (C) 2016-2018 Stephan Kreutzer
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
 * @file $/htx/workflows/edl_fulfiller/edl_fulfiller_1/SpanInfo.java
 * @brief Keeps information about spans as specified by the input file.
 * @author Stephan Kreutzer
 * @since 2018-01-08
 */



import java.io.File;



class SpanInfo
{
    public SpanInfo(String identifier,
                    File resource,
                    long start,
                    long length)
    {
        this.identifier = identifier;
        this.resource = resource;
        this.start = start;
        this.length = length;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    public File getResource()
    {
        return this.resource;
    }

    public int setResource(File resource)
    {
        this.resource = resource;
        return 0;
    }

    public long getStart()
    {
        return this.start;
    }

    public long getLength()
    {
        return this.length;
    }

    protected String identifier;
    protected File resource;
    protected long start;
    protected long length;
}
