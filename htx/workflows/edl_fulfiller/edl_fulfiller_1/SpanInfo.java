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
 * @file $/htx/workflows/edl_fulfiller/edl_fulfiller_1/SpanInfo.java
 * @brief Keeps information about a span entry of an EDL.
 * @author Stephan Kreutzer
 * @since 2017-07-15
 */



class SpanInfo
{
    public SpanInfo(String identifier,
                    int start,
                    int length)
    {
        this.identifier = identifier;
        this.start = start;
        this.length = length;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    public int getStart()
    {
        return this.start;
    }

    public int getLength()
    {
        return this.length;
    }

    protected String identifier;
    protected int start;
    protected int length;
}
