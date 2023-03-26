package com.moa.backend.controller

import com.moa.backend.repository.IdeaRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/idea")
class IdeaController(private val ideaRepository: IdeaRepository) {
    //TODO
}