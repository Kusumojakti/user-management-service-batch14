package com.batch14.user_management_service.service

import com.batch14.user_management_service.domain.dto.response.RestGetAllRoleDto

interface MasterRoleService {
    fun getAllRole(): List<RestGetAllRoleDto>
}