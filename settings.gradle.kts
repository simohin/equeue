rootProject.name = "equeue"
include("terminal-service")
include("clerk-service")
include("core-service")

pluginManagement {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    val springBootVersion: String by settings
    val kotlinVersion: String by settings
    val springDependencyManagementVersion: String by settings

    plugins {
        application
        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false
    }
}
