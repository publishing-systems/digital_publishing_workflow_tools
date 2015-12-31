# Copyright (C) 2015  Stephan Kreutzer
#
# This file is part of https_client_1, a submodule of the
# digital_publishing_workflow_tools package.
#
# https_client_1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# https_client_1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with https_client_1. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all https_client_1 clean



all: https_client_1
https_client_1: https_client_1.class



ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

Base64Coder.class: Base64Coder.java
	javac -encoding UTF-8 Base64Coder.java

https_client_1.class: https_client_1.java Base64Coder.class ProgramTerminationException.class
	javac -encoding UTF-8 https_client_1.java

clean:
	rm -f https_client_1.class
	rm -f Base64Coder.class
	rm -f ProgramTerminationException.class
