package simohin.eueue.core

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class CoreApplication

fun main(args: Array<String>) {
    SpringApplication.run(CoreApplication::class.java, *args)
}
