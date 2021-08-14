package dev.decagon.networkingclass.model

sealed class Result<out T : Any>

data class Success<out T : Any>(val data: T) : Result<T>()

data class Loading(val message: String) : Result<String>()

data class Failure(val error: Throwable?) : Result<Nothing>()