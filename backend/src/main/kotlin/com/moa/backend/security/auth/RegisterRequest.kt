package com.moa.backend.security.auth

import net.bytebuddy.asm.Advice.AllArguments

data class RegisterRequest (

    var firstName: String,

    var lastName: String,

    var email: String,

    var password: String,
)