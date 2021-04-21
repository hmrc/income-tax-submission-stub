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

import javax.inject.{Inject, Singleton}
import models.APIUser
import play.api.libs.json.Json
import play.api.libs.json.Json.JsValueWrapper
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.{DefaultDB, WriteConcern}
import reactivemongo.api.commands._
import uk.gov.hmrc.mongo.MongoConnector

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(reactiveMongoComponent: ReactiveMongoComponent) {

  lazy val mongoConnector: MongoConnector = reactiveMongoComponent.mongoConnector
  implicit lazy val db: () => DefaultDB = mongoConnector.db

  private lazy val repository = new UserRepositoryBase()

  def removeAll()(implicit ec: ExecutionContext): Future[WriteResult] = repository.removeAll(WriteConcern.Acknowledged)

  def removeByNino(nino: String)(implicit ec: ExecutionContext): Future[WriteResult] = repository.remove("nino" -> nino)

  def insertUser(user: APIUser)(implicit ec: ExecutionContext): Future[Option[APIUser]] = {
    val ninoSelector = Json.obj("nino" -> user.nino)
    val modifier = Json.obj("$set" -> user)
    repository.findAndUpdate(query = ninoSelector, update = modifier, upsert = true, fetchNewObject = true).map(result => result.result[APIUser])
  }

  def findByNino(nino: String)(implicit ec: ExecutionContext): Future[Option[APIUser]] = find("nino" -> nino)

  def find(query: (String, JsValueWrapper)*)(implicit ec: ExecutionContext): Future[Option[APIUser]] =
    repository.find(query: _*).map(_.headOption)

  def bulkInsert(users: Seq[APIUser])(implicit ec: ExecutionContext): Future[MultiBulkWriteResult] = {
    repository.bulkInsert(users)
  }

}
