package simohin.equeue.terminal.integration.core.grpc.configuration

import io.grpc.ManagedChannelBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import simohin.equeue.core.lib.grpc.ReactorQueueItemRegistrationServiceGrpc

@Configuration
@ConfigurationProperties("services.core")
class CoreGrpcClientConfiguration {

    private var host: String = "localhost"
    private var port: Int = 9090
    private val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()

    @Bean
    fun queueItemRegistrationServiceStub(): ReactorQueueItemRegistrationServiceGrpc.ReactorQueueItemRegistrationServiceStub =
        ReactorQueueItemRegistrationServiceGrpc.newReactorStub(channel)
}
