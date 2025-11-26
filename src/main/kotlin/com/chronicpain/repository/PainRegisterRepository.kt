package com.chronicpain.repository

import com.chronicpain.domain.model.PainRegister
import org.springframework.data.jpa.repository.JpaRepository

interface PainRegisterRepository : JpaRepository<PainRegister, Long?> {

    fun findByUserId(userId: Long): List<PainRegister>

}