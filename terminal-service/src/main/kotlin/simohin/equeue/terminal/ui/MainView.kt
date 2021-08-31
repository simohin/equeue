package simohin.equeue.terminal.ui

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.Command
import com.vaadin.flow.server.PWA
import reactor.core.publisher.Mono
import simohin.equeue.core.lib.grpc.QueueItem
import simohin.equeue.core.lib.grpc.QueueItemEvent
import simohin.equeue.core.lib.grpc.QueueItemEvent.Type.CREATED
import simohin.equeue.core.lib.grpc.UUID
import simohin.equeue.terminal.integration.core.grpc.service.QueueItemEventSendService
import kotlin.random.Random
import java.util.UUID as UUID_UTIL

@Route("")
@PWA(name = "Electronic queue terminal application", shortName = "E-queue Terminal App")
@Push
class MainView(
    private val queueItemEventSendService: QueueItemEventSendService
) : VerticalLayout(), AppShellConfigurator {

    init {
        setSizeFull()
        alignItems = FlexComponent.Alignment.CENTER
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        add(Button(BUTTON_TEXT) { onButtonClick() }.apply { addThemeVariants(ButtonVariant.LUMO_PRIMARY) })
    }

    private fun doInUi(command: Command) = ui.ifPresent { ui ->
        ui.access {
            command.execute()
            ui.push()
        }
    }

    private fun onQueueItemEventSent(number: String) = showNotification(
        VerticalLayout(H3(SUCCESS_TITLE), Span(number)), NotificationVariant.LUMO_SUCCESS
    )

    private fun onQueueItemEventSendFailed(string: String? = null) =
        showNotification(H3(string ?: ERROR_TITLE), NotificationVariant.LUMO_ERROR)

    private fun onButtonClick() = queueItemEventSendService.send(Mono.just(prepareEvent())).also {
        it.doOnError {
            doInUi { onQueueItemEventSendFailed() }
        }.subscribe { result ->
            doInUi {
                if (result.isSuccess) {
                    onQueueItemEventSent(result.value)
                } else {
                    onQueueItemEventSendFailed()
                }
            }
        }
    }

    private fun prepareEvent() = QueueItemEvent.newBuilder().apply {
        id = UUID.newBuilder().apply {
            value = UUID_UTIL.randomUUID().toString()
        }.build()
        type = CREATED
        item = QueueItem.newBuilder().apply {
            id = UUID.newBuilder().apply {
                value = UUID_UTIL.randomUUID().toString()
            }.build()
            value = random.nextLong(1000).toString()
        }.build()
    }.build()

    companion object {

        private const val SUCCESS_TITLE = "Ваша очередь"
        private const val ERROR_TITLE = "Не удалось занять очередь"
        private const val BUTTON_TEXT = "Занять очередь"
        private const val NOTIFICATION_DURATION = 5000
        private val random = Random

        private fun showNotification(component: Component, variant: NotificationVariant) =
            Notification(component).apply {
                position = Notification.Position.MIDDLE
                duration = NOTIFICATION_DURATION
                addThemeVariants(variant)
            }.open()
    }
}
