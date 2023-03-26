package com.moa.backend.controller

import com.moa.backend.repository.IdeaBoxRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/idea-box")
class IdeaBoxController(private val ideaBoxRepository: IdeaBoxRepository) {
    //TODO
}