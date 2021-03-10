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
import play.api.test.FakeRequest
import play.api.test.Helpers.{call, contentAsJson, contentAsString, defaultAwaitTimeout, status}
import play.mvc.Http.Status
import testUtils.TestSupport

import scala.concurrent.Future

class RequestHandlerControllerSpec extends TestSupport with MockDataRepository {

  lazy val TestRequestHandlerController: RequestHandlerController = new RequestHandlerController(mockDataRepository, cc)

  lazy val successModel = DataModel(
    _id = "test",
    SubmissionModel(uri = "test",
    method = "GET",
    status = Status.OK,
    response = None)
  )

  lazy val successWithBodyModel = DataModel(
    _id = "test",
    SubmissionModel(uri = "test",
    method = "GET",
    status = Status.OK,
    response = Some(Json.parse("""{"something" : "hello"}""")))
  )

  "The getRequestHandler method" should {

    "return the status code specified in the model" in {
      lazy val result = {
        mockFind(Future.successful(Some(successModel))).twice()
        TestRequestHandlerController.getRequestHandler("/test")(FakeRequest())
      }

      status(result) mustBe Status.OK
    }

    "return the status and body" in {
      lazy val result = TestRequestHandlerController.getRequestHandler("/test")(FakeRequest())

      mockFind(Future.successful(Some(successWithBodyModel))).twice()
      status(result) mustBe Status.OK
      contentAsString(result) mustBe s"${successWithBodyModel.submission.response.get}"
    }

    "return a 404 status when the endpoint cannot be found" in {
      lazy val result = TestRequestHandlerController.getRequestHandler("/test")(FakeRequest())

      mockFind(Future.successful(None)).twice()
      status(result) mustBe Status.NOT_FOUND
    }
  }

  "The postRequestHandler method" when {

    "matching data is found" when {

      "the data has a schemaId" when {

        "request JSON validates against the schema" when {

          "the data has a response body" should {

            val model = DataModel(
              _id = "test",
              SubmissionModel(uri = "test",
              method = "POST",
              status = Status.OK,
              response = Some(Json.obj("hello" -> "world")))
            )

            lazy val request = FakeRequest("POST", "/").withBody(Json.obj("" -> ""))
            lazy val result = {
              mockFind(Future.successful(Some(model)))
              call(TestRequestHandlerController.postRequestHandler("url"), request)
            }

            "return the status" in {
              status(result) mustBe Status.OK
            }

            "return the body" in {
              contentAsJson(result) mustBe Json.toJson(model.submission.response)
            }
          }

          "the data has no response body" should {

            val model = DataModel(
              _id = "test",
              SubmissionModel(uri = "test",
              method = "POST",
              status = Status.OK,
              response = None)
            )

            lazy val request = FakeRequest("POST", "/").withBody(Json.toJson(model))
            lazy val result = {
              mockFind(Future.successful(Some(model)))
              call(TestRequestHandlerController.postRequestHandler("url"), request)
            }

            "return the status" in {
              status(result) mustBe Status.OK
            }
          }
        }
      }
    }

    "no matching data is found" should {

      lazy val request = FakeRequest("POST", "/").withBody(Json.obj("" -> ""))
      lazy val result = {
        mockFind(Future.successful(None))
        call(TestRequestHandlerController.postRequestHandler("url"), request)
      }

      "return 404" in {
        status(result) mustBe Status.BAD_REQUEST
      }
    }
  }

  "The .generateCalculationId"  should {
    "return a json body with id " in {
      lazy val request = FakeRequest("POST", "/")
      lazy val result = {
        TestRequestHandlerController.generateCalculationId("12345", 2020)(request)
      }
      status(result) mustBe Status.OK
      bodyOf(result).contains("id") mustBe true


    }
  }
}
