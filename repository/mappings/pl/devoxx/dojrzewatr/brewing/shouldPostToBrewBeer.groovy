io.codearte.accurest.dsl.GroovyDsl.make {
    request {
        method 'POST'
        url '/brew'
        headers {
            header 'Content-Type': 'application/vnd.pl.devoxx.dojrzewatr.v1+json'
        }
    }
    response {
        status 200
    }
}