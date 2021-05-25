package com.zj.play.logic.model

sealed class PlayState<T>
data class PlayLoading<T>(val load: String = "") : PlayState<T>()
data class PlaySuccess<T>(val data: T) : PlayState<T>()
data class PlayError<T>(val e: Throwable) : PlayState<T>()