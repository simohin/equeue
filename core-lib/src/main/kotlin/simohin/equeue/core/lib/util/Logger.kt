package simohin.equeue.core.lib.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Logger {
    protected val log: Logger = LoggerFactory.getLogger(javaClass.enclosingClass ?: javaClass)
}
