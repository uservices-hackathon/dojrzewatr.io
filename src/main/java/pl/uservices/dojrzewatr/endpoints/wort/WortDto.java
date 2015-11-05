package pl.uservices.dojrzewatr.endpoints.wort;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import javax.validation.constraints.Min;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WortDto
{
	@Min(value=1, message="Value must be greater then 0")
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

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE) //
				.append("quantity", quantity) //
				.toString();
	}
}
