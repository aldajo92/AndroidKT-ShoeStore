package com.udacity.shoestore.events

open class DataEvent<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // External read but not write

    fun getDataOnce(): T? {
        return if (hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }
    }

}
