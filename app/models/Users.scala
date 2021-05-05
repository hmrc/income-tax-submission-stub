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

import filters.StubErrorFilter.{DES_500_NINO, DES_503_NINO}
import utils.RandomIdGenerator

object Users {

  val users = Seq(
    APIUser(
      "AA123459A",
      interest = Seq(
        Interest(
          "Rick Owens Bank", //TODO THIS TEST CASE SHOULD WORK AFTER SASS-536 IS COMPLETED https://jira.tools.tax.service.gov.uk/browse/SASS-536
          "000000000000001",
          interestSubmissions = Seq(
            InterestSubmission(
              2022,
              Some(99999999999.99),
              Some(99999999999.99)
            )
          )
        ),
        Interest(
          "Rick Owens Taxed Bank",
          "000000000000002",
          interestSubmissions = Seq(
            InterestSubmission(
              2022,
              Some(99999999999.99),
              None
            )
          )
        ),
        Interest(
          "Rick Owens Untaxed Bank",
          "000000000000003",
          interestSubmissions = Seq(
            InterestSubmission(
              2022,
              None,
              Some(99999999999.99)
            )
          )
        )
      ),
      dividends = Seq(
        Dividends(
          2022,
          Some(99999999999.99),
          None
        )
      ),
      giftAid = Seq(
        GiftAid(
          2022,
          giftAidPayments = Some(GiftAidPayments(
            Some(Seq("Rick Owens Charity")),
            Some(99999999999.99),
            Some(99999999999.99),
            Some(99999999999.99),
            Some(99999999999.99),
            Some(99999999999.99)
          )),
          gifts = Some(Gifts(
            Some(Seq("Rick Owens Non-UK Charity")),
            Some(99999999999.99),
            Some(99999999999.99),
            Some(99999999999.99)
          ))
        )
      ),
      employment = Seq(
        Employment(
          2022,
          Seq(HmrcEmployment("00000000-0000-0000-0000-000000000001", "Rick Owens LTD", Some("666/66666"), Some("123456789"),
            Some("2020-06-17T10:53:38Z"), Some("2020-06-17T10:53:38Z"), Some("2020-06-17T10:53:38Z"))),
          Seq(CustomerEmployment("00000000-0000-0000-0000-000000000001", "Rick Owens London LTD",
            Some("666/66666"), Some("123456789"), Some("2020-06-17T10:53:38Z"), Some("2020-06-17T10:53:38Z"), submittedOn = "2020-06-17T10:53:38Z")),
          employmentExpenses = EmploymentExpenses(
            submittedOn = Some("2022-12-12T12:12:12Z"),
            dateIgnored = Some("2022-12-11T12:12:12Z"),
            source = Some("CUSTOMER"),
            totalExpenses = Some(100.00),
            expenses = Some(ExpensesType(
              businessTravelCosts = Some(100.00),
              jobExpenses = Some(100.00),
              flatRateJobExpenses = Some(100.00),
              professionalSubscriptions = Some(100.00),
              hotelAndMealExpenses = Some(100.00),
              otherAndCapitalAllowances = Some(100.00),
              vehicleExpenses = Some(100.00),
              mileageAllowanceRelief = Some(100.00)
            ))
          )
        )
      )
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
          Some(15071993.01),
        )
      )
    ),
    //Accessibility Staging users
    APIUser(
      "AA111113A",
      interest = Seq(
        Interest(
          "First Direct",
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
          "Skipton",
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
          Some(750.50),
          Some(225.25),
        )
      )
    ),
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
    APIUser("PB133742J",
      employment = Seq(
        Employment(
          2022,
          Seq(HmrcEmployment("00000000-0000-1000-8000-000000000000", "Vera Lynn", Some("123/abc 001<Q>"), Some("123345657"),
            Some("2020-06-17T10:53:38Z"), Some("2020-06-17T10:53:38Z"), Some("2020-06-17T10:53:38Z"))),
          Seq(CustomerEmployment("00000000-0000-1000-8000-000000000002", "Vera Lynn",
            Some("123/abc 001<Q>"), Some("123345657"), Some("2020-06-17T10:53:38Z"), Some("2020-06-17T10:53:38Z"), submittedOn = "2020-06-17T10:53:38Z")),
          employmentExpenses = EmploymentExpenses(
            submittedOn = Some("2022-12-12T12:12:12Z"),
            dateIgnored = Some("2022-12-11T12:12:12Z"),
            source = None,
            totalExpenses = Some(100.00),
            expenses = Some(ExpensesType(
              businessTravelCosts = Some(100.00),
              jobExpenses = Some(100.00),
              flatRateJobExpenses = Some(100.00),
              professionalSubscriptions = Some(100.00),
              hotelAndMealExpenses = Some(100.00),
              otherAndCapitalAllowances = Some(100.00),
              vehicleExpenses = Some(100.00),
              mileageAllowanceRelief = Some(100.00)
            ))
          )
        )
      )
    ),
    APIUser(DES_500_NINO),
    APIUser(DES_503_NINO)
  )
}
