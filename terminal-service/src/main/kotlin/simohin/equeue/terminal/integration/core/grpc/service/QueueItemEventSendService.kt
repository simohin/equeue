package simohin.equeue.terminal.integration.core.grpc.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import simohin.equeue.core.lib.grpc.QueueItemEvent
import simohin.equeue.core.lib.grpc.QueueItemEventProcessingResult
import simohin.equeue.core.lib.grpc.ReactorQueueItemEventProcessorGrpc

@Service
class QueueItemEventSendService(private val coreGrpcClientStub: ReactorQueueItemEventProcessorGrpc.ReactorQueueItemEventProcessorStub) {

    fun send(eventMono: Mono<QueueItemEvent>): Mono<QueueItemEventProcessingResult> =
        coreGrpcClientStub.process(eventMono)
}
