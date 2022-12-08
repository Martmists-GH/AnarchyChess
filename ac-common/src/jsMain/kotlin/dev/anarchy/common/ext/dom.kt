package dev.anarchy.common.ext

import org.w3c.dom.*
import org.w3c.dom.events.*

inline fun <reified T: Element> Element.getElementsByClassName(className: String): List<T> {
    return getElementsByClassName(className).asList().filterIsInstance<T>()
}

inline fun <reified T: Element> Document.getElementById(id: String): T {
    return getElementById(id) as T
}

inline fun <reified T : Element> ParentNode.querySelector(selector: String): T? {
    return querySelector(selector) as T?
}

inline fun <reified T : Element> ParentNode.querySelectorAll(selector: String): List<T> {
    return querySelectorAll(selector).asList().filterIsInstance<T>()
}

fun EventTarget.addEventListener(event: String, block: () -> Any?) = addEventListener<Event>(event) { block() }
fun EventTarget.addEventListener(event: String, block: (Event) -> Any?) = addEventListener<Event>(event, block)

inline fun <reified T : Event> EventTarget.addEventListener(event: String, crossinline block: (T) -> Any?) {
    addEventListener(event, { (it as? T)?.let(block) })
}
