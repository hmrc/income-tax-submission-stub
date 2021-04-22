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

  //noinspection TypeAnnotation
  object IncomeSourceTypeB extends Enumeration with IncomeSourceType {
    type IncomeSourceTypeB = Value
    val SELF_EMPLOYMENT = Value("self-employment")
    val UK_PROPERTY = Value("uk-property")
    val FHL_PROPERTY_EEA = Value("fhl-property-eea")
    val FHL_PROPERTY_UK = Value("fhl-property-uk")
    val EMPLOYMENT = Value("employment")
    val FOREIGN_INCOME = Value("foreign-income")
    val DIVIDENDS_FROM_FOREIGN_COMPANIES = Value("dividends-from-foreign-companies")
    val TRUSTS_AND_ESTATES = Value("trusts-and-estates")
    val INTEREST_FROM_UK_BANKS = Value("interest-from-uk-banks")
    val DIVIDENDS_UK = Value("dividends-uk")
    val UK_PENSION_BENEFITS = Value("uk-pension-benefits")
    val GAINS_ON_LIFE_INSURANCE_POLICIES = Value("gains-on-life-insurance-policies")
    val SHARE_SCHEMES = Value("share-schemes")
    val PARTNERSHIP = Value("partnership")
    val RELIEF_FOR_CHARITY = Value("relief-for-charity")
    val OTHER_INCOME = Value("other-income")

    def validType(incomeSourceType: String): Either[NoSuchElementException,IncomeSourceTypeB.Value] = {
      try {
        Right(IncomeSourceTypeB.withName(incomeSourceType))
      } catch {
        case e: NoSuchElementException => Left(e)
      }
    }
  }
}
