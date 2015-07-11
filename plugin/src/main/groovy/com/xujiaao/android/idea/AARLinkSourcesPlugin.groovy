/*
 * Copyright 2014 Xujiaao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xujiaao.android.idea

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.internal.component.external.model.DefaultModuleComponentIdentifier
import org.gradle.jvm.JvmLibrary
import org.gradle.language.base.artifact.SourcesArtifact
import org.gradle.language.java.artifact.JavadocArtifact

class AARLinkSourcesPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        if (!project.rootProject.tasks.hasProperty('aarLinkSources')) {
            final AARLinkSourcesTask aarLinkSourcesTask = project.rootProject.tasks.create('aarLinkSources', AARLinkSourcesTask)

            project.rootProject.gradle.projectsEvaluated {
                def artifacts = getProjectDependencies(project).findAll { it.type.equals("aar") }
                        .unique()

                def identifiers = artifacts.collect {
                    new DefaultModuleComponentIdentifier(it.moduleVersion.id.group, it.moduleVersion.id.name, it.moduleVersion.id.version)
                }

                project.dependencies.createArtifactResolutionQuery().forComponents(identifiers)
                        .withArtifacts(JvmLibrary, SourcesArtifact, JavadocArtifact)
                        .execute().resolvedComponents.each {
                    it.getArtifacts(SourcesArtifact).each { aarLinkSourcesTask.linkSources it.file }
                    it.getArtifacts(JavadocArtifact).each { aarLinkSourcesTask.linkJavadoc it.file }
                }

                aarLinkSourcesTask.linkSources()
            }
        }
    }

    private static Collection<ResolvedArtifact> getProjectDependencies(Project project) {
        return project.rootProject.allprojects.collectMany { it.configurations }
                .collectMany { it.resolvedConfiguration.resolvedArtifacts }
    }
}