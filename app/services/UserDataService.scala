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

package services

import models.APIUser
import play.api.Logging
import play.api.mvc.Result
import play.api.mvc.Results._
import repositories.UserRepository
import utils.JsonValidation

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UserDataService @Inject()(dataRepository: UserRepository) extends JsonValidation with Logging {

   def insertUser(user: APIUser)(implicit ec: ExecutionContext): Future[Result] = {
     dataRepository.insertUser(user).map {
       case Some(value) =>
         logger.info(s"New user created (${value.nino})")
         Created
       case None =>
         logger.error(s"Failed to add user to Stub (${user.nino})")
         InternalServerError(s"Failed to add data to Stub.")
     }
    }
}
