package com.batch14.user_management_service.controller

import com.batch14.user_management_service.domain.dto.response.RestGetAllRoleDto
import com.batch14.user_management_service.service.MasterRoleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/roles")
class RoleController (
    private val masterRoleService: MasterRoleService
) {
    @GetMapping("/all")
    fun getAllRole(): ResponseEntity<List<RestGetAllRoleDto>?> {
        return ResponseEntity.ok(
            masterRoleService.getAllRole()
        )
    }
}