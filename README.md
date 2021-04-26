# income-tax-submission-stub

### NINOs with stub data

A number of NINOs hold stub data by default. Please refer to Users file in the Models package to see these NINOs.

The NINO AA123459A should include data for all DES API calls(i.e. dividends, interest, giftAid etc).

### Creating a Custom User

It is possible to create a custom user. Please see below for an example. git stat

**note empty array passed if no stub data is needed*


`http://localhost:9303/user
{
"nino": "AA011022A",
"dividends": [{
"taxYear": 2022,
"ukDividends": 99999999999.99
}],
interest: [],
giftAid: [],
employment: []
}`

### License

This code is open source software licensed under
the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
