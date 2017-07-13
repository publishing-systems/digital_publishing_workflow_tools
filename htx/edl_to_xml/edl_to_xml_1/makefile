# Copyright (C) 2017  Stephan Kreutzer
#
# This file is part of edl_to_xml_1, a submodule of the
# digital_publishing_workflow_tools package.
#
# edl_to_xml_1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# edl_to_xml_1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with edl_to_xml_1. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all edl_to_xml_1 clean



all: edl_to_xml_1
edl_to_xml_1: edl_to_xml_1.class



InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

EdlParser.class: EdlParser.java
	javac -encoding UTF-8 EdlParser.java

edl_to_xml_1.class: edl_to_xml_1.java EdlParser.class InfoMessage.class ProgramTerminationException.class
	javac -encoding UTF-8 edl_to_xml_1.java


clean:
	rm -f edl_to_xml_1.class
	rm -f EdlParser.class
	rm -f ProgramTerminationException.class
	rm -f InfoMessage.class
