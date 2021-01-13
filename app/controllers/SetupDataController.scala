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
import models.{DataModel, SubmissionModel}
import models.HttpMethod._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import repositories.DataRepository
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SetupDataController @Inject()(dataRepository: DataRepository,
                                    cc: ControllerComponents) extends BackendController(cc) {

  val addData: Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[SubmissionModel]( json =>
      json.method.toUpperCase match {
        case GET | POST => addStubDataToDB(json)
        case x => Future.successful(MethodNotAllowed(s"The method: $x is currently unsupported"))
      }
    ) recover {
      case ex => BadRequest(s"Error Parsing Json DataModel: \n $ex")
    }
  }

  private def addStubDataToDB(json: SubmissionModel): Future[Result] = {
    val dataJson = new DataModel(json.uri + json.method, json)
    dataRepository.addEntry(dataJson).map {
      case result if result.ok => Ok(s"The following JSON was added to the stub: \n\n ${Json.toJson(json)}")
      case _ => InternalServerError(s"Failed to add data to Stub.")
    }
  }

  val removeAll: Action[AnyContent] = Action.async { implicit request =>
    dataRepository.removeAll().map {
      case result if result.ok => Ok("Removed All Stubbed Data")
      case _ => InternalServerError("Unexpected Error Clearing MongoDB.")
    }
  }
}
