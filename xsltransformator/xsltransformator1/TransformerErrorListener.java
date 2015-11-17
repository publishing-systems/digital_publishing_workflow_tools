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
 * @file $/xsltransformator/xsltransformator1/TransformerErrorListener.java
 * @author Stephan Kreutzer
 * @since 2015-11-16
 */



import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;



class TransformerErrorListener implements ErrorListener
{
    public TransformerErrorListener()
    {

    }

    public void error(TransformerException exception) throws TransformerException
    {
        throw exception;
    }

    public void fatalError(TransformerException exception) throws TransformerException
    {
        throw exception;
    }

    public void warning(TransformerException exception) throws TransformerException
    {
        throw exception;
    }
}

