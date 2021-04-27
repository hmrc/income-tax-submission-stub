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

import models.{APIUser, Users}
import org.scalamock.scalatest.MockFactory
import play.api.mvc.Results
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.api.commands.{UpdateWriteResult, WriteError}
import repositories.UserRepository
import services.UserDataService
import testUtils.TestSupport
import utils.StartUpAction

import scala.concurrent.{ExecutionContext, Future}

class UserDataControllerSpec extends TestSupport with MockFactory with Results {

  lazy val mockUserDataService: UserDataService = mock[UserDataService]

  lazy val mockStartUpAction: StartUpAction = new StartUpAction(mockUserRepository, ec)

  lazy val successWriteResult: UpdateWriteResult = UpdateWriteResult(ok = true, n = 1, nModified = 0, upserted = Seq(), writeErrors = Seq(), None, None, None)
  lazy val errorWriteResult: UpdateWriteResult = UpdateWriteResult(ok = false, n = 1, nModified = 0, upserted = Seq(), writeErrors = Seq(WriteError(1, 1, "Error")), None, None, None)

  lazy val mockUserRepository: UserRepository = mock[UserRepository]

  lazy val controller = new UserDataController(mockUserDataService, mockUserRepository, mockStartUpAction, cc, ec)

  ".resetUsers" should {

    "return 200 when database is reset to default users" which {
      lazy val request = FakeRequest()

      lazy val result = {
        (mockUserRepository.insertUser(_: APIUser)(_: ExecutionContext))
          .expects(*, *)
          .returning(Future.successful(Some(APIUser("AA123456A"))))
          .repeat(Users.users.length * 2)

        (mockUserRepository.removeAll()(_: ExecutionContext))
          .expects(*)
          .returning(Future.successful(successWriteResult))

        controller.resetUsers(request)
      }
      "status must be" in {
        status(result) mustBe OK
      }
    }

    "return an InternalServerError when default users are not added back to database" which {
      lazy val request = FakeRequest()

      lazy val result = {
        (mockUserRepository.removeAll()(_: ExecutionContext))
          .expects(*)
          .returning(Future.successful(successWriteResult))

        (mockUserRepository.insertUser(_: APIUser)(_: ExecutionContext))
          .expects(*, *)
          .returning(Future.successful(None))
          .repeat(Users.users.length)

        controller.resetUsers(request)
      }

      "status must be" in {
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }

    "return an InternalServerError when database users are not removed from database" which {

      lazy val request = FakeRequest()

      lazy val result = {
        (mockUserRepository.removeAll()(_: ExecutionContext))
          .expects(*)
          .returning(Future(errorWriteResult))

        controller.resetUsers(request)
      }
      "status must be" in {
        status(result) mustBe INTERNAL_SERVER_ERROR
      }
    }
  }
}
