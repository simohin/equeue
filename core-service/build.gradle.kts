description = "equeue.core-service"
dependencies {
    implementation(project(":core-lib"))
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.data:spring-data-redis")
    implementation("io.lettuce:lettuce-core:${properties["lettuceVersion"]}")
}
