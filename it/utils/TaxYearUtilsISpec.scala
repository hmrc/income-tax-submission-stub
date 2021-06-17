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

package utils

import play.api.Application
import play.api.http.Status
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}

class TaxYearUtilsISpec extends IntegrationTest with FutureAwaits with DefaultAwaitTimeout with Status {

  implicit val application: Application = app

  "Tax year predicate" should {

    val invalidTaxYear = "2022"
    val validTaxYear = "2021-22"

    s"returns expected result with valid tax year" in {

      val url = s"income-tax/income/employments/AB200900/$validTaxYear/01312"

      val res = await(buildClient(url).delete())

      res.status mustBe NO_CONTENT
    }


    s"return $BAD_REQUEST with invalid tax year" in {

      val url = s"income-tax/income/employments/AB200900/$invalidTaxYear/01312"

      val res = await(buildClient(url).delete())

      res.status mustBe BAD_REQUEST
      res.json.toString must include("""{"code":"TAX_YEAR_ERROR","reason":"Tax year is in the incorrect format"}""")
    }
  }
}
