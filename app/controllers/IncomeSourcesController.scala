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

import javax.inject.Inject
import models.Users.findUser
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import repositories.DataRepository
import services.{DividendsService, GiftAidService, InterestService}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.ErrorResponses._
import utils.RandomIdGenerator.randomId

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class IncomeSourcesController @Inject()(dataRepository: DataRepository,
                                        interestService: InterestService,
                                        dividendsService: DividendsService,
                                        giftAidService: GiftAidService,
                                        cc: ControllerComponents) extends BackendController(cc) with Logging {

  // DES #1392 //
  def getListOfIncomeSources(nino: String,
                             incomeSourceType: String,
                             taxYear: Option[Int]): Action[AnyContent] = Action.async { implicit request =>

    import models.IncomeSourceTypes.IncomeSourceTypeB._

    logger.info(s"Get income source list for nino: $nino, taxYear: $taxYear, incomeSourceType: $incomeSourceType")

    incomeSourceType match {
      case INTEREST_FROM_UK_BANKS => findUser(nino)(interestService.getListOfIncomeSourcesInterest(nino,incomeSourceType))
      case _ => Future(notFound)
    }
  }

  // DES #1391 //
  def getIncomeSource(nino: String,
                      incomeSourceType: String,
                      taxYear: Int,
                      incomeSourceId: Option[String]): Action[AnyContent] = Action.async { implicit request =>

    import models.IncomeSourceTypes.IncomeSourceTypeA._

    logger.info(s"Get income source for nino: $nino, taxYear: $taxYear, incomeSourceType: $incomeSourceType, incomeSourceId: $incomeSourceId")

    incomeSourceType match {
      case INTEREST => findUser(nino)(interestService.getIncomeSourceInterest(incomeSourceId,taxYear))
      case DIVIDENDS => findUser(nino)(dividendsService.getIncomeSourceDividends(taxYear))
      case GIFT_AID => findUser(nino)(giftAidService.)
      case _ => Future(notFound)
    }
  }

  // DES #1393 //
  def createIncomeSource(nino: String): Action[AnyContent] = Action.async { implicit request =>

    logger.info(s"Creating income source for nino: $nino")
    Future(Ok(Json.parse(s"""{"incomeSourceId": "$randomId"}""".stripMargin)))
  }

  // DES #1390 //
  def createUpdateAnnualIncomeSource(nino: String,
                                     incomeSourceType: String,
                                     taxYear: Int): Action[AnyContent] = Action.async { implicit request =>

    logger.info(s"Creating/Updating annual income source for nino: $nino, incomeSourceType: $incomeSourceType, taxYear: $taxYear")
    Future(Ok(Json.parse(s"""{"transactionReference": "$randomId"}""".stripMargin)))
  }
}
