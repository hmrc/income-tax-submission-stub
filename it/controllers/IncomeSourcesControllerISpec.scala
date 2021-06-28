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

package controllers

import filters.StubErrorFilter.{DES_500_NINO, DES_503_NINO}
import models.IncomeSourceTypes.IncomeSourceTypeB.{INTEREST_FROM_UK_BANKS, UK_PENSION_BENEFITS}
import models.errors.StubErrors.{DES_500_ERROR_MODEL, DES_503_ERRORS_MODEL}
import play.api.Application
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import utils.IntegrationTest

class IncomeSourcesControllerISpec extends IntegrationTest with FutureAwaits with DefaultAwaitTimeout with Status {

  implicit val application: Application = app

  "GET /income-tax/nino/AA123459A/not-a-real-endpoint" should {
    s"return ${Status.NOT_FOUND} default error" in {

      val url = s"income-tax/nino/AA123459A/not-a-real-endpoint"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json.toString() must include("""{"statusCode":404,"message":"URI not found","requested":"/income-tax/nino/AA123459A/not-a-real-endpoint"}""")
    }
  }

  "GET /income-tax/nino/AA123459A/income-source/savings/annual/2022?incomeSourceId=000000000000002" should {
    s"return ${Status.OK} with json" in {

      val url = s"income-tax/nino/AA123459A/income-source/savings/annual/2022?incomeSourceId=000000000000002"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json.toString() must include("""{"savingsInterestAnnualIncome":[{"incomeSourceId":"000000000000002","taxedUkInterest":99999999999.99}]}""")
    }
    s"return ${Status.NOT_FOUND} when the income source id does not exist" in {

      val url = s"income-tax/nino/AA123459A/income-source/savings/annual/2022?incomeSourceId=123456789012345"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json.toString() must include("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""")
    }
  }

  "GET /income-tax/nino/AA123459A/income-source/dividends/annual/2022" should {
    s"return ${Status.OK} with json" in {

      val url = s"income-tax/nino/AA123459A/income-source/dividends/annual/2022"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json.toString() must include("""{"ukDividends":99999999999.99}""")

    }
    s"return ${Status.NOT_FOUND} when not data exists for the tax year" in {

      val url = s"income-tax/nino/AA123459A/income-source/dividends/annual/2023"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json.toString() must include("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""")
    }
  }

  "GET /income-tax/nino/AA123459A/income-source/rick/annual/2022" should {
    s"return ${Status.OK} with json" in {

      val url = s"income-tax/nino/AA123459A/income-source/rick/annual/2022"

      val res = await(buildClient(url).get())

      res.status mustBe Status.BAD_REQUEST
      res.body mustBe """{"code":"INVALID_TYPE","reason":"Submission has not passed validation. Invalid parameter type."}"""

    }
  }

  "GET /income-tax/nino/AA123459A/income-source/charity/annual/2022" should {
    s"return ${Status.OK} with json" in {

      val url = s"income-tax/nino/AA123459A/income-source/charity/annual/2022"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json.toString() must include("""{"giftAidPayments":{"nonUkCharitiesCharityNames":["Rick Owens Charity"],"currentYear":99999999999.99,"oneOffCurrentYear":99999999999.99,"currentYearTreatedAsPreviousYear":99999999999.99,"nextYearTreatedAsCurrentYear":99999999999.99,"nonUkCharities":99999999999.99},"gifts":{"investmentsNonUkCharitiesCharityNames":["Rick Owens Non-UK Charity"],"landAndBuildings":99999999999.99,"sharesOrSecurities":99999999999.99,"investmentsNonUkCharities":99999999999.99}}""")

    }
    s"return ${Status.NOT_FOUND} when no data exists for the tax year" in {

      val url = s"income-tax/nino/AA123459A/income-source/charity/annual/2023"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json.toString() must include("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""")
    }
  }

  "GET /income-tax/income/employments/AA123459A/2021-22" should {
    s"return ${Status.OK} with json" in {

      val url = s"income-tax/income/employments/AA123459A/2021-22"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json mustBe Json.parse("""{
                                   |	"employments": [{
                                   |		"employmentId": "00000000-0000-0000-0000-000000000001",
                                   |		"employerName": "Rick Owens LTD",
                                   |		"employerRef": "666/66666",
                                   |		"payRollId": "123456789",
                                   |		"startDate": "2020-01-04T05:01:01Z",
                                   |		"cessationDate": "2020-01-04T05:01:01Z",
                                   |		"dateIgnored": "2020-01-04T05:01:01Z"
                                   |	}],
                                   |	"customerDeclaredEmployments": [{
                                   |		"employmentId": "00000000-0000-0000-0000-000000000001",
                                   |		"employerName": "Rick Owens London LTD",
                                   |		"employerRef": "666/66666",
                                   |		"payRollId": "123456789",
                                   |		"startDate": "2020-02-04T05:01:01Z",
                                   |		"cessationDate": "2020-02-04T05:01:01Z",
                                   |		"submittedOn": "2020-02-04T05:01:01Z"
                                   |	}]
                                   |}""".stripMargin)
    }
    s"return ${Status.OK} with json when filtering by employment id" in {

      val url = s"income-tax/income/employments/AA123459A/2021-22?employmentId=00000000-0000-0000-0000-000000000001"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json mustBe Json.parse("""{
                                   |	"employments": [{
                                   |		"employmentId": "00000000-0000-0000-0000-000000000001",
                                   |		"employerName": "Rick Owens LTD",
                                   |		"employerRef": "666/66666",
                                   |		"payRollId": "123456789",
                                   |		"startDate": "2020-01-04T05:01:01Z",
                                   |		"cessationDate": "2020-01-04T05:01:01Z",
                                   |		"dateIgnored": "2020-01-04T05:01:01Z"
                                   |	}],
                                   |	"customerDeclaredEmployments": [{
                                   |		"employmentId": "00000000-0000-0000-0000-000000000001",
                                   |		"employerName": "Rick Owens London LTD",
                                   |		"employerRef": "666/66666",
                                   |		"payRollId": "123456789",
                                   |		"startDate": "2020-02-04T05:01:01Z",
                                   |		"cessationDate": "2020-02-04T05:01:01Z",
                                   |		"submittedOn": "2020-02-04T05:01:01Z"
                                   |	}]
                                   |}""".stripMargin)
    }
    s"return ${Status.OK} with json when filtering by employment id and only return one record" in {

      val url = s"income-tax/income/employments/AA133742A/2021-22?employmentId=00000000-0000-1000-8000-000000000000"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json mustBe Json.parse(
        """{
          |	"employments": [{
          |		"employmentId": "00000000-0000-1000-8000-000000000000",
          |		"employerName": "Vera Lynn",
          |		"employerRef": "123/abc 001<Q>",
          |		"payRollId": "123345657",
          |		"startDate": "2020-06-17T10:53:38Z",
          |		"cessationDate": "2020-06-17T10:53:38Z",
          |		"dateIgnored": "2020-06-17T10:53:38Z"
          |	}],
          |	"customerDeclaredEmployments": []
          |}""".stripMargin)
    }
    s"return ${Status.NOT_FOUND} with json when no data for the tax year" in {

      val url = s"income-tax/income/employments/AA123459A/2022-23"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json mustBe Json.parse("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""".stripMargin)
    }
    s"return ${Status.NOT_FOUND} with json when no data for the employment id" in {

      val url = s"income-tax/income/employments/AA123459A/2021-22?employmentId=00000000-0000-0000-0000-000000000003"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json mustBe Json.parse("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""".stripMargin)
    }
  }

  "GET /income-tax/expenses/employments/AA123459A/2021-22" should {
    s"return ${Status.OK} with json with a valid view parameter" in {

      val url = s"income-tax/expenses/employments/AA123459A/2021-22?view=HMRC-HELD"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json mustBe Json.parse("""{
                                   |    "submittedOn": "2022-12-12T12:12:12Z",
                                   |    "dateIgnored": "2022-12-11T12:12:12Z",
                                   |    "source": "HMRC-HELD",
                                   |    "totalExpenses": 100,
                                   |    "expenses": {
                                   |        "businessTravelCosts": 100,
                                   |        "jobExpenses": 100,
                                   |        "flatRateJobExpenses": 100,
                                   |        "professionalSubscriptions": 100,
                                   |        "hotelAndMealExpenses": 100,
                                   |        "otherAndCapitalAllowances": 100,
                                   |        "vehicleExpenses": 100,
                                   |        "mileageAllowanceRelief": 100
                                   |    }
                                   |}""".stripMargin)
    }
    s"return ${Status.NOT_FOUND} when user has no expenses" in {

      val url = s"income-tax/expenses/employments/BB444444A/2021-22?view=CUSTOMER"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json mustBe Json.parse("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""".stripMargin)
    }
    s"return ${Status.NOT_FOUND} with json when no data for the view" in {

      val url = s"income-tax/expenses/employments/AA123459A/2021-22?view=NOTFOUND"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json mustBe Json.parse("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""".stripMargin)
    }
    s"return ${Status.NOT_FOUND} with json when no data for the tax year" in {

      val url = s"income-tax/expenses/employments/AA123459A/2022-23?view=CUSTOMER"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json mustBe Json.parse("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""".stripMargin)
    }

    s"return ${Status.NOT_FOUND} with the json has no source value" in {

      val url = s"income-tax/expenses/employments/AA133742A/2021-22?view=LATEST"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json mustBe Json.parse("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""".stripMargin)
    }
  }

  "GET /income-tax/income/employments/AA133742A/2021-22/00000000-0000-1000-8000-000000000002" should {

    s"return ${Status.OK} with json with a valid Customer view parameter" in {

      val url = s"income-tax/income/employments/AA133742A/2021-22/00000000-0000-1000-8000-000000000002?view=CUSTOMER"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK

      res.json mustBe Json.parse(
        """{
          |	"submittedOn": "2020-02-04T05:01:01Z",
          |	"employment": {
          |		"employmentSequenceNumber": "1002",
          |		"payrollId": "123456789999",
          |		"companyDirector": false,
          |		"closeCompany": true,
          |		"directorshipCeasedDate": "2020-02-12",
          |		"startDate": "2019-04-21",
          |		"cessationDate": "2020-03-11",
          |		"occPen": false,
          |		"disguisedRemuneration": false,
          |		"employer": {
          |			"employerRef": "223/AB12399",
          |			"employerName": "maggie"
          |		},
          |		"pay": {
          |			"taxablePayToDate": 34234.15,
          |			"totalTaxToDate": 6782.92,
          |			"tipsAndOtherPayments": 67676,
          |			"payFrequency": "CALENDAR MONTHLY",
          |			"paymentDate": "2020-04-23",
          |			"taxWeekNo": 32
          |		},
          |		"deductions": {
          |			"studentLoans": {
          |				"uglDeductionAmount": 13343.45,
          |				"pglDeductionAmount": 24242.56
          |			}
          |		},
          |		"benefitsInKind": {
          |			"accommodation": 100,
          |			"assets": 100,
          |			"assetTransfer": 100,
          |			"beneficialLoan": 100,
          |			"car": 100,
          |			"carFuel": 100,
          |			"educationalServices": 100,
          |			"entertaining": 100,
          |			"expenses": 100,
          |			"medicalInsurance": 100,
          |			"telephone": 100,
          |			"service": 100,
          |			"taxableExpenses": 100,
          |			"van": 100,
          |			"vanFuel": 100,
          |			"mileage": 100,
          |			"nonQualifyingRelocationExpenses": 100,
          |			"nurseryPlaces": 100,
          |			"otherItems": 100,
          |			"paymentsOnEmployeesBehalf": 100,
          |			"personalIncidentalExpenses": 100,
          |			"qualifyingRelocationExpenses": 100,
          |			"employerProvidedProfessionalSubscriptions": 100,
          |			"employerProvidedServices": 100,
          |			"incomeTaxPaidByDirector": 100,
          |			"travelAndSubsistence": 100,
          |			"vouchersAndCreditCards": 100,
          |			"nonCash": 100
          |		}
          |	}
          |}""".stripMargin)
    }
    s"return ${Status.OK} with json with a valid LATEST view parameter" in {

      val url = s"income-tax/income/employments/AA133742A/2021-22/00000000-0000-1000-8000-000000000002?view=LATEST"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK

      res.json mustBe Json.parse(
        """{
          |	"submittedOn": "2020-02-04T05:01:01Z",
          |	"employment": {
          |		"employmentSequenceNumber": "1002",
          |		"payrollId": "123456789999",
          |		"companyDirector": false,
          |		"closeCompany": true,
          |		"directorshipCeasedDate": "2020-02-12",
          |		"startDate": "2019-04-21",
          |		"cessationDate": "2020-03-11",
          |		"occPen": false,
          |		"disguisedRemuneration": false,
          |		"employer": {
          |			"employerRef": "223/AB12399",
          |			"employerName": "maggie"
          |		},
          |		"pay": {
          |			"taxablePayToDate": 34234.15,
          |			"totalTaxToDate": 6782.92,
          |			"tipsAndOtherPayments": 67676,
          |			"payFrequency": "CALENDAR MONTHLY",
          |			"paymentDate": "2020-04-23",
          |			"taxWeekNo": 32
          |		},
          |		"deductions": {
          |			"studentLoans": {
          |				"uglDeductionAmount": 13343.45,
          |				"pglDeductionAmount": 24242.56
          |			}
          |		},
          |		"benefitsInKind": {
          |			"accommodation": 100,
          |			"assets": 100,
          |			"assetTransfer": 100,
          |			"beneficialLoan": 100,
          |			"car": 100,
          |			"carFuel": 100,
          |			"educationalServices": 100,
          |			"entertaining": 100,
          |			"expenses": 100,
          |			"medicalInsurance": 100,
          |			"telephone": 100,
          |			"service": 100,
          |			"taxableExpenses": 100,
          |			"van": 100,
          |			"vanFuel": 100,
          |			"mileage": 100,
          |			"nonQualifyingRelocationExpenses": 100,
          |			"nurseryPlaces": 100,
          |			"otherItems": 100,
          |			"paymentsOnEmployeesBehalf": 100,
          |			"personalIncidentalExpenses": 100,
          |			"qualifyingRelocationExpenses": 100,
          |			"employerProvidedProfessionalSubscriptions": 100,
          |			"employerProvidedServices": 100,
          |			"incomeTaxPaidByDirector": 100,
          |			"travelAndSubsistence": 100,
          |			"vouchersAndCreditCards": 100,
          |			"nonCash": 100
          |		}
          |	}
          |}""".stripMargin)
    }

    s"return ${Status.OK} with json with a valid LATEST view parameter and compare records" in {

      val url = s"income-tax/income/employments/AA123459A/2021-22/00000000-0000-0000-0000-000000000001?view=LATEST"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json mustBe Json.parse("""{
                                   |	"submittedOn": "2020-02-04T05:01:01Z",
                                   |	"customerAdded": "2020-02-04T05:01:01Z",
                                   |	"source": "CUSTOMER",
                                   |	"employment": {
                                   |		"employer": {
                                   |			"employerRef": "666/66666",
                                   |			"employerName": "Rick Owens LTD"
                                   |		},
                                   |		"pay": {
                                   |			"taxablePayToDate": 555.55,
                                   |			"totalTaxToDate": 555.55,
                                   |			"tipsAndOtherPayments": 555.55,
                                   |			"payFrequency": "CALENDAR MONTHLY",
                                   |			"paymentDate": "2020-04-23",
                                   |			"taxWeekNo": 32
                                   |		}
                                   |	}
                                   |}""".stripMargin)
    }

    s"return ${Status.OK} with json with a valid LATEST view parameter and compare records for when hmrc is the latest" in {

      val url = s"income-tax/income/employments/BB444444A/2021-22/00000000-5555-0000-0000-000000000001?view=LATEST"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json mustBe Json.parse(
        """{
          |	"submittedOn": "2020-03-04T05:01:01Z",
          |	"employment": {
          |		"closeCompany": true,
          |		"directorshipCeasedDate": "2020-04-20",
          |		"employer": {
          |			"employerRef": "666/66666",
          |			"employerName": "Raf Simons Ltd"
          |		},
          |		"pay": {
          |			"taxablePayToDate": 666.66,
          |			"totalTaxToDate": 666.66,
          |			"tipsAndOtherPayments": 6666.66,
          |			"payFrequency": "CALENDAR MONTHLY",
          |			"paymentDate": "2020-04-23",
          |			"taxWeekNo": 32
          |		},
          |		"benefitsInKind": {
          |			"accommodation": 100,
          |			"assets": 200,
          |			"assetTransfer": 300,
          |			"beneficialLoan": 400,
          |			"car": 500,
          |			"carFuel": 600,
          |			"educationalServices": 700,
          |			"entertaining": 800,
          |			"expenses": 900,
          |			"medicalInsurance": 1000,
          |			"telephone": 1100,
          |			"service": 1200,
          |			"taxableExpenses": 1300,
          |			"van": 1400,
          |			"vanFuel": 1500,
          |			"mileage": 1600,
          |			"nonQualifyingRelocationExpenses": 1700,
          |			"nurseryPlaces": 1800,
          |			"otherItems": 1900,
          |			"paymentsOnEmployeesBehalf": 2000,
          |			"personalIncidentalExpenses": 2100,
          |			"qualifyingRelocationExpenses": 2200,
          |			"employerProvidedProfessionalSubscriptions": 2300,
          |			"employerProvidedServices": 2400,
          |			"incomeTaxPaidByDirector": 2500,
          |			"travelAndSubsistence": 2600,
          |			"vouchersAndCreditCards": 2700,
          |			"nonCash": 2800
          |		}
          |	}
          |}
          |""".stripMargin)    }
    s"return ${Status.NOT_FOUND} when no employment data exists" in {

      val url = s"income-tax/income/employments/BB444444A/2021-22/00000000-5555-5555-0000-000000000001?view=LATEST"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json mustBe Json.parse("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""".stripMargin)
    }
    s"return ${Status.NOT_FOUND} when invalid view for user" in {

      val url = s"income-tax/income/employments/BB444444A/2021-22/00000000-5555-5555-0000-000000000001?view=VIEW"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json mustBe Json.parse("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""".stripMargin)
    }

    s"return ${Status.OK} with json with a valid HMRC-HELD view parameter" in {

      val url = s"income-tax/income/employments/AA133742A/2021-22/00000000-0000-1000-8000-000000000000?view=HMRC-HELD"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json mustBe Json.parse("""{
                                   |	"submittedOn": "2020-01-04T05:01:01Z",
                                   |	"source": "HMRC-HELD",
                                   |	"employment": {
                                   |		"employmentSequenceNumber": "1002",
                                   |		"payrollId": "123456789999",
                                   |		"companyDirector": false,
                                   |		"closeCompany": true,
                                   |		"directorshipCeasedDate": "2020-02-12",
                                   |		"startDate": "2019-04-21",
                                   |		"cessationDate": "2020-03-11",
                                   |		"occPen": false,
                                   |		"disguisedRemuneration": false,
                                   |		"employer": {
                                   |			"employerRef": "223/AB12399",
                                   |			"employerName": "maggie"
                                   |		},
                                   |		"pay": {
                                   |			"taxablePayToDate": 34234.15,
                                   |			"totalTaxToDate": 6782.92,
                                   |			"tipsAndOtherPayments": 67676,
                                   |			"payFrequency": "CALENDAR MONTHLY",
                                   |			"paymentDate": "2020-04-23",
                                   |			"taxWeekNo": 32
                                   |		},
                                   |		"deductions": {
                                   |			"studentLoans": {
                                   |				"uglDeductionAmount": 13343.45,
                                   |				"pglDeductionAmount": 24242.56
                                   |			}
                                   |		},
                                   |		"benefitsInKind": {
                                   |			"accommodation": 100,
                                   |			"assets": 100,
                                   |			"assetTransfer": 100,
                                   |			"beneficialLoan": 100,
                                   |			"car": 100,
                                   |			"carFuel": 100,
                                   |			"educationalServices": 100,
                                   |			"entertaining": 100,
                                   |			"expenses": 100,
                                   |			"medicalInsurance": 100,
                                   |			"telephone": 100,
                                   |			"service": 100,
                                   |			"taxableExpenses": 100,
                                   |			"van": 100,
                                   |			"vanFuel": 100,
                                   |			"mileage": 100,
                                   |			"nonQualifyingRelocationExpenses": 100,
                                   |			"nurseryPlaces": 100,
                                   |			"otherItems": 100,
                                   |			"paymentsOnEmployeesBehalf": 100,
                                   |			"personalIncidentalExpenses": 100,
                                   |			"qualifyingRelocationExpenses": 100,
                                   |			"employerProvidedProfessionalSubscriptions": 100,
                                   |			"employerProvidedServices": 100,
                                   |			"incomeTaxPaidByDirector": 100,
                                   |			"travelAndSubsistence": 100,
                                   |			"vouchersAndCreditCards": 100,
                                   |			"nonCash": 100
                                   |		}
                                   |	}
                                   |}""".stripMargin)
    }
    s"return ${Status.OK} with json with a valid LATEST view parameter and find the hmrc data" in {

      val url = s"income-tax/income/employments/AA133742A/2021-22/00000000-0000-1000-8000-000000000000?view=LATEST"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json mustBe Json.parse("""{
                                   |	"submittedOn": "2020-01-04T05:01:01Z",
                                   |	"source": "HMRC-HELD",
                                   |	"employment": {
                                   |		"employmentSequenceNumber": "1002",
                                   |		"payrollId": "123456789999",
                                   |		"companyDirector": false,
                                   |		"closeCompany": true,
                                   |		"directorshipCeasedDate": "2020-02-12",
                                   |		"startDate": "2019-04-21",
                                   |		"cessationDate": "2020-03-11",
                                   |		"occPen": false,
                                   |		"disguisedRemuneration": false,
                                   |		"employer": {
                                   |			"employerRef": "223/AB12399",
                                   |			"employerName": "maggie"
                                   |		},
                                   |		"pay": {
                                   |			"taxablePayToDate": 34234.15,
                                   |			"totalTaxToDate": 6782.92,
                                   |			"tipsAndOtherPayments": 67676,
                                   |			"payFrequency": "CALENDAR MONTHLY",
                                   |			"paymentDate": "2020-04-23",
                                   |			"taxWeekNo": 32
                                   |		},
                                   |		"deductions": {
                                   |			"studentLoans": {
                                   |				"uglDeductionAmount": 13343.45,
                                   |				"pglDeductionAmount": 24242.56
                                   |			}
                                   |		},
                                   |		"benefitsInKind": {
                                   |			"accommodation": 100,
                                   |			"assets": 100,
                                   |			"assetTransfer": 100,
                                   |			"beneficialLoan": 100,
                                   |			"car": 100,
                                   |			"carFuel": 100,
                                   |			"educationalServices": 100,
                                   |			"entertaining": 100,
                                   |			"expenses": 100,
                                   |			"medicalInsurance": 100,
                                   |			"telephone": 100,
                                   |			"service": 100,
                                   |			"taxableExpenses": 100,
                                   |			"van": 100,
                                   |			"vanFuel": 100,
                                   |			"mileage": 100,
                                   |			"nonQualifyingRelocationExpenses": 100,
                                   |			"nurseryPlaces": 100,
                                   |			"otherItems": 100,
                                   |			"paymentsOnEmployeesBehalf": 100,
                                   |			"personalIncidentalExpenses": 100,
                                   |			"qualifyingRelocationExpenses": 100,
                                   |			"employerProvidedProfessionalSubscriptions": 100,
                                   |			"employerProvidedServices": 100,
                                   |			"incomeTaxPaidByDirector": 100,
                                   |			"travelAndSubsistence": 100,
                                   |			"vouchersAndCreditCards": 100,
                                   |			"nonCash": 100
                                   |		}
                                   |	}
                                   |}""".stripMargin)
    }

    s"return ${Status.NOT_FOUND} when nothing found for tax year" in {

      val url = s"income-tax/income/employments/AA133742A/2022-23/00000000-0000-1000-8000-000000000000?view=HMRC-HELD"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json mustBe Json.parse("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""".stripMargin)
    }
    s"return ${Status.NOT_FOUND} when matching on LATEST and theres no data" in {

      val url = s"income-tax/income/employments/AA133742A/2022-23/10000000-0000-0000-8000-000000000000?view=LATEST"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json mustBe Json.parse("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""".stripMargin)
    }
    s"return ${Status.NOT_FOUND} when invalid view" in {

      val url = s"income-tax/income/employments/AA133742A/2022-23/00000000-0000-1000-8000-000000000000?view=VIEW"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json mustBe Json.parse("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""".stripMargin)
    }
  }

  "PUT /income-tax/income/employments/AB200900/2021-22/01312" should {
    s"return $NO_CONTENT with pay model" in {

      val url = "income-tax/income/employments/AB200900/2021-22/01312"

      val res = await(buildClient(url).put(Json.parse(
        """{
          |	"employment": {
          |		"pay": {
          |			"taxablePayToDate": 0,
          |			"totalTaxToDate": -99999999999.99,
          |			"tipsAndOtherPayments": 0
          |		},
          |  "benefitsInKind": {
          |			"accommodation": 0,
          |			"assets": 0,
          |			"assetTransfer": 0,
          |			"beneficialLoan": 0,
          |			"car": 0,
          |		  "carFuel": 0,
          |			"educationalServices": 0,
          |			"entertaining": 0,
          |			"expenses": 0,
          |			"medicalInsurance": 0,
          |			"telephone": 0,
          |			"service": 0,
          |			"taxableExpenses": 0,
          |			"van": 0,
          |			"vanFuel": 0,
          |			"mileage": 0,
          |			"nonQualifyingRelocationExpenses": 0,
          |			"nurseryPlaces": 0,
          |			"otherItems": 0,
          |			"paymentsOnEmployeesBehalf": 0,
          |			"personalIncidentalExpenses": 0,
          |			"qualifyingRelocationExpenses": 0,
          |			"employerProvidedProfessionalSubscriptions": 0,
          |			"employerProvidedServices": 0,
          |			"incomeTaxPaidByDirector": 0,
          |			"travelAndSubsistence": 0,
          |			"vouchersAndCreditCards": 0,
          |			"nonCash": 0
          |		  }
          |    }
          |}
          |""".stripMargin)))

      res.status mustBe Status.NO_CONTENT
    }

    s"return ${Status.BAD_REQUEST} without required pay model" in {

      val url = "income-tax/income/employments/AB200900/2021-22/01312"

      val res = await(buildClient(url).put(Json.parse(
        """{
          |	"employment": {
          |		"lumpSums": {
          |			"taxableLumpSumsAndCertainIncome": {
          |				"amount": 0,
          |				"taxPaid": 0,
          |				"taxTakenOffInEmployment": true
          |			    }
          |       }
          |    }
          |}""".stripMargin)))

      res.status mustBe Status.BAD_REQUEST
      res.json.toString() must include("""{"code":"SCHEMA_ERROR","reason":"The request body provided does not conform to the CreateUpdateFinancialDataSchema."}""")
    }
  }

  "DELETE /income-tax/income/employments/AB200900/2021-22/01312" should {
    s"return $NO_CONTENT when the endpoint is hit" in {

      val url = "income-tax/income/employments/AB200900/2021-22/01312"

      val res = await(buildClient(url).delete())
      res.status mustBe Status.NO_CONTENT
    }
  }

  "POST /income-tax/income/employments/AA123459/2021-22/custom" should {
    s"return $OK with json with valid request body" in {

      val url = "income-tax/income/employments/AA123459A/2021-22/custom"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |  "employerRef": "123/AZ12334",
          |  "employerName":  "AMD infotech Ltd",
          |  "startDate": "2019-01-01",
          |  "cessationDate":  "2020-06-01",
          |  "payrollId": "124214112412"
          |}""".stripMargin)))

      res.status mustBe OK
      res.json.toString() must include("""{"employmentId":"""")
    }

    s"return $BAD_REQUEST with json with invalid request body" in {
      val url = "income-tax/income/employments/AA123459A/2021-22/custom"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |  "employerRef": "123/AZ12334",
          |  "cessationDate":  "2020-06-01",
          |  "payrollId": "124214112412"
          |}""".stripMargin)))

      res.status mustBe BAD_REQUEST
      res.json.toString() must include("""{"code":"SCHEMA_ERROR","reason":"The request body provided does not conform to the AddEmploymentSchema."}""")
    }
  }

  "DELETE income-tax/income/employments/AB200900/2021-22/custom/01312" should {
    s"return $NO_CONTENT when endpoint is hit" in {

      val url = "income-tax/income/employments/AB200900/2021-22/custom/01312"

      val res = await(buildClient(url).delete())
      res.status mustBe Status.NO_CONTENT
    }
  }

  "PUT /income-tax/income/employments/AB200900/2021-22/custom/01312" should {
    s"return $OK with json with valid request body" in {

      val url = "income-tax/income/employments/AB200900/2021-22/custom/01312"

      val res = await(buildClient(url).put(Json.parse(
        """{
          |  "employerRef": "123/AZ12334",
          |  "employerName":  "AMD infotech Ltd",
          |  "startDate": "2019-01-01",
          |  "cessationDate":  "2020-06-01",
          |  "payrollId": "124214112412"
          |}""".stripMargin)))

      res.status mustBe NO_CONTENT
    }

    s"return $BAD_REQUEST with json with invalid request body" in {
      val url = "income-tax/income/employments/AB200900/2021-22/custom/01312"

      val res = await(buildClient(url).put(Json.parse(
        """{
          |  "employerRef": "123/AZ12334",
          |  "cessationDate":  "2020-06-01",
          |  "payrollId": "124214112412"
          |}""".stripMargin)))

      res.status mustBe BAD_REQUEST
      res.json.toString() must include("""{"code":"SCHEMA_ERROR","reason":"The request body provided does not conform to the UpdateEmploymentSchema."}""")
    }
  }
  "PUT income-tax/income/employments/AB234543A/2021-22/01312/ignore" should {
    s"return a $CREATED when the endpoint is hit" in {

      val url = "income-tax/income/employments/AB234543A/2021-22/01312/ignore"

      val res = await(buildClient(url).put(Json.parse(
        """{}"""
      )))
      res.status mustBe Status.CREATED
    }

    s"return a $BAD_REQUEST with json with an invalid request body" in {

      val url = "income-tax/income/employments/AB234543A/2021-22/01312/ignore"

      val res = await(buildClient(url).put(Json.parse(
        """{
          | "employmentId": "1234"
          |}""".stripMargin
      )))
      res.status mustBe Status.BAD_REQUEST
    }
  }


  "GET /income-tax/income-sources/nino/AA123459A" should {
    s"return ${Status.OK} with json" in {

      val url = s"income-tax/income-sources/nino/AA123459A?incomeSourceType=$INTEREST_FROM_UK_BANKS&taxYear=2022"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Bank","identifier":"AA123459A","incomeSourceType":"interest-from-uk-banks"""")
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Taxed Bank","identifier":"AA123459A","incomeSourceType":"interest-from-uk-banks"""")
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Untaxed Bank","identifier":"AA123459A","incomeSourceType":"interest-from-uk-banks"""")
    }
    s"return ${Status.OK} with json without year" in {

      val url = s"income-tax/income-sources/nino/AA123459A?incomeSourceType=$INTEREST_FROM_UK_BANKS"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Bank","identifier":"AA123459A","incomeSourceType":"interest-from-uk-banks"""")
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Taxed Bank","identifier":"AA123459A","incomeSourceType":"interest-from-uk-banks"""")
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Untaxed Bank","identifier":"AA123459A","incomeSourceType":"interest-from-uk-banks"""")
    }
    s"return ${Status.NOT_FOUND} when no data exists for the tax year" in {

      val url = s"income-tax/income-sources/nino/AA123459A?incomeSourceType=$INTEREST_FROM_UK_BANKS&taxYear=2025"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json.toString() must include("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""")
    }
    s"return ${Status.NOT_FOUND} for other type that has not data" in {

      val url = s"income-tax/income-sources/nino/AA123459A?incomeSourceType=$UK_PENSION_BENEFITS"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
      res.json.toString() must include("""{"code":"NOT_FOUND","reason":"The remote endpoint has indicated that no data can be found."}""")
    }
    s"return ${Status.BAD_REQUEST} for invalid type" in {

      val url = s"income-tax/income-sources/nino/AA123459A?incomeSourceType=NotAType"

      val res = await(buildClient(url).get())

      res.status mustBe Status.BAD_REQUEST
      res.body mustBe """{"code":"INVALID_TYPE","reason":"Submission has not passed validation. Invalid parameter type."}"""
    }
  }

  "POST /income-tax/income-sources/nino/AA123459A" should {
    s"return $OK with json" in {

      val url = "income-tax/income-sources/nino/AA123459A"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |    "incomeSourceType": "interest-from-uk-banks",
          |    "incomeSourceName": "Santander Business"
          |}""".stripMargin)))

      res.status mustBe OK
      res.json.toString() must include("""{"incomeSourceId":"""")
    }

    s"return $BAD_REQUEST with json" in {
      val url = "income-tax/income-sources/nino/AA123459A"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |    "incomeSourceWHAT?": "interest-from-uk-banks",
          |    "incomeSourceName": "Santander Business"
          |}""".stripMargin)))

      res.status mustBe BAD_REQUEST
      res.json.toString() must include("""{"code":"SCHEMA_ERROR","reason":"The request body provided does not conform to the CreateIncomeSourceSchema."}""")
    }
  }

  "POST /income-tax/nino/AA123459A/income-source/dividends/annual/2020" should {
    s"return $OK with json" in {

      val url = "income-tax/nino/AA123459A/income-source/dividends/annual/2020"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |  "ukDividends": 55844806400.99,
          |  "otherUkDividends": 60267421355.99
          |}""".stripMargin)))

      res.status mustBe OK
      res.json.toString() must include("""{"transactionReference":"""")
    }

    s"return $BAD_REQUEST with json" in {

      val url = "income-tax/nino/AA123459A/income-source/dividends/annual/2020"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |  "ukDIVIWHAT??": 55844806400.99,
          |  "otherUkDividends": 60267421355.99
          |}""".stripMargin)))

      res.status mustBe BAD_REQUEST
      res.json.toString() must include("""{"code":"SCHEMA_ERROR","reason":"The request body provided does not conform to the CreateUpdateIncomeSourceSchema."}""")
    }
  }

  "Stubbing Errors" when {

    "request to getListOfIncomeSources where nino is DES_500_NINO" should {

      s"return $INTERNAL_SERVER_ERROR with json" in {
        val url = s"income-tax/income-sources/nino/$DES_500_NINO?incomeSourceType=interest-from-uk-banks"

        val res = await(buildClient(url).get())

        res.status mustEqual INTERNAL_SERVER_ERROR
        res.body mustEqual Json.toJson(DES_500_ERROR_MODEL).toString()
      }
    }

    "request to getListOfIncomeSources where nino is DES_503_NINO" should {

      s"return $SERVICE_UNAVAILABLE with json" in {
        val url = s"income-tax/income-sources/nino/$DES_503_NINO?incomeSourceType=interest-from-uk-banks"

        val res = await(buildClient(url).get())

        res.status mustEqual SERVICE_UNAVAILABLE
        res.body mustEqual Json.toJson(DES_503_ERRORS_MODEL).toString()
      }
    }

    "request to getIncomeSource where nino is DES_500_NINO" should {

      s"return $INTERNAL_SERVER_ERROR with json" in {
        val url = s"income-tax/nino/$DES_500_NINO/income-source/taxed/annual/2022?incomeSourceType=savings"

        val res = await(buildClient(url).get())

        res.status mustEqual INTERNAL_SERVER_ERROR
        res.body mustEqual Json.toJson(DES_500_ERROR_MODEL).toString()
      }
    }

    "request to getIncomeSource where nino is DES_503_NINO" should {

      s"return $SERVICE_UNAVAILABLE with json" in {
        val url = s"income-tax/nino/$DES_503_NINO/income-source/taxed/annual/2022?incomeSourceType=savings"

        val res = await(buildClient(url).get())

        res.status mustEqual SERVICE_UNAVAILABLE
        res.body mustEqual Json.toJson(DES_503_ERRORS_MODEL).toString()
      }
    }
  }

  "POST /income-tax/nino/AA123459A/income-source/savings/annual/2020" should {
    s"return ${Status.OK} with json" in {

      val url = "income-tax/nino/AA123459A/income-source/savings/annual/2020"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |  "incomeSourceId": "123456789012345",
          |  "taxedUkInterest": 60267421355.99,
          |  "untaxedUkInterest": 60267421355.99
          |}""".stripMargin)))

      res.status mustBe Status.OK
      res.json.toString() must include("""{"transactionReference":"""")
    }

    s"return ${Status.BAD_REQUEST} with json when id is too small" in {

      val url = "income-tax/nino/AA123459A/income-source/savings/annual/2020"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |  "incomeSourceId": "00001",
          |  "taxedUkInterest": 60267421355.99,
          |  "untaxedUkInterest": 60267421355.99
          |}""".stripMargin)))

      res.status mustBe Status.BAD_REQUEST
      res.json.toString() must include("""{"code":"SCHEMA_ERROR","reason":"The request body provided does not conform to the CreateUpdateIncomeSourceSchema."}""")
    }
  }

  "POST /income-tax/nino/AA123459A/income-source/charity/annual/2020" should {
    s"return ${Status.OK} with json" in {

      val url = "income-tax/nino/AA123459A/income-source/charity/annual/2020"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |   "giftAidPayments": {
          |      "nonUkCharitiesCharityNames": [
          |         "abcdefghijklmnopqr"
          |      ],
          |      "currentYear": 23426505146.99,
          |      "oneOffCurrentYear": 80331713889.99,
          |      "currentYearTreatedAsPreviousYear": 44753493320.99,
          |      "nextYearTreatedAsCurrentYear": 88970014371.99,
          |      "nonUkCharities": 77143081269.00
          |   },
          |   "gifts": {
          |      "investmentsNonUkCharitiesCharityNames": [
          |         "abcdefghijklmnopqr"
          |      ],
          |      "landAndBuildings": 11200049718.00,
          |      "sharesOrSecurities": 82198960626.00,
          |      "investmentsNonUkCharities": 24966390172.00
          |   }
          |}""".stripMargin)))

      res.status mustBe Status.OK
      res.json.toString() must include("""{"transactionReference":"""")
    }

    s"return ${Status.BAD_REQUEST} with json when invalid field" in {

      val url = "income-tax/nino/AA123459A/income-source/charity/annual/2020"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |   "giftAidPayments": {
          |      "nonUkCharitiesCharityNames": [
          |         "abcdefghijklmnopqr"
          |      ],
          |      "currentBear": 23426505146.99
          |   },
          |   "gifts": {
          |      "investmentsNonUkCharitiesCharityNames": [
          |         "abcdefghijklmnopqr"
          |      ],
          |      "landAndBuildings": 11200049718.00,
          |      "sharesOrSecurities": 82198960626.00,
          |      "investmentsNonUkCharities": 24966390172.00
          |   }
          |}""".stripMargin)))

      res.status mustBe Status.BAD_REQUEST
      res.json.toString() must include("""{"code":"SCHEMA_ERROR","reason":"The request body provided does not conform to the CreateUpdateIncomeSourceSchema."}""")
    }
  }

}
