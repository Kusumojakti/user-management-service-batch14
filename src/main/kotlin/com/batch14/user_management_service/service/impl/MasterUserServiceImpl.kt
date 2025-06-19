package com.batch14.user_management_service.service.impl

import com.batch14.user_management_service.domain.constant.Constant
import com.batch14.user_management_service.domain.dto.request.RegRegisterDto
import com.batch14.user_management_service.domain.dto.request.ReqLoginDto
import com.batch14.user_management_service.domain.dto.request.ReqUpdateUserDto
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
import jakarta.servlet.http.HttpServletRequest
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class MasterUserServiceImpl (
    private val masterUserRepository: MasterUserRepository,
    private val masterRoleRepository: MasterRoleRepository,
    private val jwtUtil: JwtUtils,
    private val bcrypt: BCryptUtil,
    private val httpServletRequest: HttpServletRequest

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

    @Cacheable(
        "getUserById",
        key = "{#id}"
    )
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
        if (existingUserUsername.isPresent) {
            throw CustomException("Username already exists!", 400)
        }

        val hashPw = bcrypt.hash(req.password)

        val userRow = MasterUserEntity(
            email = req.email,
            password = hashPw,
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

    override fun updateUser(
        req: ReqUpdateUserDto
    ): RestGetAllUserGto {
        val userId = httpServletRequest.getHeader(Constant.HEADER_USER_ID)

        val user = masterUserRepository.findById(userId.toInt()).orElseThrow{
            throw CustomException("User ID $userId not found",
                HttpStatus.BAD_REQUEST.value())
        }

        var existingUser = masterUserRepository.findFirstByUsername(req.username)
        if (existingUser.isPresent) {
            if (existingUser.get().id != user.id){
                throw CustomException(
                    "Username telah terdaftar",
                    HttpStatus.BAD_REQUEST.value()
                )
            }
        }

        val existingUserEmail = masterUserRepository.findFirstByEmail(req.email)
        if (existingUserEmail != null) {
            if (existingUserEmail.id == user.id){
                throw CustomException(
                    "Email telah terdaftar",
                    HttpStatus.BAD_REQUEST.value()
                )
            }
        }

        user.email = req.email
        user.username = req.username
        user.updatedBy = userId

        val result = masterUserRepository.save(user)

        return RestGetAllUserGto(
            id = result.id,
            username = result.username,
            email = result.email
        )
    }

    override fun softDeleteUser(id: Int): RestGetAllUserGto {
        val userRow = masterUserRepository.findById(id)
            .orElseThrow { Exception("User not found with id: $id") }

        userRow.isDelete = true
        masterUserRepository.save(userRow)

        val allUsers = masterUserRepository.findAll()
        return RestGetAllUserGto(
            id = userRow.id,
            username = userRow.username,
            email = userRow.email,
            roleId = userRow.role?.id,
            roleName = userRow.role?.name
        )
    }

    override fun hardDeleteUser(id: Int): RestGetAllUserGto {
        val userRole = httpServletRequest.getHeader(Constant.HEADER_USER_ROLE)
        if (userRole != "admin") {
            throw CustomException("You are not authorized to perform this action", HttpStatus.FORBIDDEN.value())
        } else {
            val userRow = masterUserRepository.findById(id)
                .orElseThrow { Exception("User not found with id: $id") }

            masterUserRepository.delete(userRow)

            return RestGetAllUserGto(
                id = userRow.id,
                username = userRow.username,
                email = userRow.email,
                roleId = userRow.role?.id,
                roleName = userRow.role?.name
            )
        }

//        val user = masterUserRepository.findById(userId.toInt()).orElseThrow{
//            throw CustomException("User ID $userId not found",
//                HttpStatus.BAD_REQUEST.value())
//        }

    }


}