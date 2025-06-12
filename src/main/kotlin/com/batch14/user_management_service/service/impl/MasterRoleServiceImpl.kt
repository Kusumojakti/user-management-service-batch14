package com.batch14.user_management_service.service.impl

import com.batch14.user_management_service.domain.dto.response.RestGetAllRoleDto
import com.batch14.user_management_service.repository.MasterRoleRepository
import com.batch14.user_management_service.service.MasterRoleService
import org.springframework.stereotype.Service

@Service
class MasterRoleServiceImpl(
    private val masterRoleRepository: MasterRoleRepository
): MasterRoleService {
    override fun getAllRole(): List<RestGetAllRoleDto> {
        val rawRole = masterRoleRepository.findAll()
        val result = mutableListOf<RestGetAllRoleDto>()
        rawRole.forEach { role ->
            result.add(
                RestGetAllRoleDto(
                    id = role.id,
                    name = role.name
                )
            )
        }
        return result
    }
}