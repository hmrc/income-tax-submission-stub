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

package models.errors

import models.errors.StubErrors.{DES_500_ERROR_MODEL, DES_503_ERRORS_MODEL}
import filters.StubErrorFilter
import filters.StubErrorFilter.{DES_500_NINO, DES_503_NINO}
import models.{ErrorBodyModel, ErrorsBodyModel}
import play.api.mvc.{RequestHeader, Result, Results}
import play.api.test.{DefaultAwaitTimeout, FakeRequest, Helpers}
import testUtils.TestSupport

import scala.concurrent.Future

class StubErrorFilterTest extends TestSupport with Results with DefaultAwaitTimeout {

  val stubErrorFilter = new StubErrorFilter()

  def nextFilter(request: RequestHeader): Future[Result] = Future(Ok("Response ok"))

  "StubErrorFilter" when {

    Seq(
      s"/income-tax/income-sources/nino/$DES_500_NINO",
      s"/income-tax/nino/$DES_500_NINO/income-source/taxed/annual/2022",
      s"/income-tax/nino/$DES_500_NINO/income-source/untaxed/annual/2022",
      s"/income-tax/nino/$DES_500_NINO/taxYear/2022/tax-calculation"
    ).foreach { requestPath =>

      s"request to $requestPath where nino is a DES_500_NINO" should {
        val request = FakeRequest("GET", requestPath)
        val result = stubErrorFilter(nextFilter _)(request)

        "status is 500" in {
          Helpers.status(result) mustEqual 500
        }

        "response body is des error" in {
          Helpers.contentAsJson(result).as[ErrorBodyModel] mustEqual DES_500_ERROR_MODEL
        }
      }
    }

    Seq(
      s"/income-tax/income-sources/nino/$DES_503_NINO",
      s"/income-tax/nino/$DES_503_NINO/income-source/taxed/annual/2022",
      s"/income-tax/nino/$DES_503_NINO/income-source/untaxed/annual/2022",
      s"/income-tax/nino/$DES_503_NINO/taxYear/2022/tax-calculation"
    ).foreach { requestPath =>

      s"request to $requestPath where nino is a DES_503_NINO" should {
        val request = FakeRequest("GET", requestPath)
        val result = stubErrorFilter(nextFilter _)(request)

        "status is 503" in {
          Helpers.status(result) mustEqual 503
        }

        "response body is des error" in {
          Helpers.contentAsJson(result).as[ErrorsBodyModel] mustEqual DES_503_ERRORS_MODEL
        }
      }
    }
  }

}