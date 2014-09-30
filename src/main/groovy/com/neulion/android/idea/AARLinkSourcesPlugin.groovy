package com.neulion.android.idea

import org.gradle.api.Plugin
import org.gradle.api.Project

class AARLinkSourcesPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.configurations.create('aarLinkSources')
        project.configurations.create('aarLinkJavadoc')

        if (!project.rootProject.tasks.hasProperty('aarLinkSources')) {
            final AARLinkSourcesTask aarLinkSourcesTask = project.rootProject.tasks.create('aarLinkSources', AARLinkSourcesTask)

            project.rootProject.gradle.projectsEvaluated {
                project.rootProject.allprojects.each {
                    if (it.configurations.hasProperty('aarLinkSources')) {
                        it.configurations.aarLinkSources.each { File file ->
                            aarLinkSourcesTask.linkSources file
                        }

                        it.configurations.aarLinkJavadoc.each { File file ->
                            aarLinkSourcesTask.linkJavadoc file
                        }
                    }
                }

                aarLinkSourcesTask.executeWithoutThrowingTaskFailure()
            }
        }
    }
}