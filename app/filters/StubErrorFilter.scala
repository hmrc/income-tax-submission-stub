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

package filters

import akka.stream.Materializer
import models.errors.StubErrors.{DES_500_ERROR_MODEL, DES_503_ERRORS_MODEL}
import filters.StubErrorFilter.{DES_500_NINO, DES_503_NINO}
import play.api.libs.json.Json
import play.api.mvc.{Filter, RequestHeader, Result, Results}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class StubErrorFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext) extends Filter with Results {
  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {

    val NinoPattern = ".*/nino/([a-zA-Z0-9]+).*".r

    requestHeader.path match {
      case NinoPattern(nino) if nino.equalsIgnoreCase(DES_500_NINO) => Future(InternalServerError(Json.toJson(DES_500_ERROR_MODEL)))
      case NinoPattern(nino) if nino.equalsIgnoreCase(DES_503_NINO) => Future(ServiceUnavailable(Json.toJson(DES_503_ERRORS_MODEL)))
      case _ => nextFilter(requestHeader)
    }

  }
}

object StubErrorFilter {

  val DES_500_NINO = "AA999901AA"
  val DES_503_NINO = "AA999902AA"

}