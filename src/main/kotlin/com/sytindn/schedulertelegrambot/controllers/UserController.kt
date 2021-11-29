package com.sytindn.schedulertelegrambot.controllers

import com.sytindn.schedulertelegrambot.controllers.userStateControllers.userStateControllersImpl.*
import com.sytindn.schedulertelegrambot.service.AppService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class UserController(private val appService: AppService) {
    fun handleMessage(update: Update, sendMessageBuilder: SendMessage.SendMessageBuilder) {
        val user = appService.findUserByChatId(update.message.chatId.toString())

        val userStateController = if (user == null) UnregisteredUserStateController()
        else
            when (user.userState?.stateName) {
                "Default state" -> DefaultUserStateController()
                "Fills username" -> FillsUsernameStateController()
                "Fills birthday" -> FillsBirthdayStateController()
                "Fills goal header" -> FillsGoalHeaderStateController()
                "Fills goal content" -> FillsGoalContentStateController()
                "Fills goal planned date" -> FillsGoalPlannedDateStateController()
                "Requests delete himself" -> RequestsDeleteHimselfStateController()

                null -> throw IllegalArgumentException("У инициализированного пользователя null state в БД")
                else -> throw IllegalArgumentException()
            }

        userStateController.handleMessage(update, sendMessageBuilder, user, appService)
        sendMessageBuilder.chatId(update.message.chatId.toString())
    }

}