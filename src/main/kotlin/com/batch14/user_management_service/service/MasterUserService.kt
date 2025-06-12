package com.batch14.user_management_service.service

import com.batch14.user_management_service.domain.dto.request.RegRegisterDto
import com.batch14.user_management_service.domain.dto.request.ReqLoginDto
import com.batch14.user_management_service.domain.dto.response.ResLoginDto
import com.batch14.user_management_service.domain.dto.response.RestGetAllUserGto
import com.batch14.user_management_service.domain.dto.response.RestGetUserByIdDto
import com.batch14.user_management_service.domain.entity.MasterUserEntity

interface MasterUserService {
    fun findAllActiveUsers(): List<RestGetAllUserGto>

    fun getUserById(id: Int): RestGetUserByIdDto

    fun register(req: RegRegisterDto): RestGetAllUserGto

    fun login(Req: ReqLoginDto): ResLoginDto
}