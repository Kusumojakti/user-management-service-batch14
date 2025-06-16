package com.batch14.user_management_service.controller

import com.batch14.user_management_service.domain.dto.request.RegRegisterDto
import com.batch14.user_management_service.domain.dto.request.ReqLoginDto
import com.batch14.user_management_service.domain.dto.request.ReqUpdateUserDto
import com.batch14.user_management_service.domain.dto.response.BaseResponses
import com.batch14.user_management_service.domain.dto.response.ResLoginDto
import com.batch14.user_management_service.domain.dto.response.RestGetAllUserGto
import com.batch14.user_management_service.domain.dto.response.RestGetUserByIdDto
import com.batch14.user_management_service.service.MasterUserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController (
    private val masterUserService: MasterUserService
) {
    @GetMapping("/active")
    fun getAllActiveUsers(): ResponseEntity<
            BaseResponses<List<RestGetAllUserGto>>>{
        return ResponseEntity.ok(
            BaseResponses(
                data = masterUserService.findAllActiveUsers()
            )
//            masterUserService.findAllActiveUsers()
        )
    }

    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Int
    ): RestGetUserByIdDto {
            return masterUserService.getUserById(id)
    }

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody req: RegRegisterDto
    ) : ResponseEntity<BaseResponses<RestGetAllUserGto>> {
        val user = masterUserService.register(req)
        return ResponseEntity(
            BaseResponses(
                data = user,
                message = "User registered successfully."
            ),
            HttpStatus.CREATED
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody req: ReqLoginDto
    ): ResponseEntity<BaseResponses<ResLoginDto>> {
        return ResponseEntity(
            BaseResponses(
                data = masterUserService.login(req),
                message = "Login successful."
            ),
            HttpStatus.OK
        )
    }

    @PutMapping()
    fun updateUser(
        @RequestBody req: ReqUpdateUserDto
    ): ResponseEntity<BaseResponses<RestGetAllUserGto>>{
        return ResponseEntity.ok(
            BaseResponses(
                data = masterUserService.updateUser(req)
            )
        )
    }

    @PutMapping("/{id}/soft-delete")
    fun softDeleteUser(
        @PathVariable id: Int
    ): ResponseEntity<BaseResponses<RestGetAllUserGto>> {
        val user = masterUserService.softDeleteUser(id)
        return ResponseEntity.ok(
            BaseResponses(
                data = user,
                message = "User soft deleted successfully."
            )
        )
    }

    @DeleteMapping("/{id}/hard-delete")
    fun hardDeleteUser(
        @PathVariable id: Int,
    ): ResponseEntity<BaseResponses<String>> {
        masterUserService.hardDeleteUser(id)
        return ResponseEntity.ok(
            BaseResponses(
                data = "User hard deleted successfully.",
                message = "Success"
            )
        )
    }


}