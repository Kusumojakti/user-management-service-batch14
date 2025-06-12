package com.batch14.user_management_service.domain.dto.response

data class RestGetUserByIdDto(
    val id: Int,
    val email: String,
    val username: String,
    var roleId: Int? = null,
    var roleName: String? = null
)
