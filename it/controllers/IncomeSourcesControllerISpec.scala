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

import play.api.Application
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import utils.IntegrationTest

class IncomeSourcesControllerISpec extends IntegrationTest with FutureAwaits with DefaultAwaitTimeout {

  implicit val application: Application = app

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
}
