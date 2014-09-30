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