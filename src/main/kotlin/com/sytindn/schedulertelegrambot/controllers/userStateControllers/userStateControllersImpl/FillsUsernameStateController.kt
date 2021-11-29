package com.sytindn.schedulertelegrambot.controllers.userStateControllers.userStateControllersImpl

import com.sytindn.schedulertelegrambot.controllers.userStateControllers.IUserStateController
import com.sytindn.schedulertelegrambot.models.User
import com.sytindn.schedulertelegrambot.service.AppService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import javax.validation.Validation

class FillsUsernameStateController : IUserStateController {

    companion object {
        const val DATABASE_STATE_NAME = "Fills username"
    }

    override fun handleMessage(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        user?.name = update.message.text

        val validator = Validation.buildDefaultValidatorFactory().validator
        val constraintViolations = validator.validateProperty(user, "name")
        if (constraintViolations.isEmpty()) {
            setState(user, appService, FillsBirthdayStateController.DATABASE_STATE_NAME)
            appService.saveUser(user!!)
            sendMessageBuilder.text("Введите дату рождения в формате dd.mm.yyyy")
        } else {
            sendMessageBuilder.text("Имя должно содержать не менее 2 символов")
        }

    }
}