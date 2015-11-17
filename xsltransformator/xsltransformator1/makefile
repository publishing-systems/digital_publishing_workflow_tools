# Copyright (C) 2014-2015  Stephan Kreutzer
#
# This file is part of xsltransformator1, a submodule of the
# digital_publishing_workflow_tools package.
#
# xsltransformator1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# xsltransformator1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with xsltransformator1. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all xsltransformator1 clean



all: xsltransformator1
xsltransformator1: xsltransformator1.class



JobDefinition.class: JobDefinition.java
	javac JobDefinition.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac ProgramTerminationException.java

TransformerErrorListener.class: TransformerErrorListener.java
	javac TransformerErrorListener.java

EntityResolverLocal.class: EntityResolverLocal.java
	javac EntityResolverLocal.java

EntityResolverLocal2.class: EntityResolverLocal2.java
	javac EntityResolverLocal2.java

xsltransformator1.class: xsltransformator1.java JobDefinition.class ProgramTerminationException.class TransformerErrorListener.class EntityResolverLocal.class EntityResolverLocal2.class
	javac xsltransformator1.java

clean:
	rm -f xsltransformator1.class
	rm -f EntityResolverLocal2.class
	rm -f EntityResolverLocal.class
	rm -f TransformerErrorListener.class
	rm -f ProgramTerminationException.class
	rm -f JobDefinition.class

