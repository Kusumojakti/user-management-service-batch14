package com.batch14.user_management_service.domain.dto.response

import java.io.Serial
import java.io.Serializable

data class RestGetUserByIdDto(
    val id: Int,
    val email: String,
    val username: String,
    var roleId: Int? = null,
    var roleName: String? = null
): Serializable {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -62823849598192565L
    }
}

