package com.sytindn.schedulertelegrambot.controllers.userStateControllers.userStateControllersImpl

import com.sytindn.schedulertelegrambot.controllers.userStateControllers.IUserStateController
import com.sytindn.schedulertelegrambot.models.User
import com.sytindn.schedulertelegrambot.service.AppService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.validation.Validation

class FillsGoalPlannedDateStateController : IUserStateController {

    companion object {
        const val DATABASE_STATE_NAME = "Fills goal planned date"
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

        val args = update.message.text.split(' ', limit = 3)

        var dateTime: LocalDateTime? = null
        try {
            val date = LocalDate.parse(args[0], DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            val time = LocalTime.of(23, 59)
            dateTime = LocalDateTime.of(date, time)
        }
        catch(e: DateTimeParseException) {
            val dateString = args.getOrNull(1)
            if (dateString != null)
                try {
                    val time = LocalTime.parse(args[0], DateTimeFormatter.ofPattern("HH:mm"))
                    val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    dateTime = LocalDateTime.of(date, time)
                } catch (e: DateTimeParseException) { }
        }
        if (dateTime == null) {
            sendMessageBuilder.text("Дата должна быть корректной, формат даты |dd.MM.yyyy| либо |hh:mm dd.MM.yyyy|")
            return
        }

        val goal = user!!.goals!!.first{it!!.plannedFinishDate == null}
        goal!!.plannedFinishDate = dateTime
        val validator = Validation.buildDefaultValidatorFactory().validator
        val constraintViolations = validator.validateProperty(goal, "plannedFinishDate")
        if (constraintViolations.isEmpty())
        {
            setState(user, appService, DefaultUserStateController.DATABASE_STATE_NAME)
            appService.saveUser(user)
            sendMessageBuilder.text("*${goal.header}*\n\n${goal.content ?: "Пусто"}\n\nДо ${goal.plannedFinishDate}")
        } else {
            sendMessageBuilder.text("Планируемое время выполнения задачи не может быть в прошлом")
        }
    }

    override fun cancelCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        val goal = user!!.goals!!.first{it.plannedFinishDate == null}
        goal.user = null
        user.goals!!.remove(goal)
        setState(user, appService, DefaultUserStateController.DATABASE_STATE_NAME)
        appService.saveUser(user)
        sendMessageBuilder.text("Создание задания отменено")
    }
}