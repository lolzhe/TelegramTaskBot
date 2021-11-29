package com.sytindn.schedulertelegrambot.models

import org.springframework.validation.annotation.Validated
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Past
import javax.validation.constraints.Size

@Entity
@Validated
@Table(name = "Users")
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "userState_id")
    open var userState: UserState? = null

    @Column(unique = true)
    open var chatId: String? = null

    @NotBlank
    @Size(min = 2, max = 90)
    open var name: String? = null

    @Past
    open var birthDate: LocalDate? = null

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("plannedFinishDate")
    open var goals: MutableList<Goal>? = null

}