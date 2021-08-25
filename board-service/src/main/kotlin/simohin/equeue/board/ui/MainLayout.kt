package simohin.equeue.board.ui

import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.DetachEvent
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.PWA
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.UnicastProcessor
import simohin.equeue.board.configuration.QueueConfiguration
import simohin.equeue.board.model.QueueItem
import simohin.equeue.board.ui.component.QueueItemsDiv
import java.time.Duration

@Route
@PWA(name = "Electronic queue dashboard application", shortName = "E-queue Board App")
@Push
class MainLayout(
    private val queueItemsProcessor: UnicastProcessor<QueueItem>,
    private val queueItems: Flux<QueueItem>
) : VerticalLayout(),
    AppShellConfigurator {

    private val title = H1("Текущая очередь").apply {
        style["margin"] = "10px"
        style["cursor"] = "default"
    }
    private val titleWrapper = createTitleWrapper()
    private val items = QueueItemsDiv()
    private var subscription: Disposable? = null


    init {
        style.set("margin", "0")
        style.set("padding", "0")

        createNavbarContent()
        add(items)
        expand(items)
    }

    override fun onAttach(attachEvent: AttachEvent?) {
        super.onAttach(attachEvent)
        if (subscription == null) {
            subscription = queueItems.subscribe {
                items.add(Paragraph(it.value))
            }
        }

        Flux.generate<QueueItem> { sink ->
            sink.next(QueueItem("A${QueueConfiguration.nextInt(1000)}"))
        }.delayElements(Duration.ofSeconds(3)).take(20).subscribe { item ->
            ui.ifPresent {
                it.access {
                    queueItemsProcessor.onNext(item)
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
