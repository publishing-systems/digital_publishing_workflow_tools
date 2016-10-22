/* Copyright (C) 2016  Stephan Kreutzer
 *
 * This file is part of onix_to_woocommerce_1 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * onix_to_woocommerce_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * onix_to_woocommerce_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with onix_to_woocommerce_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/onix_to_woocommerce/onix_to_woocommerce_1/UploadFileMapping.java
 * @brief Upload source file mapped to destination name.
 * @author Stephan Kreutzer
 * @since 2016-03-11
 */



import java.io.File;



class UploadFileMapping
{
    public UploadFileMapping(File sourceFile,
                             String destinationName)
    {
        this.sourceFile = sourceFile;
        this.destinationName = destinationName;
    }

    public File getSourceFile()
    {
        return this.sourceFile;
    }

    public String getDestinationName()
    {
        return this.destinationName;
    }

    protected File sourceFile;
    protected String destinationName;
}
