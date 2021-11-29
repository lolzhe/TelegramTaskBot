package com.sytindn.schedulertelegrambot.models

import javax.persistence.*

@Entity
@Table(name = "UserStates")
open class UserState() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    //таблица возможных состояний должна быть инициализирована заранее
    @Column(unique = true)
    open var stateName: String? = null


    constructor(stateName: String) : this(){
        this.stateName = stateName
    }

}