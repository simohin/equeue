import com.google.protobuf.gradle.*

description = "equeue.core-lib"

tasks.bootJar { enabled = false }

plugins {
    id("com.google.protobuf")
    idea
}

dependencies {
    implementation("com.salesforce.servicelibs:reactor-grpc-stub:${properties["reactiveGrpcVersion"]}")
}

protobuf {

    generatedFilesBaseDir = "$projectDir/src/generated"
    protoc {
        artifact = "com.google.protobuf:protoc:${properties["protocVersion"]}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${properties["grpcVersion"]}"
        }
        id("reactor") {
            artifact = "com.salesforce.servicelibs:reactor-grpc:${properties["reactiveGrpcVersion"]}"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("reactor")
            }
        }
    }
}

idea {
    module {
        val sources = setOf(
            file("src/generated/main/java"),
            file("src/generated/main/grpc")
        )
        sourceDirs.addAll(sources)
        generatedSourceDirs.addAll(sources)
    }
}
