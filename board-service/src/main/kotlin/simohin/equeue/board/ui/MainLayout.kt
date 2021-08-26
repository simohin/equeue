package simohin.equeue.board.ui

import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import simohin.equeue.board.configuration.QueueConfiguration
import simohin.equeue.board.model.QueueItem
import java.time.Duration

@Route("")
@PWA(name = "Electronic queue dashboard application", shortName = "E-queue Board App")
@Push
class MainLayout(
    private val queueItemsFlux: Flux<QueueItem>
) : VerticalLayout(),
    AppShellConfigurator {

    private val title = H1("Текущая очередь").apply {
        style["margin"] = "10px"
        style["cursor"] = "default"
    }
    private val titleWrapper = createTitleWrapper()
    private val itemsDataProvider = ListDataProvider<QueueItem>(mutableSetOf())
    private val itemsGrid = Grid(QueueItem::class.java).apply {
        removeAllColumns()
        addColumn("value").setHeader("Номер талона")
        setItems(itemsDataProvider)
        style["margin"] = "10px"
    }
    private var subscription: Disposable? = null


    init {
        style["margin"] = "0"
        style["padding"] = "0"
        setSizeFull()

        createNavbarContent()
        add(itemsGrid)
        expand(itemsGrid)
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        if (subscription == null) {
            subscription = queueItemsFlux.subscribe {
                itemsDataProvider.apply {
                    items.add(it)
                    refreshAll()
                }
            }
        }
    }

    override fun onDetach(detachEvent: DetachEvent?) {
        subscription?.dispose()
        super.onDetach(detachEvent)
    }

    private fun createNavbarContent() = add(HorizontalLayout().apply {
        setId("header")
        themeList.set("dark", true)
        setWidthFull()
        isPadding = false
        isSpacing = false
        alignItems = FlexComponent.Alignment.CENTER
        add(titleWrapper)
    })

    private fun createTitleWrapper(): Component = VerticalLayout().apply {
        isPadding = false
        isSpacing = false
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        add(title)
    }
}
