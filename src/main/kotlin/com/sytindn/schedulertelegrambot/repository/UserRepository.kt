package com.sytindn.schedulertelegrambot.repository

import com.sytindn.schedulertelegrambot.models.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User?, Long> {
    fun findByChatId(chatId: String) : User?
}