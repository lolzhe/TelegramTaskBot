package com.sytindn.schedulertelegrambot.repository

import com.sytindn.schedulertelegrambot.models.Goal
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GoalRepository : CrudRepository<Goal?, Long> {
}