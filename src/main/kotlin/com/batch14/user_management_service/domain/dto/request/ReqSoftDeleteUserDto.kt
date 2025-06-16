package com.batch14.user_management_service.domain.dto.request

data class ReqSoftDeleteUserDto(
    val id: Int,
    val isDelete: Boolean
)
