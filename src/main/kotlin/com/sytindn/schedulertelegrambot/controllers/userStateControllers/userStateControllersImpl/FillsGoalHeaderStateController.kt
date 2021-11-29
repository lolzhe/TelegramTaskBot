package com.sytindn.schedulertelegrambot.controllers.userStateControllers.userStateControllersImpl

import com.sytindn.schedulertelegrambot.controllers.userStateControllers.IUserStateController
import com.sytindn.schedulertelegrambot.models.Goal
import com.sytindn.schedulertelegrambot.models.User
import com.sytindn.schedulertelegrambot.service.AppService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import javax.validation.Validation

class FillsGoalHeaderStateController : IUserStateController {

    companion object {
        const val DATABASE_STATE_NAME = "Fills goal header"
    }

    override fun handleMessage(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        if (update.message.text == "/cancel")
        {
            cancelCommand(update, sendMessageBuilder, user, appService)
            return;
        }
        val newGoal = Goal()
        newGoal.header = update.message.text
        val validator = Validation.buildDefaultValidatorFactory().validator
        val constraintViolations = validator.validateProperty(newGoal, "header")
        if (constraintViolations.isEmpty())
        {
            if (user!!.goals == null)
                user.goals = mutableListOf(newGoal)
            else
                user.goals!!.add(newGoal)
            newGoal.user = user
            setState(user, appService, FillsGoalContentStateController.DATABASE_STATE_NAME)
            appService.saveUser(user)
            sendMessageBuilder.text("Введите текст задачи")
        } else {
            sendMessageBuilder.text("Название задачи обязательно и должно содержать не больше 50 символов")
        }
    }

    override fun cancelCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        setState(user, appService, DefaultUserStateController.DATABASE_STATE_NAME)
        appService.saveUser(user!!)
        sendMessageBuilder.text("Создание задания отменено")
    }
}