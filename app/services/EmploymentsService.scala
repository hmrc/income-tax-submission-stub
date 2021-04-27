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

import javax.inject.Inject
import models.APIUser
import models.DESModels.{EmploymentExpenses, EmploymentsDetail}
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results.Ok
import utils.ErrorResponses.notFound

import scala.concurrent.{ExecutionContext, Future}

class EmploymentsService @Inject()() {

  def getListOfEmployments(taxYear: Int, employmentId: Option[String])(implicit ec: ExecutionContext): APIUser => Future[Result] = {
    user =>

      user.employment.find(_.taxYear == taxYear).fold(Future(notFound)) {
        employment =>

          val employmentsDetail: EmploymentsDetail = employmentId.fold {
            EmploymentsDetail(employment.hmrcEmployments, employment.customerEmployments)
          } {
            employmentId =>
              EmploymentsDetail(
                employment.hmrcEmployments.filter(_.employmentId.equals(employmentId)),
                employment.customerEmployments.filter(_.employmentId.equals(employmentId))
              )
          }

          employmentsDetail match {
            case EmploymentsDetail(hmrc, customer) if hmrc.isEmpty && customer.isEmpty => Future(notFound)
            case model => Future(Ok(Json.toJson(model)))
          }
      }
  }

  def getEmploymentExpenses(taxYear: Int, view: String)(implicit ec: ExecutionContext): APIUser => Future[Result] = {
    user =>
      user.employment.find(_.taxYear == taxYear).fold(Future(notFound)) {
        employment =>
          employment.employmentExpenses.source match {
            case None => Future(notFound)
            case _ =>
              val employmentExpenses: EmploymentExpenses = EmploymentExpenses(
                dateIgnored = employment.employmentExpenses.dateIgnored,
                source = employment.employmentExpenses.source,
                submittedOn = employment.employmentExpenses.submittedOn,
                totalExpenses = employment.employmentExpenses.totalExpenses,
                expenses = employment.employmentExpenses.expenses
              )
              employmentExpenses match {
                case model => if (!model.source.get.equals(view)) Future(notFound) else Future(Ok(Json.toJson(model)))
              }
          }
      }
  }
}
