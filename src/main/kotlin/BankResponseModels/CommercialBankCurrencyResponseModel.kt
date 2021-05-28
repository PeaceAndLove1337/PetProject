package BankResponseModels

interface CommercialBankCurrencyResponseModel :CurrencyResponseModel{
    val buyCost:Double
    val sellCost:Double
}