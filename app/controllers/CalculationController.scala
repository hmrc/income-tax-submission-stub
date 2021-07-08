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

import javax.inject.Inject
import play.api.Logging
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.RandomIdGenerator.randomId
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CalculationController @Inject()(cc: ControllerComponents) extends BackendController(cc) with Logging {

  def generateCalculationId(nino: String, taxYear: Int): Action[JsValue] = Action.async(parse.json) { implicit request =>

    logger.info(s"Generating calculation id for nino: $nino, taxYear: $taxYear")

    if (request.body != Json.parse("{}")) {
      val message = s"[generateCalculationId] API needs empty json supplied. Nino with request: $nino"
      logger.error(message)
      Future(BadRequest(message))
    } else {
      Future(Ok(Json.parse(s"""{"id": "$randomId"}""".stripMargin)))
    }
  }

  // DES #1416 - v1.0.0 //
  def declareCrystallisationForTaxYear(nino: String, taxYear: Int, calcId: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    if (request.body != Json.parse("{}")) {
      val message = s"[declareCrystallisationForTaxYear] API needs empty json supplied. Nino with request: $nino"
      logger.error(message)
      Future(BadRequest(message))
    } else {
      Future(NoContent)
    }
  }
}
