package hu.bme.aut.android.cicanevelde.domain.result

sealed class CleanLitterResult {
    data object Success : CleanLitterResult()
    data object LitterBoxNotFound : CleanLitterResult()
    data object NoLitterAvailable : CleanLitterResult()
    data object LitterAlreadyClean : CleanLitterResult()
}