# Copyright (C) 2016 Stephan Kreutzer
#
# This file is part of onix_to_woocommerce_1 workflow, a submodule of the
# digital_publishing_workflow_tools package.
#
# onix_to_woocommerce_1 workflow is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# onix_to_woocommerce_1 workflow is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with onix_to_woocommerce_1 workflow. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all onix_to_woocommerce_1 clean



all: onix_to_woocommerce_1
onix_to_woocommerce_1: onix_to_woocommerce_1.class



InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

Base64Coder.class: Base64Coder.java
	javac -encoding UTF-8 Base64Coder.java

UploadFileMapping.class: UploadFileMapping.java
	javac -encoding UTF-8 UploadFileMapping.java

onix_to_woocommerce_1.class: onix_to_woocommerce_1.java InfoMessage.class ProgramTerminationException.class Base64Coder.class UploadFileMapping.java
	javac -encoding UTF-8 onix_to_woocommerce_1.java

clean:
	rm -f onix_to_woocommerce_1.class
	rm -f Base64Coder.class
	rm -f UploadFileMapping.class
	rm -f ProgramTerminationException.class
	rm -f InfoMessage.class

