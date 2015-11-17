/* Copyright (C) 2015 Stephan Kreutzer
 *
 * This file is part of xsltransformator1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * xsltransformator1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * xsltransformator1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with xsltransformator1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/xsltransformator/xsltransformator1/JobDefinition.java
 * @brief Manages all files involved in a xsltransformator1 job.
 * @author Stephan Kreutzer
 * @since 2015-11-14
 */



import java.io.File;



class JobDefinition
{
    public JobDefinition(File inFile,
                         File entitiesResolverConfigFile,
                         File stylesheetFile,
                         File outFile)
    {
        this.inFile = inFile;
        this.entitiesResolverConfigFile = entitiesResolverConfigFile;
        this.stylesheetFile = stylesheetFile;
        this.outFile = outFile;
    }

    public File GetInFile()
    {
        return this.inFile;
    }

    public File GetEntitiesResolverConfigFile()
    {
        return this.entitiesResolverConfigFile;
    }

    public File GetStylesheetFile()
    {
        return this.stylesheetFile;
    }

    public File GetOutFile()
    {
        return this.outFile;
    }

    protected File inFile;
    protected File entitiesResolverConfigFile;
    protected File stylesheetFile;
    protected File outFile;
}

