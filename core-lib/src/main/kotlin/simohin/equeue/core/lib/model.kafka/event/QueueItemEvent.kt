package simohin.equeue.core.lib.model.kafka.event

import com.fasterxml.jackson.annotation.JsonProperty
import simohin.equeue.core.lib.model.kafka.QueueItem
import java.util.*

data class QueueItemEvent(
    @JsonProperty("id")
    val id: UUID = UUID.randomUUID(),
    @JsonProperty("type")
    val type: Type,
    @JsonProperty("item")
    val item: QueueItem
) {
    enum class Type {
        CREATED
    }
}
