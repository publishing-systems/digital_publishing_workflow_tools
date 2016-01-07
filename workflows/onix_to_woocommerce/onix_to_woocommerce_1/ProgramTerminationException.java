/* Copyright (C) 2016 Stephan Kreutzer
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
 * @file $/workflows/onix_to_woocommerce/onix_to_woocommerce_1/ProgramTerminationException.java
 * @brief Handles error reporting and program termination for onix_to_woocommerce_1 workflow.
 * @author Stephan Kreutzer
 * @since 2016-01-02
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
        this.normalTermination = false;
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

    public boolean isNormalTermination()
    {
        return this.normalTermination;
    }

    public void setNormalTermination(boolean normalTermination)
    {
        this.normalTermination = normalTermination;
    }

    protected String id;
    protected String bundle;
    protected Object[] arguments;
    protected boolean normalTermination;
}