#!/bin/sh
# Copyright (C) 2018 Stephan Kreutzer
#
# This file is part of wordpress_retriever_1 workflow, a submodule of the
# digital_publishing_workflow_tools package.
#
# wordpress_retriever_1 workflow is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# wordpress_retriever_1 workflow is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with wordpress_retriever_1 workflow. If not, see <http://www.gnu.org/licenses/>.

mkdir ./temp

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<!-- This file was created by posts_to_all_1.sh of wordpress_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see http://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->
<xml-xslt-transformator-1-jobfile>
  <job input-file=\"../output.xml\" entities-resolver-config-file=\"../../../../xml_xslt_transformator/xml_xslt_transformator_1/entities/config_empty.xml\" stylesheet-file=\"../posts_to_xhtml_1_1.xsl\" output-file=\"../output.html\"/>
</xml-xslt-transformator-1-jobfile>" > "./temp/jobfile_xml_xslt_transformator_1_posts_to_all_1_html.xml"

java -cp ../../../xml_xslt_transformator/xml_xslt_transformator_1 xml_xslt_transformator_1 ./temp/jobfile_xml_xslt_transformator_1_posts_to_all_1_html.xml ./temp/resultinfo_xml_xslt_transformator_1_posts_to_all.xml_xslt_transformator

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<!-- This file was created by posts_to_all_1.sh of wordpress_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see http://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->
<xml-xslt-transformator-1-jobfile>
  <job input-file=\"../output.xml\" entities-resolver-config-file=\"../../../../xml_xslt_transformator/xml_xslt_transformator_1/entities/config_empty.xml\" stylesheet-file=\"../posts_to_odt2html_template1_epub.xsl\" output-file=\"./output.html\"/>
</xml-xslt-transformator-1-jobfile>" > "./temp/jobfile_xml_xslt_transformator_1_posts_to_all_1_epub.xml"

java -cp ../../../xml_xslt_transformator/xml_xslt_transformator_1 xml_xslt_transformator_1 ./temp/jobfile_xml_xslt_transformator_1_posts_to_all_1_epub.xml ./temp/resultinfo_xml_xslt_transformator_1_posts_to_all.xml_xslt_transformator
java -cp ../../../../automated_digital_publishing/workflows/ html2epub1 ./temp/output.html ./html2epub1_config.xml
cp ../../../../automated_digital_publishing/workflows/temp/epub/out.epub ./output.epub

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
<!-- This file was created by posts_to_all_1.sh of wordpress_retriever_1 workflow, which is free software licensed under the GNU Affero General Public License 3 or any later version (see http://github.com/publishing-systems/digital_publishing_workflow_tools/ and http://www.publishing-systems.org). -->
<xml-xslt-transformator-1-jobfile>
  <job input-file=\"../output.xml\" entities-resolver-config-file=\"../../../../xml_xslt_transformator/xml_xslt_transformator_1/entities/config_empty.xml\" stylesheet-file=\"../posts_to_odt2html_template1_pdf.xsl\" output-file=\"./output.html\"/>
</xml-xslt-transformator-1-jobfile>" > "./temp/jobfile_xml_xslt_transformator_1_posts_to_all_1_pdf.xml"

java -cp ../../../xml_xslt_transformator/xml_xslt_transformator_1 xml_xslt_transformator_1 ./temp/jobfile_xml_xslt_transformator_1_posts_to_all_1_pdf.xml ./temp/resultinfo_xml_xslt_transformator_1_posts_to_all.xml_xslt_transformator
java -cp ../../../../automated_digital_publishing/workflows/ html2pdf2 ./temp/output.html
cp ../../../../automated_digital_publishing/workflows/temp/pdf/output.tex ./output.tex
cp ../../../../automated_digital_publishing/workflows/temp/pdf/output.pdf ./output.pdf
