package com.example.mystoryapp.ui

import com.example.mystoryapp.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "name + $i",
                "description $i",
                "photo $i",
                "createdAt $i",
            )
            items.add(story)
        }
        return items
    }
}