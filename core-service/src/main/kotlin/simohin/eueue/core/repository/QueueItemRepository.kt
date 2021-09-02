package simohin.eueue.core.repository

import org.springframework.data.repository.CrudRepository
import simohin.eueue.core.entity.QueueItem

interface QueueItemRepository : CrudRepository<QueueItem, String>
