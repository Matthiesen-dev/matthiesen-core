import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.architectury.plugin.TransformingTask
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType

plugins {
    id("matthiesen.platform-resources-conventions")
}

pluginManager.withPlugin("com.gradleup.shadow") {
    val shadowJar = tasks.named<ShadowJar>("shadowJar") {
        archiveClassifier.set("dev-shadow")
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
    }

    tasks.named<Jar>("jar") {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveClassifier.set("dev-slim")
    }

    tasks.named<RemapJarTask>("remapJar") {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set(project.version.toString())
    }

    tasks.named<RemapSourcesJarTask>("remapSourcesJar") {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
    }

    afterEvaluate {
        tasks.withType<TransformingTask>().configureEach {
            dependsOn(shadowJar)
            input.set(shadowJar.flatMap { it.archiveFile })
        }
    }
}

