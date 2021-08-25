package simohin.equeue.board.ui.component

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.Div

class QueueItemsDiv : Div() {

    init {
        style["overflow-y"] = "scroll"
        style["width"] = "100%"
    }

    override fun add(vararg components: Component?) {
        super.add(*components)
        components[components.size - 1]!!.element.callJsFunction("scrollIntoView")
    }
}
