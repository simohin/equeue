package simohin.equeue.terminal.ui

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import simohin.equeue.terminal.model.QueueItem
import simohin.equeue.terminal.service.QueueItemProducerService
import java.util.*

@Route
class MainView(
    private val queueItemProducerService: QueueItemProducerService,
) : VerticalLayout() {

    private val queueItemDialogText = H3()
    private val queueItemDialog = Dialog().apply {
        isOpened = false
        val textWrapper = Div().apply { add(queueItemDialogText) }
        add(
            H2("Ваш номер очереди"),
            textWrapper,
            Button("Закрыть").apply {
                addThemeVariants(ButtonVariant.LUMO_ERROR)
                addClickListener {
                    isOpened = !isOpened
                }
            }
        )
        expand(textWrapper)
    }

    init {
        setSizeFull()
        alignItems = FlexComponent.Alignment.CENTER
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        add(Button("Занять очередь") { onClick() }.apply { addThemeVariants(ButtonVariant.LUMO_PRIMARY) }, queueItemDialog)
    }

    private fun onClick() {
        QueueItem("A${nextInt(10000)}").also {
            try {
                queueItemProducerService.send(it).block()
                onQueueItemSent(it)
            } catch (e: Throwable) {
                onQueueItemFailed(e)
            }
        }
    }

    private fun onQueueItemSent(item: QueueItem) {
        queueItemDialogText.text = item.value
        queueItemDialog.isOpened = !queueItemDialog.isOpened
    }

    private fun onQueueItemFailed(error: Throwable) =
        Notification.show(error.message).apply {
            addThemeVariants(NotificationVariant.LUMO_ERROR)
        }

    companion object : Random()
}
