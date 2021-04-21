/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

object IncomeSourceTypes {

  sealed trait IncomeSourceType

  object IncomeSourceTypeA extends IncomeSourceType {
    val INTEREST = "savings"
    val DIVIDENDS = "dividends"
    val GIFT_AID = "charity"
  }

  object IncomeSourceTypeB extends IncomeSourceType {
    val SELF_EMPLOYMENT = "self-employment"
    val UK_PROPERTY = "uk-property"
    val FHL_PROPERTY_EEA = "fhl-property-eea"
    val FHL_PROPERTY_UK = "fhl-property-uk"
    val EMPLOYMENT = "employment"
    val FOREIGN_INCOME = "foreign-income"
    val DIVIDENDS_FROM_FOREIGN_COMPANIES = "dividends-from-foreign-companies"
    val TRUSTS_AND_ESTATES = "trusts-and-estates"
    val INTEREST_FROM_UK_BANKS = "interest-from-uk-banks"
    val DIVIDENDS_UK = "dividends-uk"
    val UK_PENSION_BENEFITS = "uk-pension-benefits"
    val GAINS_ON_LIFE_INSURANCE_POLICIES = "gains-on-life-insurance-policies"
    val SHARE_SCHEMES = "share-schemes"
    val PARTNERSHIP = "partnership"
    val RELIEF_FOR_CHARITY = "relief-for-charity"
    val OTHER_INCOME = "other-income"
  }
}
