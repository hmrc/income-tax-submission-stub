# income-tax-submission-stub

### NINOs with stub data

A number of NINOs hold stub data by default. Please refer to Users file in the Models package to see these NINOs.


### Creating a Custom User

It is possible to create a custom user. See below for an example.

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

### DES APIs used in this stub

#### DES #1390 - (Currently coded to v1.4.3, latest is 2.0.1) 
[DES #1390 Spec](https://docs.google.com/document/d/1jI81NR-vKfHenAqXDO_Tbl-8xHwGp8InR9kP7lBJ-y4/edit)

#### DES #1391 - v1.3.0 
[DES #1391 Spec](https://docs.google.com/document/d/1OU5qMgMWSNF5qul5XwsFPItoo0AqwgZnYiR3uC2KqZ0/edit?usp=sharing)

#### DES #1392 - v1.1.0 
[DES #1392 Spec](https://docs.google.com/document/d/1OY-6hHY14iC05mRhNFZXGD8u9QXgrBGHaKei__kJ_WE/edit?usp=sharing)

#### DES #1393 - (Currently coded to v1.1.0, latest is 1.2.0) 
[DES #1393 Spec](https://docs.google.com/document/d/1vR0pHaftJvH7rQjX2KDzs-ImRBGj2Bhl3hkOzmeWSQo/edit)

#### DES #1416 - v1.0.0 
[DES #1416 Spec](https://docs.google.com/document/d/1qwevflOetUdPGgs_Yh_jgJTLJRZMnIpBVVZc6nsqUg4/edit)

#### DES #1426 - v3.0.0 
[DES #1426 Spec](https://confluence.tools.tax.service.gov.uk/pages/viewpage.action?pageId=179485292&preview=%2F179485292%2F211881840%2FDES+API%2316+%28DES+API%231426%29+Liability+Calculator+-+v3.0.0.docx)

#### DES #1643 - v1.0.0 
[DES #1643 Spec](https://docs.google.com/document/d/1vovuKZA3VE1_uRK6CFMXhDPVhzbwapQz/edit#heading=h.30j0zll)

#### DES #1644 - v1.0.0 
[DES #1644 Spec](https://docs.google.com/document/d/15EKD45y1okwjSak-J_7W0J-czAIzx_h-)

#### DES #1645 - v1.2.2 
[DES #1645 Spec](https://drive.google.com/file/d/1Ck-JT9cpqgD_kx7tpBJZI0WpclpF8Jy6/view?usp=sharing)

#### DES #1647 - v1.2.0 
[DES #1647 Spec](https://confluence.tools.tax.service.gov.uk/download/attachments/210504381/DES%20API%231647%20Get%20Employment%20Data%20v1.2.0.docx?version=1&modificationDate=1601891156000&api=v2)

#### DES #1661 - v1.3.0 
[DES #1661 Spec](https://confluence.tools.tax.service.gov.uk/pages/viewpage.action?pageId=179485292&preview=%2F179485292%2F202083654%2FDES+API%231661+Add+Employment+v1.3.0.docx)

#### DES #1662 - v1.3.0 
[DES #1662 Spec](https://confluence.tools.tax.service.gov.uk/pages/viewpage.action?pageId=179485292&preview=%2F179485292%2F202083655%2FDES+API%231662+Update+Employment+v1.3.0.docx)

#### DES #1663 - v1.0.0 
[DES #1663 Spec](https://drive.google.com/file/d/1rE6h_toS2ssyvbWUUJhF-cWOgChhNG6i/view?usp=sharing)

#### DES #1664 - v2.0.0 
[DES #1664 Spec](https://docs.google.com/document/d/1mIeUqHhl8tYIC6bE9KJllKNOdb-IbtGJq1rUmo1tckM/edit)

#### DES #1668 - v1.1.0 
[DES #1668 Spec](https://drive.google.com/file/d/1QoGX6CA8gbZOOYnTuf_UAMMkrvXpiFBl/view?usp=sharing)

#### DES #1669  - v1.2.0 
[DES #1669 Spec](https://confluence.tools.tax.service.gov.uk/pages/viewpage.action?pageId=223054532&preview=%2F223054532%2F252708196%2FAPI%231669_Create_Update_Employment_Expenses_v1.2.0.pdf)

#### DES #1670 - v1.0.0 
[DES #1670 Spec](https://drive.google.com/file/d/1UNtNnzxO5sPkzNPRSVRTBdkoUFEvecYv/view?usp=sharing)

### License

This code is open source software licensed under
the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
