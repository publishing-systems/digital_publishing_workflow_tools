# Copyright (C) 2016 Stephan Kreutzer
#
# This file is part of onix_to_woocommerce_2 workflow, a submodule of the
# digital_publishing_workflow_tools package.
#
# onix_to_woocommerce_2 workflow is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# onix_to_woocommerce_2 workflow is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with onix_to_woocommerce_2 workflow. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all onix_to_woocommerce_2 clean



all: onix_to_woocommerce_2
onix_to_woocommerce_2: onix_to_woocommerce_2.class



InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

ONIXFileInfo.class: ONIXFileInfo.java
	javac -encoding UTF-8 ONIXFileInfo.java

Base64Coder.class: Base64Coder.java
	javac -encoding UTF-8 Base64Coder.java

onix_to_woocommerce_2.class: onix_to_woocommerce_2.java Base64Coder.class ONIXFileInfo.class InfoMessage.class ProgramTerminationException.class
	javac -encoding UTF-8 onix_to_woocommerce_2.java

clean:
	rm -f onix_to_woocommerce_2.class
	rm -f Base64Coder.class
	rm -f ONIXFileInfo.class
	rm -f ProgramTerminationException.class
	rm -f InfoMessage.class

