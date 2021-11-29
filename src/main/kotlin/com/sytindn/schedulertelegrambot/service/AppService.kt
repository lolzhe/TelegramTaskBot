package com.sytindn.schedulertelegrambot.service

import com.sytindn.schedulertelegrambot.models.Goal
import com.sytindn.schedulertelegrambot.models.User
import com.sytindn.schedulertelegrambot.repository.*
import org.springframework.stereotype.Component

@Component
open class AppService (private val userRepository: UserRepository,
                       private val userStateRepository: UserStateRepository,
                       private val goalRepository: GoalRepository){

    fun findUserByChatId(chatId: String) = userRepository.findByChatId(chatId)
    fun saveUser(user: User) = userRepository.save(user)
    fun deleteUser(user: User) = userRepository.delete(user)

    fun findUserStateByName(stateName: String) = userStateRepository.findByStateName(stateName)

    fun saveGoal(goal: Goal) = goalRepository.save(goal)


}