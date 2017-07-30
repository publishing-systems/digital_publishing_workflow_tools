/* Copyright (C) 2017 Stephan Kreutzer
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
 * @file $/workflows/wordpress_media_library_file_uploader/wordpress_media_library_file_uploader_1/ResultMediaLink.java
 * @brief Keeps the link information about a successful upload.
 * @author Stephan Kreutzer
 * @since 2017-07-28
 */




class ResultMediaLink
{
    public ResultMediaLink(String inputFile,
                           String inputName,
                           String resultLink)
    {
        this.inputFile = inputFile;
        this.inputName = inputName;
        this.resultLink = resultLink;
    }

    public String GetInputFile()
    {
        return this.inputFile;
    }

    public String GetInputName()
    {
        return this.inputName;
    }

    public String GetResultLink()
    {
        return this.resultLink;
    }

    protected String inputFile;
    protected String inputName;
    protected String resultLink;
}
