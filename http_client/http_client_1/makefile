# Copyright (C) 2015-2016  Stephan Kreutzer
#
# This file is part of digital_publishing_workflow_tools.
#
# digital_publishing_workflow_tools is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# digital_publishing_workflow_tools is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with digital_publishing_workflow_tools. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all http_client_1 clean



all: http_client_1
http_client_1: http_client_1.class



ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ResponseHeaderField.class: ResponseHeaderField.java
	javac -encoding UTF-8 ResponseHeaderField.java

http_client_1.class: http_client_1.java InfoMessage.class ResponseHeaderField.class ProgramTerminationException.class
	javac -encoding UTF-8 http_client_1.java

clean:
	rm -f http_client_1.class
	rm -f ResponseHeaderField.class
	rm -f InfoMessage.class
	rm -f ProgramTerminationException.class

