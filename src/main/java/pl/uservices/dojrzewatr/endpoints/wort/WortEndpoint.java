/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package pl.uservices.dojrzewatr.endpoints.wort;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pl.uservices.dojrzewatr.domain.Warehouse;
import pl.uservices.dojrzewatr.domain.Wort;

@RestController
@RequestMapping(value="/wort")
public class WortEndpoint
{
	private static final Logger LOGGER = LoggerFactory.getLogger(WortEndpoint.class);

	private final Warehouse warehouse;

	@Autowired
	public WortEndpoint(final Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@RequestMapping(method = POST)
	public @ResponseBody ResponseEntity saveWort(@RequestBody final Wort wort) {

		LOGGER.debug("Saving wort: " + wort.toString());

		warehouse.addWort(wort);

		LOGGER.info("Wort: " + wort.toString() + " saved.");

		return ResponseEntity.noContent().build();
	}
}
