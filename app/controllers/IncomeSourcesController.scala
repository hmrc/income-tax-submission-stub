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

import play.api.Logging
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import services._
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.ErrorResponses._
import utils.RandomIdGenerator.{randomEmploymentId, randomId}
import utils.TaxYearUtils.{checkTaxYearIsInValidFormat, convertStringTaxYear}

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class IncomeSourcesController @Inject()(interestService: InterestService,
                                        dividendsService: DividendsService,
                                        giftAidService: GiftAidService,
                                        employmentsService: EmploymentsService,
                                        userDataService: UserDataService,
                                        cc: ControllerComponents) extends BackendController(cc) with Logging {

  // DES #1390 - (Currently coded to v1.4.3, latest is 2.0.1) //
  def createUpdateAnnualIncomeSource(nino: String,
                                     incomeSourceType: String,
                                     taxYear: Int): Action[JsValue] = Action.async(parse.json) { implicit request =>

    implicit val APINumber: Int = 1390

    import models.IncomeSourceTypes.IncomeSourceTypeA._

    logger.info(s"Creating/Updating annual income source for nino: $nino, incomeSourceType: $incomeSourceType, taxYear: $taxYear")

    val outcome: Either[Result, Boolean] = incomeSourceType match {
      case DIVIDENDS => dividendsService.validateCreateUpdateIncomeSource
      case INTEREST => interestService.validateCreateUpdateIncomeSource
      case GIFT_AID => giftAidService.validateCreateUpdateIncomeSource
    }

    outcome match {
      case Left(error) =>
        logger.error(s"[createUpdateAnnualIncomeSource] The request body provided does not conform to the schema. Nino with request: $nino")
        Future(error)
      case Right(_) => Future(Ok(Json.parse(s"""{"transactionReference": "$randomId"}""".stripMargin)))
    }
  }

  // DES #1391 - v1.3.0 //
  def getIncomeSource(nino: String,
                      incomeSourceType: String,
                      taxYear: Int,
                      incomeSourceId: Option[String]): Action[AnyContent] = Action.async { _ =>

    import models.IncomeSourceTypes.IncomeSourceTypeA._

    logger.info(s"Get income source for nino: $nino, taxYear: $taxYear, incomeSourceType: $incomeSourceType, incomeSourceId: $incomeSourceId")

    incomeSourceType match {
      case INTEREST => userDataService.findUser(nino)(interestService.getIncomeSourceInterest(incomeSourceId, taxYear))
      case DIVIDENDS => userDataService.findUser(nino)(dividendsService.getIncomeSourceDividends(taxYear))
      case GIFT_AID => userDataService.findUser(nino)(giftAidService.getIncomeSourceGiftAid(taxYear))
      case _ =>
        logger.error(s"[getIncomeSource] Income source type invalid. Nino with request: $nino")
        Future(incomeSourceTypeInvalid)
    }
  }

  // DES #1392 - v1.1.0 //
  def getListOfIncomeSources(nino: String,
                             incomeSourceType: String,
                             taxYear: Option[Int]): Action[AnyContent] = Action.async { _ =>

    import models.IncomeSourceTypes.IncomeSourceTypeB._
    import models.IncomeSourceTypes._

    logger.info(s"Get income source list for nino: $nino, taxYear: $taxYear, incomeSourceType: $incomeSourceType")

    IncomeSourceTypeB.validType(incomeSourceType) match {
      case Right(INTEREST_FROM_UK_BANKS) => userDataService.findUser(nino)(interestService.getListOfIncomeSourcesInterest(nino, incomeSourceType, taxYear))
      case Right(_) => Future(notFound)
      case Left(_) =>
        logger.error(s"[getListOfIncomeSources] Income source type invalid. Nino with request: $nino")
        Future(incomeSourceTypeInvalid)
    }
  }

  // DES #1393 - (Currently coded to v1.1.0, latest is 1.2.0) //
  def createIncomeSource(nino: String): Action[JsValue] = Action.async(parse.json) { implicit request =>

    implicit val APINumber: Int = 1393

    logger.info(s"Creating income source for nino: $nino")

    val outcome: Either[Result, Boolean] = interestService.validateCreateIncomeSource

    outcome match {
      case Left(error) =>
        logger.error(s"[createIncomeSource] The request body provided does not conform to the schema. Nino with request: $nino")
        Future(error)
      case Right(_) => Future(Ok(Json.parse(s"""{"incomeSourceId": "$randomId"}""".stripMargin)))
    }
  }

  // DES #1643 - v1.0.0 //
  def createUpdateEmploymentFinancialData(nino: String, taxYear: String, employmentId: String): Action[JsValue] = Action.async(parse.json) { implicit request =>

    checkTaxYearIsInValidFormat(taxYear, nino) {
      implicit val APINumber: Int = 1643

      logger.info(s"Creating/Updating annual income source for nino: $nino, taxYear: $taxYear, employmentId:$employmentId")

      employmentsService.validateCreateUpdateIncomeSource match {
        case Left(error) =>
          logger.error(s"[createUpdateEmploymentFinancialData] The request body provided does not conform to the schema. Nino with request: $nino")
          Future(error)
        case Right(_) => Future(NoContent)
      }
    }
  }

  // DES #1644 - v1.0.0//
  def deleteEmploymentFinancialData(nino: String, taxYear: String, employmentId: String): Action[AnyContent] = Action.async { _ =>
    checkTaxYearIsInValidFormat(taxYear, nino) {
      logger.info(s"Delete employment financial data for nino: $nino, taxYear: $taxYear, employmentID: $employmentId")
      Future(NoContent)
    }
  }

  // DES #1645 - v1.2.2 //
  def getEmploymentsList(nino: String, taxYear: String, employmentId: Option[String]): Action[AnyContent] = Action.async { _ =>
    checkTaxYearIsInValidFormat(taxYear, nino) {
      logger.info(s"Get list of employments for nino: $nino, taxYear: $taxYear, employmentId: ${employmentId.getOrElse("None")}")
      userDataService.findUser(nino)(employmentsService.getListOfEmployments(convertStringTaxYear(taxYear), employmentId))
    }
  }

  // DES #1647 - v1.2.0 //
  def getEmploymentData(nino: String, taxYear: String, employmentId: String, view: String): Action[AnyContent] = Action.async { _ =>
    checkTaxYearIsInValidFormat(taxYear, nino) {
      logger.info(s"Get employment data for nino: $nino, taxYear: $taxYear, employmentID: $employmentId, view: $view")
      userDataService.findUser(nino)(employmentsService.getEmploymentData(convertStringTaxYear(taxYear), employmentId, view))
    }
  }

  // DES #1661 - v1.3.0 //
  def addEmployment(nino: String, taxYear: String): Action[JsValue] = Action.async(parse.json) { implicit request =>

    checkTaxYearIsInValidFormat(taxYear, nino) {
      implicit val APINumber: Int = 1661

      logger.info(s"Adding Employment for nino: $nino, taxYear: $taxYear")

      val outcome = employmentsService.validateAddEmployment

      outcome match {
        case Left(error) =>
          logger.error(s"[addEmployment] The request body provided does not conform to the schema. Nino with request: $nino")
          Future(error)
        case Right(_) => Future(Ok(Json.parse(s"""{"employmentId": "$randomEmploymentId"}""".stripMargin)))
      }
    }

  }

  // DES #1662 - v1.3.0 //
  def updateEmployment(nino: String, taxYear: String, employmentId: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    checkTaxYearIsInValidFormat(taxYear, nino) {
      implicit val APINumber: Int = 1662

      logger.info(s"Updating Employment for nino: $nino, taxYear: $taxYear, employmentId: $employmentId")

      val outcome = employmentsService.validateUpdateEmployment

      outcome match {
        case Left(error) =>
          logger.error(s"[updateEmployment] The request body provided does not conform to the schema. Nino with request: $nino")
          Future(error)
        case Right(_) => Future(NoContent)
      }
    }
  }

  // DES #1663 - v1.0.0 //
  def deleteEmployment(nino: String, taxYear: String, employmentId: String): Action[AnyContent] = Action.async { _ =>
    checkTaxYearIsInValidFormat(taxYear, nino) {
      logger.info(s"Delete employment for nino: $nino, taxYear: $taxYear, employmentID: $employmentId")
      Future(NoContent)
    }
  }

  // DES #1664 - v2.0.0 //

  def ignoreEmployment(nino: String, taxYear: String, employmentId: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    checkTaxYearIsInValidFormat(taxYear, nino) {

      logger.info(s"Ignore employment for nino: $nino, taxYear: $taxYear, employmentId: $employmentId")

      if(request.body != Json.parse("""{}""")){
        logger.error(s"[ignoreEmployment] API needs empty json supplied. Nino with request: $nino")
        Future(BadRequest)
      } else {
      Future(Created)
      }
    }
  }

  // DES #1668 - v1.1.0 //
  def getEmploymentsExpenses(nino: String, taxYear: String, view: String): Action[AnyContent] = Action.async { _ =>
    checkTaxYearIsInValidFormat(taxYear, nino) {
      logger.info(s"Get employment expenses for nino: $nino, taxYear: $taxYear, view: $view")
      userDataService.findUser(nino)(employmentsService.getEmploymentExpenses(convertStringTaxYear(taxYear), view))
    }
  }

  // DES #1669 - v1.2.0 //

  def createUpdateEmploymentExpenses(nino: String, taxYear: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    checkTaxYearIsInValidFormat(taxYear, nino) {
      implicit val APINumber: Int = 1669

      employmentsService.validateCreateUpdateEmploymentExpenses match {
        case Left(error) =>
          logger.error(s"[createUpdateEmploymentExpenses] The request body provided does not conform to the schema. Nino with request: $nino")
          Future(error)
        case Right(_) =>
          logger.info(s"Creating/Updating employment expenses for nino: $nino, taxYear: $taxYear")
          Future(NoContent)
      }
    }
  }

  // DES #1670 - v1.0.0 //

  def deleteEmploymentExpenses(nino: String, taxYear: String): Action[AnyContent] = Action.async { _ =>
    checkTaxYearIsInValidFormat(taxYear, nino) {
      logger.info(s"Delete employment expense data for nino: $nino, taxYear: $taxYear")
      Future(NoContent)
    }
  }

}
