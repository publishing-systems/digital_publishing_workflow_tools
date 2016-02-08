/* Copyright (C) 2016 Stephan Kreutzer
 *
 * This file is part of onix_to_woocommerce_2 workflow, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * onix_to_woocommerce_2 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * onix_to_woocommerce_2 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with onix_to_woocommerce_2 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/workflows/onix_to_woocommerce/onix_to_woocommerce_2/ONIXFileInfo.java
 * @brief Various information on an ONIX input file.
 * @author Stephan Kreutzer
 * @since 2016-02-07
 */



import java.io.File;



class ONIXFileInfo
{
    public ONIXFileInfo(File onixFile,
                        String isbn)
    {
        this.onixFile = onixFile;
        this.isbn = isbn;
    }

    public File getFile()
    {
        return this.onixFile;
    }

    public String getISBN()
    {
        return this.isbn;
    }

    protected File onixFile;
    protected String isbn;
}
