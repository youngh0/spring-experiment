package com.example.experiment.kotlin

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
open class User(
        @Id
        @GeneratedValue
        val id: Long? = null,

        val name: String
) {
}
