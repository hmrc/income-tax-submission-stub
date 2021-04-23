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

package repositories

import models.{APIUser, Dividends}
import org.scalatest.BeforeAndAfterEach
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import utils.IntegrationTest

import scala.concurrent.Future

class UserRepositoryISpec extends IntegrationTest with FutureAwaits with DefaultAwaitTimeout with BeforeAndAfterEach {

  val repo: UserRepository = app.injector.instanceOf[UserRepository]

  override protected def beforeEach(): Unit = {
    await(repo.removeAll())
  }

  "removeAll" should {
    "remove all users" in {
      await(repo.insertUser(APIUser("AA")))
      await(repo.insertUser(APIUser("AB")))

      await(repo.removeAll())

      val usersInRepo = await(Future.sequence(Seq("AA", "AB").map(nino => repo.findByNino(nino))))
      usersInRepo.exists(_.isDefined) mustBe false
    }
  }

  "removeByNino" should {
    "remove a user by their nino" in {
      val user = APIUser("AA12345DD")
      await(repo.insertUser(user))
      await(repo.findByNino(user.nino)) mustBe Some(user)

      await(repo.removeByNino(user.nino))

      await(repo.findByNino(user.nino)) mustBe None
    }
  }

  "insertUser" when {

    val user = APIUser("AA030405A")

    "user does not exist" should {
      "insert a user" in {
        await(repo.insertUser(user))

        await(repo.findByNino(user.nino)) mustBe Some(user)
      }
    }

    "user does exist" should {
      "update the existing user with new user data" in {
        await(repo.insertUser(user))
        await(repo.findByNino(user.nino)) mustBe Some(user)

        val updatedUser = user.copy(dividends = Seq(Dividends(2022, Some(9.99), None)))
        await(repo.insertUser(updatedUser))

        await(repo.findByNino(user.nino)) mustBe Some(updatedUser)
      }
    }
  }

  "findByNino" should {
    "find a user by using their nino" in {
      val user = APIUser("AA000001A")
      await(repo.insertUser(user))
      await(repo.findByNino(user.nino)) mustBe Some(user)
    }
  }

}
