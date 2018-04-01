# Copyright (C) 2018  Stephan Kreutzer
#
# This file is part of wordpress_client_1 workflow, a submodule of the
# digital_publishing_workflow_tools package.
#
# wordpress_client_1 workflow is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# wordpress_client_1 workflow is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with wordpress_client_1 workflow. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all wordpress_client_1 clean



all: wordpress_client_1
wordpress_client_1: wordpress_client_1.class



InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

wordpress_client_1.class: wordpress_client_1.java InfoMessage.class ProgramTerminationException.class
	javac -encoding UTF-8 wordpress_client_1.java


clean:
	rm -f wordpress_client_1.class
	rm -f ProgramTerminationException.class
	rm -f InfoMessage.class
