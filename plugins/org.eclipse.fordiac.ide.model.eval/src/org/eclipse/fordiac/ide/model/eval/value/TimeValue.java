/**
 * Copyright (c) 2022, 2023 Martin Erich Jobst
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Martin Jobst - initial API and implementation and/or initial documentation
 */
package org.eclipse.fordiac.ide.model.eval.value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;

import org.eclipse.fordiac.ide.model.data.TimeType;
import org.eclipse.fordiac.ide.model.datatype.helper.IecTypes;
import org.eclipse.fordiac.ide.model.value.TypedValueConverter;

public final class TimeValue implements AnyDurationValue {
	public static final TimeValue DEFAULT = new TimeValue(0);

	private final long value;

	private TimeValue(final long value) {
		this.value = value;
	}

	public static TimeValue toTimeValue(final long value) {
		return new TimeValue(value);
	}

	public static TimeValue toTimeValue(final Number value) {
		return new TimeValue(value.longValue());
	}

	public static TimeValue toTimeValue(final Duration value) {
		return new TimeValue(value.toNanos());
	}

	public static TimeValue toTimeValue(final String string) {
		return TimeValue.toTimeValue(((Duration) TypedValueConverter.INSTANCE_TIME.toValue(string)));
	}

	public static TimeValue toTimeValue(final AnyMagnitudeValue value) {
		return TimeValue.toTimeValue(value.longValue());
	}

	@Override
	public TimeType getType() {
		return IecTypes.ElementaryTypes.TIME;
	}

	@Override
	public byte byteValue() {
		return (byte) value;
	}

	@Override
	public short shortValue() {
		return (short) value;
	}

	@Override
	public int intValue() {
		return (int) value;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public BigInteger bigIntegerValue() {
		return BigInteger.valueOf(value);
	}

	@Override
	public BigDecimal bigDecimalValue() {
		return BigDecimal.valueOf(value);
	}

	@Override
	public Duration toDuration() {
		return Duration.ofNanos(value);
	}

	@Override
	public int hashCode() {
		return Long.hashCode(value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TimeValue other = (TimeValue) obj;
		return value == other.value;
	}

	@Override
	public String toString() {
		return TypedValueConverter.INSTANCE_TIME.toString(toDuration());
	}
}
