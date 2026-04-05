package hu.bme.aut.android.cicanevelde.domain.result

sealed class UseLitterResult {
    data object Success : UseLitterResult()
    data object NoNeedTo : UseLitterResult()
    data object NoLitterAvailable : UseLitterResult()
}