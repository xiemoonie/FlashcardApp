package com.example.czechapp

import kotlinx.serialization.Serializable


@Serializable
data class CardList constructor(val listCard : ArrayList<Card>)


