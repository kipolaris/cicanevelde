package hu.bme.aut.android.cicanevelde.domain.result

sealed class CareActionResult {
    data class Success(val coinsEarned: Int = 0) : CareActionResult()
    data object CatNotFound : CareActionResult()
    data object NotEnoughEnergy : CareActionResult()
    data object ItemRequired : CareActionResult()
    data object NoItemAvailable : CareActionResult()
}