package com.batch14.user_management_service.service.impl

import com.batch14.user_management_service.exception.CustomException
import com.batch14.user_management_service.repository.MasterUserRepository
import com.batch14.user_management_service.service.RedisExampleService
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisExampleServiceImpl (
    private val stringRedisTemplate: StringRedisTemplate,
    private val masterUserRepository: MasterUserRepository
): RedisExampleService {
    override fun set(userId: Int): String {
       val user = masterUserRepository.findById(userId).orElseThrow {
           throw CustomException("User not found", 404)
       }

        val operations = stringRedisTemplate.opsForValue()
        operations.set("user-service:user:name ${user.id}", user.username, Duration.ofMinutes(10))

        return "User name ${user.id} has been set in Redis with value: ${user.username}"
    }

    override fun get(userId: Int): String {
        val user = masterUserRepository.findById(userId).orElseThrow {
            throw CustomException("User not found", 404)
        }

        val operationString = stringRedisTemplate.opsForValue()

        val name = operationString.get("user-service:user:name ${user.id}")
            ?: throw CustomException("User name not found in Redis", 404)

        return "User ID: ${user.id}, name: $name"
    }

}