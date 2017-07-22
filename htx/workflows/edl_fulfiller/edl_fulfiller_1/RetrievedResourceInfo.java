/* Copyright (C) 2017 Stephan Kreutzer
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
 * @file $/htx/workflows/edl_fulfiller/edl_fulfiller_1/RetrievedResourceInfo.java
 * @brief Information regarding the success or failure of retrieving a resource.
 * @author Stephan Kreutzer
 * @since 2017-07-20
 */



import java.io.File;



class RetrievedResourceInfo
{
    public RetrievedResourceInfo(String identifier,
                                 int index,
                                 boolean success,
                                 File resourceFile)
    {
        this.identifier = identifier;
        this.index = index;
        this.success = success;
        this.resourceFile = resourceFile;
    }

    public String GetIdentifier()
    {
        return this.identifier;
    }

    public int GetIndex()
    {
        return this.index;
    }

    public boolean GetSuccess()
    {
        return this.success;
    }

    public File GetResourceFile()
    {
        return this.resourceFile;
    }

    protected String identifier;
    protected int index;
    protected boolean success;
    protected File resourceFile;
}
