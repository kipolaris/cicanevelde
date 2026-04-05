package hu.bme.aut.android.cicanevelde.domain.result

sealed class SleepResult {
    data object Success : SleepResult()
    data object NotTiredEnough : SleepResult()
}