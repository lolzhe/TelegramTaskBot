package com.sytindn.schedulertelegrambot.controllers.userStateControllers.userStateControllersImpl

import com.sytindn.schedulertelegrambot.controllers.userStateControllers.IUserStateController
import com.sytindn.schedulertelegrambot.models.User
import com.sytindn.schedulertelegrambot.service.AppService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.lang.Math.abs
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import javax.validation.Validation

class DefaultUserStateController : IUserStateController {

    companion object {
        const val DATABASE_STATE_NAME = "Default state"
    }

    override fun handleMessage(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        val command = update.message.text.split(' ', limit =  2).first()

        when (command) {
            "/start" -> startCommand(update, sendMessageBuilder, user, appService)
            "/goals" -> goalsCommand(update, sendMessageBuilder, user, appService)
            "/create" -> createCommand(update, sendMessageBuilder, user, appService)
            "/goal" -> goalCommand(update, sendMessageBuilder, user, appService)
            "/delete" -> deleteCommand(update, sendMessageBuilder, user, appService)
            "/finish" -> finishCommand(update, sendMessageBuilder, user, appService)
            "/account" -> accountCommand(update, sendMessageBuilder, user, appService)
            "/killme" -> killMeCommand(update, sendMessageBuilder, user, appService)
            "/cancel" -> sendMessageBuilder.text("Отменять нечего")
            else -> sendMessageBuilder.text("Не понял")
        }
    }

    override fun startCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        sendMessageBuilder.text("Набор доступных вам команд: \n\n" +
                "/goals - показать все невыполненные задачи\n" +
                "/create - создать новую задачу\n" +
                "/goal |номер задачи| - показать задачу\n" +
                "/delete |номер задачи| - удалить задачу\n" +
                "/finish |номер задачи| \"сейчас\", либо |dd.mm.yyyy| или |hh:mm dd.mm.yyyy| " +
                "- завершить задачу сейчас или в желаемый момент\n\n" +
                "/cancel - отменить выполняемую операцию" +
                "/account - изменить данные аккаунта\n" +
                "/killme - удалить аккаунт")
    }

    override fun goalsCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        if (user!!.goals == null || user.goals!!.isEmpty()) {
            sendMessageBuilder.text("У вас нет невыполненных задач")
        } else {
            val goalsStringBuilder = StringBuilder()
            for (i in 0 until user.goals!!.size)
                goalsStringBuilder.append("${i+1}. ${user.goals!![i].header!!} до ${user.goals!![i].plannedFinishDate}\n")
            sendMessageBuilder.text("Список невыполненых задач:\n\n$goalsStringBuilder")
        }
    }

    override fun createCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        setState(user, appService, FillsGoalHeaderStateController.DATABASE_STATE_NAME)
        appService.saveUser(user!!)
        sendMessageBuilder.text("Как называется ваша задача?")
    }

    override fun goalCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        val goalNumber = update.message.text.split(' ', limit = 3).getOrNull(1)?.toIntOrNull()
        if (goalNumber != null) {
            if (0 < goalNumber && goalNumber <= (user!!.goals?.size ?: 0)) {
                val goal = user.goals!![goalNumber - 1]
                sendMessageBuilder.text("*$goalNumber. ${goal.header}*\n\n${goal.content ?: "Пусто"}\n\nДо ${goal.plannedFinishDate}")
            } else
                sendMessageBuilder.text("Задачи под таким номером нет")
        } else {
            sendMessageBuilder.text("Синтаксис команды /goal |номер задачи|")
        }
    }

    override fun deleteCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        val goalNumber = update.message.text.split(' ', limit = 3).getOrNull(1)?.toIntOrNull()
        if (goalNumber != null) {
            if (0 < goalNumber && goalNumber <= (user!!.goals?.size ?: 0)) {
                val goal = user.goals!![goalNumber - 1]
                goal!!.user = null
                user.goals!!.removeAt(goalNumber - 1)
                //appService.deleteGoalById(goal.id!!)
                appService.saveUser(user)
                //appService.deleteGoalById(user.goals!![goalNumber - 1].id!!)
                //appService.deleteGoal(goal!!)
                sendMessageBuilder.text("Вы удалили $goalNumber задание")
            } else
                sendMessageBuilder.text("Задачи под таким номером нет")
        } else {
            sendMessageBuilder.text("Синтаксис команды /delete |номер задачи|")
        }
    }

    override fun finishCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        val args = update.message.text.split(' ').drop(1)

        val goalNumber = args.getOrNull(0)?.toIntOrNull()
        if (!(goalNumber != null && 0 < goalNumber && goalNumber <= (user!!.goals?.size ?: 0))) {
            sendMessageBuilder.text("Команда введена неверно или такого задания не существует\n" +
                    "/finish |номер задачи| \"сейчас\", либо |dd.mm.yyyy| или |hh:mm dd.mm.yyyy|")
            return
        }

        //время может быть либо сейчас либо |dd.MM.yyyy| либо |hh:mm dd.MM.yyyy|
        val hoursOrDateString = args.getOrNull(1)
        var dateTime: LocalDateTime? = null
        when (hoursOrDateString) {
            "сейчас" -> dateTime = LocalDateTime.now()
            null -> sendMessageBuilder.text("Команда не распознана\n" +
                    "/finish |номер задачи| \"сейчас\", либо |dd.mm.yyyy| или |hh:mm dd.mm.yyyy|")
            else -> {
                try {
                    val date = LocalDate.parse(hoursOrDateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    val time = LocalTime.of(0, 0)
                    dateTime = LocalDateTime.of(date, time)
                }
                catch(e: DateTimeParseException) {
                    val dateString = args.getOrNull(2)
                    if (dateString != null)
                        try {
                            val time = LocalTime.parse(hoursOrDateString, DateTimeFormatter.ofPattern("HH:mm"))
                            val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                            dateTime = LocalDateTime.of(date, time)
                        }
                        catch(e: DateTimeParseException){ }
                }
            }
        }
        if (dateTime == null) {
            sendMessageBuilder.text(
                "Дата не распознана\n" +
                        "/finish |номер задачи| |сейчас|, либо |dd.mm.yyyy| или |hh:mm dd.mm.yyyy|")
            return
        }
        val goal = user.goals?.get(goalNumber - 1)!!
        goal.actualFinishDate = dateTime
        val validator = Validation.buildDefaultValidatorFactory().validator
        val constraintViolations = validator.validateProperty(goal, "actualFinishDate")
        if (constraintViolations.isEmpty())
        {
            appService.saveGoal(goal)
            val period = Period.between(goal.actualFinishDate?.toLocalDate(), goal.plannedFinishDate?.toLocalDate())
            if (period.isZero) {
                val duration =
                    Duration.between(goal.actualFinishDate?.toLocalTime(), goal.plannedFinishDate?.toLocalTime())
                if (duration.isZero)
                    sendMessageBuilder.text("Вы выполнили задачу $goalNumber. ${goal.header}" +
                            " в запланированное время")
                else
                    if (period.isNegative)
                        sendMessageBuilder.text("Вы выполнили задачу $goalNumber. ${goal.header}" +
                                " с опозданием в ${kotlin.math.abs(duration.toHours())} часов, " +
                                "${kotlin.math.abs(duration.toMinutes())} минут")
                    else
                        sendMessageBuilder.text("Вы выполнили задачу $goalNumber. ${goal.header}" +
                                " раньше запланированного на ${duration.toHours()} часов, ${duration.toMinutesPart()} минут")
            }
            else {
                val days = kotlin.math.abs(ChronoUnit.DAYS.between(goal.actualFinishDate?.toLocalDate(), goal.plannedFinishDate?.toLocalDate()))
                if (period.isNegative)
                    sendMessageBuilder.text(
                        "Вы выполнили задачу $goalNumber. ${goal.header}" +
                                " с опозданием в $days дней"
                    )
                else
                    sendMessageBuilder.text(
                        "Вы выполнили задачу $goalNumber. ${goal.header}" +
                                " раньше запланированого на $days дней"
                    )
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////
            /////по условию задачи кажется, что даже выполненные задачи нужно сохранять в БД, поэтому у entity задачи есть
            /////дата её завершения. Однако задача всё равно нигде больше не используется поэтому пусть удаляется тут
            user.goals!!.remove(goal)
            goal.user = null
            appService.saveUser(user)
            ////////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////////////////////////////////

        } else {
            sendMessageBuilder.text("Время завершения задачи не должно быть в будущем")
        }
    }

    override fun accountCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        setState(user, appService, FillsUsernameStateController.DATABASE_STATE_NAME)
        appService.saveUser(user!!)
        sendMessageBuilder.text("Как вас зовут?")
    }

    override fun killMeCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    ) {
        setState(user, appService, RequestsDeleteHimselfStateController.DATABASE_STATE_NAME)
        appService.saveUser(user!!)
        sendMessageBuilder.text("Вы уверены что хотите удалить все свои данные? Напишите *ДА* заглавными буквами если уверены" +
                "\n\nЭто действие невозможно отменить")
    }

}