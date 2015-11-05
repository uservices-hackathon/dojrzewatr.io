package pl.uservices.dojrzewatr.domain;

/**
 * Created: 11/5/15
 *
 * @author Ryszard Bancarzewski <ryszard.bancarzewski@sap.com>
 */
public class Wort
{

	private final long timestamp;
	private final Integer amount;

	public Wort(final Integer amount, final long timestamp)
	{
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public Integer getAmount()
	{
		return amount;
	}

	public long getTimestamp()
	{
		return timestamp;
	}
}
