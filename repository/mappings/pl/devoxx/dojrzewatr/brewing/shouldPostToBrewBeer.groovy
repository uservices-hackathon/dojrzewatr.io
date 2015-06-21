io.codearte.accurest.dsl.GroovyDsl.make {
    request {
        method 'POST'
        url '/brew'
        headers {
            header 'Content-Type': 'application/vnd.pl.devoxx.dojrzewatr.v1+json'
        }
        body("""{
            "ingredients": [
                        { "type": "MALT", "quantity": "${value(client(regex("[0-9]+")), server(1000))}" },
                        { "type" : "WATER", "quantity": "${value(client(regex("[0-9]+")), server(1000))}" },
                        { "type" : "HOP", "quantity": "${value(client(regex("[0-9]+")), server(1000))}" },
                        { "type" : "YIEST", "quantity": "${value(client(regex("[0-9]+")), server(1000))}" }
                ]
        }""")
    }
    response {
        status 200
    }
}