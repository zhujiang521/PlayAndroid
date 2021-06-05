package com.zj.play.logic.model


sealed class PlayState<out R> {
    fun isLoading() = this is PlayLoading
    fun isSuccessful() = this is PlaySuccess

    override fun toString(): String {
        return when (this) {
            is PlaySuccess<*> -> "Success[data=$data]"
            is PlayError -> "Error[exception=${error}]"
            PlayLoading -> "Loading"
        }
    }
}

data class PlaySuccess<out T>(val data: T) : PlayState<T>()
data class PlayError(val error: Throwable) : PlayState<Nothing>()
object PlayLoading : PlayState<Nothing>()

/**
 * [PlayState.data] if [Result] is of query [PlayState]
 */
fun <T> PlayState<T>?.successOr(fallback: T): T {
    if (this == null) return fallback
    return (this as? PlaySuccess<T>)?.data ?: fallback
}

val <T> PlayState<T>.data: T?
    get() = (this as? PlaySuccess)?.data
