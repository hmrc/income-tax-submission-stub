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

import models.{ErrorBodyModel, ErrorsBodyModel}

object StubErrors {

  val DES_500_ERROR_MODEL: ErrorBodyModel = ErrorBodyModel("SERVER_ERROR", "DES is currently experiencing problems that require live service intervention.")
  val DES_503_ERRORS_MODEL: ErrorsBodyModel = ErrorsBodyModel(
    Seq(ErrorBodyModel("SERVICE_UNAVAILABLE", "Service A is Down"), ErrorBodyModel("SERVICE_UNAVAILABLE", "Service B is Down"))
  )

}
