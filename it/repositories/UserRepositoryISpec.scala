

package repositories

import org.scalatest.BeforeAndAfterEach
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import utils.IntegrationTest

class UserRepositoryISpec extends IntegrationTest with FutureAwaits with DefaultAwaitTimeout with BeforeAndAfterEach {

  val repo: UserRepository = app.injector.instanceOf[UserRepository]

  "removeAll" should {
    "remove all users" in {

    }
  }

  "removeByNino" should {
    "remove a user by their nino" in {

    }
  }

  "insertUser" should {
    "insert a new user" in {

    }
    "update data if user already exists" in {

    }
  }

  "findByNino" should {
    "find a user by using their nino" in {

    }
  }

  "find" should {
    "find a user" in {

    }
  }

  "bulkInsert" should {
    "insert full list of users" in {

    }
  }

}
