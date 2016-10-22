/* Copyright (C) 2016  Stephan Kreutzer
 *
 * This file is part of wordpress_media_library_file_uploader_1 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * wordpress_media_library_file_uploader_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * wordpress_media_library_file_uploader_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with wordpress_media_library_file_uploader_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/wordpress_media_library_file_uploader/wordpress_media_library_file_uploader_1/InputFileInfo.java
 * @brief Information about an input file.
 * @author Stephan Kreutzer
 * @since 2016-03-07
 */



import java.io.File;



class InputFileInfo
{
    public InputFileInfo(File inputFile,
                         String name,
                         String type,
                         boolean overwrite)
    {
        this.inputFile = inputFile;
        this.name = name;
        this.type = type;
        this.overwrite = overwrite;
    }

    public File getInputFile()
    {
        return this.inputFile;
    }

    public String getName()
    {
        return this.name;
    }

    public String getType()
    {
        return this.type;
    }

    public boolean getOverwrite()
    {
        return this.overwrite;
    }

    protected File inputFile;
    protected String name;
    protected String type;
    protected boolean overwrite;
}
