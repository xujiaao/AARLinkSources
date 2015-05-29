/*
 * Copyright 2014 Xujiaao
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xujiaao.android.idea

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AARLinkSourcesTask extends DefaultTask {

    // -------------------------------------------------------------------------------------------------------------------------------------
    // Interfaces
    // -------------------------------------------------------------------------------------------------------------------------------------

    void linkSources(File file) {
        link file, 'sources'
    }

    void linkJavadoc(File file) {
        link file, 'javadoc'
    }

    // -------------------------------------------------------------------------------------------------------------------------------------
    // Task Actions
    // -------------------------------------------------------------------------------------------------------------------------------------

    @TaskAction
    def linkSources() {
        if (inputs.getProperties() != null) {
            outputs.files.each { File xml ->
                def root = new XmlParser().parse(xml)

                def path = null;
                if ((path = inputs.getProperties().get("${xml.name}:sources".toString()))) {
                    appendPath(root.library[0].SOURCES[0], path)
                }
                if ((path = inputs.getProperties().get("${xml.name}:javadoc".toString()))) {
                    appendPath(root.library[0].JAVADOC[0], path)
                }

                new XmlNodePrinter(new PrintWriter(new FileWriter(xml))).print(root)

                project.logger.debug("Link success: {}", xml.name)
            }
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------------
    // Tools
    // -------------------------------------------------------------------------------------------------------------------------------------

    private Set<String> processedFiles = new HashSet<String>()

    private void link(File file, String type) {
        String name = file.name

        if (!processedFiles.contains(name)) {
            project.logger.debug("Link {}: {}", type, name)

            processedFiles.add(name)

            int index;

            if ((index = name.lastIndexOf('.')) != -1) {
                name = name.substring 0, index
            }

            if ((index = name.lastIndexOf('-')) != -1) {
                name = name.substring 0, index
            }

            if (name) {
                name = name.replace('.', '_').replace('-', '_')

                File xml = project.file("./.idea/libraries/${name}.xml")
                if (xml.exists() && xml.isFile()) {
                    inputs.property "${xml.name}:${type}".toString(), generatePath(file)
                    outputs.file xml
                } else {
                    project.logger.debug("No such file: {}", xml.absolutePath)
                }
            }
        }
    }

    private String generatePath(File file) {
        String path = file.absolutePath

        String root = null;
        if ((root = project.rootDir) && path.startsWith(root)) {
            path = '$PROJECT_DIR$' + path.substring(root.length())
        } else if ((root = System.getProperty("user.home")) && path.startsWith(root)) {
            path = '$USER_HOME$' + path.substring(root.length())
        }

        path.replaceAll(/\\+/, '/')
    }

    static def appendPath(Object node, Object path) {
        if (node) {
            node.children().clear()
            node.appendNode('root', [url: "jar://${path}!/"])
        }
    }
}