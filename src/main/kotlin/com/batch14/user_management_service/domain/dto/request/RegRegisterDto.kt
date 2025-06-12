package com.batch14.user_management_service.domain.dto.request

import jakarta.validation.constraints.NotBlank

data class RegRegisterDto(
    @field:NotBlank(message = "email wajid diisi!")
    val email: String,
    @field:NotBlank(message = "password wajid diisi!")
    val password: String,
    @field:NotBlank(message = "username wajid diisi!")
    val username: String,
    var roleId: Int?
)
