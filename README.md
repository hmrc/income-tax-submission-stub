
# income-tax-submission-stub

### Typical Json setup should look like


`http://localhost:9303/setup/data
{
	"uri": "/income-tax/nino/1234567890/income-source/dividends/annual/2020",
	"method":"POST",
	"status":200,
	"response": {
	  "string": "ok"
	}
}`

Or

`http://localhost:9303/setup/data
{
	"uri": "/income-tax/nino/1234567890/income-source/dividends/annual/2020",
	"method":"GET",
	"status":200,
	"response": {
	  "string": "ok"
	}
}`

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
