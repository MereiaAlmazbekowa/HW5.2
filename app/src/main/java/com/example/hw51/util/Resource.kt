package com.example.hw51.util

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    class Error<T>(val message: String) : Resource<T>()
    class Success<T>(val data: T) : Resource<T>()

}