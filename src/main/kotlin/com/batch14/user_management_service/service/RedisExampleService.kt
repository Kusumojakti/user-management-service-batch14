package com.batch14.user_management_service.service

interface RedisExampleService {
    fun set(userId: Int): String

    fun get(userId: Int): String
}