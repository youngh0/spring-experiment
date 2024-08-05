package com.example.experiment.kotlin

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ServiceTest(val userRepo: UserRepo) {

    fun createUser(): Long {
        return 1L
    }
}
