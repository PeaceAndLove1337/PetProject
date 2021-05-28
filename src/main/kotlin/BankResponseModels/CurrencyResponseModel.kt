package BankResponseModels

import data.Bank

interface CurrencyResponseModel{
    val bank:Bank
    val updateTime:String
}