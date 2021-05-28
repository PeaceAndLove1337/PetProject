package BankResponseModels

import data.Bank

data class CbResponseModel(
    override val bank: Bank,
    override val updateTime: String,
    val isoCur: String,
    val nominal: Int,
    val nameInRussian: String,
    val recommendedCost: Double,

    ) : CurrencyResponseModel
