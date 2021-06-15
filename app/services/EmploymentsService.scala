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
import models.APIModels._
import models.DESModels.{DESEmploymentExpenses, DESEmploymentsList}
import models.{ErrorBodyModel, ErrorModel}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Request, Result}
import play.api.mvc.Results.Ok
import utils.ErrorResponses.notFound

import scala.concurrent.{ExecutionContext, Future}

class EmploymentsService @Inject()(validateRequestService: ValidateRequestService) {

  def getListOfEmployments(taxYear: Int, employmentId: Option[String])(implicit ec: ExecutionContext): APIUser => Future[Result] = {
    user =>

      user.employment.find(_.taxYear == taxYear).fold(Future(notFound)) {
        employment =>

          val employmentsDetail: DESEmploymentsList = employmentId.fold {
            DESEmploymentsList(employment.hmrcEmployments.map(_.toDESEmploymentList), employment.customerEmployments.map(_.toDESEmploymentList))
          } {
            employmentId =>
              DESEmploymentsList(
                employment.hmrcEmployments.filter(_.employmentId.equals(employmentId)).map(_.toDESEmploymentList),
                employment.customerEmployments.filter(_.employmentId.equals(employmentId)).map(_.toDESEmploymentList)
              )
          }

          employmentsDetail match {
            case DESEmploymentsList(hmrc, customer) if hmrc.isEmpty && customer.isEmpty => Future(notFound)
            case model => Future(Ok(Json.toJson(model)))
          }
      }
  }

  def getEmploymentExpenses(taxYear: Int, view: String)(implicit ec: ExecutionContext): APIUser => Future[Result] = {
    user =>
      user.employment.find(_.taxYear == taxYear).fold(Future(notFound)) {
        employment =>
          employment.employmentExpenses.map {
            expenses =>
              expenses.source match {
                case None => Future(notFound)
                case _ =>
                  val employmentExpenses: DESEmploymentExpenses = DESEmploymentExpenses(
                    dateIgnored = expenses.dateIgnored,
                    source = expenses.source,
                    submittedOn = expenses.submittedOn,
                    totalExpenses = expenses.totalExpenses,
                    expenses = expenses.expenses
                  )
                  employmentExpenses match {
                    case model => if (!model.source.get.equals(view)) Future(notFound) else Future(Ok(Json.toJson(model)))
                  }
              }
          }.getOrElse(Future(notFound))
      }
  }

  def getEmploymentData(taxYear: Int, employmentId: String, view: String)(implicit ec: ExecutionContext): APIUser => Future[Result] = {
    user =>
      user.employment.find(_.taxYear == taxYear).fold(Future(notFound)) {
        employment =>
          val employmentData: Option[EmploymentData] = view match {
            case "CUSTOMER" => employment.customerEmployments.find(_.employmentId == employmentId).flatMap(_.employmentData)
            case "HMRC-HELD" => employment.hmrcEmployments.find(_.employmentId == employmentId).flatMap(_.employmentData)
            case "LATEST" =>

              val customerEmploymentData: Option[EmploymentData] = employment.customerEmployments.find(_.employmentId == employmentId).flatMap(_.employmentData)
              val hmrcEmploymentData: Option[EmploymentData] = employment.hmrcEmployments.find(_.employmentId == employmentId).flatMap(_.employmentData)

              (customerEmploymentData,hmrcEmploymentData) match {
                case (customer@Some(EmploymentData(customerDate,_, _, _, _)),hmrc@Some(EmploymentData(hmrcDate,_, _, _, _))) => if(customerDate > hmrcDate) customer else hmrc
                case (customer@Some(_),_) => customer
                case (_,hmrc@Some(_)) => hmrc
                case _ => None
              }
            case _ => None
          }

          employmentData match {
            case Some(data) => Future(Ok(Json.toJson(data.toDESEmploymentData)))
            case None => Future(notFound)
          }
      }
  }

  def validateAddEmployment(implicit request: Request[JsValue], APINumber: Int): Either[Result, Boolean] = {
    validateRequestService.validateRequest(ErrorModel(400, ErrorBodyModel("SCHEMA_ERROR", "The request body provided does not conform to the AddEmploymentSchema.")), APINumber)
  }


  def validateCreateUpdateIncomeSource(implicit request: Request[JsValue], APINumber: Int): Either[Result,Boolean] = {
    validateRequestService.validateRequest(ErrorModel(400,ErrorBodyModel("SCHEMA_ERROR", "The request body provided does not conform to the CreateUpdateFinancialDataSchema.")), APINumber)
  }
}
