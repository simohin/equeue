package simohin.eueue.core.ui.view

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import simohin.eueue.core.ui.MainLayout

private const val PAGE_TITLE = "Управление очередями"
private const val ROUTE_VALUE = "queues"

@PageTitle(PAGE_TITLE)
@Route(value = ROUTE_VALUE, layout = MainLayout::class)
class QueuesView : VerticalLayout() {

    init {
        add("Вьюшка очередей")
    }

    companion object {
        fun getTitle() = PAGE_TITLE
    }
}
