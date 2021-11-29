package com.sytindn.schedulertelegrambot.controllers.userStateControllers.userStateControllersImpl

import com.sytindn.schedulertelegrambot.controllers.userStateControllers.IUserStateController
import com.sytindn.schedulertelegrambot.models.User
import com.sytindn.schedulertelegrambot.service.AppService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class UnregisteredUserStateController : IUserStateController {

    override fun handleMessage(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        if (update.message.text == "/start")
            startCommand(update, sendMessageBuilder, user, appService)
        else
            sendMessageBuilder.text("Для начала работы напишите /start")
    }

    override fun startCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        val newUser = User()
        setState(newUser, appService, FillsUsernameStateController.DATABASE_STATE_NAME)
        newUser.chatId = update.message.chatId.toString()
        appService.saveUser(newUser)

        sendMessageBuilder.text("Как вас зовут?")
    }
}