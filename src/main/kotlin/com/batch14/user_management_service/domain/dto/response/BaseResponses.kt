package com.batch14.user_management_service.domain.dto.response

import java.util.UUID

data class BaseResponses<T>(
    val reqId: UUID? = UUID.randomUUID(),
    val status: String = "T",
    val message: String? = "Berhasil",
    val error: Any? = null,
    val data: T? = null
)
