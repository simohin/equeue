package simohin.equeue.terminal.ui

import com.vaadin.flow.component.ClickEvent
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
import simohin.equeue.core.lib.grpc.RegisterQueueItemRequest
import simohin.equeue.core.lib.grpc.RegisterQueueItemResponse
import simohin.equeue.core.lib.grpc.UUID
import simohin.equeue.terminal.integration.core.grpc.service.QueueItemRegistrationService
import kotlin.random.Random

@Route("")
@PWA(name = "Electronic queue terminal application", shortName = "E-queue Terminal App")
@Push
class MainView(
    private val queueItemRegistrationService: QueueItemRegistrationService
) : VerticalLayout(), AppShellConfigurator {

    private val button = Button(BUTTON_TEXT) { onButtonClick(it) }.apply {
        addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        isDisableOnClick = true
    }

    init {
        setSizeFull()
        alignItems = FlexComponent.Alignment.CENTER
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        add(button)
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

    private fun onButtonClick(clickEvent: ClickEvent<Button>): Mono<RegisterQueueItemResponse> =
        queueItemRegistrationService.register(
            Mono.just(
                RegisterQueueItemRequest.newBuilder()
                    .setId(UUID.newBuilder().setValue(java.util.UUID.randomUUID().toString())).build()
            )
        ).also {
            it.doOnError {
                doInUi { onQueueItemEventSendFailed() }
            }.subscribe { response ->
                doInUi {
                    if (response.isSuccess) {
                        onQueueItemEventSent(response.item.value)
                    } else {
                        onQueueItemEventSendFailed()
                    }
                }
            }
            clickEvent.source.isEnabled = true
        }

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
