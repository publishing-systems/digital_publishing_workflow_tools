# Copyright (C) 2016  Stephan Kreutzer
#
# This file is part of setup_1, a submodule of the
# digital_publishing_workflow_tools package.
#
# setup_1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# setup_1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with setup_1. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all setup_1 clean



all: setup_1
setup_1: setup_1.class



setup_1.class: setup_1.java
	javac -encoding UTF-8 setup_1.java

clean:
	rm -f setup_1.class
