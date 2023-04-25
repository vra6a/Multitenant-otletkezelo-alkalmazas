package com.moa.backend.mapper

interface Mapper<D, SD, E> {
    fun modelToDto(entity: E): D
    fun modelToSlimDto(entity: E): SD
    fun dtoToModel(domain: D): E
    fun slimDtoToModel(domain: SD): E
}