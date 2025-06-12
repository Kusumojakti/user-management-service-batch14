package com.batch14.user_management_service.domain.dto.request

import jakarta.validation.constraints.NotBlank

data class ReqLoginDto(
    val username: String,
    val password: String,
)
