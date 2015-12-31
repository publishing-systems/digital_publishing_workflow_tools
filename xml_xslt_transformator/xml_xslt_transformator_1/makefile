# Copyright (C) 2014-2015  Stephan Kreutzer
#
# This file is part of xml_xslt_transformator_1, a submodule of the
# digital_publishing_workflow_tools package.
#
# xml_xslt_transformator_1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# xml_xslt_transformator_1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with xml_xslt_transformator_1. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all xml_xslt_transformator_1 clean



all: xml_xslt_transformator_1
xml_xslt_transformator_1: xml_xslt_transformator_1.class



JobDefinition.class: JobDefinition.java
	javac -encoding UTF-8 JobDefinition.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

TransformerErrorListener.class: TransformerErrorListener.java
	javac -encoding UTF-8 TransformerErrorListener.java

EntityResolverLocal.class: EntityResolverLocal.java
	javac -encoding UTF-8 EntityResolverLocal.java

EntityResolverLocal2.class: EntityResolverLocal2.java
	javac -encoding UTF-8 EntityResolverLocal2.java

xml_xslt_transformator_1.class: xml_xslt_transformator_1.java JobDefinition.class ProgramTerminationException.class TransformerErrorListener.class EntityResolverLocal.class EntityResolverLocal2.class
	javac -encoding UTF-8 xml_xslt_transformator_1.java

clean:
	rm -f xml_xslt_transformator_1.class
	rm -f EntityResolverLocal2.class
	rm -f EntityResolverLocal.class
	rm -f TransformerErrorListener.class
	rm -f ProgramTerminationException.class
	rm -f JobDefinition.class

