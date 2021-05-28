package BankResponseModels

import data.Bank

data class VtbResponseModel(
    override val bank: Bank,
    override val updateTime: String,
    val description:String,
    val isoCur:String,
    val fullNameInRussian:String,
    override val buyCost:Double,
    override val sellCost:Double,

    ):CommercialBankCurrencyResponseModel
