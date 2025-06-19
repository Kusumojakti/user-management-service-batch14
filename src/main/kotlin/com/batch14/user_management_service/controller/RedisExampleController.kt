package com.batch14.user_management_service.controller

import com.batch14.user_management_service.domain.dto.response.BaseResponses
import com.batch14.user_management_service.service.RedisExampleService
import jakarta.ws.rs.Path
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/redis")
class RedisExampleController (
    private val redisExampleService: RedisExampleService
) {
    @PostMapping("/{id}")
    fun setValue(
        @PathVariable id: String,
    ): ResponseEntity<BaseResponses<String>> {
//        val value = redisExampleService.setValue(req.key, req.value)
        return ResponseEntity.ok(
            BaseResponses(
                data = redisExampleService.set(id.toInt()),
                message = "Value set successfully."
            )
        )
    }

    @GetMapping("/{id}")
    fun getValue(
        @PathVariable id: String,
    ): ResponseEntity<BaseResponses<String>> {
        return ResponseEntity.ok(
            BaseResponses(
                data = redisExampleService.get(id.toInt()),
                message = "Value retrieved successfully."
            )
        )
    }
}