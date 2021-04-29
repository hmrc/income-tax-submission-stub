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

package models.APIUsers

import models.DESModels.EmploymentData
import play.api.libs.json.{Format, Json, OFormat}


case class EmploymentSource(employmentId: String,
                            employerName: String,
                            employerRef: Option[String] = None,
                            payRollId: Option[String] = None,
                            startDate: Option[String] = None,
                            cessationDate: Option[String] = None,
                            dateIgnored: Option[String] = None, //Empty for Customer Employment
                            submittedOn: Option[String] = None, //Mandatory for CustomerEmployment
                            employmentData: Option[EmploymentData] = None)

object EmploymentSource {
  implicit val formats: OFormat[EmploymentSource] = Json.format[EmploymentSource]
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

case class EmploymentModel(employmentSequenceNumber: Option[String] = None,
                           payrollId: Option[String] = None,
                           companyDirector: Option[Boolean] = None,
                           closeCompany: Option[Boolean] = None,
                           directorshipCeasedDate: Option[String] = None,
                           startDate: Option[String] = None,
                           cessationDate: Option[String] = None,
                           occPen: Option[Boolean] = None,
                           disguisedRemuneration: Option[Boolean] = None,
                           employer: EmployerModel,
                           pay: PayModel,
                           customerEstimatedPay: Option[CustomerEstimatedPay] = None,
                           deductions: Option[EmploymentDeductions] = None,
                           benefitsInKind: Option[Benefits] = None
                          )

object EmploymentModel {
  implicit val format: Format[EmploymentModel] = Json.format[EmploymentModel]
}

case class EmployerModel(employerRef: Option[String] = None,
                         employerName: String
                          )

object EmployerModel {
  implicit val format: Format[EmployerModel] = Json.format[EmployerModel]
}


case class PayModel(taxablePayToDate: BigDecimal,
                    totalTaxToDate: BigDecimal,
                    tipsAndOtherPayments: Option[BigDecimal] = None,
                    payFrequency: String,
                    paymentDate: String,
                    taxWeekNo: Option[Int] = None,
                    taxMonthNo: Option[Int] = None
                               )

object PayModel {
  implicit val format: Format[PayModel] = Json.format[PayModel]
}


case class CustomerEstimatedPay(amount: Option[BigDecimal] = None)

object CustomerEstimatedPay {
  implicit val format: Format[CustomerEstimatedPay] = Json.format[CustomerEstimatedPay]
}


case class EmploymentDeductions(studentLoans: Option[StudentLoans])

object EmploymentDeductions {
  implicit val format: Format[EmploymentDeductions] = Json.format[EmploymentDeductions]
}


case class StudentLoans(uglDeductionAmount: Option[BigDecimal] = None,
                        pglDeductionAmount: Option[BigDecimal] = None)

object StudentLoans {
  implicit val format: Format[StudentLoans] = Json.format[StudentLoans]
}
