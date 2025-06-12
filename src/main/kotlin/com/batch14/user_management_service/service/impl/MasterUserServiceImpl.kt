package com.batch14.user_management_service.service.impl

import com.batch14.user_management_service.domain.dto.request.RegRegisterDto
import com.batch14.user_management_service.domain.dto.request.ReqLoginDto
import com.batch14.user_management_service.domain.dto.response.ResLoginDto
import com.batch14.user_management_service.domain.dto.response.RestGetAllUserGto
import com.batch14.user_management_service.domain.dto.response.RestGetUserByIdDto
import com.batch14.user_management_service.domain.entity.MasterUserEntity
import com.batch14.user_management_service.exception.CustomException
import com.batch14.user_management_service.repository.MasterRoleRepository
import com.batch14.user_management_service.repository.MasterUserRepository
import com.batch14.user_management_service.service.MasterUserService
import com.batch14.user_management_service.utils.BCryptUtil
import com.batch14.user_management_service.utils.JwtUtils
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class MasterUserServiceImpl (
    private val masterUserRepository: MasterUserRepository,
    private val masterRoleRepository: MasterRoleRepository,
    private val jwtUtil: JwtUtils,
    private val bcrypt: BCryptUtil

) : MasterUserService {
    override fun findAllActiveUsers(): List<RestGetAllUserGto> {
        val rawData = masterUserRepository.getAllActiveUser()
        val result = mutableListOf<RestGetAllUserGto>()
        rawData.forEach { u ->
            result.add(
                RestGetAllUserGto(
                    username = u.username,
                    id = u.id,
                    email = u.email,
                    roleId = u.role?.id,
                    roleName = u.role?.name
                )
            )
        }
        return result
    }

    override fun getUserById(id: Int): RestGetUserByIdDto {
        val user = masterUserRepository.findById(id)
            .orElseThrow{
                RuntimeException("User not found!")
            }

        return RestGetUserByIdDto(
            id = user.id,
            username = user.username,
            email = user.email,
            roleId = user.role?.id,
            roleName = user.role?.name
        )
    }

    override fun register(req: RegRegisterDto): RestGetAllUserGto {
        val role = if(req.roleId == null) {
            Optional.empty()
        } else {
            masterRoleRepository.findById(req.roleId!!)
        }

        val existingUserEmail = masterUserRepository.findFirstByEmail(req.email)
        if (existingUserEmail != null) {
            throw CustomException("Email already exists!", 400)
        }

        val existingUserUsername = masterUserRepository.findFirstByUsername(req.username)
        if (existingUserUsername != null) {
            throw CustomException("Username already exists!", 400)
        }

        val userRow = MasterUserEntity(
            email = req.email,
            password = req.password,
            username = req.username,
            role = if(role.isPresent ) {
                role.get()
            } else {
                null
            }
        )

        val user = masterUserRepository.save(userRow)
        return RestGetAllUserGto(
            id = user.id,
            email = user.email,
            username = user.username,
            roleId = user.role?.id,
            roleName = user.role?.name
        )
    }

    override fun login(req: ReqLoginDto): ResLoginDto {
        val userEntityOpt = masterUserRepository.findFirstByUsername(req.username)

        if (userEntityOpt.isEmpty) {
            throw CustomException("Username atau Password salah", 400)
        }

        val userEntity = userEntityOpt.get()

        if (!bcrypt.verify(req.password, userEntity.password)) {
            throw CustomException("Username atau Password salah", 400)
        }

        val role = if (userEntity.role != null) {
            userEntity.role!!.name
        } else {
            "user"
        }

        val token = jwtUtil.generateToken(userEntity.id, role)

        return ResLoginDto(token)
    }
}