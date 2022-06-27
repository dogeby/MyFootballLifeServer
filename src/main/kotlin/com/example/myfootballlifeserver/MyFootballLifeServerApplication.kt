package com.example.myfootballlifeserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class MyFootballLifeServerApplication

fun main(args: Array<String>) {
	runApplication<MyFootballLifeServerApplication>(*args)
}
