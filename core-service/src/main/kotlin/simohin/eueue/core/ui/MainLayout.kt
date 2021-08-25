package simohin.eueue.core.ui

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentUtil
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.tabs.TabsVariant
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLink
import simohin.eueue.core.ui.view.MainView
import simohin.eueue.core.ui.view.QueuesView

@Route
class MainLayout : AppLayout() {

    private val menu = createMenu()
    private val title = H2(MainView.getTitle()).apply {
        style.set("margin", "10px")
        style.set("cursor", "default")
    }
    private val titleWrapper = createTitleWrapper()

    init {
        addToNavbar(true, createNavbarContent())
        addToDrawer(createDrawerContent())
        isDrawerOpened = false
    }

    override fun afterNavigation() = super.afterNavigation().also {
        menu.children.filter { ComponentUtil.getData(it, Class::class.java).equals(content::class.java) }
            .findFirst().map(Tab::class.java::cast).ifPresent(menu::setSelectedTab)
        title.text = content.javaClass.getAnnotation(PageTitle::class.java).value
    }

    private fun createDrawerContent(): Component = VerticalLayout().apply {
        setSizeFull()
        isPadding = false
        isSpacing = false
        alignItems = FlexComponent.Alignment.STRETCH
        add(menu)
    }

    private fun createMenu(): Tabs = Tabs().apply {
        orientation = Tabs.Orientation.VERTICAL
        addThemeVariants(TabsVariant.LUMO_MINIMAL)
        setId("tabs")
        createMenuItems().forEach { add(it) }
    }

    private fun createTitleWrapper(): Component = VerticalLayout().apply {
        isPadding = false
        isSpacing = false
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        add(title)
    }

    private fun createMenuItems(): List<Tab> = tabsMap.map {
        createTab(it.key, it.value)
    }

    private fun createTab(itemTitle: String, targetComponent: Class<out Component>): Tab = Tab().apply {
        add(RouterLink(itemTitle, targetComponent))
    }.also {
        ComponentUtil.setData(it, Class::class.java, targetComponent)
    }

    private fun createNavbarContent(): Component = HorizontalLayout().apply {
        setId("header")
        themeList.set("dark", true)
        setWidthFull()
        isPadding = false
        isSpacing = false
        alignItems = FlexComponent.Alignment.CENTER
        add(DrawerToggle().apply {
            setHeightFull()
            alignItems = FlexComponent.Alignment.CENTER
        })
        add(titleWrapper)
    }

    companion object {
        val tabsMap = mapOf(
            MainView.getTitle() to MainView::class.java,
            QueuesView.getTitle() to QueuesView::class.java
        )
    }
}
