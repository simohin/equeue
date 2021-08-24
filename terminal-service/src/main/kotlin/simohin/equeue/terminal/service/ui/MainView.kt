package simohin.equeue.terminal.service.ui

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route
class MainView : VerticalLayout() {

    init {
        add(H1("Название очереди"))
        add(Text("Человек перед вами: ${getQueueCounter()}. Примерное время ожидания минут: ${getEstimatedWaitMinutes()}."))
        add(Button("Занять очередь") { onClick(it) })
    }

    private fun onClick(it: ClickEvent<Button>?) = println(it.toString())
    private fun getQueueCounter(): Int = 1
    private fun getEstimatedWaitMinutes(): Int = 10
}
