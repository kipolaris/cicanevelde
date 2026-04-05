package hu.bme.aut.android.cicanevelde.domain.result.item

sealed class FillLitterResult {
    data object Success: FillLitterResult()
    data object LitterBoxNotFound: FillLitterResult()
    data object LitterAlreadyFull: FillLitterResult()
}