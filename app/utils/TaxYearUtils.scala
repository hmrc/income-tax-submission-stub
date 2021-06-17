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

package utils

import models.{ErrorBodyModel, ErrorModel}
import play.api.Logging
import play.api.mvc.Result
import play.api.mvc.Results.Status
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TaxYearUtils extends Logging{

  def convertStringTaxYear(taxYear: String): Int ={

    //2021-22 -> 2022
    taxYear.take(4).toInt + 1
  }

  def checkTaxYearIsInValidFormat(taxYear:String, nino: String)(block: => Future[Result]): Future[Result] ={
    //check tax year is in 2021-22 format

    if (!taxYear.matches("^2[0-9]{3}-[0-9]{2}$")) {
      logger.error(s"The tax year does not conform to des spec. Nino with request: $nino")
      val errors = ErrorModel(400, ErrorBodyModel("TAX_YEAR_ERROR", "Tax year is in the incorrect format"))
      Future(Status(errors.status)(errors.toJson))
    }else{
      block
    }
  }

}
