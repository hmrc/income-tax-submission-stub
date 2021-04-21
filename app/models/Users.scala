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

package models

import play.api.mvc.Result
import utils.RandomIdGenerator
import scala.concurrent.Future
import utils.ErrorResponses._
import scala.concurrent.ExecutionContext.Implicits.global

object Users {

  val users = Seq(
    APIUser(
      "BB777777B",
      interest =
        (1 to 11).map {
          num =>
            Interest(
              num.toString,
              RandomIdGenerator.randomId,
              interestSubmissions = Seq(
                InterestSubmission(
                  2022,
                  None,
                  Some(100)
                )
              )
            )
        }
    ),
    APIUser(
      "AA000001A",
      dividends = Seq(
        Dividends(
          2022,
          Some(55844806400.99),
          Some(60267421355.99)
        )
      )
    ),
    APIUser(
      "AA000002A",
      dividends = Seq(
        Dividends(
          2022,
          Some(750.50),
          Some(225.25)
        )
      )
    ),
    APIUser(
      "AA000003A",
      interest = Seq(
        Interest(
          "Halifax",
          RandomIdGenerator.randomId,
          interestSubmissions = Seq(
            InterestSubmission(
              2022,
              None,
              Some(4000)
            )
          )
        ),
        Interest(
          "Nationwide",
          RandomIdGenerator.randomId,
          interestSubmissions = Seq(
            InterestSubmission(
              2022,
              None,
              Some(4000)
            )
          )
        ),
        Interest(
          "Monzo",
          RandomIdGenerator.randomId,
          interestSubmissions = Seq(
            InterestSubmission(
              2022,
              Some(4000),
              None
            )
          )
        ),
        Interest(
          "TSB Account",
          RandomIdGenerator.randomId,
          interestSubmissions = Seq(
            InterestSubmission(
              2022,
              Some(4000),
              None
            )
          )
        )
      ),
      dividends = Seq(
        Dividends(
          2022,
          Some(46985.99),
          Some(15071993.01)
        )
      )
    )
  )

  //TODO Update with actual error response for 404
  def findUser(nino: String, notFoundResult: Future[Result] = Future(notFound))
              (function: APIUser => Future[Result]): Future[Result] = {

    Users.users.find(_.nino.equals(nino)).fold(notFoundResult)(function)
  }
}
