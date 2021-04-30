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

import models.APIModels.{EmploymentDetails, ExpensesType, GiftAidPayments, Gifts, EmploymentSource}
import play.api.libs.json.{Format, Json, OFormat}

object DESModels {

  // DES #1392 //
  case class DESIncomeSourceModel(incomeSourceId: String,
                                  incomeSourceName: String,
                                  identifier: String,
                                  incomeSourceType: String)

  object DESIncomeSourceModel {
    implicit val format: Format[DESIncomeSourceModel] = Json.format[DESIncomeSourceModel]
  }
  // DES #1392 //

  // DES #1391 //
  case class DESInterestDetail(incomeSourceId: String, taxedUkInterest: Option[BigDecimal], untaxedUkInterest: Option[BigDecimal])

  object DESInterestDetail {
    implicit val format: Format[DESInterestDetail] = Json.format[DESInterestDetail]
  }

  case class DESInterestDetails(savingsInterestAnnualIncome: Seq[DESInterestDetail])

  object DESInterestDetails {
    implicit val format: Format[DESInterestDetails] = Json.format[DESInterestDetails]
  }

  case class DESDividendsDetail(ukDividends: Option[BigDecimal],
                                otherUkDividends: Option[BigDecimal])

  object DESDividendsDetail {
    implicit val formats: Format[DESDividendsDetail] = Json.format[DESDividendsDetail]
  }

  case class DESGiftAidDetail(giftAidPayments: Option[GiftAidPayments],
                              gifts: Option[Gifts])

  object DESGiftAidDetail {
    implicit val format: OFormat[DESGiftAidDetail] = Json.format[DESGiftAidDetail]
  }
  // DES #1391 //

  // DES #1645 //
  case class DESEmploymentsList(employments: Seq[EmploymentSource],
                                customerDeclaredEmployments: Seq[EmploymentSource])

  object DESEmploymentsList {
    implicit val format: OFormat[DESEmploymentsList] = Json.format[DESEmploymentsList]
  }
  // DES #1645 //

  // DES #1668 //
  case class DESEmploymentExpenses(submittedOn: Option[String],
                                   dateIgnored: Option[String],
                                   source: Option[String],
                                   totalExpenses: Option[BigDecimal],
                                   expenses: Option[ExpensesType]
                               )

  object DESEmploymentExpenses {
    implicit val format: Format[DESEmploymentExpenses] = Json.format[DESEmploymentExpenses]
  }
  // DES #1668 //


  // DES #1647 //
  case class DESEmploymentData(submittedOn: String,
                               customerAdded: Option[String] = None,
                               source: Option[String] = None,
                               dateIgnored: Option[String] = None,
                               employment: EmploymentDetails)

  object DESEmploymentData {
    implicit val format: Format[DESEmploymentData] = Json.format[DESEmploymentData]
  }
  // DES #1647 //
}
