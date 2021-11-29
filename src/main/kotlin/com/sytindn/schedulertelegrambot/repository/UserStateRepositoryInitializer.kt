package com.sytindn.schedulertelegrambot.repository

import com.sytindn.schedulertelegrambot.controllers.userStateControllers.userStateControllersImpl.RequestsDeleteHimselfStateController
import com.sytindn.schedulertelegrambot.models.UserState
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class UserStateRepositoryInitializer (private val userStateRepository: UserStateRepository) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val count = userStateRepository.count()

        if (count == 0L) {
            val states = mutableListOf<UserState>(
                UserState("Default state"),
                UserState("Fills username"),
                UserState("Fills birthday"),
                UserState("Fills goal header"),
                UserState("Fills goal content"),
                UserState("Fills goal planned date"),
                UserState("Requests delete himself")
            )
            userStateRepository.saveAll(states)
        }
    }

}