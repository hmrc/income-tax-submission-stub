

package controllers

import play.api.Application
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import utils.IntegrationTest

class UserDataControllerISpec extends IntegrationTest with FutureAwaits with DefaultAwaitTimeout with Status {

  implicit val application: Application = app

  "POST /user" should {
    s"return $CREATED with json" in {

      val url = "user"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |	"nino": "AA123459A",
          |	"dividends": [{
          |		"taxYear": 2022,
          |		"ukDividends": 99999999999.99
          |	}],
          |	"interest": [{
          |		"incomeSourceName": "Rick Owens Bank",
          |		"incomeSourceId": "ZZIS1527A0DAFE4",
          |		"interestSubmissions": [{
          |			"taxYear": 2022,
          |			"taxedUkInterest": 99999999999.99,
          |			"untaxedUkInterest": 99999999999.99
          |		}]
          |	}, {
          |		"incomeSourceName": "Rick Owens Taxed Bank",
          |		"incomeSourceId": "ZZISED2D9C1AD6D",
          |		"interestSubmissions": [{
          |			"taxYear": 2022,
          |			"taxedUkInterest": 99999999999.99
          |		}]
          |	}, {
          |		"incomeSourceName": "Rick Owens Untaxed Bank",
          |		"incomeSourceId": "ZZIS48933C0B436",
          |		"interestSubmissions": [{
          |			"taxYear": 2022,
          |			"untaxedUkInterest": 99999999999.99
          |		}]
          |	}],
          |	"giftAid": [{
          |		"taxYear": 2022,
          |		"giftAidPayments": {
          |			"nonUkCharitiesCharityNames": ["Rick Owens Charity"],
          |			"currentYear": 99999999999.99,
          |			"oneOffCurrentYear": 99999999999.99,
          |			"currentYearTreatedAsPreviousYear": 99999999999.99,
          |			"nextYearTreatedAsCurrentYear": 99999999999.99,
          |			"nonUkCharities": 99999999999.99
          |		},
          |		"gifts": {
          |			"investmentsNonUkCharitiesCharityNames": ["Rick Owens Non-UK Charity"],
          |			"landAndBuildings": 99999999999.99,
          |			"sharesOrSecurities": 99999999999.99,
          |			"investmentsNonUkCharities": 99999999999.99
          |		}
          |	}]
          |}""".stripMargin)))

      println(res.body)
      res.status mustBe CREATED
    }
    s"return $BAD_REQUEST with json" in {
      val url = "user"

      val res = await(buildClient(url).post(Json.parse(
        """{
          |    "nino": "AA030405A",
          |    "dividends": [],
          |    "interest": []
          |}""".stripMargin)))

      res.status mustBe BAD_REQUEST
      res.body mustBe "Invalid APIUser payload: List((/giftAid,List(JsonValidationError(List(error.path.missing),WrappedArray()))))"
    }

  }
}
