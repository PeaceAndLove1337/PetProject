package BankResponseModels

import data.Bank

data class SberbankResponseCurrencyModel(
    override val bank: Bank,
    override val updateTime: String,
    val isoCur:String,
    val nameInRussian:String,
    val nameInEnglish:String,
    override val buyCost:Double,
    override val sellCost:Double,
    // Вероятнее всего параметр отвечающий за давность обновления
):CommercialBankCurrencyResponseModel
