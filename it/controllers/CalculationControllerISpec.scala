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

class CalculationControllerISpec extends IntegrationTest with FutureAwaits with DefaultAwaitTimeout {

  implicit val application: Application = app

  "POST /income-tax/nino/AA123456A/taxYear/2022/tax-calculation" should {
    s"return ${Status.OK} with json" in {

      val url = "income-tax/nino/AA123456A/taxYear/2022/tax-calculation"

      val result = await(buildClient(url).post(Json.parse("""{}""".stripMargin)))

      result.status mustBe Status.OK
      result.json.toString() must include("""{"id":"""")
    }

    s"return ${Status.BAD_REQUEST} with json" in {

      val url = "income-tax/nino/AA123456A/taxYear/2022/tax-calculation"

      val result = await(buildClient(url).post(Json.parse("""[]""".stripMargin)))

      result.status mustBe Status.BAD_REQUEST
      result.body must include("""API needs empty json supplied.""")
    }
  }


  "POST /income-tax/calculation/nino/AA123456A/2022/041f7e4d-87b9-4d4a-a296-3cfbdf92f7e2/declare-crystallisation" should {
    s"return ${Status.NO_CONTENT} with json" in {

      val url = "income-tax/calculation/nino/AA123456A/2022/041f7e4d-87b9-4d4a-a296-3cfbdf92f7e2/declare-crystallisation"

      val result = await(buildClient(url).post(Json.parse("""{}""".stripMargin)))

      result.status mustBe Status.NO_CONTENT
    }

    s"return ${Status.BAD_REQUEST} with json" in {

      val url = "income-tax/calculation/nino/AA123456A/2022/041f7e4d-87b9-4d4a-a296-3cfbdf92f7e2/declare-crystallisation"

      val result = await(buildClient(url).post(Json.parse("""[]""".stripMargin)))

      result.status mustBe Status.BAD_REQUEST
      result.body must include("""API needs empty json supplied.""")
    }
  }


}

