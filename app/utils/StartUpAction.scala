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

import models.APIUsers.APIUser
import models.Users

import javax.inject.{Inject, Singleton}
import repositories.UserRepository

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

@Singleton
class StartUpAction @Inject()(userRepository: UserRepository,
                              implicit val ec: ExecutionContext) {

  def initialiseUsers(): Future[Seq[APIUser]] = {
    Future.sequence(Users.users.map(user => userRepository.insertUser(user))).map(_.flatten)
  }
  Await.result(initialiseUsers(), 5.seconds)
}