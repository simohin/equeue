package simohin.eueue.core.configuration.redis

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
@ConfigurationProperties("redis")
class RedisConfiguration {
    private var host: String = "localhost"
    private var port: Int = 6379

    @Bean
    fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory = LettuceConnectionFactory(host, port)
}
