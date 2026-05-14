package com.akira.survivalalpha.util

import java.util.*

abstract class ProtectedStorage<E : Any> {
    protected val content = mutableListOf<E>()
    val contentView: List<E> get() = Collections.unmodifiableList(content)

    protected fun add(element: E) = this.content.add(element)

    protected fun remove(element: E) = this.content.remove(element)

    fun forEach(block: (E) -> Unit) = content.forEach(block)
}