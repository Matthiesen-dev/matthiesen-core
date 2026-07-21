import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named

plugins {
    id("matthiesen.project-conventions")
}

pluginManager.withPlugin("dev.architectury.loom") {
    configure<LoomGradleExtensionAPI> {
        enableTransitiveAccessWideners.set(true)
        accessWidenerPath.set(project(":common").layout.projectDirectory.file("src/main/resources/matthiesen_core.accesswidener"))
        silentMojangMappingsLicense()
    }

    tasks.named<org.gradle.jvm.tasks.Jar>("jar") {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveClassifier.set("dev-slim")
    }

    tasks.named<RemapJarTask>("remapJar") {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set(project.version.toString())
    }

    tasks.named<RemapSourcesJarTask>("remapSourcesJar") {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("sources")
    }
}

