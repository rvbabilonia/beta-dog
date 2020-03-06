# beta-dog

[![Build Status](https://travis-ci.org/rvbabilonia/beta-dog.svg)](https://travis-ci.org/rvbabilonia/beta-dog)

## Introduction

"Because the beta always follows the alpha."

Beta-Dog is a scraper for the New Zealand stock market. It fetches all the available instruments/securities and
sorts them according to price-to-earning ratio or earnings per share to aid an investor.

## Usage

1. To view all the instruments, go to `https://API_GATEWAY/v1/instruments`.

2. Available query parameters are `filter` which can be `ALL` (default), `EQUITIES` or `FUNDS` and `order` which can be
`DEFAULT` (default), `PE_RATIO` or `EPS`.
