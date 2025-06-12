package com.batch14.user_management_service.repository

import com.batch14.user_management_service.domain.entity.MasterUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface MasterUserRepository: JpaRepository<MasterUserEntity, Int> {
    @Query( """
        
        SELECT U FROM MasterUserEntity U
        WHERE U.isDelete = false
        AND U.isActive = true
    
    """
    )

    fun getAllActiveUser(): List<MasterUserEntity>

    fun findFirstByEmail(email: String): MasterUserEntity?

    fun findFirstByUsername(username: String): Optional<MasterUserEntity>
}