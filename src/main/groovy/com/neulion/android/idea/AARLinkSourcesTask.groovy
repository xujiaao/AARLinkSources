package com.neulion.android.idea

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AARLinkSourcesTask extends DefaultTask {
    private static final String TAG = 'AARLinkSources'

    boolean debug = false;

    // -------------------------------------------------------------------------------------------------------------------------------------
    // Interfaces
    // -------------------------------------------------------------------------------------------------------------------------------------

    void debug(boolean enabled) {
        debug = enabled
    }

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
                } else if ((path = inputs.getProperties().get("${xml.name}:javadoc".toString()))) {
                    appendPath(root.library[0].JAVADOC[0], path)
                }

                new XmlNodePrinter(new PrintWriter(new FileWriter(xml))).print(root)

                if (debug) {
                    println "[${TAG}] [Info] Link success: ${xml.name}"
                }
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
            if (debug) {
                println "[${TAG}] [Info] Link ${type}: ${name}"
            }

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
                } else if (debug) {
                    println "[${TAG}] [Error] No such file: ${xml.absolutePath}"
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