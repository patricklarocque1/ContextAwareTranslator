package com.example.core.context

class ConversationStore(
    private val maxEntries: Int = 20
) {
    private val entries = ArrayDeque<Pair<String,String>>() // source -> translated

    fun add(source: String, translated: String) {
        entries.addLast(source to translated)
        while (entries.size > maxEntries) {
            entries.removeFirst()
        }
    }

    fun history(): List<Pair<String,String>> = entries.toList()
}