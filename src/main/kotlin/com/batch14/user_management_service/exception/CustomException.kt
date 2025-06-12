package com.batch14.user_management_service.exception

class CustomException(
    val exceptionMessage: String?,
    val statusCode: Int,
    val data: Any? = null
) : RuntimeException()