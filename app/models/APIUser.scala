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

import models.DESModels.EmploymentData
import play.api.libs.json.{Json, OFormat}

trait IncomeSource

case class InterestSubmission(taxYear: Int,
                              taxedUkInterest: Option[BigDecimal],
                              untaxedUkInterest: Option[BigDecimal])

object InterestSubmission {
  implicit val formats: OFormat[InterestSubmission] = Json.format[InterestSubmission]
}

case class Interest(incomeSourceName: String,
                    incomeSourceId: String,
                    interestSubmissions: Seq[InterestSubmission]) extends IncomeSource

object Interest {
  implicit val formats: OFormat[Interest] = Json.format[Interest]
}

case class Dividends(taxYear: Int,
                     ukDividends: Option[BigDecimal] = None,
                     otherUkDividends: Option[BigDecimal] = None) extends IncomeSource

object Dividends {
  implicit val formats: OFormat[Dividends] = Json.format[Dividends]
}

case class GiftAidPayments(nonUkCharitiesCharityNames: Option[Seq[String]] = None,
                           currentYear: Option[BigDecimal] = None,
                           oneOffCurrentYear: Option[BigDecimal] = None,
                           currentYearTreatedAsPreviousYear: Option[BigDecimal] = None,
                           nextYearTreatedAsCurrentYear: Option[BigDecimal] = None,
                           nonUkCharities: Option[BigDecimal] = None)

object GiftAidPayments {
  implicit val format: OFormat[GiftAidPayments] = Json.format[GiftAidPayments]
}

case class Gifts(investmentsNonUkCharitiesCharityNames: Option[Seq[String]] = None,
                 landAndBuildings: Option[BigDecimal] = None,
                 sharesOrSecurities: Option[BigDecimal] = None,
                 investmentsNonUkCharities: Option[BigDecimal] = None)

object Gifts {
  implicit val format: OFormat[Gifts] = Json.format[Gifts]
}

case class GiftAid(taxYear: Int,
                   giftAidPayments: Option[GiftAidPayments] = None,
                   gifts: Option[Gifts] = None) extends IncomeSource

object GiftAid {
  implicit val formats: OFormat[GiftAid] = Json.format[GiftAid]
}

case class Employment(taxYear: Int,
                      hmrcEmployments: Seq[HmrcEmployment],
                      customerEmployments: Seq[CustomerEmployment],
                      employmentData: Seq[EmploymentData],
                      employmentExpenses: EmploymentExpenses) extends IncomeSource

object Employment {
  implicit val formats: OFormat[Employment] = Json.format[Employment]
}

case class HmrcEmployment(employmentId: String,
                          employerName: String,
                          employerRef: Option[String] = None,
                          payRollId: Option[String] = None,
                          startDate: Option[String] = None,
                          cessationDate: Option[String] = None,
                          dateIgnored: Option[String] = None)

object HmrcEmployment {
  implicit val formats: OFormat[HmrcEmployment] = Json.format[HmrcEmployment]
}

case class CustomerEmployment(employmentId: String,
                              employerName: String,
                              employerRef: Option[String] = None,
                              payRollId: Option[String] = None,
                              startDate: Option[String] = None,
                              cessationDate: Option[String] = None,
                              submittedOn: String)

object CustomerEmployment {
  implicit val formats: OFormat[CustomerEmployment] = Json.format[CustomerEmployment]
}

case class EmploymentExpenses(submittedOn: Option[String],
                              dateIgnored: Option[String],
                              source: Option[String],
                              totalExpenses: Option[BigDecimal],
                              expenses: Option[ExpensesType]
                             )

object EmploymentExpenses {
  implicit val format: OFormat[EmploymentExpenses] = Json.format[EmploymentExpenses]
}

case class ExpensesType(
                         businessTravelCosts: Option[BigDecimal],
                         jobExpenses: Option[BigDecimal],
                         flatRateJobExpenses: Option[BigDecimal],
                         professionalSubscriptions: Option[BigDecimal],
                         hotelAndMealExpenses: Option[BigDecimal],
                         otherAndCapitalAllowances: Option[BigDecimal],
                         vehicleExpenses: Option[BigDecimal],
                         mileageAllowanceRelief: Option[BigDecimal]
                       )

object ExpensesType {
  implicit val formats: OFormat[ExpensesType] = Json.format[ExpensesType]
}

case class APIUser(nino: String,
                   dividends: Seq[Dividends] = Seq(),
                   interest: Seq[Interest] = Seq(),
                   giftAid: Seq[GiftAid] = Seq(),
                   employment: Seq[Employment] = Seq())

object APIUser {
  implicit val formats: OFormat[APIUser] = Json.format[APIUser]
}
