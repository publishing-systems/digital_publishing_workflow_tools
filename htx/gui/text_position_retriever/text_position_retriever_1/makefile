# Copyright (C) 2014-2017  Stephan Kreutzer
#
# This file is part of text_position_retriever_1, a submodule of the
# digital_publishing_workflow_tools package.
#
# text_position_retriever_1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# text_position_retriever_1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with text_position_retriever_1. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all text_position_retriever_1 clean



all: text_position_retriever_1
text_position_retriever_1: text_position_retriever_1.class



text_position_retriever_1.class: text_position_retriever_1.java InfoMessage.class ProgramTerminationException.class
	javac -encoding UTF-8 text_position_retriever_1.java

InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

clean:
	rm -f ./*.class
