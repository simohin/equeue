package simohin.eueue.core.grpc.processor

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import simohin.equeue.core.lib.grpc.QueueItemEvent
import simohin.equeue.core.lib.grpc.QueueItemEventProcessingResult
import simohin.equeue.core.lib.grpc.ReactorQueueItemEventProcessorGrpc
import simohin.eueue.core.grpc.configuration.ReactiveGrpcServerConfiguration
import java.util.logging.Logger

@Component
class QueueItemEventProcessor : ReactorQueueItemEventProcessorGrpc.QueueItemEventProcessorImplBase(),
    ReactiveGrpcServerConfiguration.GrpcService {

    override fun process(request: Mono<QueueItemEvent>): Mono<QueueItemEventProcessingResult> =
        request.map(this::doProcess)

    fun doProcess(event: QueueItemEvent): QueueItemEventProcessingResult =
        QueueItemEventProcessingResult.newBuilder().setIsSuccess(true).setValue(event.item.value).build()
            .also { log.info("Processed event: $event") }

    companion object {
        val log: Logger = Logger.getLogger(QueueItemEventProcessor::class.java.simpleName)
    }
}
