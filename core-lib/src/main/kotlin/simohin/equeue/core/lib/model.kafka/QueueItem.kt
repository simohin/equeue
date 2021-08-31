package simohin.equeue.core.lib.model.kafka

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class QueueItem(
    @JsonProperty("id")
    val id: UUID = UUID.randomUUID(),
    @JsonProperty("value")
    val value: String = nextInt(1000).toString()
) {
    companion object : Random()
}
