package simohin.equeue.terminal

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class TerminalServiceApplication

fun main(args: Array<String>) {
    SpringApplication.run(TerminalServiceApplication::class.java, *args)
}
