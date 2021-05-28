package BankResponseModels

import data.Bank

data class TinkoffResponseCurrencyModel(
    override val bank: Bank,
    override val updateTime: String,
    val isoCur: String,
    override val buyCost: Double,
    override val sellCost: Double,
) : CommercialBankCurrencyResponseModel
