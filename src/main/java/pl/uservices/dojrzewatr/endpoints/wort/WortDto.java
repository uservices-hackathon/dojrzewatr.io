package pl.uservices.dojrzewatr.endpoints.wort;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WortDto
{
	private final Integer quantity;

	@JsonCreator
	public WortDto(@JsonProperty("quantity") final Integer quantity)
	{
		this.quantity = quantity;
	}

	public Integer getQuantity()
	{
		return quantity;
	}
}
