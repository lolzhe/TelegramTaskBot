package com.sytindn.schedulertelegrambot.controllers

import com.sytindn.schedulertelegrambot.config.BotConfig
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class BotLongPollingController(private val botConfig: BotConfig,
                               private val userController: UserController
                               ) : TelegramLongPollingBot()
{

    override fun getBotToken() = botConfig.token;

    override fun getBotUsername() = botConfig.username;

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            print(botToken)
            var messageBuilder = SendMessage.builder()
            messageBuilder.parseMode("markdown")
            userController.handleMessage(update, messageBuilder)
            execute(messageBuilder.build())
        }

    }

}