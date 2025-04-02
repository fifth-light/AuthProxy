plugins {
    alias(libs.plugins.fabric.loom)
}

group = "top.fifthlight.authproxy"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    minecraft(libs.minecraft)
    mappings(variantOf(libs.yarn) { classifier("v2") })

    modImplementation(libs.fabric.loader)
}

loom {
    mixin {
        useLegacyMixinAp = false
    }
}

tasks.processResources {
    inputs.properties("version" to project.version)

    from(file("LICENSE"))

    filesMatching("fabric.mod.json") {
        expand("version" to inputs.properties["version"])
    }
}

java {
    toolchain {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

