package com.sytindn.schedulertelegrambot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix="bot")
data class BotConfig(val username : String, val token : String)