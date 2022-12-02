package com.abcd.hellokmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform