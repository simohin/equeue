package simohin.equeue.terminal.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("QueueItem")
data class QueueItem(
    @JsonProperty("value")
    val value: String
)
