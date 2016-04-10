package io.github.cragz.numberswhatgoup.logging;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class NwguFormatter extends Formatter
{
	private final SimpleDateFormat date;

	public NwguFormatter()
	{
		date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String format(LogRecord record)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(date.format(record.getMillis()));
		builder.append(" ");
		builder.append(formatMessage(record));
		builder.append("\r\n");
		return builder.toString();
	}
}