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

import mocks.MockDataRepository
import models.{DataModel, SubmissionModel}
import play.api.libs.json.Json
import play.api.test.Helpers.{defaultAwaitTimeout, status}
import play.api.test.FakeRequest
import play.mvc.Http.Status
import testUtils.TestSupport

import scala.concurrent.Future

class SetupDataControllerSpec extends TestSupport with MockDataRepository {

  object TestSetupDataController extends SetupDataController(mockDataRepository, cc)

  "SetupDataController .addData" when {

    "request body is valid" when {

      "method is GET or POST" should {

        val model: SubmissionModel = SubmissionModel(
          uri = "1234",
          method = "GET",
          response = Some(Json.parse("{}")),
          status = Status.OK
        )
        val dataModel: DataModel = DataModel(
          _id = "1234GET",
          submission = model
        )


        mockAddEntry(dataModel)(Future.successful(successWriteResult))

        lazy val request = FakeRequest().withBody(Json.toJson(model)).withHeaders(("Content-Type", "application/json"))
        lazy val result = TestSetupDataController.addData(request)

        "return an OK" in {
          status(result) mustBe Status.OK
        }
      }

      "method is PUT" should {

        val model: SubmissionModel = SubmissionModel(
          uri = "1234",
          method = "PUT",
          response = Some(Json.parse("{}")),
          status = Status.OK
        )
        val dataModel: DataModel = DataModel(
          _id = "1234PUT",
          submission = model
        )

        lazy val request = FakeRequest().withBody(Json.toJson(model)).withHeaders(("Content-Type", "application/json"))
        lazy val result = TestSetupDataController.addData(request)

        "return 405" in {
          status(result) mustBe Status.METHOD_NOT_ALLOWED
        }
      }
    }

    "request body is not valid" should {

      lazy val request = FakeRequest().withBody(Json.obj("key" -> "value")).withHeaders(("Content-Type", "application/json"))
      lazy val result = TestSetupDataController.addData(request)

      "return 400" in {
        status(result) mustBe Status.BAD_REQUEST
      }
    }
  }

  "SetupDataController.removeAllData" when {

    "removal is successful" should {

      lazy val request = FakeRequest()
      lazy val result = TestSetupDataController.removeAll(request)

      "return 200" in {
        mockRemoveAll()(Future.successful(successWriteResult))

        status(result) mustBe Status.OK
      }
    }

    "removal is unsuccessful" should {

      lazy val request = FakeRequest()
      lazy val result = TestSetupDataController.removeAll(request)

      "return 500" in {
        mockRemoveAll()(Future.successful(errorWriteResult))

        status(result) mustBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }
}
