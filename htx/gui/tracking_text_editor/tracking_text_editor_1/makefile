# Copyright (C) 2014-2017  Stephan Kreutzer
#
# This file is part of tracking_text_editor_1, a submodule of the
# digital_publishing_workflow_tools package.
#
# tracking_text_editor_1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# tracking_text_editor_1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with tracking_text_editor_1. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all tracking_text_editor_1 clean



all: tracking_text_editor_1
tracking_text_editor_1: tracking_text_editor_1.class



tracking_text_editor_1.class: tracking_text_editor_1.java InfoMessage.class ProgramTerminationException.class FlushingWindowAdapter.class PositionIndicatorCaret.class
	javac -encoding UTF-8 tracking_text_editor_1.java

InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

FlushingWindowAdapter.class: FlushingWindowAdapter.java
	javac -encoding UTF-8 FlushingWindowAdapter.java

PositionIndicatorCaret.class: PositionIndicatorCaret.java
	javac -encoding UTF-8 PositionIndicatorCaret.java

clean:
	rm -f ./*.class
