package BankResponseModels

import data.Bank

data class AlphabankResponseModel(
    override val bank: Bank,
    override val updateTime: String,
    val nameOnRussian: String,
    override val buyCost: Double,
    override val sellCost: Double
) : CommercialBankCurrencyResponseModel
