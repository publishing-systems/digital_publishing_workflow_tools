/* Copyright (C) 2016-2018 Stephan Kreutzer
 *
 * This file is part of text_concatenator_1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * text_concatenator_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * text_concatenator_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with text_concatenator_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/text_concatenator/text_concatenator_1/SpanInfo.java
 * @brief References a portion of text within a file.
 * @author Stephan Kreutzer
 * @since 2018-01-14
 */



import java.io.File;



class SpanInfo
{
    public SpanInfo(File file,
                    long start,
                    int length)
    {
        this.file = file;
        this.start = start;
        this.length = length;
        this.complete = false;
    }

    public SpanInfo(File file)
    {
        this.file = file;
        this.start = 0;
        this.length = Integer.MAX_VALUE;
        this.complete = true;
    }

    public File getFile()
    {
        return this.file;
    }

    public long getStart()
    {
        if (this.complete == true)
        {
            throw new UnsupportedOperationException();
        }

        return this.start;
    }

    public int getLength()
    {
        if (this.complete == true)
        {
            throw new UnsupportedOperationException();
        }

        return this.length;
    }

    public boolean getComplete()
    {
        return complete;
    }

    protected File file;
    protected long start;
    protected int length;
    protected boolean complete;
}
