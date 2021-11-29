package com.sytindn.schedulertelegrambot.controllers.userStateControllers.userStateControllersImpl

import com.sytindn.schedulertelegrambot.controllers.userStateControllers.IUserStateController
import com.sytindn.schedulertelegrambot.models.Goal
import com.sytindn.schedulertelegrambot.models.User
import com.sytindn.schedulertelegrambot.service.AppService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import javax.validation.Validation

class FillsGoalContentStateController : IUserStateController {

    companion object {
        const val DATABASE_STATE_NAME = "Fills goal content"
    }

    override fun handleMessage(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        if (update.message.text == "/cancel") {
            cancelCommand(update, sendMessageBuilder, user, appService)
            return
        }
        val goal = user!!.goals!!.first{it.content == null}
        goal!!.content = update.message.text
        val validator = Validation.buildDefaultValidatorFactory().validator
        val constraintViolations = validator.validateProperty(goal, "content")
        if (constraintViolations.isEmpty())
        {
            setState(user, appService, FillsGoalPlannedDateStateController.DATABASE_STATE_NAME)
            appService.saveUser(user)
            sendMessageBuilder.text("Введите планируемую дату выполнения задачи в формате |dd.MM.yyyy| " +
                    "либо |hh:mm dd.MM.yyyy|")
        } else {
            sendMessageBuilder.text("Содержание задачи должно быть длинной не более 500 символов")
        }
    }

    override fun cancelCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        val goal = user!!.goals!!.first{it.content == null}
        goal.user = null
        user.goals!!.remove(goal)
        setState(user, appService, DefaultUserStateController.DATABASE_STATE_NAME)
        appService.saveUser(user)
        sendMessageBuilder.text("Создание задания отменено")
    }

}