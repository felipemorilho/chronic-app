package com.chronicpain.usecase

fun interface UseCase<IN, OUT> {

    fun execute(input: IN): OUT
}