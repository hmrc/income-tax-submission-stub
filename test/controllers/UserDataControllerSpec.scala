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

import com.mongodb.client.result.DeleteResult
import models.APIModels.APIUser
import models.Users
import org.mongodb.scala.result.DeleteResult
import org.scalamock.scalatest.MockFactory
import play.api.mvc.Results
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.UserRepository
import services.UserDataService
import testUtils.TestSupport
import utils.StartUpAction

import scala.concurrent.{ExecutionContext, Future}

class UserDataControllerSpec extends TestSupport with MockFactory with Results {

  lazy val mockUserDataService: UserDataService = mock[UserDataService]

  lazy val mockStartUpAction: StartUpAction = new StartUpAction(mockUserRepository, ec)

  lazy val successWriteResult: DeleteResult = {
    DeleteResult.acknowledged(5)
  }
  lazy val errorWriteResult: DeleteResult = {
    DeleteResult.unacknowledged()
  }

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
