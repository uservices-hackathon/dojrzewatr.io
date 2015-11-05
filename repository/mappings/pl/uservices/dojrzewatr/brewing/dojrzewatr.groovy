io.codearte.accurest.dsl.GroovyDsl.make {
	request {
		method "POST"
		url "/wort"
		body('''
                    {
                    "quantity":1
                    }
                '''
		)
		headers { header("Content-Type", "application/json") }
	}
	response {
		status 204
	}
}
