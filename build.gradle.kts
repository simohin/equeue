import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    id("com.vaadin")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "simohin"
version = "${properties["applicationVersion"]}"

tasks.bootJar { enabled = false }
tasks.jar { enabled = true }

allprojects {

    repositories {
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
}

subprojects {

    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "com.vaadin")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.springframework.boot:spring-boot-devtools")
        implementation("com.vaadin:vaadin-spring-boot-starter:${properties["vaadinVersion"]}")
//        implementation("com.vaadin:vaadin:${properties["vaadinVersion"]}")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
