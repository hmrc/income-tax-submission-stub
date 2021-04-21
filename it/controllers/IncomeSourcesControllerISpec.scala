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

import models.IncomeSourceTypes.IncomeSourceTypeB.{INTEREST_FROM_UK_BANKS, UK_PENSION_BENEFITS}
import play.api.Application
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import utils.IntegrationTest

class IncomeSourcesControllerISpec extends IntegrationTest with FutureAwaits with DefaultAwaitTimeout {

  implicit val application: Application = app

  "GET /income-tax/nino/AA123456A/income-source/savings/annual/2022?incomeSourceId=000000000000002" should {
    s"return ${Status.OK} with json" in {

      val url = s"income-tax/nino/AA123456A/income-source/savings/annual/2022?incomeSourceId=000000000000002"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json.toString() must include("""{"savingsInterestAnnualIncome":[{"incomeSourceId":"000000000000002","taxedUkInterest":99999999999.99}]}""")
    }
    s"return ${Status.NOT_FOUND}" in {

      val url = s"income-tax/nino/AA123456A/income-source/savings/annual/2022?incomeSourceId=123456789012345"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
    }
  }

  "GET /income-tax/nino/AA123456A/income-source/dividends/annual/2022" should {
    s"return ${Status.OK} with json" in {

      val url = s"income-tax/nino/AA123456A/income-source/dividends/annual/2022"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json.toString() must include("""{"ukDividends":99999999999.99}""")

    }
    s"return ${Status.NOT_FOUND}" in {

      val url = s"income-tax/nino/AA123456A/income-source/dividends/annual/2023"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
    }
  }

  "GET /income-tax/nino/AA123456A/income-source/rick/annual/2022" should {
    s"return ${Status.OK} with json" in {

      val url = s"income-tax/nino/AA123456A/income-source/rick/annual/2022"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND

    }
  }

  "GET /income-tax/nino/AA123456A/income-source/charity/annual/2022" should {
    s"return ${Status.OK} with json" in {

      val url = s"income-tax/nino/AA123456A/income-source/charity/annual/2022"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json.toString() must include("""{"giftAidPayments":{"nonUkCharitiesCharityNames":["Rick Owens Charity"],"currentYear":99999999999.99,"oneOffCurrentYear":99999999999.99,"currentYearTreatedAsPreviousYear":99999999999.99,"nextYearTreatedAsCurrentYear":99999999999.99,"nonUkCharities":99999999999.99},"gifts":{"investmentsNonUkCharitiesCharityNames":["Rick Owens Non-UK Charity"],"landAndBuildings":99999999999.99,"sharesOrSecurities":99999999999.99,"investmentsNonUkCharities":99999999999.99}}""")

    }
    s"return ${Status.NOT_FOUND}" in {

      val url = s"income-tax/nino/AA123456A/income-source/charity/annual/2023"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
    }
  }

  "GET /income-tax/income-sources/nino/AA123456A" should {
    s"return ${Status.OK} with json" in {

      val url = s"income-tax/income-sources/nino/AA123456A?incomeSourceType=$INTEREST_FROM_UK_BANKS&taxYear=2022"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Bank","identifier":"AA123456A","incomeSourceType":"interest-from-uk-banks"""")
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Taxed Bank","identifier":"AA123456A","incomeSourceType":"interest-from-uk-banks"""")
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Untaxed Bank","identifier":"AA123456A","incomeSourceType":"interest-from-uk-banks"""")
    }
    s"return ${Status.OK} with json without year" in {

      val url = s"income-tax/income-sources/nino/AA123456A?incomeSourceType=$INTEREST_FROM_UK_BANKS"

      val res = await(buildClient(url).get())

      res.status mustBe Status.OK
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Bank","identifier":"AA123456A","incomeSourceType":"interest-from-uk-banks"""")
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Taxed Bank","identifier":"AA123456A","incomeSourceType":"interest-from-uk-banks"""")
      res.json.toString() must include(""""incomeSourceName":"Rick Owens Untaxed Bank","identifier":"AA123456A","incomeSourceType":"interest-from-uk-banks"""")
    }
    s"return ${Status.NOT_FOUND}" in {

      val url = s"income-tax/income-sources/nino/AA123456A?incomeSourceType=$INTEREST_FROM_UK_BANKS&taxYear=2025"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
    }
    s"return ${Status.NOT_FOUND} for other type" in {

      val url = s"income-tax/income-sources/nino/AA123456A?incomeSourceType=$UK_PENSION_BENEFITS"

      val res = await(buildClient(url).get())

      res.status mustBe Status.NOT_FOUND
    }
  }

  "POST /income-tax/income-sources/nino/AA123456A" should {
    s"return ${Status.OK} with json" in {

      val url = "income-tax/income-sources/nino/AA123456A"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |    "incomeSourceType": "interest-from-uk-banks",
          |    "incomeSourceName": "Santander Business"
          |}""".stripMargin)))

      res.status mustBe Status.OK
      res.json.toString() must include("""{"incomeSourceId":"""")
    }

    s"return ${Status.BAD_REQUEST} with json" in {

      val url = "income-tax/income-sources/nino/AA123456A"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |    "incomeSourceWHAT?": "interest-from-uk-banks",
          |    "incomeSourceName": "Santander Business"
          |}""".stripMargin)))

      res.status mustBe Status.BAD_REQUEST
      res.json.toString() must include("""{"code":"ERROR","reason":"FAIL"}""")
    }
  }

  "POST /income-tax/nino/AA123456A/income-source/dividends/annual/2020" should {
    s"return ${Status.OK} with json" in {

      val url = "income-tax/nino/AA123456A/income-source/dividends/annual/2020"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |  "ukDividends": 55844806400.99,
          |  "otherUkDividends": 60267421355.99
          |}""".stripMargin)))

      res.status mustBe Status.OK
      res.json.toString() must include("""{"transactionReference":"""")
    }

    s"return ${Status.BAD_REQUEST} with json" in {

      val url = "income-tax/nino/AA123456A/income-source/dividends/annual/2020"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |  "ukDIVIWHAT??": 55844806400.99,
          |  "otherUkDividends": 60267421355.99
          |}""".stripMargin)))

      res.status mustBe Status.BAD_REQUEST
      res.json.toString() must include("""{"code":"ERROR","reason":"FAIL"}""")
    }
  }

  "POST /income-tax/nino/AA123456A/income-source/savings/annual/2020" should {
    s"return ${Status.OK} with json" in {

      val url = "income-tax/nino/AA123456A/income-source/savings/annual/2020"

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

      val url = "income-tax/nino/AA123456A/income-source/savings/annual/2020"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |  "incomeSourceId": "00001",
          |  "taxedUkInterest": 60267421355.99,
          |  "untaxedUkInterest": 60267421355.99
          |}""".stripMargin)))

      res.status mustBe Status.BAD_REQUEST
      res.json.toString() must include("""{"code":"ERROR","reason":"FAIL"}""")
    }
  }

  "POST /income-tax/nino/AA123456A/income-source/charity/annual/2020" should {
    s"return ${Status.OK} with json" in {

      val url = "income-tax/nino/AA123456A/income-source/charity/annual/2020"

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

      val url = "income-tax/nino/AA123456A/income-source/charity/annual/2020"

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
      res.json.toString() must include("""{"code":"ERROR","reason":"FAIL"}""")
    }
  }
}
