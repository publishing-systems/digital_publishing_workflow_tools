# Copyright (C) 2017  Stephan Kreutzer
#
# This file is part of change_instructions_executor_1, a submodule of the
# digital_publishing_workflow_tools package.
#
# change_instructions_executor_1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# change_instructions_executor_1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with change_instructions_executor_1. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all change_instructions_executor_1 clean



all: change_instructions_executor_1
change_instructions_executor_1: change_instructions_executor_1.class



InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

change_instructions_executor_1.class: change_instructions_executor_1.java InfoMessage.class ProgramTerminationException.class
	javac -encoding UTF-8 change_instructions_executor_1.java


clean:
	rm -f change_instructions_executor_1.class
	rm -f ProgramTerminationException.class
	rm -f InfoMessage.class
