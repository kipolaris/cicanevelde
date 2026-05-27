package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.repository.BowlRepository
import hu.bme.aut.android.cicanevelde.data.repository.LitterRepository
import hu.bme.aut.android.cicanevelde.domain.model.Cat
import hu.bme.aut.android.cicanevelde.domain.model.enums.CatActivity
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType
import javax.inject.Inject

class DecideCatBehaviorUseCase @Inject constructor(
    private val bowlRepository: BowlRepository,
    private val litterRepository: LitterRepository
) {

    suspend operator fun invoke(cat: Cat): Pair<RoomType, CatActivity> {
        return when {
            cat.stats.hunger < 60 && bowlRepository.getRandomFilledBowl() != null ->
                RoomType.KITCHEN to CatActivity.EATING

            cat.stats.bladder < 60 && litterRepository.getFirstCleanLitter() != null ->
                RoomType.BATHROOM to CatActivity.USING_LITTER

            cat.stats.energy < 30 ->
                RoomType.LIVINGROOM to CatActivity.SLEEPING

            else ->
                RoomType.LIVINGROOM to CatActivity.IDLE
        }
    }
}