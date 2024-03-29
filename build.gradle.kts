plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    maven {
        name = "Paper MC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }

    maven {
        name = "PlaceholderAPI"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    maven {
        name = "Vault API"
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.2")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
}

tasks.processResources {
    expand("version" to project.version)
}

group = "xyz.distemi.prtp"
version = "1.4.2"
description = "PRTP"
java.sourceCompatibility = JavaVersion.VERSION_1_8

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.register("copy_to_1_12_2_test_env") {
    doLast {
        copy {
            from("./build/libs/PRTP-1.4.jar")
            into("../test-server1.12.2/plugins/")
        }
    }
}
tasks.register("copy_to_1_19_2_test_env") {
    doLast {
        copy {
            from("./build/libs/PRTP-1.4.jar")
            into("../test-server1.19.2/plugins/")
        }
    }
}
tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    minimize()
    exclude("META-INF")
}