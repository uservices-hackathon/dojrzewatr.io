io.codearte.accurest.dsl.GroovyDsl.make {
    request {
        method 'POST'
        url '/brew'
        headers {
            header 'Content-Type': 'application/vnd.pl.uservices.dojrzewatr.v1+json'
        }
        body("""{
            "ingredients": [
                        { "type": "MALT", "quantity": "${value(stub(regex("[0-9]+")), test(1000))}" },
                        { "type" : "WATER", "quantity": "${value(stub(regex("[0-9]+")), test(1000))}" },
                        { "type" : "HOP", "quantity": "${value(stub(regex("[0-9]+")), test(1000))}" },
                        { "type" : "YEAST", "quantity": "${value(stub(regex("[0-9]+")), test(1000))}" }
                ]
        }""")
    }
    response {
        status 200
    }
}