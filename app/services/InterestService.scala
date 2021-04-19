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
import models.{APIUser, ErrorBodyModel, ErrorModel}
import models.DESModels.{IncomeSourceModel, InterestDetail, InterestDetails}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Request, Result}
import play.api.mvc.Results._
import utils.ErrorResponses.notFound

import scala.concurrent.{ExecutionContext, Future}

class InterestService @Inject()(validateRequestService: ValidateRequestService){

  def getListOfIncomeSourcesInterest(nino: String,incomeSourceType: String)(implicit ec: ExecutionContext): APIUser => Future[Result] = {
    user =>

      val incomeSources: Seq[IncomeSourceModel] = user.interest.map {
        interest =>
          IncomeSourceModel(interest.incomeSourceId, interest.incomeSourceName, nino, incomeSourceType)
      }

      Future(Ok(Json.toJson(incomeSources)))
  }

  def getIncomeSourceInterest(incomeSourceId: Option[String],taxYear: Int)(implicit ec: ExecutionContext): APIUser => Future[Result] = {
    user =>

      val interestIncomeSource: Option[InterestDetails] = user.interest.filter(x => incomeSourceId.contains(x.incomeSourceId)).map {
        interest =>

          val interestSubmissionsForTaxYear: Seq[InterestDetail] = interest.interestSubmissions.filter(_.taxYear == taxYear).map {
            submission =>
              InterestDetail(interest.incomeSourceId, submission.taxedUkInterest, submission.untaxedUkInterest)
          }
          InterestDetails(interestSubmissionsForTaxYear)
      }.headOption

      interestIncomeSource.fold(Future(notFound))(incomeSource => Future(Ok(Json.toJson(incomeSource))))
  }


  def validateCreateUpdateIncomeSource(implicit request: Request[JsValue], executionContext: ExecutionContext, APINumber: Int): Either[Result,Boolean] ={
    //TODO Replace with actual error responses
    validateRequestService.validateRequest(ErrorModel(400,ErrorBodyModel("ERROR","FAIL")), APINumber)
  }

}
