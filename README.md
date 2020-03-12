# beta-dog

[![Build Status](https://travis-ci.org/rvbabilonia/beta-dog.svg)](https://travis-ci.org/rvbabilonia/beta-dog)

## Introduction

"Because the beta always follows the alpha."

Beta-Dog is a scraper for the New Zealand stock market. It fetches all the available instruments/securities and
sorts them according to price-to-earning ratio or earnings per share to aid a newbie investor.

## Usage

1. To view all the instruments, go to https://beta-dog.herokuapp.com/api/v1/instruments.

2. Available query parameters are `filter` which can be `ALL` (default), `EQUITIES` or `FUNDS` and `order` which can be
`DEFAULT` (default), `PE_RATIO` or `EPS`.

3. To view Swagger UI, go to https://beta-dog.herokuapp.com/api/v1/swagger-ui-custom.html.
