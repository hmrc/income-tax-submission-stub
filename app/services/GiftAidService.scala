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

import models.APIModels.APIUser
import models.APIModels.Gifts
import models.APIModels.GiftAidPayments
import models.DESModels.DESGiftAidDetail
import models.{ErrorBodyModel, ErrorModel}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Results.Ok
import play.api.mvc.{Request, Result}
import utils.ErrorResponses.notFound

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class GiftAidService @Inject()(validateRequestService: ValidateRequestService){

  def getIncomeSourceGiftAid(taxYear: Int)(implicit ec: ExecutionContext): APIUser => Future[Result] = {
    user =>
      user.giftAid.find(_.taxYear == taxYear).fold(Future(notFound)){
        giftAid =>

          val payments: Option[GiftAidPayments] = giftAid.giftAidPayments
          val gifts: Option[Gifts] = giftAid.gifts

          Future(Ok(Json.toJson(DESGiftAidDetail(payments,gifts))))
      }
  }

  def validateCreateUpdateIncomeSource(implicit request: Request[JsValue], APINumber: Int): Either[Result,Boolean] = {
    validateRequestService.validateRequest(ErrorModel(400,ErrorBodyModel("SCHEMA_ERROR", "The request body provided does not conform to the CreateUpdateIncomeSourceSchema.")), APINumber)
  }

}
