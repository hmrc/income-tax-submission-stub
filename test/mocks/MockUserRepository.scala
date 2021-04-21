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

package mocks

import models.APIUser
import org.scalamock.handlers.{CallHandler1, CallHandler2}
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.Json.JsValueWrapper
import reactivemongo.api.commands.{UpdateWriteResult, WriteError, WriteResult}
import repositories.UserRepository
import testUtils.TestSupport

import scala.concurrent.{ExecutionContext, Future}

trait MockUserRepository extends TestSupport with MockFactory {

  val successWriteResult = UpdateWriteResult(ok = true, n = 1, nModified = 0, upserted = Seq(), writeErrors = Seq(), None, None, None)
  val errorWriteResult = UpdateWriteResult(ok = false, n = 1, nModified = 0, upserted = Seq(), writeErrors = Seq(WriteError(1,1,"Error")), None, None, None)

  lazy val mockUserRepository: UserRepository = mock[UserRepository]

  def mockInsertUser(document: APIUser)
                  (response: Future[WriteResult]): CallHandler2[APIUser, ExecutionContext, Future[WriteResult]] = {
    (mockUserRepository.insertUser(_: APIUser)(_: ExecutionContext))
      .expects(document, *)
      .returning(response)
  }

  def mockRemoveById(url: String)
                    (response: Future[WriteResult]): CallHandler2[String, ExecutionContext, Future[WriteResult]] = {
    (mockUserRepository.removeByNino(_: String)(_: ExecutionContext))
      .expects(url, *)
      .returning(response)
  }

  def mockRemoveAll()
                   (response: Future[WriteResult]): CallHandler1[ExecutionContext, Future[WriteResult]] = {
    (mockUserRepository.removeAll()(_: ExecutionContext))
      .expects(*)
      .returning(response)
  }

  def mockFindById(url: String)
                  (response: Future[APIUser]): CallHandler2[String, ExecutionContext, Future[Option[APIUser]]] = {
    (mockUserRepository.findByNino(_: String)(_: ExecutionContext))
      .expects(*, *)
      .returning(response)
  }

  def mockFind(response: Future[Option[APIUser]]): CallHandler2[(String, JsValueWrapper), ExecutionContext, Future[Option[APIUser]]] = {
    (mockUserRepository.find(_: (String, JsValueWrapper))(_: ExecutionContext))
      .expects(*, *)
      .returning(response)
  }
}
