package com.batch14.user_management_service.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
@Table(name = "mst_roles")
data class MasterRoleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue(
//        strategy = GenerationType.SEQUENCE, generator = "mst_roles_id_seq"
//    )
//    @SequenceGenerator(
//        name = "mst_roles_id_seq",
//        sequenceName = "mst_roles_id_seq",
//        allocationSize = 1
//    )
    @Column(name = "id")
    var id: Int,

    @Column(name = "name")
    var name: String,

    @Column(name = "created_by")
    var createdBy: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", insertable = false, updatable = false)
    var createdAt: Timestamp,

    @Column(name = "updated_by")
    var updatedBy: String? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false, updatable = false)
    var updatedAt: Timestamp,

    @Column(name = "deleted_at")
    var deletedAt: Timestamp? = null,

    @Column(name = "deleted_by")
    var deletedBy: String? = null,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @Column(name = "is_delete")
    var isDelete: Boolean = false
)
