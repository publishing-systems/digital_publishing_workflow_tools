# Copyright (C) 2016-2018  Stephan Kreutzer
#
# This file is part of json_to_xml_2, a submodule of the
# digital_publishing_workflow_tools package.
#
# json_to_xml_2 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# json_to_xml_2 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with json_to_xml_2. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all json_to_xml_2 clean



all: json_to_xml_2
json_to_xml_2: json_to_xml_2.class



InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

JsonNode.class: JsonNode.java
	javac -encoding UTF-8 JsonNode.java

ParserJson.class: ParserJson.java
	javac -encoding UTF-8 ParserJson.java

json_to_xml_2.class: json_to_xml_2.java ParserJson.class JsonNode.class InfoMessage.class ProgramTerminationException.class
	javac -encoding UTF-8 json_to_xml_2.java

clean:
	rm -f json_to_xml_2.class
	rm -f ParserJson*.class
	rm -f JsonNode.class
	rm -f ProgramTerminationException.class
	rm -f InfoMessage.class
