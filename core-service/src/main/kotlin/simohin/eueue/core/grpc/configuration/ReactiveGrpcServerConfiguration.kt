package simohin.eueue.core.grpc.configuration

import io.grpc.BindableService
import io.grpc.Server
import io.grpc.ServerBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("reactive.grpc.server")
class ReactiveGrpcServerConfiguration(private val grpcServices: Collection<GrpcService>) {

    private var port: Int = 9090

    @Bean
    fun reactiveGrpcServer(): Server = ServerBuilder.forPort(port)
        .apply { grpcServices.forEach(this::addService) }
        .build().start()
        .also { it.awaitTermination() }

    interface GrpcService : BindableService
}
