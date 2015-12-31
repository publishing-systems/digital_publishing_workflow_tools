/* Copyright (C) 2015 Stephan Kreutzer
 *
 * This file is part of https_client_1, a submodule of the
 * digital_publishing_workflow_tools package.
 *
 * https_client_1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * https_client_1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with https_client_1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/https_client/https_client_1/ProgramTerminationException.java
 * @brief Handles error reporting and program termination for https_client_1.
 * @author Stephan Kreutzer
 * @since 2015-11-14
 */



class ProgramTerminationException extends RuntimeException
{
    public ProgramTerminationException(String id,
                                       Exception cause,
                                       String message,
                                       String bundle,
                                       Object ... arguments)
    {
        super(message, cause);

        this.id = id;
        this.bundle = bundle;
        this.arguments = arguments;
    }

    public String getId()
    {
        return this.id;
    }

    public String getBundle()
    {
        return this.bundle;
    }

    public Object[] getArguments()
    {
        return this.arguments;
    }

    protected String id;
    protected String bundle;
    protected Object[] arguments;
}
