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

import testUtils.TestSupport

class TaxYearConversionSpec extends TestSupport {

  "The TaxYearConversion" should {

    s"return int from string" in {
      TaxYearConversion.convertStringTaxYear("2021-22") mustBe 2022
      TaxYearConversion.convertStringTaxYear("2099-00") mustBe 2100
      TaxYearConversion.convertStringTaxYear("2029-30") mustBe 2030
    }
  }
}
