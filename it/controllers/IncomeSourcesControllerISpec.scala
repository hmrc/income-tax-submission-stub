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
import models.errors.StubErrors.{DES_500_ERROR_MODEL, DES_503_ERRORS_MODEL}
import play.api.Application
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import utils.IntegrationTest

class IncomeSourcesControllerISpec extends IntegrationTest with FutureAwaits with DefaultAwaitTimeout with Status {

  implicit val application: Application = app

  "POST /income-tax/income-sources/nino/AA123456A" should {
    s"return $OK with json" in {

      val url = "income-tax/income-sources/nino/AA123456A"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |    "incomeSourceType": "interest-from-uk-banks",
          |    "incomeSourceName": "Santander Business"
          |}""".stripMargin)))

      res.status mustBe OK
      res.json.toString() must include("""{"incomeSourceId":"""")
    }

    s"return $BAD_REQUEST with json" in {
      val url = "income-tax/income-sources/nino/AA123456A"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |    "incomeSourceWHAT?": "interest-from-uk-banks",
          |    "incomeSourceName": "Santander Business"
          |}""".stripMargin)))

      res.status mustBe BAD_REQUEST
      res.json.toString() must include("""{"code":"SCHEMA_ERROR","reason":"The request body provided does not conform to the CreateIncomeSourceSchema."}""")
    }
  }

  "POST /income-tax/nino/AA123456A/income-source/dividends/annual/2020" should {
    s"return $OK with json" in {

      val url = "income-tax/nino/AA123456A/income-source/dividends/annual/2020"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |  "ukDividends": 55844806400.99,
          |  "otherUkDividends": 60267421355.99
          |}""".stripMargin)))

      res.status mustBe OK
      res.json.toString() must include("""{"transactionReference":"""")
    }

    s"return $BAD_REQUEST with json" in {

      val url = "income-tax/nino/AA123456A/income-source/dividends/annual/2020"

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

        println(res.body)
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
}
