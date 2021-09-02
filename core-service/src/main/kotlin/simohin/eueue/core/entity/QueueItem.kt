package simohin.eueue.core.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import simohin.equeue.core.lib.grpc.QueueItem as GrpcQueueItem

@RedisHash("QueueItem")
data class QueueItem(
    @Id
    val id: String,
    val value: String
) {
    constructor(
        item: GrpcQueueItem
    ) : this(item.id.value, item.value)
}
