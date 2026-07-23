plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.minecraft-module-conventions")
    id("matthiesen.shadow-platform-conventions")
    id("matthiesen.publishing-conventions")
}

architectury {
    common("neoforge", "fabric")
}

val shadowBundle: Configuration by configurations.creating

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    compileOnly(libs.bundles.commonCompileOnly)
    implementation(libs.bundles.commonImplementation)
    modCompileOnly(libs.bundles.commonModCompileOnly)
    modImplementation(libs.bundles.commonModImplementation)
    modImplementation(libs.bundles.commonModImplementationNoTransitive) { isTransitive = false }

    shadowBundle(libs.faststats)

    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        inputs.property("mod_name", project.property("mod_name").toString())
        filesMatching("pack.mcmeta") {
            expand(project.properties)
        }
    }

    shadowJar {
        configurations = listOf(shadowBundle)
    }
}
