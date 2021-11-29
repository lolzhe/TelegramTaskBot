package com.sytindn.schedulertelegrambot.controllers.userStateControllers

import com.sytindn.schedulertelegrambot.controllers.userStateControllers.userStateControllersImpl.FillsBirthdayStateController
import com.sytindn.schedulertelegrambot.models.User
import com.sytindn.schedulertelegrambot.service.AppService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

interface IUserStateController {

     fun handleMessage(
            update: Update,
            sendMessageBuilder: SendMessage.SendMessageBuilder,
            user: User?,
            appService: AppService
        )

     fun setState(user: User?, appService: AppService, stateName: String)
     {
         val userState = appService.findUserStateByName(stateName)
         user?.userState = userState
     }

    fun startCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    )
    {
        throw NotImplementedError("startCommand not implemented")
    }

    fun goalsCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    )
    {
        throw NotImplementedError("goalsCommand not implemented")
    }

    fun createCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    )
    {
        throw NotImplementedError("createCommand not implemented")
    }

    fun goalCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    )
    {
        throw NotImplementedError("goalCommand not implemented")
    }

    fun deleteCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    )
    {
        throw NotImplementedError("deleteCommand not implemented")
    }

    fun finishCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    )
    {
        throw NotImplementedError("finishCommand not implemented")
    }

    fun accountCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    )
    {
        throw NotImplementedError("accountCommand not implemented")
    }

    fun killMeCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    )
    {
        throw NotImplementedError("killMeCommand not implemented")
    }

    fun cancelCommand(
        update: Update,
        sendMessageBuilder: SendMessage.SendMessageBuilder,
        user: User?,
        appService: AppService
    )
    {
        throw NotImplementedError("killMeCommand not implemented")
    }
}