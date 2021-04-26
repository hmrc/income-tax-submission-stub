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

import models.DESModels.EmploymentExpenses
import models.{APIUser, ErrorBodyModel, ErrorModel}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Results._
import play.api.mvc.{Request, Result}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class EmploymentExpensesService @Inject()(validateRequestService: ValidateRequestService){

  def getEmploymentExpenses(taxYear: Int, nino: String, view: String)(implicit ec: ExecutionContext): APIUser => Future[Result] = {
    user =>
      val expenses = EmploymentExpenses(
          dateIgnored = user.employmentExpenses.dateIgnored,
          source = user.employmentExpenses.source,
          submittedOn = user.employmentExpenses.submittedOn,
          totalExpenses = user.employmentExpenses.totalExpenses,
          expenses = user.employmentExpenses.expenses
        )
      Future(Ok(Json.toJson(expenses)))
}

  def validateCreateUpdateIncomeSource(implicit request: Request[JsValue], APINumber: Int): Either[Result,Boolean] = {
    validateRequestService.validateRequest(ErrorModel(400,ErrorBodyModel("SCHEMA_ERROR", "The request body provided does not conform to the CreateUpdateIncomeSourceSchema.")), APINumber)
  }

}
