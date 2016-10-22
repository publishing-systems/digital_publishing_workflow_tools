# Copyright (C) 2016  Stephan Kreutzer
#
# This file is part of wordpress_media_library_file_uploader_1 workflow, a submodule of the
# digital_publishing_workflow_tools package.
#
# wordpress_media_library_file_uploader_1 workflow is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# wordpress_media_library_file_uploader_1 workflow is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with wordpress_media_library_file_uploader_1 workflow. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all wordpress_media_library_file_uploader_1 clean



all: wordpress_media_library_file_uploader_1
wordpress_media_library_file_uploader_1: wordpress_media_library_file_uploader_1.class



InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

InputFileInfo.class: InputFileInfo.java
	javac -encoding UTF-8 InputFileInfo.java

JobFileProcessor.class: JobFileProcessor.java
	javac -encoding UTF-8 JobFileProcessor.java

Base64Coder.class: Base64Coder.java
	javac -encoding UTF-8 Base64Coder.java

wordpress_media_library_file_uploader_1.class: wordpress_media_library_file_uploader_1.java JobFileProcessor.class Base64Coder.class InputFileInfo.class InfoMessage.class ProgramTerminationException.class
	javac -encoding UTF-8 wordpress_media_library_file_uploader_1.java

clean:
	rm -f wordpress_media_library_file_uploader_1.class
	rm -f Base64Coder.class
	rm -f JobFileProcessor.class
	rm -f InputFileInfo.class
	rm -f ProgramTerminationException.class
	rm -f InfoMessage.class

