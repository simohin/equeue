package simohin.eueue.core

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class CoreServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(CoreServiceApplication::class.java, *args)
}
