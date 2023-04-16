package com.example.czechapp

import kotlinx.serialization.Serializable

@Serializable
data class Card constructor(val id: Int, val sentence : String?,val translation: String?)