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
import models.DESModels.GiftAidDetail
import models.{APIUser, ErrorBodyModel, ErrorModel}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Results.Ok
import play.api.mvc.{Request, Result}
import utils.ErrorResponses.notFound

import scala.concurrent.{ExecutionContext, Future}

class GiftAidService @Inject()(validateRequestService: ValidateRequestService){

  def getIncomeSourceGiftAid(taxYear: Int)(implicit ec: ExecutionContext): APIUser => Future[Result] = {
    user =>
      user.giftAid.find(_.taxYear == taxYear).fold(Future(notFound)){
        giftAid =>
          Future(Ok(Json.toJson(GiftAidDetail(
            giftAid.giftAidPayments,
            giftAid.gifts
          ))))
      }
  }

  def validateCreateUpdateIncomeSource(implicit request: Request[JsValue], executionContext: ExecutionContext, APINumber: Int): Either[Result,Boolean] ={
    //TODO Replace with actual error responses
    validateRequestService.validateRequest(ErrorModel(400,ErrorBodyModel("ERROR","FAIL")), APINumber)
  }

}
