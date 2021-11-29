package com.sytindn.schedulertelegrambot.controllers.userStateControllers.userStateControllersImpl

import com.sytindn.schedulertelegrambot.controllers.userStateControllers.IUserStateController
import com.sytindn.schedulertelegrambot.models.User
import com.sytindn.schedulertelegrambot.service.AppService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.validation.Validation

class FillsBirthdayStateController : IUserStateController {

    companion object {
        const val DATABASE_STATE_NAME = "Fills birthday"
    }

    override fun handleMessage(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        val timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val birthday: LocalDate
        try {
            birthday = LocalDate.parse(update.message.text, timeFormatter)
        } catch (ex: DateTimeParseException) {
            sendMessageBuilder.text("Дата рождения должна быть в формате dd.mm.yyyy")
            return
        }

        user?.birthDate = birthday
        val validator = Validation.buildDefaultValidatorFactory().validator
        val constraintViolations = validator.validateProperty(user, "birthDate")
        if (constraintViolations.isEmpty())
        {
            setState(user, appService, DefaultUserStateController.DATABASE_STATE_NAME)
            appService.saveUser(user!!)

            sendMessageBuilder.text("Вы зарегистрированы, ${user.name}. Для создания новой задачи используйте команду /create")
        } else {
            sendMessageBuilder.text("Дата рождения не должна быть в будущем")
        }
    }
}