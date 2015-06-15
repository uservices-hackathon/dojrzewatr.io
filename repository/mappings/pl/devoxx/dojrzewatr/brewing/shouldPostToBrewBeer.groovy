io.codearte.accurest.dsl.GroovyDsl.make {
    request {
        method 'POST'
        url '/brew'
        headers {
            header 'Content-Type': 'application/vnd.pl.devoxx.dojrzewatr.v1+json'
        }
        body(
                ingredients: [
                        [type: 'MALT', quantity: 1000],
                        [type: 'WATER', quantity: 1000],
                        [type: 'HOP', quantity: 1000],
                        [type: 'YIEST', quantity: 1000]
                ]
        )
    }
    response {
        status 200
    }
}