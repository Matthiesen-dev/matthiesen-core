plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.shadow-platform-conventions")
}

val generatedResources = file("src/generated/resources")

sourceSets.main {
    resources {
        srcDir(generatedResources)
    }
}

architectury {
    platformSetupLoomIde()
    fabric()
}

val shadowBundle: Configuration by configurations.creating

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    compileOnly(libs.bundles.fabricCompileOnly)
    implementation(libs.bundles.fabricImplementation)
    modCompileOnly(libs.bundles.fabricModCompileOnly)
    modRuntimeOnly(libs.bundles.fabricModRuntimeOnly)
    modImplementation(libs.bundles.fabricModImplementation)
    modImplementation(libs.bundles.fabricModImplementationNoTransitive) { isTransitive = false }

    implementation(project(":common", configuration = "namedElements"))
    "developmentFabric"(project(":common", configuration = "namedElements"))
    shadowBundle(project(":common", configuration = "transformProductionFabric"))

    shadowBundle(libs.faststats)

    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

tasks {
    // The AW file is needed in :fabric project resources when the game is run.
    val copyAccessWidener by registering(Copy::class) {
        description = "Copies the access widener file to the generated resources directory"
        from(loom.accessWidenerPath)
        into(generatedResources)
    }

    processResources {
        dependsOn(copyAccessWidener)

        filesMatching("fabric.mod.json") {
            expand(project.properties)
        }
    }

    sourcesJar {
        dependsOn(copyAccessWidener)
    }

    shadowJar {
        configurations = listOf(shadowBundle)
    }
}
