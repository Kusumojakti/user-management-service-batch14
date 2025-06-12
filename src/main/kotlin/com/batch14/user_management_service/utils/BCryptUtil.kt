package com.batch14.user_management_service.utils

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class BCryptUtil {
    fun hash(password: String?): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(11))
    }

    fun verify(password: String?, hash: String?): Boolean {
        return BCrypt.checkpw(password, hash)
    }
}