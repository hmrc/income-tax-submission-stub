/*
 * Copyright 2020 HM Revenue & Customs
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
import models.DataModel
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.call
import play.mvc.Http.Status
import testUtils.TestSupport

class RequestHandlerControllerSpec extends TestSupport with MockDataRepository {

  lazy val TestRequestHandlerController: RequestHandlerController = new RequestHandlerController(mockDataRepository, cc)

  lazy val successModel = DataModel(
    _id = "test",
    uri = "test",
    method = "GET",
    status = Status.OK,
    response = None
  )

  lazy val successWithBodyModel = DataModel(
    _id = "test",
    uri = "test",
    method = "GET",
    status = Status.OK,
    response = Some(Json.parse("""{"something" : "hello"}"""))
  )

  "The getRequestHandler method" should {

    "return the status code specified in the model" in {
      lazy val result = {
        mockFind(Some(successModel)).twice()
        TestRequestHandlerController.getRequestHandler("/test")(FakeRequest())
      }

      status(result) shouldBe Status.OK
    }

    "return the status and body" in {
      lazy val result = TestRequestHandlerController.getRequestHandler("/test")(FakeRequest())

      mockFind(Some(successWithBodyModel)).twice()
      status(result) shouldBe Status.OK
      await(bodyOf(result)) shouldBe s"${successWithBodyModel.response.get}"
    }

    "return a 404 status when the endpoint cannot be found" in {
      lazy val result = TestRequestHandlerController.getRequestHandler("/test")(FakeRequest())

      mockFind(None).twice()
      status(result) shouldBe Status.NOT_FOUND
    }
  }

  "The postRequestHandler method" when {

    "matching data is found" when {

      "the data has a schemaId" when {

        "request JSON validates against the schema" when {

          "the data has a response body" should {

            val model = DataModel(
              _id = "test",
              uri = "test",
              method = "POST",
              status = Status.OK,
              response = Some(Json.obj("hello" -> "world"))
            )

            lazy val request = FakeRequest("POST", "/").withBody(Json.obj("" -> ""))
            lazy val result = {
              mockFind(Some(model))
              call(TestRequestHandlerController.postRequestHandler("url"), request)
            }

            "return the status" in {
              await(status(result)) shouldBe Status.OK
            }

            "return the body" in {
              await(jsonBodyOf(result)) shouldBe Json.toJson(model.response)
            }
          }

          "the data has no response body" should {

            val model = DataModel(
              _id = "test",
              uri = "test",
              method = "POST",
              status = Status.OK,
              response = None
            )

            lazy val request = FakeRequest("POST", "/").withBody(Json.toJson(model))
            lazy val result = {
              mockFind(Some(model))
              call(TestRequestHandlerController.postRequestHandler("url"), request)
            }

            "return the status" in {
              await(status(result)) shouldBe Status.OK
            }
          }
        }
      }
    }

    "no matching data is found" should {

      lazy val request = FakeRequest("POST", "/").withBody(Json.obj("" -> ""))
      lazy val result = {
        mockFind(None)
        call(TestRequestHandlerController.postRequestHandler("url"), request)
      }

      "return 404" in {
        await(status(result)) shouldBe Status.BAD_REQUEST
      }
    }
  }
}
