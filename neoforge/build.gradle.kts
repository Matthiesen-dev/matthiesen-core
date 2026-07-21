plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.shadow-platform-conventions")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

evaluationDependsOn(":common")

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.neoforged.net/releases/")
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    minecraft(libs.minecraft.net)
    mappings(loom.officialMojangMappings())
    neoForge(libs.neoforge)

    compileOnly(libs.bundles.neoforgeCompileOnly)
    implementation(libs.bundles.neoforgeImplementation)
    modCompileOnly(libs.bundles.neoforgeModCompileOnly)
    modImplementation(libs.bundles.neoforgeModImplementation)
    modImplementation(libs.bundles.neoforgeModImplementationNoTransitive) { isTransitive = false }

    implementation(project(":common", configuration = "namedElements"))
    "developmentNeoForge"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowBundle(project(":common", configuration = "transformProductionNeoForge"))

    shadowBundle(libs.faststats)

    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

tasks {
    processResources {
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(project.properties)
        }
    }

    sourcesJar {
        val depSources = project(":common").tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        dependsOn(depSources)
        from(depSources.flatMap { it.archiveFile }.map { zipTree(it) }) {
            exclude("architectury.accessWidener")
        }
    }

    shadowJar {
        exclude("fabric.mod.json")
        exclude("architectury-common.accessWidener")
        exclude("architectury.common.json")
        configurations = listOf(shadowBundle)
    }

    remapJar {
        atAccessWideners.add("matthiesen_lib.accesswidener")
    }
}
