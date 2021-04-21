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

import models.APIUser
import play.api.libs.json.JsValue
import play.api.mvc.{Action, ControllerComponents}
import services.UserDataService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global


class UserDataController @Inject()(dataService: UserDataService,
                                   cc: ControllerComponents) extends BackendController(cc) {

  val postUser: Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[APIUser]( model => dataService.insertUser(model)
    ) recover {
      case ex => BadRequest(s"Error Parsing Json DataModel: \n $ex")
    }
  }

}
