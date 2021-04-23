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

import play.api.libs.json.{JsValue, Json}
import testUtils.TestSupport

class JsonValidationSpec extends TestSupport with JsonValidation {

  private case class JsonTest(name: String, json: JsValue, outcome: Boolean)

  "The json Schema validation utils" should {
    val schemaJsonDoc = "/jsonSchemas/notfound.json"

    s"return false if the schema cant be found" in {
      isValidJsonAccordingToJsonSchema(Json.parse("{}"), schemaJsonDoc) mustBe false
    }
  }
}
