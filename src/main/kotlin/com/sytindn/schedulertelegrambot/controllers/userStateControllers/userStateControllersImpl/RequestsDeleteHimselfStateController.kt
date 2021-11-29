package com.sytindn.schedulertelegrambot.controllers.userStateControllers.userStateControllersImpl

import com.sytindn.schedulertelegrambot.controllers.userStateControllers.IUserStateController
import com.sytindn.schedulertelegrambot.models.User
import com.sytindn.schedulertelegrambot.service.AppService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

class RequestsDeleteHimselfStateController : IUserStateController {

    companion object {
        const val DATABASE_STATE_NAME = "Requests delete himself"
    }

    override fun handleMessage(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        if (update.message.text == "ДА") {
            appService.deleteUser(user!!)
            sendMessageBuilder.text("Все связанные с вами записи удалены")
        } else {
            setState(user, appService, DefaultUserStateController.DATABASE_STATE_NAME)
            appService.saveUser(user!!)
            sendMessageBuilder.text("Вы не написали *ДА*, удаление не произошло")
        }
    }

}