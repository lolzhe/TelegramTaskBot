package com.sytindn.schedulertelegrambot.models

import org.springframework.validation.annotation.Validated
import javax.persistence.*
import java.time.LocalDateTime
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Size

@Entity
@Validated
@Table(name = "Goals")
open class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    open var user: User? = null

    @NotBlank
    @Size(min = 3, max = 50)
    open var header: String? = null

    @Size(max = 500)
    open var content: String? = null

    @Future
    open var plannedFinishDate: LocalDateTime? = null

    @PastOrPresent
    open var actualFinishDate: LocalDateTime? = null

}