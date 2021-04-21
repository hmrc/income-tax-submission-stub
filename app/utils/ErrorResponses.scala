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
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results._

object ErrorResponses {
  val notFound: Result = NotFound

  val userNotFound: Result = NotFound(Json.obj(
    "code" -> "NOT_FOUND",
    "message" -> "The remote endpoint has indicated that no data can be found.")
  )

  val incomeSourceTypeNotFound: Result = BadRequest(Json.obj(
    "code" -> "INVALID_TYPE",
    "message" -> "Submission has not passed validation. Invalid parameter type.")
  )
}
