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

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{Format, Json, OFormat, __}

case class EmploymentData(submittedOn: String,
                          customerAdded: Option[String],
                          source: Option[String],
                          dateIgnored: Option[String],
                          employment: Seq[EmploymentDataDetails]
                         )

object EmploymentData {
  implicit val format: Format[EmploymentData] = Json.format[EmploymentData]
}

case class EmploymentDataDetails(employmentSequenceNumber: Option[String],
                                 payrollId: Option[String],
                                 companyDirector: Option[Boolean],
                                 closeCompany: Option[Boolean],
                                 directorshipCeasedDate: Option[String],
                                 startDate: Option[String],
                                 cessationDate: Option[String],
                                 occPen: Option[Boolean],
                                 disguisedRemuneration: Option[Boolean],
                                 employer: Seq[EmployerDetails],
                                 pay: Seq[EmploymentPayDetails],
                                 customerEstimatedPay: Option[CustomerEstimatedPay],
                                 deductions: Option[EmploymentDeductions],
                                 benefitsInKind: Option[BenefitsInKind]
                                )

object EmploymentDataDetails {
  implicit val format: Format[EmploymentDataDetails] = Json.format[EmploymentDataDetails]
}

case class EmployerDetails(employerRef: Option[String],
                           employerName: String
                          )

object EmployerDetails {
  implicit val format: Format[EmployerDetails] = Json.format[EmployerDetails]
}


case class EmploymentPayDetails(taxablePayToDate: BigDecimal,
                                totalTaxToDate: BigDecimal,
                                tipsAndOtherPayments: Option[BigDecimal],
                                payFrequency: String,
                                paymentDate: String,
                                taxWeekNo: Option[Int],
                                taxMonthNo: Option[Int]
                               )

object EmploymentPayDetails {
  implicit val format: Format[EmploymentPayDetails] = Json.format[EmploymentPayDetails]
}


case class CustomerEstimatedPay(amount: Option[BigDecimal])

object CustomerEstimatedPay {
  implicit val format: Format[CustomerEstimatedPay] = Json.format[CustomerEstimatedPay]
}


case class EmploymentDeductions(studentLoans: Option[StudentLoans])

object EmploymentDeductions {
  implicit val format: Format[EmploymentDeductions] = Json.format[EmploymentDeductions]
}


case class StudentLoans(uglDeductionAmount: Option[BigDecimal],
                        pglDeductionAmount: Option[BigDecimal])

object StudentLoans {
  implicit val format: Format[StudentLoans] = Json.format[StudentLoans]
}


case class BenefitsInKind(accommodation: Option[BigDecimal] = None,
                          assets: Option[BigDecimal] = None,
                          assetTransfer: Option[BigDecimal] = None,
                          beneficialLoan: Option[BigDecimal] = None,
                          car: Option[BigDecimal] = None,
                          carFuel: Option[BigDecimal] = None,
                          educationalServices: Option[BigDecimal] = None,
                          entertaining: Option[BigDecimal] = None,
                          expenses: Option[BigDecimal] = None,
                          medicalInsurance: Option[BigDecimal] = None,
                          telephone: Option[ BigDecimal] = None,
                          service: Option[BigDecimal] = None,
                          taxableExpenses: Option[BigDecimal] = None,
                          van: Option[BigDecimal] = None,
                          vanFuel: Option[BigDecimal] = None,
                          mileage: Option[BigDecimal] = None,
                          nonQualifyingRelocationExpenses: Option[BigDecimal] = None,
                          nurseryPlaces: Option[BigDecimal] = None,
                          otherItems: Option[BigDecimal] = None,
                          paymentsOnEmployeesBehalf: Option[BigDecimal] = None,
                          personalIncidentalExpenses: Option[BigDecimal] = None,
                          qualifyingRelocationExpenses: Option[BigDecimal] = None,
                          employerProvidedProfessionalSubscriptions: Option[BigDecimal] = None,
                          employerProvidedServices: Option[BigDecimal] = None,
                          incomeTaxPaidByDirector: Option[BigDecimal] = None,
                          travelAndSubsistence: Option[BigDecimal] = None,
                          vouchersAndCreditCards: Option[BigDecimal] = None,
                          nonCash: Option[BigDecimal] = None
                         )

object BenefitsInKind {
  val firstSetOfFields: OFormat[(Option[BigDecimal], Option[BigDecimal], Option[BigDecimal], Option[BigDecimal],
    Option[BigDecimal], Option[BigDecimal], Option[BigDecimal], Option[BigDecimal], Option[BigDecimal],
    Option[BigDecimal], Option[BigDecimal], Option[BigDecimal], Option[BigDecimal], Option[BigDecimal],
    Option[BigDecimal], Option[BigDecimal], Option[BigDecimal], Option[BigDecimal], Option[BigDecimal],
    Option[BigDecimal], Option[BigDecimal], Option[BigDecimal])] = (
    (__ \ "accommodation").formatNullable[BigDecimal] and
      (__ \ "assets").formatNullable[BigDecimal] and
      (__ \ "assetTransfer").formatNullable[BigDecimal] and
      (__ \ "beneficialLoan").formatNullable[BigDecimal] and
      (__ \ "car").formatNullable[BigDecimal] and
      (__ \ "carFuel").formatNullable[BigDecimal] and
      (__ \ "educationalServices").formatNullable[BigDecimal] and
      (__ \ "entertaining").formatNullable[BigDecimal] and
      (__ \ "expenses").formatNullable[BigDecimal] and
      (__ \ "medicalInsurance").formatNullable[BigDecimal] and
      (__ \ "telephone").formatNullable[BigDecimal] and
      (__ \ "service").formatNullable[BigDecimal] and
      (__ \ "taxableExpenses").formatNullable[BigDecimal] and
      (__ \ "van").formatNullable[BigDecimal] and
      (__ \ "vanFuel").formatNullable[BigDecimal] and
      (__ \ "mileage").formatNullable[BigDecimal] and
      (__ \ "nonQualifyingRelocationExpenses").formatNullable[BigDecimal] and
      (__ \ "nurseryPlaces").formatNullable[BigDecimal] and
      (__ \ "otherItems").formatNullable[BigDecimal] and
      (__ \ "paymentsOnEmployeesBehalf").formatNullable[BigDecimal] and
      (__ \ "personalIncidentalExpenses").formatNullable[BigDecimal] and
      (__ \ "qualifyingRelocationExpenses").formatNullable[BigDecimal]
    ).tupled

  val secondSetOfFields: OFormat[(Option[BigDecimal], Option[BigDecimal], Option[BigDecimal], Option[BigDecimal],
    Option[BigDecimal], Option[BigDecimal])] = (
    (__ \ "employerProvidedProfessionalSubscriptions").formatNullable[BigDecimal] and
      (__ \ "employerProvidedServices").formatNullable[BigDecimal] and
      (__ \ "incomeTaxPaidByDirector").formatNullable[BigDecimal] and
      (__ \ "travelAndSubsistence").formatNullable[BigDecimal] and
      (__ \ "vouchersAndCreditCards").formatNullable[BigDecimal] and
      (__ \ "nonCash").formatNullable[BigDecimal]
    ).tupled

  implicit val hugeCaseClassReads: OFormat[BenefitsInKind] = {
    (firstSetOfFields and secondSetOfFields).apply({
      case (
        (accommodation, assets, assetTransfer, beneficialLoan, car, carFuel, educationalServices, entertaining,
        expenses, medicalInsurance, telephone, service, taxableExpenses, van, vanFuel, mileage, nonQualifyingRelocationExpenses,
        nurseryPlaces, otherItems, paymentsOnEmployeesBehalf, personalIncidentalExpenses, qualifyingRelocationExpenses),
        (employerProvidedProfessionalSubscriptions, employerProvidedServices, incomeTaxPaidByDirector, travelAndSubsistence,
        vouchersAndCreditCards, nonCash)
        ) =>
        BenefitsInKind(
          accommodation, assets, assetTransfer, beneficialLoan, car, carFuel, educationalServices, entertaining, expenses,
          medicalInsurance, telephone, service, taxableExpenses, van, vanFuel, mileage, nonQualifyingRelocationExpenses,
          nurseryPlaces, otherItems, paymentsOnEmployeesBehalf, personalIncidentalExpenses, qualifyingRelocationExpenses,
          employerProvidedProfessionalSubscriptions, employerProvidedServices, incomeTaxPaidByDirector, travelAndSubsistence,
          vouchersAndCreditCards, nonCash
        )
    }, {
      benefitsInKind =>
        (
          (benefitsInKind.accommodation, benefitsInKind.assets, benefitsInKind.assetTransfer, benefitsInKind.beneficialLoan, benefitsInKind.car, benefitsInKind.carFuel,
            benefitsInKind.educationalServices, benefitsInKind.entertaining, benefitsInKind.expenses, benefitsInKind.medicalInsurance, benefitsInKind.telephone,
            benefitsInKind.service, benefitsInKind.taxableExpenses, benefitsInKind.van, benefitsInKind.vanFuel, benefitsInKind.mileage,
            benefitsInKind.nonQualifyingRelocationExpenses, benefitsInKind.nurseryPlaces, benefitsInKind.otherItems, benefitsInKind.paymentsOnEmployeesBehalf,
            benefitsInKind.personalIncidentalExpenses, benefitsInKind.qualifyingRelocationExpenses
          ), (benefitsInKind.employerProvidedProfessionalSubscriptions, benefitsInKind.employerProvidedServices, benefitsInKind.incomeTaxPaidByDirector,
          benefitsInKind.travelAndSubsistence, benefitsInKind.vouchersAndCreditCards, benefitsInKind.nonCash)
        )
    })
  }
}
