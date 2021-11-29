package com.sytindn.schedulertelegrambot.repository

import com.sytindn.schedulertelegrambot.models.UserState
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserStateRepository : CrudRepository<UserState?, Long> {

    fun findByStateName(stateName: String) : UserState?
}