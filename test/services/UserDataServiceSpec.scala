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
import play.api.libs.json.Json
import play.api.mvc.{Result, Results}
import play.api.test.Helpers._
import repositories.UserRepository
import testUtils.TestSupport

import scala.concurrent.{ExecutionContext, Future}

class UserDataServiceSpec extends TestSupport with Results {

  lazy val mockUserRepository: UserRepository = mock[UserRepository]
  val userDataService = new UserDataService(mockUserRepository, ec)

  ".insertUser" should {
    val user = APIUser("NINO")

    "return Created when a new user is added to the database" in {
      (mockUserRepository.insertUser(_: APIUser)(_: ExecutionContext))
        .expects(user, *)
        .returning(Future.successful(Some(user)))

      val result = userDataService.insertUser(APIUser("NINO"))
      status(result) mustBe CREATED
    }

    "return an InternalServerError when a new user is not added to the database" in {
      (mockUserRepository.insertUser(_: APIUser)(_: ExecutionContext))
        .expects(user, *)
        .returning(Future.successful(None))

      val result = userDataService.insertUser(APIUser("NINO"))
      status(result) mustBe INTERNAL_SERVER_ERROR
    }

    "return an InternalServerError when the repository query fails" in {
      (mockUserRepository.insertUser(_: APIUser)(_: ExecutionContext))
        .expects(user, *)
        .returning(Future.failed(new RuntimeException("Could not connect to mongo")))

      val result = userDataService.insertUser(APIUser("NINO"))
      status(result) mustBe INTERNAL_SERVER_ERROR
    }
  }

  ".findUser" should {
    val nino = "AA123456A"
    val user = APIUser(nino)
    def fn(user: APIUser): Future[Result] = { Future(Ok) }

    "find a user in the database" in {
      (mockUserRepository.findByNino(_: String)(_: ExecutionContext))
        .expects(nino, *)
        .returning(Future.successful(Some(user)))

      val result = userDataService.findUser(nino)(fn)
      status(result) mustBe OK
    }

    "cannot find user in the database" in {
      (mockUserRepository.findByNino(_: String)(_: ExecutionContext))
        .expects(nino, *)
        .returning(Future.successful(None))

      val result = userDataService.findUser(nino)(fn)

      status(result) mustBe NOT_FOUND
      contentAsJson(result) mustBe Json.obj("code" -> "NOT_FOUND", "message" -> "The remote endpoint has indicated that no data can be found.")
    }

    "return an InternalServerError when the repository query fails" in {
      mockUserRepository.findByNino("AA123456A")
      (mockUserRepository.findByNino(_: String)(_: ExecutionContext))
        .expects(nino, *)
        .returning(Future.failed(new RuntimeException("Could not connect to mongo")))

      val result = userDataService.findUser(nino)(fn)
      status(result) mustBe INTERNAL_SERVER_ERROR
    }
  }

}
