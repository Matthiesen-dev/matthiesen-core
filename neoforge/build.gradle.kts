plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.shadow-platform-conventions")
    id("matthiesen.publishing-conventions")
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
    modRuntimeOnly(libs.bundles.neoforgeModRuntimeOnly)
    modImplementation(libs.bundles.neoforgeModImplementation)
    modImplementation(libs.bundles.neoforgeModImplementationNoTransitive) { isTransitive = false }

    implementation(project(":common", configuration = "namedElements"))
    "developmentNeoForge"(project(":common", configuration = "transformProductionNeoForge")) {
        isTransitive = false
    }
    shadowBundle(project(":common", configuration = "transformProductionNeoForge"))

    runtimeOnly(libs.sqlite.jdbc)
    shadowBundle(libs.sqlite.jdbc)

    runtimeOnly(libs.mysql.connector.j)
    shadowBundle(libs.mysql.connector.j)

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
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
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
        relocate("com.mysql", "dev.matthiesen.matthiesen_core.shadow.com.mysql")
        relocate("com.google.protobuf", "dev.matthiesen.matthiesen_core.shadow.com.google.protobuf")
        relocate("org.sqlite", "dev.matthiesen.matthiesen_core.shadow.org.sqlite")
    }

    remapJar {
        atAccessWideners.add("matthiesen_core.accesswidener")
    }
}
