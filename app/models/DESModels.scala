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

import models.APIUsers.{EmploymentModel, ExpensesType, GiftAidPayments, Gifts, EmploymentSource}
import play.api.libs.json.{Format, Json, OFormat}

object DESModels {

  // DES #1392 //
  case class IncomeSourceModel(incomeSourceId: String,
                               incomeSourceName: String,
                               identifier: String,
                               incomeSourceType: String)

  object IncomeSourceModel {
    implicit val format: Format[IncomeSourceModel] = Json.format[IncomeSourceModel]
  }
  // DES #1392 //

  // DES #1391 //
  case class InterestDetail(incomeSourceId: String, taxedUkInterest: Option[BigDecimal], untaxedUkInterest: Option[BigDecimal])

  object InterestDetail {
    implicit val format: Format[InterestDetail] = Json.format[InterestDetail]
  }

  case class InterestDetails(savingsInterestAnnualIncome: Seq[InterestDetail])

  object InterestDetails {
    implicit val format: Format[InterestDetails] = Json.format[InterestDetails]
  }

  case class DividendsDetail(ukDividends: Option[BigDecimal],
                             otherUkDividends: Option[BigDecimal])

  object DividendsDetail {
    implicit val formats: Format[DividendsDetail] = Json.format[DividendsDetail]
  }

  case class GiftAidDetail(giftAidPayments: Option[GiftAidPayments],
                           gifts: Option[Gifts])

  object GiftAidDetail {
    implicit val format: OFormat[GiftAidDetail] = Json.format[GiftAidDetail]
  }
  // DES #1391 //

  // DES #1645 //
  case class EmploymentsDetail(employments: Seq[EmploymentSource],
                               customerDeclaredEmployments: Seq[EmploymentSource])

  object EmploymentsDetail {
    implicit val format: OFormat[EmploymentsDetail] = Json.format[EmploymentsDetail]
  }
  // DES #1645 //

  // DES #1668 //
  case class EmploymentExpenses(submittedOn: Option[String],
                                dateIgnored: Option[String],
                                source: Option[String],
                                totalExpenses: Option[BigDecimal],
                                expenses: Option[ExpensesType]
                               )

  object EmploymentExpenses {
    implicit val format: Format[EmploymentExpenses] = Json.format[EmploymentExpenses]
  }
  // DES #1668 //


  // DES #1647 //
  case class EmploymentData(submittedOn: String,
                            customerAdded: Option[String] = None,
                            source: Option[String] = None,
                            dateIgnored: Option[String] = None,
                            employment: EmploymentModel
                           )

  object EmploymentData {
    implicit val format: Format[EmploymentData] = Json.format[EmploymentData]
  }

  // DES #1647 //
}
