package hu.bme.aut.android.cicanevelde.domain.mappers

import hu.bme.aut.android.cicanevelde.data.entity.CatEntity
import hu.bme.aut.android.cicanevelde.domain.model.Cat

fun CatEntity.toDomain() = Cat(
    id = id,
    name = name,
    age = age,
    gender = gender,
    pattern = pattern,
    stats = stats
)

fun Cat.toEntity() = CatEntity(
    id = id,
    name = name,
    age = age,
    gender = gender,
    pattern = pattern,
    stats = stats
)