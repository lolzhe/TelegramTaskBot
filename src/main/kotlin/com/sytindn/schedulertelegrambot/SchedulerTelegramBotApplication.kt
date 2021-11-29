package com.sytindn.schedulertelegrambot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
open class SchedulerTelegramBotApplication

fun main(args: Array<String>) {
	runApplication<SchedulerTelegramBotApplication>(*args)
}
