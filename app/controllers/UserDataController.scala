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

import models.APIModels.APIUser
import models.Users
import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import repositories.UserRepository
import services.UserDataService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.StartUpAction

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}


class UserDataController @Inject()(dataService: UserDataService,
                                   userRepository: UserRepository,
                                   startUpAction: StartUpAction,
                                   cc: ControllerComponents,
                                   implicit val ec: ExecutionContext) extends BackendController(cc) {

  def postUser: Action[JsValue] = Action.async(parse.json) { implicit request =>
    withJsonBody[APIUser](model => dataService.insertUser(model))
  }

  def resetUsers: Action[AnyContent] = Action.async { _ =>
    userRepository.removeAll().flatMap {
      case result if result.wasAcknowledged() =>
        startUpAction.initialiseUsers().map {
          start =>
            if (start.length == Users.users.length) {
              Ok("MongoDB users were reset to default")
            } else {
              InternalServerError("Unexpected Error Resetting Users.")
            }
        }
      case _ => Future(InternalServerError("Unexpected Error Clearing MongoDB."))
    }
  }
}
