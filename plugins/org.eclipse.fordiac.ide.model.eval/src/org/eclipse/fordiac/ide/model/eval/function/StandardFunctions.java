/*******************************************************************************
 * Copyright (c) 2022 Martin Erich Jobst
 * 				 2022 Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Martin Jobst - initial API and implementation and/or initial documentation
 *   Martin Melik Merkumians - added some functions
 *   Hesam Rezaee - add Hovering features
 *
 *******************************************************************************/
package org.eclipse.fordiac.ide.model.eval.function;

import java.math.BigInteger;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.eclipse.fordiac.ide.model.eval.AbstractEvaluator;
import org.eclipse.fordiac.ide.model.eval.value.AnyBitValue;
import org.eclipse.fordiac.ide.model.eval.value.AnyDurationValue;
import org.eclipse.fordiac.ide.model.eval.value.AnyElementaryValue;
import org.eclipse.fordiac.ide.model.eval.value.AnyIntValue;
import org.eclipse.fordiac.ide.model.eval.value.AnyNumValue;
import org.eclipse.fordiac.ide.model.eval.value.AnyRealValue;
import org.eclipse.fordiac.ide.model.eval.value.AnySCharsValue;
import org.eclipse.fordiac.ide.model.eval.value.AnyStringValue;
import org.eclipse.fordiac.ide.model.eval.value.AnyUnsignedValue;
import org.eclipse.fordiac.ide.model.eval.value.AnyValue;
import org.eclipse.fordiac.ide.model.eval.value.AnyWCharsValue;
import org.eclipse.fordiac.ide.model.eval.value.BoolValue;
import org.eclipse.fordiac.ide.model.eval.value.ByteValue;
import org.eclipse.fordiac.ide.model.eval.value.CharValue;
import org.eclipse.fordiac.ide.model.eval.value.DIntValue;
import org.eclipse.fordiac.ide.model.eval.value.DWordValue;
import org.eclipse.fordiac.ide.model.eval.value.DateAndTimeValue;
import org.eclipse.fordiac.ide.model.eval.value.DateValue;
import org.eclipse.fordiac.ide.model.eval.value.IntValue;
import org.eclipse.fordiac.ide.model.eval.value.LDateAndTimeValue;
import org.eclipse.fordiac.ide.model.eval.value.LDateValue;
import org.eclipse.fordiac.ide.model.eval.value.LIntValue;
import org.eclipse.fordiac.ide.model.eval.value.LRealValue;
import org.eclipse.fordiac.ide.model.eval.value.LTimeOfDayValue;
import org.eclipse.fordiac.ide.model.eval.value.LTimeValue;
import org.eclipse.fordiac.ide.model.eval.value.LWordValue;
import org.eclipse.fordiac.ide.model.eval.value.RealValue;
import org.eclipse.fordiac.ide.model.eval.value.SIntValue;
import org.eclipse.fordiac.ide.model.eval.value.StringValue;
import org.eclipse.fordiac.ide.model.eval.value.TimeOfDayValue;
import org.eclipse.fordiac.ide.model.eval.value.TimeValue;
import org.eclipse.fordiac.ide.model.eval.value.UDIntValue;
import org.eclipse.fordiac.ide.model.eval.value.UIntValue;
import org.eclipse.fordiac.ide.model.eval.value.ULIntValue;
import org.eclipse.fordiac.ide.model.eval.value.USIntValue;
import org.eclipse.fordiac.ide.model.eval.value.ValueOperations;
import org.eclipse.fordiac.ide.model.eval.value.WCharValue;
import org.eclipse.fordiac.ide.model.eval.value.WStringValue;
import org.eclipse.fordiac.ide.model.eval.value.WordValue;
import org.eclipse.fordiac.ide.model.eval.variable.Variable;

@SuppressWarnings("squid:S100") // ST Name conventions must be used here
public interface StandardFunctions extends Functions {

	@SuppressWarnings("unchecked")
	@Comment("Calculates absolute value.")
	static <T extends AnyNumValue> T ABS(final T value) {
		return (T) ValueOperations.abs(value);
	}

	@SuppressWarnings("unchecked")
	@Comment("Returns square root value.")
	static <T extends AnyRealValue> T SQRT(final T value) {
		return (T) ValueOperations.sqrt(value);
	}

	@Comment("Returns the natural logarithm.")
	static <T extends AnyRealValue> T LN(final T value) {
		return apply(value, Math::log);
	}

	@Comment("Returns the logarithm base 10.")
	static <T extends AnyRealValue> T LOG(final T value) {
		return apply(value, Math::log10);
	}

	@Comment("Returns e exponent of the input.")
	static <T extends AnyRealValue> T EXP(final T value) {
		return apply(value, Math::exp);
	}

	@Comment("Returns the sine amount.")
	@ReturnValueComment("Between -1.0 and 1.0")
	static <T extends AnyRealValue> T SIN(@Comment("Radians unit.") final T value) {
		return apply(value, Math::sin);
	}

	@Comment("Returns the cosine amount.")
	@ReturnValueComment("Between -1.0 and 1.0")
	static <T extends AnyRealValue> T COS(@Comment("Radians unit.") final T value) {
		return apply(value, Math::cos);
	}

	@Comment("Returns the tangent amount.")
	static <T extends AnyRealValue> T TAN(@Comment("Radians unit.") final T value) {
		return apply(value, Math::tan);
	}

	@Comment("Returns the principle arc sine.")
	@ReturnValueComment("Radians unit.")
	static <T extends AnyRealValue> T ASIN(@Comment("Between -1.0 and 1.0.") final T value) {
		return apply(value, Math::asin);
	}

	@Comment("Returns the principle arc cosine.")
	@ReturnValueComment("Radians unit.")
	static <T extends AnyRealValue> T ACOS(@Comment("Between -1.0 and 1.0.") final T value) {
		return apply(value, Math::acos);
	}

	@Comment("Returns the principle arc tangent.")
	@ReturnValueComment("Radians unit.")
	static <T extends AnyRealValue> T ATAN(final T value) {
		return apply(value, Math::atan);
	}

	@Comment("Returns the principle arc tangent between inputs.")
	@ReturnValueComment("Radians unit.")
	static <T extends AnyRealValue> T ATAN2(@Comment("The first input.") final T x,
			@Comment("The second input.") final T y) {
		return apply(x, y, Math::atan2);
	}

	@SuppressWarnings("unchecked")
	@Comment("Calculates the addition of inputs.")
	static <T extends AnyNumValue> T ADD(final T... values) {
		return Stream.of(values).reduce((a, b) -> (T) ValueOperations.add(a, b)).orElseThrow();
	}

	@SuppressWarnings("unchecked")
	@Comment("Calculates the multiplication of inputs.")
	static <T extends AnyNumValue> T MUL(final T... values) {
		return Stream.of(values).reduce((a, b) -> (T) ValueOperations.multiply(a, b)).orElseThrow();
	}

	@SuppressWarnings("unchecked")
	@Comment("Calculates the substraction of inputs.")
	static <T extends AnyNumValue> T SUB(final T first, final T second) {
		return (T) ValueOperations.subtract(first, second);
	}

	@SuppressWarnings("unchecked")
	@Comment("Calculates the division of the dividend by the divisor.")
	static <T extends AnyNumValue> T DIV(@Comment("The dividend.") final T first,
			@Comment("The divisor, must not be 0.") final T second) {
		return (T) ValueOperations.divideBy(first, second);
	}

	@SuppressWarnings("unchecked")
	@Comment("Calculates the remainder after division of the dividend by the divisor.")
	static <T extends AnyIntValue> T MOD(@Comment("The dividend.") final T first,
			@Comment("The divisor, must not be 0.") final T second) {
		return (T) ValueOperations.remainderBy(first, second);
	}

	@SuppressWarnings("unchecked")
	@Comment("Calculates the base in power of the exponent.")
	static <T extends AnyRealValue, U extends AnyNumValue> T EXPT(@Comment("Base.") final T first,
			@Comment("Exponent.") final U second) {
		return (T) ValueOperations.power(first, second);
	}

	/* Bit operations */

	@SuppressWarnings("unchecked")
	@Comment("Shifts the input value to left by some bits.")
	static <T extends AnyBitValue, U extends AnyIntValue> T SHL(@Comment("The value to be shifted.") final T value,
			@Comment("The number of bits to shift.") final U moveby) {
		return (T) ValueOperations.shiftLeft(value, moveby);
	}

	@SuppressWarnings("unchecked")
	@Comment("Shifts the input value to right by some bits.")
	static <T extends AnyBitValue, U extends AnyIntValue> T SHR(@Comment("The value to be shifted.") final T value,
			@Comment("The number of bits to shift.") final U moveby) {
		return (T) ValueOperations.shiftRight(value, moveby);

	}

	@SuppressWarnings("unchecked")
	@Comment("Rotates the input value to left by some bits.")
	static <T extends AnyBitValue, U extends AnyIntValue> T ROL(@Comment("The value to be rotated.") final T value,
			@Comment("The number of bits to rotation.") final U moveby) {
		return (T) ValueOperations.rotateLeft(value, moveby);
	}

	@SuppressWarnings("unchecked")
	@Comment("Rotates the input vlaue to right by some bits.")
	static <T extends AnyBitValue, U extends AnyIntValue> T ROR(@Comment("The value to be rotated.") final T value,
			@Comment("The number of bits to rotation.") final U moveby) {
		return (T) ValueOperations.rotateRight(value, moveby);
	}

	@SuppressWarnings("unchecked")
	@Comment("Calculates result of logical AND operation.")
	static <T extends AnyValue> T AND(final T... values) {
		return Stream.of(values).reduce((a, b) -> (T) ValueOperations.bitwiseAnd(a, b)).orElseThrow();
	}

	@SuppressWarnings("unchecked")
	@Comment("Calculates result of logical OR operation.")
	static <T extends AnyBitValue> T OR(final T... values) {
		return Stream.of(values).reduce((a, b) -> (T) ValueOperations.bitwiseOr(a, b)).orElseThrow();
	}

	@SuppressWarnings("unchecked")
	@Comment("Calculates result of logical XOR operation.")
	static <T extends AnyBitValue> T XOR(final T... values) {
		return Stream.of(values).reduce((a, b) -> (T) ValueOperations.bitwiseXor(a, b)).orElseThrow();
	}

	@SuppressWarnings("unchecked")
	@Comment("Calculates result of logical NOT operation.")
	static <T extends AnyBitValue> T NOT(final T first) {
		return (T) ValueOperations.bitwiseNot(first);
	}

	/* Selection and comparison */
	@Comment("Assigns one value to another.")
	static <T extends AnyValue> T MOVE(final T value) {
		return value;
	}

	@Comment("Returns one of two values depending on the value of a BOOL selector value.")
	static <T extends AnyValue> T SEL(@Comment("The selector value.") final BoolValue g,
			@Comment("This value will be returned if selector is false") final T in0,
			@Comment("This value will be returned if selector is true.") final T in1) {
		return g.boolValue() ? in1 : in0;
	}

	@SafeVarargs
	@Comment("Returns the highest input value.")
	static <T extends AnyElementaryValue> T MAX(final T... values) {
		return Stream.of(values).max(ValueOperations::compareTo).orElseThrow();
	}

	@SafeVarargs
	@Comment("Returns the lowest input value.")
	static <T extends AnyElementaryValue> T MIN(final T... values) {
		return Stream.of(values).min(ValueOperations::compareTo).orElseThrow();
	}

	@Comment("Returns a value clamped to a minimum and maximum range.")
	static <T extends AnyElementaryValue> T LIMIT(@Comment("The lowest possible value.") final T min,
			@Comment("The input value.") final T value, @Comment("The highest possible value.") final T max) {
		if (ValueOperations.operator_greaterThan(value, max)) {
			return max;
		} else if (ValueOperations.operator_lessThan(value, min)) {
			return min;
		} else {
			return value;
		}
	}

	@SafeVarargs
	@Comment("Returns one value from a set of input values. An integer selector value chooses which value to return.")
	static <T extends AnyIntValue, U extends AnyValue> U MUX(@Comment("Selector input.") final T k,
			@Comment("Value will be returned, if the selector selects this input.") final U... values) {
		return values[k.intValue()];
	}

	/* Comparisons */

	@SafeVarargs
	@Comment("Identifies if a value is greater than another value and operator can be cascaded to operate on 2 or more parameters.")
	static <T extends AnyElementaryValue> BoolValue GT(final T... values) {
		T last = null;
		for (final T element : values) {
			if (last != null && !ValueOperations.operator_greaterThan(last, element)) {
				return BoolValue.FALSE;
			}
			last = element;
		}
		return BoolValue.TRUE;
	}

	@SuppressWarnings("unchecked")
	@Comment("Identifies if a value is greater than or equal to another value and operator can be cascaded to operate on 2 or more parameters.")
	static <T extends AnyElementaryValue> BoolValue GE(final T... values) {
		T last = null;
		for (final T element : values) {
			if (last != null && !ValueOperations.operator_greaterEqualsThan(last, element)) {
				return BoolValue.FALSE;
			}
			last = element;
		}
		return BoolValue.TRUE;
	}

	@SuppressWarnings("unchecked")
	@Comment("Identifies if a value is equal to another value and operator can be cascaded to operate on 2 or more parameters.")
	static <T extends AnyElementaryValue> BoolValue EQ(final T... values) {
		T last = null;
		for (final T element : values) {
			if (last != null && !ValueOperations.equals(last, element)) {
				return BoolValue.FALSE;
			}
			last = element;
		}
		return BoolValue.TRUE;
	}

	@SuppressWarnings("unchecked")
	@Comment("Identifies if a value is less than or equal to another value and operator can be cascaded to operate on 2 or more parameters.")
	static <T extends AnyElementaryValue> BoolValue LE(final T... values) {
		T last = null;
		for (final T element : values) {
			if (last != null && !ValueOperations.operator_lessEqualsThan(last, element)) {
				return BoolValue.FALSE;
			}
			last = element;
		}
		return BoolValue.TRUE;
	}

	@SuppressWarnings("unchecked")
	@Comment("Identifies if a value is less than another value and operator can be cascaded to operate on 2 or more parameters.")
	static <T extends AnyElementaryValue> BoolValue LT(final T... values) {
		T last = null;
		for (final T element : values) {
			if (last != null && !ValueOperations.operator_lessThan(last, element)) {
				return BoolValue.FALSE;
			}
			last = element;
		}
		return BoolValue.TRUE;
	}

	@Comment("Identifies if a value is unequal to another value.")
	static <T extends AnyElementaryValue> BoolValue NE(final T first, final T second) {
		return ValueOperations.equals(first, second) ? BoolValue.FALSE : BoolValue.TRUE;
	}

	/* ANY_CHARS functions */
	@Comment("Returns the length of a String.")
	static <T extends AnyStringValue> ULIntValue LEN(final T string) {
		return ULIntValue.toULIntValue(string.length());
	}

	@Comment("Returns the left-most n characters of a String.")
	static <U extends AnyIntValue> StringValue LEFT(final StringValue string,
			@Comment("The number of characters to be returned.") final U length) {
		return apply(string, value -> value.substring(0, length.intValue()));
	}

	@Comment("Returns the left-most n characters of a WString.")
	static <U extends AnyIntValue> WStringValue LEFT(final WStringValue string,
			@Comment("The number of characters to be returned.") final U length) {
		return apply(string, value -> value.substring(0, length.intValue()));
	}

	@Comment("Returns the right-most n characters of a String.")
	static <U extends AnyIntValue> StringValue RIGHT(final StringValue string,
			@Comment("The number of characters to be returned.") final U length) {
		return apply(string, value -> value.substring(value.length() - length.intValue()));
	}

	@Comment("Returns the right-most n characters of a WString.")
	static <U extends AnyIntValue> WStringValue RIGHT(final WStringValue string,
			@Comment("The number of characters to be returned.") final U length) {
		return apply(string, value -> value.substring(value.length() - length.intValue()));
	}

	@Comment("Returns the middle n characters of a String starting at an offset.")
	static <U extends AnyIntValue, V extends AnyIntValue> StringValue MID(final StringValue string,
			@Comment("The number of characters to be returned.") final U length,
			@Comment("The 1-based starting offset.") final V position) {
		return apply(string,
				value -> value.substring(position.intValue() - 1, position.intValue() + length.intValue() - 1));
	}

	@Comment("Returns the middle n characters of a WString starting at an offset.")
	static <U extends AnyIntValue, V extends AnyIntValue> WStringValue MID(final WStringValue string,
			@Comment("The number of characters to be returned.") final U length,
			@Comment("The 1-based starting offset.") final V position) {
		return apply(string,
				value -> value.substring(position.intValue() - 1, position.intValue() + length.intValue() - 1));
	}

	@SuppressWarnings("unchecked")
	@SafeVarargs
	@Comment("Concatenates two or more Strings.")
	static <T extends AnySCharsValue> StringValue CONCAT(final T... strings) {
		return StringValue.toStringValue(
				Stream.of(strings).reduce((value1, value2) -> (T) apply(value1, value2, String::concat)).orElseThrow());
	}

	@SuppressWarnings("unchecked")
	@SafeVarargs
	@Comment("Concatenates two or more WStrings.")
	static <T extends AnyWCharsValue> WStringValue CONCAT(final T... strings) {
		return WStringValue.toWStringValue(
				Stream.of(strings).reduce((value1, value2) -> (T) apply(value1, value2, String::concat)).orElseThrow());
	}

	@Comment("Deletes characters from a String, then inserts another String at the position of the deleted characters.")
	static <U extends AnyIntValue, T extends AnySCharsValue> StringValue INSERT(final StringValue first,
			@Comment("The String to be inserted.") final T second,
			@Comment("The replacement position.") final U position) {
		return apply(first, value -> value.substring(0, position.intValue()).concat(second.stringValue())
				.concat(value.substring(position.intValue())));
	}

	@Comment("Deletes characters from a WString, then inserts another WString at the position of the deleted characters.")
	static <U extends AnyIntValue, T extends AnyWCharsValue> WStringValue INSERT(final WStringValue first,
			@Comment("The WString to be inserted.") final T second,
			@Comment("The replacement position.") final U position) {
		return apply(first, value -> value.substring(0, position.intValue()).concat(second.stringValue())
				.concat(value.substring(position.intValue())));
	}

	@Comment("Deletes a number of characters from a String starting at a position in the String.")
	static <U extends AnyIntValue, V extends AnyIntValue> StringValue DELETE(final StringValue string,
			@Comment("The number of character to be deleted.") final U length,
			@Comment("The delete position.") final V position) {
		return apply(string, value -> value.substring(0, position.intValue() - 1)
				.concat(value.substring(position.intValue() + length.intValue() - 1)));
	}

	@Comment("Deletes a number of characters from a WString starting at a position in the WString.")
	static <U extends AnyIntValue, V extends AnyIntValue> WStringValue DELETE(final WStringValue string,
			@Comment("The number of character to be deleted.") final U length,
			@Comment("The delete position.") final V position) {
		return apply(string, value -> value.substring(0, position.intValue() - 1)
				.concat(value.substring(position.intValue() + length.intValue() - 1)));
	}

	@Comment("Deletes characters from a String, then inserts another String at the position of the deleted characters.")
	static <U extends AnyIntValue, V extends AnyIntValue, T extends AnySCharsValue> StringValue REPLACE(
			final StringValue first, @Comment("The String to be inserted.") final T second,
			@Comment("The number of character to be deleted before the new String is inserted.") final U length,
			@Comment("The replacement position.") final V position) {
		return apply(first, value -> value.substring(0, position.intValue() - 1).concat(second.stringValue())
				.concat(value.substring(position.intValue() + length.intValue() - 1)));
	}

	@Comment("Deletes characters from a WString, then inserts another WString at the position of the deleted characters.")
	static <U extends AnyIntValue, V extends AnyIntValue, T extends AnyWCharsValue> WStringValue REPLACE(
			final WStringValue first, @Comment("The WString to be inserted.") final T second,
			@Comment("The number of character to be deleted before the new WString is inserted.") final U length,
			@Comment("The replacement position.") final V position) {
		return apply(first, value -> value.substring(0, position.intValue() - 1).concat(second.stringValue())
				.concat(value.substring(position.intValue() + length.intValue() - 1)));
	}

	@Comment("Returns the location of the first occurrence a String in a String.")
	static <T extends AnySCharsValue> ULIntValue FIND(@Comment("The String to be searched.") final StringValue first,
			@Comment("The String to be found.") final T second) {
		return ULIntValue.toULIntValue(first.stringValue().indexOf(second.stringValue()) + 1L);
	}

	@Comment("Returns the location of the first occurrence a WString in a WString.")
	static <T extends AnyWCharsValue> ULIntValue FIND(@Comment("The WString to be searched.") final WStringValue first,
			@Comment("The WString to be found.") final T second) {
		return ULIntValue.toULIntValue(first.stringValue().indexOf(second.stringValue()) + 1L);
	}

	/* Numeric functions for time and date */

	@SuppressWarnings("unchecked")
	@Comment("Returns addition of two DURATION values.")
	static <T extends AnyDurationValue> T ADD(final T first, final T second) {
		return (T) ValueOperations.add(first, second);
	}

	@Comment("Returns addition of two TIME values.")
	static TimeValue ADD_TIME(final TimeValue first, final TimeValue second) {
		return ADD(first, second);
	}

	@Comment("Returns addition of two LTIME values.")
	static LTimeValue ADD_LTIME(final LTimeValue first, final LTimeValue second) {
		return ADD(first, second);
	}

	@Comment("Returns addition of TIME_OF_DAY value and TIME value as TIME_OF_DAY value.")
	static TimeOfDayValue ADD(final TimeOfDayValue first, final TimeValue second) {
		return TimeOfDayValue.toTimeOfDayValue(first.toNanos() + second.longValue());
	}

	@Comment("Returns addition of LTIME_OF_DAY value and long LTIME value as LTIME_OF_DAY value.")
	static LTimeOfDayValue ADD(final LTimeOfDayValue first, final LTimeValue second) {
		return LTimeOfDayValue.toLTimeOfDayValue(first.toNanos() + second.longValue());
	}

	@Comment("Returns addition of TIME_OF_DAY value and TIME value as TIME_OF_DAY value.")
	static TimeOfDayValue ADD_TOD_TIME(final TimeOfDayValue first, final TimeValue second) {
		return ADD(first, second);
	}

	@Comment("Returns addition of LTIME_OF_DAY value and LTIME value as LTIME_OF_DAY value.")
	static LTimeOfDayValue ADD_LTOD_LTIME(final LTimeOfDayValue first, final LTimeValue second) {
		return ADD(first, second);
	}

	@Comment("Returns addition of DATE_AND_TIME value and TIME value as DATE_AND_TIME value.")
	static DateAndTimeValue ADD(final DateAndTimeValue first, final TimeValue second) {
		return DateAndTimeValue.toDateAndTimeValue(first.toNanos() + second.longValue());
	}

	@Comment("Returns addition of LDATE_AND_TIME value and LTIME value as LDATE_AND_TIME value.")
	static LDateAndTimeValue ADD(final LDateAndTimeValue first, final LTimeValue second) {
		return LDateAndTimeValue.toLDateAndTimeValue(first.toNanos() + second.longValue());
	}

	@Comment("Returns addition of DATE_AND_TIME value and TIME value as DATE_AND_TIME value.")
	static DateAndTimeValue ADD_DT_TIME(final DateAndTimeValue first, final TimeValue second) {
		return ADD(first, second);
	}

	@Comment("Returns addition of LDATE_AND_TIME value and LTIME value as LDATE_AND_TIME value.")
	static LDateAndTimeValue ADD_LDT_LTIME(final LDateAndTimeValue first, final LTimeValue second) {
		return ADD(first, second);
	}

	@SuppressWarnings("unchecked")
	@Comment("Returns subtraction of two DURATION values.")
	static <T extends AnyDurationValue> T SUB(final T first, final T second) {
		return (T) ValueOperations.subtract(first, second);
	}

	@Comment("Returns subtraction of two TIME values.")
	static TimeValue SUB_TIME(final TimeValue first, final TimeValue second) {
		return SUB(first, second);
	}

	@Comment("Returns subtraction of two LTIME values.")
	static LTimeValue SUB_LTIME(final LTimeValue first, final LTimeValue second) {
		return SUB(first, second);
	}

	@Comment("Returns subtraction of two DATE values as TIME value.")
	static TimeValue SUB(final DateValue first, final DateValue second) {
		return TimeValue.toTimeValue(first.toNanos() - second.toNanos());
	}

	@Comment("Returns subtraction of two LDATE values as LTIME value.")
	static LTimeValue SUB(final LDateValue first, final LDateValue second) {
		return LTimeValue.toLTimeValue(first.toNanos() - second.toNanos());
	}

	@Comment("Returns subtraction of two DATE values as TIME value.")
	static TimeValue SUB_DATE_DATE(final DateValue first, final DateValue second) {
		return SUB(first, second);
	}

	@Comment("Returns subtraction of two LDATE values as LTIME value.")
	static LTimeValue SUB_LDATE_LDATE(final LDateValue first, final LDateValue second) {
		return SUB(first, second);
	}

	@Comment("Returns subtraction of TIME_OF_DAY value and TIME value as TIME_OF_DAY value.")
	static TimeOfDayValue SUB(final TimeOfDayValue first, final TimeValue second) {
		return TimeOfDayValue.toTimeOfDayValue(first.toNanos() - second.longValue());
	}

	@Comment("Returns subtraction of LTIME_OF_DAY value and LTIME value as LTIME_OF_DAY value.")
	static LTimeOfDayValue SUB(final LTimeOfDayValue first, final LTimeValue second) {
		return LTimeOfDayValue.toLTimeOfDayValue(first.toNanos() - second.longValue());
	}

	@Comment("Returns subtraction of TIME_OF_DAY value and TIME value as TIME_OF_DAY value.")
	static TimeOfDayValue SUB_TOD_TIME(final TimeOfDayValue first, final TimeValue second) {
		return SUB(first, second);
	}

	@Comment("Returns subtraction of LTIME_OF_DAY value and LTIME value as LTIME_OF_DAY value.")
	static LTimeOfDayValue SUB_LTOD_LTIME(final LTimeOfDayValue first, final LTimeValue second) {
		return SUB(first, second);
	}

	@Comment("Returns subtraction of two TIME_OF_DAY values as TIME value.")
	static TimeValue SUB(final TimeOfDayValue first, final TimeOfDayValue second) {
		return TimeValue.toTimeValue(first.toNanos() - second.toNanos());
	}

	@Comment("Returns subtraction of two LTIME_OF_DAY values as LTIME value.")
	static LTimeValue SUB(final LTimeOfDayValue first, final LTimeOfDayValue second) {
		return LTimeValue.toLTimeValue(first.toNanos() - second.toNanos());
	}

	@Comment("Returns subtraction of two TIME_OF_DAY values as TIME value.")
	static TimeValue SUB_TOD_TOD(final TimeOfDayValue first, final TimeOfDayValue second) {
		return SUB(first, second);
	}

	@Comment("Returns subtraction of LTIME_OF_DAY values as LTIME value.")
	static LTimeValue SUB_LTOD_LTOD(final LTimeOfDayValue first, final LTimeOfDayValue second) {
		return SUB(first, second);
	}

	@Comment("Returns subtraction of DATE_AND_TIME value and TIME value as DATE_AND_TIME value.")
	static DateAndTimeValue SUB(final DateAndTimeValue first, final TimeValue second) {
		return DateAndTimeValue.toDateAndTimeValue(first.toNanos() - second.longValue());
	}

	@Comment("Returns subtraction of LDATE_AND_TIME value and LTIME value as LDATE_AND_TIME value.")
	static LDateAndTimeValue SUB(final LDateAndTimeValue first, final LTimeValue second) {
		return LDateAndTimeValue.toLDateAndTimeValue(first.toNanos() - second.longValue());
	}

	@Comment("Returns subtraction of DATE_AND_TIME value and TIME value as DATE_AND_TIME value.")
	static DateAndTimeValue SUB_DT_TIME(final DateAndTimeValue first, final TimeValue second) {
		return SUB(first, second);
	}

	@Comment("Returns subtraction of LDATE_AND_TIME value and LTIME value as LDATE_AND_TIME value.")
	static LDateAndTimeValue SUB_LDT_LTIME(final LDateAndTimeValue first, final LTimeValue second) {
		return SUB(first, second);
	}

	@Comment("Returns subtraction of two DATE_AND_TIME values as TIME value.")
	static TimeValue SUB(final DateAndTimeValue first, final DateAndTimeValue second) {
		return TimeValue.toTimeValue(first.toNanos() - second.toNanos());
	}

	@Comment("Returns subtraction of two LDATE_AND_TIME values as LTIME value.")
	static LTimeValue SUB(final LDateAndTimeValue first, final LDateAndTimeValue second) {
		return LTimeValue.toLTimeValue(first.toNanos() - second.toNanos());
	}

	@Comment("Returns subtraction of two DATE_AND_TIME values as TIME value.")
	static TimeValue SUB_DT_DT(final DateAndTimeValue first, final DateAndTimeValue second) {
		return SUB(first, second);
	}

	@Comment("Returns subtraction of two LDATE_AND_TIME values as LTIME value.")
	static LTimeValue SUB_LDT_LDT(final LDateAndTimeValue first, final LDateAndTimeValue second) {
		return SUB(first, second);
	}

	@SuppressWarnings("unchecked")
	@Comment("Returns multiplication of DURATION value and ANY_NUM value as DURATION value.")
	static <T extends AnyDurationValue, U extends AnyNumValue> T MUL(final T first, final U second) {
		return (T) ValueOperations.multiply(first, second);
	}

	@Comment("Returns multiplication of TIME value and ANY_NUM value as TIME value.")
	static <U extends AnyNumValue> TimeValue MUL_TIME(final TimeValue first, final U second) {
		return MUL(first, second);
	}

	@Comment("Returns multiplication of LTIME value and ANY_NUM value as LTIME value.")
	static <U extends AnyNumValue> LTimeValue MUL_LTIME(final LTimeValue first, final U second) {
		return MUL(first, second);
	}

	@SuppressWarnings("unchecked")
	@Comment("Returns division of DURATION value and ANY_NUM value as DURATION value.")
	static <T extends AnyDurationValue, U extends AnyNumValue> T DIV(final T first, final U second) {
		return (T) ValueOperations.divideBy(first, second);
	}

	@Comment("Returns division of TIME value and ANY_NUM value as TIME value.")
	static <U extends AnyNumValue> TimeValue DIV_TIME(final TimeValue first, final U second) {
		return DIV(first, second);
	}

	@Comment("Returns division of LTIME value and ANY_NUM value as LTIME value.")
	static <U extends AnyNumValue> LTimeValue DIV_LTIME(final LTimeValue first, final U second) {
		return DIV(first, second);
	}

	// Additional time functions CONCAT and SPLIT
	@Comment("Concatenates DATE value and TIME_OF_DAY value and returns DATE_AND_TIME value.")
	static DateAndTimeValue CONCAT_DATE_TOD(final DateValue first, final TimeOfDayValue second) {
		return DateAndTimeValue.toDateAndTimeValue(first.toLocalDate().atTime(second.toLocalTime()));
	}

	@Comment("Concatenates DATE value and LTIME_OF_DAY value and returns LDATE_AND_TIME value.")
	static LDateAndTimeValue CONCAT_DATE_LTOD(final DateValue first, final LTimeOfDayValue second) {
		return LDateAndTimeValue.toLDateAndTimeValue(first.toLocalDate().atTime(second.toLocalTime()));
	}

	@Comment("Concatenates time informations about year, month and day and returns DATE value.")
	static <T extends AnyIntValue> DateValue CONCAT_DATE(@Comment("The year number.") final T year,
			@Comment("The month number.") final T month, @Comment("The day number.") final T day) {
		return DateValue.toDateValue(LocalDate.of(year.intValue(), month.intValue(), day.intValue()));
	}

	@Comment("Concatenates time informations about hour, minute, second and millisecond and returns TIME_OF_DAY value.")
	static <T extends AnyIntValue> TimeOfDayValue CONCAT_TOD(@Comment("The hour number.") final T hour,
			@Comment("The minute number.") final T minute, @Comment("The second number.") final T second,
			@Comment("The millisecond number.") final T millisecond) {
		return TimeOfDayValue.toTimeOfDayValue(LocalTime.of(hour.intValue(), minute.intValue(), second.intValue(),
				millisecond.intValue() * 1_000_000));
	}

	@Comment("Concatenates time informations about hour, minute, second and millisecond and returns LTIME_OF_DAY value.")
	static <T extends AnyIntValue> LTimeOfDayValue CONCAT_LTOD(@Comment("The hour number.") final T hour,
			@Comment("The minute number.") final T minute, @Comment("The second number.") final T second,
			@Comment("The millisecond number.") final T millisecond) {
		return LTimeOfDayValue.toLTimeOfDayValue(LocalTime.of(hour.intValue(), minute.intValue(), second.intValue(),
				millisecond.intValue() * 1_000_000));
	}

	@Comment("Concatenates time informations about year, month, day, hour, minute, second and millisecond and returns DATE_AND_TIME value.")
	static <T extends AnyIntValue> DateAndTimeValue CONCAT_DT(@Comment("The year number.") final T year,
			@Comment("The month number.") final T month, @Comment("The day number.") final T day,
			@Comment("The hour number.") final T hour, @Comment("The minute number.") final T minute,
			@Comment("The second number.") final T second, @Comment("The millisecond number.") final T millisecond) {
		return DateAndTimeValue.toDateAndTimeValue(LocalDateTime.of(year.intValue(), month.intValue(), day.intValue(),
				hour.intValue(), minute.intValue(), second.intValue(), millisecond.intValue() * 1_000_000));
	}

	@Comment("Concatenates time informations about year, month, day, hour, minute, second and millisecond and returns LDATE_AND_TIME value.")
	static <T extends AnyIntValue> LDateAndTimeValue CONCAT_LDT(@Comment("The year number.") final T year,
			@Comment("The month number.") final T month, @Comment("The day number.") final T day,
			@Comment("The hour number.") final T hour, @Comment("The minute number.") final T minute,
			@Comment("The second number.") final T second, @Comment("The millisecond number.") final T millisecond) {
		return LDateAndTimeValue.toLDateAndTimeValue(LocalDateTime.of(year.intValue(), month.intValue(), day.intValue(),
				hour.intValue(), minute.intValue(), second.intValue(), millisecond.intValue() * 1_000_000));
	}

	@Comment("Splits time informations about year, month and day of DATE value.")
	static <T extends AnyIntValue, U extends AnyIntValue, V extends AnyIntValue> void SPLIT_DATE(
			@Comment("The input date time.") final DateValue in, @Comment("The year number.") final Variable<T> year,
			@Comment("The month number.") final Variable<U> month, @Comment("The day number.") final Variable<V> day) {
		final LocalDate value = in.toLocalDate();
		year.setValue(DIntValue.toDIntValue(value.getYear()));
		month.setValue(DIntValue.toDIntValue(value.getMonthValue()));
		day.setValue(DIntValue.toDIntValue(value.getDayOfMonth()));
	}

	@Comment("Splits time informations about day, hour, minute and second of TIME_OF_DAY value.")
	static <T extends AnyIntValue, U extends AnyIntValue, V extends AnyIntValue, W extends AnyIntValue> void SPLIT_TOD(
			@Comment("The input date time.") final TimeOfDayValue in,
			@Comment("The hour number.") final Variable<T> hour,
			@Comment("The minute number.") final Variable<U> minute,
			@Comment("The second number.") final Variable<V> second,
			@Comment("The millisecond number.") final Variable<W> millisecond) {
		final LocalTime value = in.toLocalTime();
		hour.setValue(DIntValue.toDIntValue(value.getHour()));
		minute.setValue(DIntValue.toDIntValue(value.getMinute()));
		second.setValue(DIntValue.toDIntValue(value.getSecond()));
		millisecond.setValue(DIntValue.toDIntValue(value.getNano() / 1000000));
	}

	@Comment("Splits time informations about day, hour, minute and second of LTIME_OF_DAY value.")
	static <T extends AnyIntValue, U extends AnyIntValue, V extends AnyIntValue, W extends AnyIntValue> void SPLIT_LTOD(
			@Comment("The input date time.") final LTimeOfDayValue in,
			@Comment("The hour number.") final Variable<T> hour,
			@Comment("The minute number.") final Variable<U> minute,
			@Comment("The millisecond number.") final Variable<V> second, final Variable<W> millisecond) {
		final LocalTime value = in.toLocalTime();
		hour.setValue(DIntValue.toDIntValue(value.getHour()));
		minute.setValue(DIntValue.toDIntValue(value.getMinute()));
		second.setValue(DIntValue.toDIntValue(value.getSecond()));
		millisecond.setValue(DIntValue.toDIntValue(value.getNano() / 1000000));
	}

	@Comment("Splits time informations about year, month, day, hour, minute and second of DATE_AND_TIME value.")
	static <T extends AnyIntValue, U extends AnyIntValue, V extends AnyIntValue, W extends AnyIntValue, X extends AnyIntValue, Y extends AnyIntValue, Z extends AnyIntValue> void SPLIT_DT(
			@Comment("The input date time.") final DateAndTimeValue in,
			@Comment("The year number.") final Variable<T> year, @Comment("The month number.") final Variable<U> month,
			@Comment("The day number.") final Variable<V> day, @Comment("The hour number.") final Variable<W> hour,
			@Comment("The minute number.") final Variable<X> minute,
			@Comment("The second number.") final Variable<Y> second,
			@Comment("The millisecond number.") final Variable<Z> millisecond) {
		final LocalDateTime value = in.toLocalDateTime();
		year.setValue(DIntValue.toDIntValue(value.getYear()));
		month.setValue(DIntValue.toDIntValue(value.getMonthValue()));
		day.setValue(DIntValue.toDIntValue(value.getDayOfMonth()));
		hour.setValue(DIntValue.toDIntValue(value.getHour()));
		minute.setValue(DIntValue.toDIntValue(value.getMinute()));
		second.setValue(DIntValue.toDIntValue(value.getSecond()));
		millisecond.setValue(DIntValue.toDIntValue(value.getNano() / 1000000));
	}

	@Comment("Splits time informations about year, month, day, hour, minute and second of LDATE_AND_TIME value.")
	static <T extends AnyIntValue, U extends AnyIntValue, V extends AnyIntValue, W extends AnyIntValue, X extends AnyIntValue, Y extends AnyIntValue, Z extends AnyIntValue> void SPLIT_LDT(
			@Comment("The input date time.") final LDateAndTimeValue in,
			@Comment("The year number.") final Variable<T> year, @Comment("The month number.") final Variable<U> month,
			@Comment("The day number.") final Variable<V> day, @Comment("The hour number.") final Variable<W> hour,
			@Comment("The minute number.") final Variable<X> minute,
			@Comment("The second number.") final Variable<Y> second,
			@Comment("The millisecond number.") final Variable<Z> millisecond) {
		final LocalDateTime value = in.toLocalDateTime();
		year.setValue(DIntValue.toDIntValue(value.getYear()));
		month.setValue(DIntValue.toDIntValue(value.getMonthValue()));
		day.setValue(DIntValue.toDIntValue(value.getDayOfMonth()));
		hour.setValue(DIntValue.toDIntValue(value.getHour()));
		minute.setValue(DIntValue.toDIntValue(value.getMinute()));
		second.setValue(DIntValue.toDIntValue(value.getSecond()));
		millisecond.setValue(DIntValue.toDIntValue(value.getNano() / 1000000));
	}

	@Comment("Returns the day of week of DATE value.")
	static SIntValue DAY_OF_WEEK(final DateValue value) {
		return SIntValue.toSIntValue((byte) value.toLocalDate().getDayOfWeek().getValue());
	}

	@Comment("Converts a value into the big-endian format.")
	static <T extends AnyValue> T TO_BIG_ENDIAN(final T value) {
		return value;
	}

	@SuppressWarnings("unchecked")
	@Comment("Converts a value into the little-endian format.")
	static <T extends AnyValue> T TO_LITTLE_ENDIAN(final T value) {
		return (T) ValueOperations.reverseBytes(value);
	}

	@Comment("Converts an entered value in big-endian format into the appropriate endian format of the target system.")
	static <T extends AnyValue> T FROM_BIG_ENDIAN(final T value) {
		return value;
	}

	@SuppressWarnings("unchecked")
	@Comment("Converts an entered value in little-endian format into the appropriate endian format of the target system.")
	static <T extends AnyValue> T FROM_LITTLE_ENDIAN(final T value) {
		return (T) ValueOperations.reverseBytes(value);
	}

	/* Validation functions */

	@Comment("Returns TRUE if the parameter is a ANY_REAL number.")
	static <T extends AnyRealValue> BoolValue IS_VALID(final T value) {
		return BoolValue.toBoolValue(Double.isFinite(value.doubleValue()));
	}

	@Comment("Returns TRUE if the parameter is valid BCD.")
	static <T extends AnyBitValue> BoolValue IS_VALID_BCD(final T value) {
		for (final byte packed : value.bigIntegerValue().toByteArray()) {
			final int packedInt = Byte.toUnsignedInt(packed);
			final int upper = packedInt >>> BCD_BITS;
			final int lower = packedInt & BCD_MASK;
			if (upper > BCD_DIGIT_MAX || lower > BCD_DIGIT_MAX) {
				return BoolValue.FALSE;
			}
		}
		return BoolValue.TRUE;
	}

	/* conversion functions */

	/* LREAL_TO */
	@Comment("Converts LREAL value TO REAL value.")
	static RealValue LREAL_TO_REAL(final LRealValue value) {
		return RealValue.toRealValue(value);
	}

	@Comment("Converts LREAL value to LINT value.")
	static LIntValue LREAL_TO_LINT(final LRealValue value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Converts LREAL value to DINT value.")
	static DIntValue LREAL_TO_DINT(final LRealValue value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Converts LREAL value to INT value.")
	static IntValue LREAL_TO_INT(final LRealValue value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Converts LREAL value to SINT value.")
	static SIntValue LREAL_TO_SINT(final LRealValue value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Converts LREAL value to LINT value.")
	static ULIntValue LREAL_TO_ULINT(final LRealValue value) {
		return ULIntValue.toULIntValue(value);
	}

	@Comment("Converts LREAL value to UDINT value.")
	static UDIntValue LREAL_TO_UDINT(final LRealValue value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Converts LREAL value to UINT value.")
	static UIntValue LREAL_TO_UINT(final LRealValue value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Converts LREAL value to USINT value.")
	static USIntValue LREAL_TO_USINT(final LRealValue value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Converts LREAL value to LWORD value.")
	static LWordValue LREAL_TO_LWORD(final LRealValue value) {
		return LWordValue.toLWordValue(Double.doubleToRawLongBits(value.doubleValue()));
	}

	/* REAL_TO */
	@Comment("Converts REAL value to LREAL value.")
	static LRealValue REAL_TO_LREAL(final RealValue value) {
		return LRealValue.toLRealValue(value);
	}

	@Comment("Converts REAL value to LINT value.")
	static LIntValue REAL_TO_LINT(final RealValue value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Converts REAL value to DINT value.")
	static DIntValue REAL_TO_DINT(final RealValue value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Converts REAL value to INT value.")
	static IntValue REAL_TO_INT(final RealValue value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Converts REAL value to SINT value.")
	static SIntValue REAL_TO_SINT(final RealValue value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Converts REAL value to ULINT value.")
	static ULIntValue REAL_TO_ULINT(final RealValue value) {
		return ULIntValue.toULIntValue(value);
	}

	@Comment("Converts REAL value to UDINT value.")
	static UDIntValue REAL_TO_UDINT(final RealValue value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Converts REAL value to UINT value.")
	static UIntValue REAL_TO_UINT(final RealValue value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Converts REAL value to USINT value.")
	static USIntValue REAL_TO_USINT(final RealValue value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Converts REAL value to DWORD value.")
	static DWordValue REAL_TO_DWORD(final RealValue value) {
		return DWordValue.toDWordValue(Float.floatToRawIntBits(value.floatValue()));
	}

	/* LINT_TO */
	@Comment("Converts LINT value to LREAL value.")
	static LRealValue LINT_TO_LREAL(final LIntValue value) {
		return LRealValue.toLRealValue(value);
	}

	@Comment("Converts LINT value to REAL value.")
	static RealValue LINT_TO_REAL(final LIntValue value) {
		return RealValue.toRealValue(value);
	}

	@Comment("Converts LINT value to DINT value.")
	static DIntValue LINT_TO_DINT(final LIntValue value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Converts LINT value to INT value.")
	static IntValue LINT_TO_INT(final LIntValue value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Converts LINT value to SINT value.")
	static SIntValue LINT_TO_SINT(final LIntValue value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Converts LINT value to ULINT value.")
	static ULIntValue LINT_TO_ULINT(final LIntValue value) {
		return ULIntValue.toULIntValue(value);
	}

	@Comment("Converts LINT value to UDINT value.")
	static UDIntValue LINT_TO_UDINT(final LIntValue value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Converts LINT value to UINT value.")
	static UIntValue LINT_TO_UINT(final LIntValue value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Converts LINT value to USINT value.")
	static USIntValue LINT_TO_USINT(final LIntValue value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Converts LINT value to long word value.")
	static LWordValue LINT_TO_LWORD(final LIntValue value) {
		return LWordValue.toLWordValue(value.longValue());
	}

	@Comment("Converts LINT value to DWORD value.")
	static DWordValue LINT_TO_DWORD(final LIntValue value) {
		return DWordValue.toDWordValue(value.intValue());
	}

	@Comment("Converts LINT value to WORD value.")
	static WordValue LINT_TO_WORD(final LIntValue value) {
		return WordValue.toWordValue(value.shortValue());
	}

	@Comment("Converts LINT value to BYTE value.")
	static ByteValue LINT_TO_BYTE(final LIntValue value) {
		return ByteValue.toByteValue(value.byteValue());
	}

	/* DINT_TO */
	@Comment("Converts DINT value to REAL value.")
	static RealValue DINT_TO_REAL(final DIntValue value) {
		return RealValue.toRealValue(value);
	}

	@Comment("Converts DINT value to long LINT value.")
	static LIntValue DINT_TO_LINT(final DIntValue value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Converts DINT value to INT value.")
	static IntValue DINT_TO_INT(final DIntValue value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Converts DINT value to SINT value.")
	static SIntValue DINT_TO_SINT(final DIntValue value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Converts DINT value to ULINT value.")
	static ULIntValue DINT_TO_ULINT(final DIntValue value) {
		return ULIntValue.toULIntValue(value);
	}

	@Comment("Converts DINT value to UDINT value.")
	static UDIntValue DINT_TO_UDINT(final DIntValue value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Converts DINT value to UINT value.")
	static UIntValue DINT_TO_UINT(final DIntValue value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Converts DINT value to USINT value.")
	static USIntValue DINT_TO_USINT(final DIntValue value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Converts DINT value to LWORD value.")
	static LWordValue DINT_TO_LWORD(final DIntValue value) {
		return LWordValue.toLWordValue(value.longValue());
	}

	@Comment("Converts DINT value to DWORD value.")
	static DWordValue DINT_TO_DWORD(final DIntValue value) {
		return DWordValue.toDWordValue(value.intValue());
	}

	@Comment("Converts DINT value to WORD value.")
	static WordValue DINT_TO_WORD(final DIntValue value) {
		return WordValue.toWordValue(value.shortValue());
	}

	@Comment("Converts DINT value to BYTE value.")
	static ByteValue DINT_TO_BYTE(final DIntValue value) {
		return ByteValue.toByteValue(value.byteValue());
	}

	/* INT_TO */
	@Comment("Converts INT value to LREAL value.")
	static LRealValue INT_TO_LREAL(final IntValue value) {
		return LRealValue.toLRealValue(value);
	}

	@Comment("Converts INT value to REAL value.")
	static RealValue INT_TO_REAL(final IntValue value) {
		return RealValue.toRealValue(value);
	}

	@Comment("Converts INT value to LINT value.")
	static LIntValue INT_TO_LINT(final IntValue value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Converts INT value to DINT value.")
	static DIntValue INT_TO_DINT(final IntValue value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Converts INT value to SINT value.")
	static SIntValue INT_TO_SINT(final IntValue value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Converts INT value to ULINT value.")
	static ULIntValue INT_TO_ULINT(final IntValue value) {
		return ULIntValue.toULIntValue(value);
	}

	@Comment("Converts INT value to UDINT value.")
	static UDIntValue INT_TO_UDINT(final IntValue value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Converts INT value to UINT value.")
	static UIntValue INT_TO_UINT(final IntValue value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Converts INT value to USINT value.")
	static USIntValue INT_TO_USINT(final IntValue value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Converts INT value to LWORD value.")
	static LWordValue INT_TO_LWORD(final IntValue value) {
		return LWordValue.toLWordValue(value.longValue());
	}

	@Comment("Converts INT value to DWORD value.")
	static DWordValue INT_TO_DWORD(final IntValue value) {
		return DWordValue.toDWordValue(value.intValue());
	}

	@Comment("Converts INT value to WORD value.")
	static WordValue INT_TO_WORD(final IntValue value) {
		return WordValue.toWordValue(value.shortValue());
	}

	@Comment("Converts INT value to BYTE value.")
	static ByteValue INT_TO_BYTE(final IntValue value) {
		return ByteValue.toByteValue(value.byteValue());
	}

	/* SINT_TO */
	@Comment("Converts SINT value to LREAL value.")
	static LRealValue SINT_TO_LREAL(final SIntValue value) {
		return LRealValue.toLRealValue(value);
	}

	@Comment("Converts SINT value to REAL value.")
	static RealValue SINT_TO_REAL(final SIntValue value) {
		return RealValue.toRealValue(value);
	}

	@Comment("Converts SINT value to LINT value.")
	static LIntValue SINT_TO_LINT(final SIntValue value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Converts SINT value to DINT value.")
	static DIntValue SINT_TO_DINT(final SIntValue value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Converts SINT value to INT value.")
	static IntValue SINT_TO_INT(final SIntValue value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Converts SINT value to ULINT value.")
	static ULIntValue SINT_TO_ULINT(final SIntValue value) {
		return ULIntValue.toULIntValue(value);
	}

	@Comment("Converts SINT value to UDINT value.")
	static UDIntValue SINT_TO_UDINT(final SIntValue value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Converts SINT value to UINT value.")
	static UIntValue SINT_TO_UINT(final SIntValue value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Converts SINT value to USINT value.")
	static USIntValue SINT_TO_USINT(final SIntValue value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Converts SINT value to LWORD value.")
	static LWordValue SINT_TO_LWORD(final SIntValue value) {
		return LWordValue.toLWordValue(value.longValue());
	}

	@Comment("Converts SINT value to DWORD value.")
	static DWordValue SINT_TO_DWORD(final SIntValue value) {
		return DWordValue.toDWordValue(value.intValue());
	}

	@Comment("Converts SINT value to WORD value.")
	static WordValue SINT_TO_WORD(final SIntValue value) {
		return WordValue.toWordValue(value.shortValue());
	}

	@Comment("Converts SINT value to BYTE value.")
	static ByteValue SINT_TO_BYTE(final SIntValue value) {
		return ByteValue.toByteValue(value.byteValue());
	}

	/* ULINT_TO */
	@Comment("Converts ULINT value to LREAL value.")
	static LRealValue ULINT_TO_LREAL(final ULIntValue value) {
		return LRealValue.toLRealValue(value);
	}

	@Comment("Converts ULINT value to REAL value.")
	static RealValue ULINT_TO_REAL(final ULIntValue value) {
		return RealValue.toRealValue(value);
	}

	@Comment("Converts ULINT value to LINT value.")
	static LIntValue ULINT_TO_LINT(final ULIntValue value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Converts ULINT value to DINT value.")
	static DIntValue ULINT_TO_DINT(final ULIntValue value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Converts ULINT value to INT value.")
	static IntValue ULINT_TO_INT(final ULIntValue value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Converts ULINT value to SINT value.")
	static SIntValue ULINT_TO_SINT(final ULIntValue value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Converts ULINT value to UDINT value.")
	static UDIntValue ULINT_TO_UDINT(final ULIntValue value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Converts ULINT value to UINT value.")
	static UIntValue ULINT_TO_UINT(final ULIntValue value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Converts ULINT value to USINT value.")
	static USIntValue ULINT_TO_USINT(final ULIntValue value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Converts ULINT value to LWORD value.")
	static LWordValue ULINT_TO_LWORD(final ULIntValue value) {
		return LWordValue.toLWordValue(value.longValue());
	}

	@Comment("Converts ULINT value to DWORD value.")
	static DWordValue ULINT_TO_DWORD(final ULIntValue value) {
		return DWordValue.toDWordValue(value.intValue());
	}

	@Comment("Converts ULINT value to WORD value.")
	static WordValue ULINT_TO_WORD(final ULIntValue value) {
		return WordValue.toWordValue(value.shortValue());
	}

	@Comment("Converts ULINT value to BYTE value.")
	static ByteValue ULINT_TO_BYTE(final ULIntValue value) {
		return ByteValue.toByteValue(value.byteValue());
	}

	/* UDINT_TO */
	@Comment("Converts UDINT value to LREAL value.")
	static LRealValue UDINT_TO_LREAL(final UDIntValue value) {
		return LRealValue.toLRealValue(value);
	}

	@Comment("Converts UDINT value to REAL value.")
	static RealValue UDINT_TO_REAL(final UDIntValue value) {
		return RealValue.toRealValue(value);
	}

	@Comment("Converts UDINT value to LINT value.")
	static LIntValue UDINT_TO_LINT(final UDIntValue value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Converts UDINT value to DINT value.")
	static DIntValue UDINT_TO_DINT(final UDIntValue value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Converts UDINT value to INT value.")
	static IntValue UDINT_TO_INT(final UDIntValue value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Converts UDINT value to SINT value.")
	static SIntValue UDINT_TO_SINT(final UDIntValue value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Converts UDINT value to ULINT value.")
	static ULIntValue UDINT_TO_ULINT(final UDIntValue value) {
		return ULIntValue.toULIntValue(value);
	}

	@Comment("Converts UDINT value to UINT value.")
	static UIntValue UDINT_TO_UINT(final UDIntValue value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Converts UDINT value to USINT value.")
	static USIntValue UDINT_TO_USINT(final UDIntValue value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Converts UDINT value to LWORD value.")
	static LWordValue UDINT_TO_LWORD(final UDIntValue value) {
		return LWordValue.toLWordValue(value.longValue());
	}

	@Comment("Converts UDINT value to DWORD value.")
	static DWordValue UDINT_TO_DWORD(final UDIntValue value) {
		return DWordValue.toDWordValue(value.intValue());
	}

	@Comment("Converts UDINT value to WORD value.")
	static WordValue UDINT_TO_WORD(final UDIntValue value) {
		return WordValue.toWordValue(value.shortValue());
	}

	@Comment("Converts UDINT value to BYTE value.")
	static ByteValue UDINT_TO_BYTE(final UDIntValue value) {
		return ByteValue.toByteValue(value.byteValue());
	}

	/* UINT_TO */
	@Comment("Converts UINT value to LREAL value.")
	static LRealValue UINT_TO_LREAL(final UIntValue value) {
		return LRealValue.toLRealValue(value);
	}

	@Comment("Converts UINT value to REAL value.")
	static RealValue UINT_TO_REAL(final UIntValue value) {
		return RealValue.toRealValue(value);
	}

	@Comment("Converts UINT value to LINT value.")
	static LIntValue UINT_TO_LINT(final UIntValue value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Converts UINT value to DINT value.")
	static DIntValue UINT_TO_DINT(final UIntValue value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Converts UINT value to INT value.")
	static IntValue UINT_TO_INT(final UIntValue value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Converts UINT value to SINT value.")
	static SIntValue UINT_TO_SINT(final UIntValue value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Converts UINT value to ULINT value.")
	static ULIntValue UINT_TO_ULINT(final UIntValue value) {
		return ULIntValue.toULIntValue(value);
	}

	@Comment("Converts UINT value to UDINT value.")
	static UDIntValue UINT_TO_UDINT(final UIntValue value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Converts UINT value to USINT value.")
	static USIntValue UINT_TO_USINT(final UIntValue value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Converts UINT value to LWORD value.")
	static LWordValue UINT_TO_LWORD(final UIntValue value) {
		return LWordValue.toLWordValue(value.longValue());
	}

	@Comment("Converts UINT value to DWORD value.")
	static DWordValue UINT_TO_DWORD(final UIntValue value) {
		return DWordValue.toDWordValue(value.intValue());
	}

	@Comment("Converts UINT value to WORD value.")
	static WordValue UINT_TO_WORD(final UIntValue value) {
		return WordValue.toWordValue(value.shortValue());
	}

	@Comment("Converts UINT value to BYTE value.")
	static ByteValue UINT_TO_BYTE(final UIntValue value) {
		return ByteValue.toByteValue(value.byteValue());
	}

	/* USINT_TO */
	@Comment("Converts USINT Value to LREAL value.")
	static LRealValue USINT_TO_LREAL(final USIntValue value) {
		return LRealValue.toLRealValue(value);
	}

	@Comment("Converts USINT Value to REAL value.")
	static RealValue USINT_TO_REAL(final USIntValue value) {
		return RealValue.toRealValue(value);
	}

	@Comment("Converts USINT Value to LINT value.")
	static LIntValue USINT_TO_LINT(final USIntValue value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Converts USINT Value to DINT value.")
	static DIntValue USINT_TO_DINT(final USIntValue value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Converts USINT Value to INT value.")
	static IntValue USINT_TO_INT(final USIntValue value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Converts USINT Value to SINT value.")
	static SIntValue USINT_TO_SINT(final USIntValue value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Converts USINT Value to ULINT value.")
	static ULIntValue USINT_TO_ULINT(final USIntValue value) {
		return ULIntValue.toULIntValue(value);
	}

	@Comment("Converts USINT Value to UDINT value.")
	static UDIntValue USINT_TO_UDINT(final USIntValue value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Converts USINT Value to UINT value.")
	static UIntValue USINT_TO_UINT(final USIntValue value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Converts USINT Value to LWORD value.")
	static LWordValue USINT_TO_LWORD(final USIntValue value) {
		return LWordValue.toLWordValue(value.longValue());
	}

	@Comment("Converts USINT Value to DWORD value.")
	static DWordValue USINT_TO_DWORD(final USIntValue value) {
		return DWordValue.toDWordValue(value.intValue());
	}

	@Comment("Converts USINT Value to WORD value.")
	static WordValue USINT_TO_WORD(final USIntValue value) {
		return WordValue.toWordValue(value.shortValue());
	}

	@Comment("Converts USINT Value to BYTE value.")
	static ByteValue USINT_TO_BYTE(final USIntValue value) {
		return ByteValue.toByteValue(value.byteValue());
	}

	/* LWORD_TO */
	@Comment("Converts LWORD value to LREAL value.")
	static LRealValue LWORD_TO_LREAL(final LWordValue value) {
		return LRealValue.toLRealValue(Double.longBitsToDouble(value.longValue()));
	}

	@Comment("Converts LWORD value to LINT value.")
	static LIntValue LWORD_TO_LINT(final LWordValue value) {
		return LIntValue.toLIntValue(value.longValue());
	}

	@Comment("Converts LWORD value to DINT value.")
	static DIntValue LWORD_TO_DINT(final LWordValue value) {
		return DIntValue.toDIntValue(value.intValue());
	}

	@Comment("Converts LWORD value to INT value.")
	static IntValue LWORD_TO_INT(final LWordValue value) {
		return IntValue.toIntValue(value.shortValue());
	}

	@Comment("Converts LWORD value to SINT value.")
	static SIntValue LWORD_TO_SINT(final LWordValue value) {
		return SIntValue.toSIntValue(value.byteValue());
	}

	@Comment("Converts LWORD value to ULINT value.")
	static ULIntValue LWORD_TO_ULINT(final LWordValue value) {
		return ULIntValue.toULIntValue(value.longValue());
	}

	@Comment("Converts LWORD value to UDINT value.")
	static UDIntValue LWORD_TO_UDINT(final LWordValue value) {
		return UDIntValue.toUDIntValue(value.intValue());
	}

	@Comment("Converts LWORD value to UINT value.")
	static UIntValue LWORD_TO_UINT(final LWordValue value) {
		return UIntValue.toUIntValue(value.shortValue());
	}

	@Comment("Converts LWORD value to USINT value.")
	static USIntValue LWORD_TO_USINT(final LWordValue value) {
		return USIntValue.toUSIntValue(value.byteValue());
	}

	@Comment("Converts LWORD value to DWORD value.")
	static DWordValue LWORD_TO_DWORD(final LWordValue value) {
		return DWordValue.toDWordValue(value);
	}

	@Comment("Converts LWORD value to WORD value.")
	static WordValue LWORD_TO_WORD(final LWordValue value) {
		return WordValue.toWordValue(value);
	}

	@Comment("Converts LWORD value to BYTE value.")
	static ByteValue LWORD_TO_BYTE(final LWordValue value) {
		return ByteValue.toByteValue(value);
	}

	@Comment("Converts LWORD value to BOOL value.")
	static BoolValue LWORD_TO_BOOL(final LWordValue value) {
		return BoolValue.toBoolValue(value);
	}

	/* DWORD_TO */
	@Comment("Converts DWORD value to REAL value.")
	static RealValue DWORD_TO_REAL(final DWordValue value) {
		return RealValue.toRealValue(Float.intBitsToFloat(value.intValue()));
	}

	@Comment("Converts DWORD value to LINT value.")
	static LIntValue DWORD_TO_LINT(final DWordValue value) {
		return LIntValue.toLIntValue(value.longValue());
	}

	@Comment("Converts DWORD value to DINT value.")
	static DIntValue DWORD_TO_DINT(final DWordValue value) {
		return DIntValue.toDIntValue(value.intValue());
	}

	@Comment("Converts DWORD value to INT value.")
	static IntValue DWORD_TO_INT(final DWordValue value) {
		return IntValue.toIntValue(value.shortValue());
	}

	@Comment("Converts DWORD value to SINT value.")
	static SIntValue DWORD_TO_SINT(final DWordValue value) {
		return SIntValue.toSIntValue(value.byteValue());
	}

	@Comment("Converts DWORD value to ULINT value.")
	static ULIntValue DWORD_TO_ULINT(final DWordValue value) {
		return ULIntValue.toULIntValue(value.longValue());
	}

	@Comment("Converts DWORD value to UDINT value.")
	static UDIntValue DWORD_TO_UDINT(final DWordValue value) {
		return UDIntValue.toUDIntValue(value.intValue());
	}

	@Comment("Converts DWORD value to UINT value.")
	static UIntValue DWORD_TO_UINT(final DWordValue value) {
		return UIntValue.toUIntValue(value.shortValue());
	}

	@Comment("Converts DWORD value to USINT value.")
	static USIntValue DWORD_TO_USINT(final DWordValue value) {
		return USIntValue.toUSIntValue(value.byteValue());
	}

	@Comment("Converts DWORD value to LWORD value.")
	static LWordValue DWORD_TO_LWORD(final DWordValue value) {
		return LWordValue.toLWordValue(value);
	}

	@Comment("Converts DWORD value to WORD value.")
	static WordValue DWORD_TO_WORD(final DWordValue value) {
		return WordValue.toWordValue(value);
	}

	@Comment("Converts DWORD value to BYTE value.")
	static ByteValue DWORD_TO_BYTE(final DWordValue value) {
		return ByteValue.toByteValue(value);
	}

	@Comment("Converts DWORD value to BOOL value.")
	static BoolValue DWORD_TO_BOOL(final DWordValue value) {
		return BoolValue.toBoolValue(value);
	}

	/* WORD_TO */
	@Comment("Converts WORD value to LINT value.")
	static LIntValue WORD_TO_LINT(final WordValue value) {
		return LIntValue.toLIntValue(value.longValue());
	}

	@Comment("Converts WORD value to DINT value.")
	static DIntValue WORD_TO_DINT(final WordValue value) {
		return DIntValue.toDIntValue(value.intValue());
	}

	@Comment("Converts WORD value to INT value.")
	static IntValue WORD_TO_INT(final WordValue value) {
		return IntValue.toIntValue(value.shortValue());
	}

	@Comment("Converts WORD value to SINT value.")
	static SIntValue WORD_TO_SINT(final WordValue value) {
		return SIntValue.toSIntValue(value.byteValue());
	}

	@Comment("Converts WORD value to ULINT value.")
	static ULIntValue WORD_TO_ULINT(final WordValue value) {
		return ULIntValue.toULIntValue(value.longValue());
	}

	@Comment("Converts WORD value to UDINT value.")
	static UDIntValue WORD_TO_UDINT(final WordValue value) {
		return UDIntValue.toUDIntValue(value.intValue());
	}

	@Comment("Converts WORD value to UINT value.")
	static UIntValue WORD_TO_UINT(final WordValue value) {
		return UIntValue.toUIntValue(value.shortValue());
	}

	@Comment("Converts WORD value to USINT value.")
	static USIntValue WORD_TO_USINT(final WordValue value) {
		return USIntValue.toUSIntValue(value.byteValue());
	}

	@Comment("Converts WORD value to LWORD value.")
	static LWordValue WORD_TO_LWORD(final WordValue value) {
		return LWordValue.toLWordValue(value);
	}

	@Comment("Converts WORD value to DWORD value.")
	static DWordValue WORD_TO_DWORD(final WordValue value) {
		return DWordValue.toDWordValue(value);
	}

	@Comment("Converts WORD value to BYTE value.")
	static ByteValue WORD_TO_BYTE(final WordValue value) {
		return ByteValue.toByteValue(value);
	}

	@Comment("Converts WORD value to BOOL value.")
	static BoolValue WORD_TO_BOOL(final WordValue value) {
		return BoolValue.toBoolValue(value);
	}

	/* BYTE_TO */
	@Comment("Converts BYTE value to LINT value.")
	static LIntValue BYTE_TO_LINT(final ByteValue value) {
		return LIntValue.toLIntValue(value.longValue());
	}

	@Comment("Converts BYTE value to DINT value.")
	static DIntValue BYTE_TO_DINT(final ByteValue value) {
		return DIntValue.toDIntValue(value.intValue());
	}

	@Comment("Converts BYTE value to INT value.")
	static IntValue BYTE_TO_INT(final ByteValue value) {
		return IntValue.toIntValue(value.shortValue());
	}

	@Comment("Converts BYTE value to SINT value.")
	static SIntValue BYTE_TO_SINT(final ByteValue value) {
		return SIntValue.toSIntValue(value.byteValue());
	}

	@Comment("Converts BYTE value to ULINT value.")
	static ULIntValue BYTE_TO_ULINT(final ByteValue value) {
		return ULIntValue.toULIntValue(value.longValue());
	}

	@Comment("Converts BYTE value to UDINT value.")
	static UDIntValue BYTE_TO_UDINT(final ByteValue value) {
		return UDIntValue.toUDIntValue(value.intValue());
	}

	@Comment("Converts BYTE value to UINT value.")
	static UIntValue BYTE_TO_UINT(final ByteValue value) {
		return UIntValue.toUIntValue(value.shortValue());
	}

	@Comment("Converts BYTE value to USINT value.")
	static USIntValue BYTE_TO_USINT(final ByteValue value) {
		return USIntValue.toUSIntValue(value.byteValue());
	}

	@Comment("Converts BYTE value to LWORD value.")
	static LWordValue BYTE_TO_LWORD(final ByteValue value) {
		return LWordValue.toLWordValue(value);
	}

	@Comment("Converts BYTE value to DWORD value.")
	static DWordValue BYTE_TO_DWORD(final ByteValue value) {
		return DWordValue.toDWordValue(value);
	}

	@Comment("Converts BYTE value to WORD value.")
	static WordValue BYTE_TO_WORD(final ByteValue value) {
		return WordValue.toWordValue(value);
	}

	@Comment("Converts BYTE value to BOOL value.")
	static BoolValue BYTE_TO_BOOL(final ByteValue value) {
		return BoolValue.toBoolValue(value);
	}

	@Comment("Converts BYTE value to CHAR value.")
	static CharValue BYTE_TO_CHAR(final ByteValue value) {
		return CharValue.toCharValue(value.byteValue());
	}

	/* BOOL_TO */
	@Comment("Converts BOOL value to LINT value.")
	static LIntValue BOOL_TO_LINT(final BoolValue value) {
		return LIntValue.toLIntValue(value.longValue());
	}

	@Comment("Converts BOOL value to DINT value.")
	static DIntValue BOOL_TO_DINT(final BoolValue value) {
		return DIntValue.toDIntValue(value.intValue());
	}

	@Comment("Converts BOOL value to INT value.")
	static IntValue BOOL_TO_INT(final BoolValue value) {
		return IntValue.toIntValue(value.shortValue());
	}

	@Comment("Converts BOOL value to SINT value.")
	static SIntValue BOOL_TO_SINT(final BoolValue value) {
		return SIntValue.toSIntValue(value.byteValue());
	}

	@Comment("Converts BOOL value to ULINT value.")
	static ULIntValue BOOL_TO_ULINT(final BoolValue value) {
		return ULIntValue.toULIntValue(value.longValue());
	}

	@Comment("Converts BOOL value to UDINT value.")
	static UDIntValue BOOL_TO_UDINT(final BoolValue value) {
		return UDIntValue.toUDIntValue(value.intValue());
	}

	@Comment("Converts BOOL value to UINT value.")
	static UIntValue BOOL_TO_UINT(final BoolValue value) {
		return UIntValue.toUIntValue(value.shortValue());
	}

	@Comment("Converts BOOL value to USINT value.")
	static USIntValue BOOL_TO_USINT(final BoolValue value) {
		return USIntValue.toUSIntValue(value.byteValue());
	}

	@Comment("Converts BOOL value to LWORD value.")
	static LWordValue BOOL_TO_LWORD(final BoolValue value) {
		return LWordValue.toLWordValue(value);
	}

	@Comment("Converts BOOL value to DWORD value.")
	static DWordValue BOOL_TO_DWORD(final BoolValue value) {
		return DWordValue.toDWordValue(value);
	}

	@Comment("Converts BOOL value to WORD value.")
	static WordValue BOOL_TO_WORD(final BoolValue value) {
		return WordValue.toWordValue(value);
	}

	@Comment("Converts BOOL value to BYTE value.")
	static ByteValue BOOL_TO_BYTE(final BoolValue value) {
		return ByteValue.toByteValue(value);
	}

	/* CHAR_TO */
	@Comment("Converts CHAR value to LWORD value.")
	static LWordValue CHAR_TO_LWORD(final CharValue value) {
		return LWordValue.toLWordValue(value.charValue());
	}

	@Comment("Converts CHAR value to DWORD value.")
	static DWordValue CHAR_TO_DWORD(final CharValue value) {
		return DWordValue.toDWordValue(value.charValue());
	}

	@Comment("Converts CHAR value to WORD value.")
	static WordValue CHAR_TO_WORD(final CharValue value) {
		return WordValue.toWordValue((short) value.charValue());
	}

	@Comment("Converts CHAR value to BYTE value.")
	static ByteValue CHAR_TO_BYTE(final CharValue value) {
		return ByteValue.toByteValue((byte) value.charValue());
	}

	/* WCHAR_TO */
	@Comment("Converts WCHAR value to LWORD value.")
	static LWordValue WCHAR_TO_LWORD(final WCharValue value) {
		return LWordValue.toLWordValue(value.charValue());
	}

	@Comment("Converts WCHAR value to double DWORD value.")
	static DWordValue WCHAR_TO_DWORD(final WCharValue value) {
		return DWordValue.toDWordValue(value.charValue());
	}

	@Comment("Converts WCHAR value to WORD value.")
	static WordValue WCHAR_TO_WORD(final WCharValue value) {
		return WordValue.toWordValue((short) value.charValue());
	}

	/***************************************/
	@Comment("Truncates the floating point number of REAL value and returns SINT value.")
	static <T extends AnyRealValue> SIntValue TRUNC_SINT(final T value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns INT value.")
	static <T extends AnyRealValue> IntValue TRUNC_INT(final T value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns DINT value.")
	static <T extends AnyRealValue> DIntValue TRUNC_DINT(final T value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns LINT value.")
	static <T extends AnyRealValue> LIntValue TRUNC_LINT(final T value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns USINT value.")
	static <T extends AnyRealValue> USIntValue TRUNC_USINT(final T value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns UINT value.")
	static <T extends AnyRealValue> UIntValue TRUNC_UINT(final T value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns UDINT value.")
	static <T extends AnyRealValue> UDIntValue TRUNC_UDINT(final T value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns ULINT value.")
	static <T extends AnyRealValue> ULIntValue TRUNC_ULINT(final T value) {
		return ULIntValue.toULIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns SINT value.")
	static SIntValue LREAL_TRUNC_SINT(final LRealValue value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Truncates the floating point number of LREAL value and returns INT value.")
	static IntValue LREAL_TRUNC_INT(final LRealValue value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Truncates the floating point number of LREAL value and returns DINT value.")
	static DIntValue LREAL_TRUNC_DINT(final LRealValue value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Truncates the floating point number of LREAL value and returns LINT value.")
	static LIntValue LREAL_TRUNC_LINT(final LRealValue value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Truncates the floating point number of LREAL value and returns USINT value.")
	static USIntValue LREAL_TRUNC_USINT(final LRealValue value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Truncates the floating point number of LREAL value and returns UINT value.")
	static UIntValue LREAL_TRUNC_UINT(final LRealValue value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Truncates the floating point number of LREAL value and returns UDINT value.")
	static UDIntValue LREAL_TRUNC_UDINT(final LRealValue value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Truncates the floating point number of LREAL value and returns ULINT value.")
	static ULIntValue LREAL_TRUNC_ULINT(final LRealValue value) {
		return ULIntValue.toULIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns SINT value.")
	static SIntValue REAL_TRUNC_SINT(final RealValue value) {
		return SIntValue.toSIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns INT value.")
	static IntValue REAL_TRUNC_INT(final RealValue value) {
		return IntValue.toIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns DINT value.")
	static DIntValue REAL_TRUNC_DINT(final RealValue value) {
		return DIntValue.toDIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns LINT value.")
	static LIntValue REAL_TRUNC_LINT(final RealValue value) {
		return LIntValue.toLIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns USINT value.")
	static USIntValue REAL_TRUNC_USINT(final RealValue value) {
		return USIntValue.toUSIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns UINT value.")
	static UIntValue REAL_TRUNC_UINT(final RealValue value) {
		return UIntValue.toUIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns UDINT value.")
	static UDIntValue REAL_TRUNC_UDINT(final RealValue value) {
		return UDIntValue.toUDIntValue(value);
	}

	@Comment("Truncates the floating point number of REAL value and returns ULINT value.")
	static ULIntValue REAL_TRUNC_ULINT(final RealValue value) {
		return ULIntValue.toULIntValue(value);
	}

	/***************************************/
	@Comment("Converts BCD value to USINT value.")
	static <T extends AnyBitValue> USIntValue BCD_TO_USINT(final T value) {
		return USIntValue.toUSIntValue(convertFromBCD(value.bigIntegerValue()));
	}

	@Comment("Converts BCD value to UINT value.")
	static <T extends AnyBitValue> UIntValue BCD_TO_UINT(final T value) {
		return UIntValue.toUIntValue(convertFromBCD(value.bigIntegerValue()));
	}

	@Comment("Converts BCD value to UDINT value.")
	static <T extends AnyBitValue> UDIntValue BCD_TO_UDINT(final T value) {
		return UDIntValue.toUDIntValue(convertFromBCD(value.bigIntegerValue()));
	}

	@Comment("Converts BCD value to ULINT value.")
	static <T extends AnyBitValue> ULIntValue BCD_TO_ULINT(final T value) {
		return ULIntValue.toULIntValue(convertFromBCD(value.bigIntegerValue()));
	}

	@Comment("Converts any Unsigned value to BYTE BCD value.")
	static <T extends AnyUnsignedValue> ByteValue TO_BCD_BYTE(final T value) {
		return ByteValue.toByteValue(convertToBCD(value.bigIntegerValue()));
	}

	@Comment("Converts any Unsigned value to WORD BCD value.")
	static <T extends AnyUnsignedValue> WordValue TO_BCD_WORD(final T value) {
		return WordValue.toWordValue(convertToBCD(value.bigIntegerValue()));
	}

	@Comment("Converts any Unsigned value to DWORD BCD value.")
	static <T extends AnyUnsignedValue> DWordValue TO_BCD_DWORD(final T value) {
		return DWordValue.toDWordValue(convertToBCD(value.bigIntegerValue()));
	}

	@Comment("Converts any Unsigned value to LWORD BCD value.")
	static <T extends AnyUnsignedValue> LWordValue TO_BCD_LWORD(final T value) {
		return LWordValue.toLWordValue(convertToBCD(value.bigIntegerValue()));
	}

	@Comment("Converts BYTE BCD value to USINT value.")
	static USIntValue BYTE_BCD_TO_USINT(final ByteValue value) {
		return BCD_TO_USINT(value);
	}

	@Comment("Converts BYTE BCD value to UINT value.")
	static UIntValue BYTE_BCD_TO_UINT(final ByteValue value) {
		return BCD_TO_UINT(value);
	}

	@Comment("Converts BYTE BCD value to UDINT value.")
	static UDIntValue BYTE_BCD_TO_UDINT(final ByteValue value) {
		return BCD_TO_UDINT(value);
	}

	@Comment("Converts BYTE BCD value to ULINT value.")
	static ULIntValue BYTE_BCD_TO_ULINT(final ByteValue value) {
		return BCD_TO_ULINT(value);
	}

	@Comment("Converts WORD BCD value to USINT value.")
	static USIntValue WORD_BCD_TO_USINT(final WordValue value) {
		return BCD_TO_USINT(value);
	}

	@Comment("Converts WORD BCD value to UINT value.")
	static UIntValue WORD_BCD_TO_UINT(final WordValue value) {
		return BCD_TO_UINT(value);
	}

	@Comment("Converts WORD BCD value to UDINT value.")
	static UDIntValue WORD_BCD_TO_UDINT(final WordValue value) {
		return BCD_TO_UDINT(value);
	}

	@Comment("Converts WORD BCD value to ULINT value.")
	static ULIntValue WORD_BCD_TO_ULINT(final WordValue value) {
		return BCD_TO_ULINT(value);
	}

	@Comment("Converts DWORD BCD value to USINT value.")
	static USIntValue DWORD_BCD_TO_USINT(final DWordValue value) {
		return BCD_TO_USINT(value);
	}

	@Comment("Converts DWORD BCD value to UINT value.")
	static UIntValue DWORD_BCD_TO_UINT(final DWordValue value) {
		return BCD_TO_UINT(value);
	}

	@Comment("Converts DWORD BCD value to UDINT value.")
	static UDIntValue DWORD_BCD_TO_UDINT(final DWordValue value) {
		return BCD_TO_UDINT(value);
	}

	@Comment("Converts DWORD BCD value to ULINT value.")
	static ULIntValue DWORD_BCD_TO_ULINT(final DWordValue value) {
		return BCD_TO_ULINT(value);
	}

	@Comment("Converts LWORD BCD value to USINT value.")
	static USIntValue LWORD_BCD_TO_USINT(final LWordValue value) {
		return BCD_TO_USINT(value);
	}

	@Comment("Converts LWORD BCD value to UINT value.")
	static UIntValue LWORD_BCD_TO_UINT(final LWordValue value) {
		return BCD_TO_UINT(value);
	}

	@Comment("Converts LWORD BCD value to UDINT value.")
	static UDIntValue LWORD_BCD_TO_UDINT(final LWordValue value) {
		return BCD_TO_UDINT(value);
	}

	@Comment("Converts LWORD BCD value to ULINT value.")
	static ULIntValue LWORD_BCD_TO_ULINT(final LWordValue value) {
		return BCD_TO_ULINT(value);
	}

	@Comment("Converts USINT value to BYTE BCD value.")
	static ByteValue USINT_TO_BCD_BYTE(final USIntValue value) {
		return TO_BCD_BYTE(value);
	}

	@Comment("Converts UINT value to BYTE BCD value.")
	static ByteValue UINT_TO_BCD_BYTE(final UIntValue value) {
		return TO_BCD_BYTE(value);
	}

	@Comment("Converts UDINT value to BYTE BCD value.")
	static ByteValue UDINT_TO_BCD_BYTE(final UDIntValue value) {
		return TO_BCD_BYTE(value);
	}

	@Comment("Converts ULINT value to BYTE BCD value.")
	static ByteValue ULINT_TO_BCD_BYTE(final ULIntValue value) {
		return TO_BCD_BYTE(value);
	}

	@Comment("Converts USINT value to WORD BCD value.")
	static WordValue USINT_TO_BCD_WORD(final USIntValue value) {
		return TO_BCD_WORD(value);
	}

	@Comment("Converts UINT value to WORD BCD value.")
	static WordValue UINT_TO_BCD_WORD(final UIntValue value) {
		return TO_BCD_WORD(value);
	}

	@Comment("Converts UDINT value to WORD BCD value.")
	static WordValue UDINT_TO_BCD_WORD(final UDIntValue value) {
		return TO_BCD_WORD(value);
	}

	@Comment("Converts ULINT value to WORD BCD value.")
	static WordValue ULINT_TO_BCD_WORD(final ULIntValue value) {
		return TO_BCD_WORD(value);
	}

	@Comment("Converts USINT value to DWORD BCD value.")
	static DWordValue USINT_TO_BCD_DWORD(final USIntValue value) {
		return TO_BCD_DWORD(value);
	}

	@Comment("Converts UINT value to DWORD BCD value.")
	static DWordValue UINT_TO_BCD_DWORD(final UIntValue value) {
		return TO_BCD_DWORD(value);
	}

	@Comment("Converts UDINT value to DWORD BCD value.")
	static DWordValue UDINT_TO_BCD_DWORD(final UDIntValue value) {
		return TO_BCD_DWORD(value);
	}

	@Comment("Converts ULINT value to DWORD BCD value.")
	static DWordValue ULINT_TO_BCD_DWORD(final ULIntValue value) {
		return TO_BCD_DWORD(value);
	}

	@Comment("Converts USINT value to LWORD BCD value.")
	static LWordValue USINT_TO_BCD_LWORD(final USIntValue value) {
		return TO_BCD_LWORD(value);
	}

	@Comment("Converts UINT value to LWORD BCD value.")
	static LWordValue UINT_TO_BCD_LWORD(final UIntValue value) {
		return TO_BCD_LWORD(value);
	}

	@Comment("Converts UDINT value to LWORD BCD value.")
	static LWordValue UDINT_TO_BCD_LWORD(final UDIntValue value) {
		return TO_BCD_LWORD(value);
	}

	@Comment("Converts ULINT value to LWORD BCD value.")
	static LWordValue ULINT_TO_BCD_LWORD(final ULIntValue value) {
		return TO_BCD_LWORD(value);
	}

	int BCD_MASK = 0x0f;
	int BCD_BITS = 4;
	int BCD_DIGIT_MAX = 0x9;

	private static BigInteger convertToBCD(final BigInteger value) {
		// pack lower 4 bits of each digit in each byte
		var result = BigInteger.ZERO;
		final var digits = value.toString();
		for (final char digit : digits.toCharArray()) {
			result = result.shiftLeft(BCD_BITS);
			result = result.or(BigInteger.valueOf(digit & BCD_MASK));
		}
		return result;
	}

	private static BigInteger convertFromBCD(final BigInteger value) {
		if (value.signum() < 0) {
			throw new IllegalArgumentException();
		}
		// unpack lower 4 bits of each digit in each byte
		var result = BigInteger.ZERO;
		for (final byte packed : value.toByteArray()) {
			final int packedInt = Byte.toUnsignedInt(packed);
			final int upper = packedInt >>> BCD_BITS;
			final int lower = packedInt & BCD_MASK;
			if (upper > BCD_DIGIT_MAX || lower > BCD_DIGIT_MAX) {
				throw new IllegalArgumentException();
			}
			result = result.multiply(BigInteger.TEN);
			result = result.add(BigInteger.valueOf(upper));
			result = result.multiply(BigInteger.TEN);
			result = result.add(BigInteger.valueOf(lower));
		}
		return result;
	}

	@Comment("Returns relative time stamp.")
	static TimeValue NOW_MONOTONIC() {
		return TimeValue.toTimeValue(Instant.EPOCH.until(AbstractEvaluator.currentClock().instant(), ChronoUnit.NANOS));
	}

	static DateAndTimeValue NOW() {
		return DateAndTimeValue
				.toDateAndTimeValue(Instant.EPOCH.until(AbstractEvaluator.currentClock().instant(), ChronoUnit.NANOS));
	}

	@OnlySupportedBy("4diac IDE")
	@Comment("Returns relative time stamp.")
	static void OVERRIDE_NOW_MONOTONIC(final TimeValue value) {
		final Duration duration = value.toDuration();
		final Instant instant = Instant.ofEpochSecond(duration.getSeconds(), duration.getNano());
		AbstractEvaluator.setClock(Clock.fixed(instant, ZoneId.systemDefault()));
	}

	/* Date and Time conversions */
	@Comment("Converts LTIME value to TIME value.")
	static TimeValue LTIME_TO_TIME(final LTimeValue value) {
		return TimeValue.toTimeValue(value);
	}

	@Comment("Converts TIME value to LTIME value.")
	static LTimeValue TIME_TO_LTIME(final TimeValue value) {
		return LTimeValue.toLTimeValue(value);
	}

	@Comment("Converts LDATE_AND_TIME value to DATE_AND_TIME value.")
	static DateAndTimeValue LDT_TO_DT(final LDateAndTimeValue value) {
		return DateAndTimeValue.toDateAndTimeValue(value);
	}

	@Comment("Converts LDATE_AND_TIME value to DATE value.")
	static DateValue LDT_TO_DATE(final LDateAndTimeValue value) {
		return DateValue.toDateValue(value);
	}

	@Comment("Converts LDATE_AND_TIME value to LTIME_OF_DAY value.")
	static LTimeOfDayValue LDT_TO_LTOD(final LDateAndTimeValue value) {
		return LTimeOfDayValue.toLTimeOfDayValue(value);
	}

	@Comment("Converts LDATE_AND_TIME value to TIME_OF_DAY value.")
	static TimeOfDayValue LDT_TO_TOD(final LDateAndTimeValue value) {
		return TimeOfDayValue.toTimeOfDayValue(value);
	}

	@Comment("Converts DATE_AND_TIME value to LDATE_AND_TIME value.")
	static LDateAndTimeValue DT_TO_LDT(final DateAndTimeValue value) {
		return LDateAndTimeValue.toLDateAndTimeValue(value);
	}

	@Comment("Converts DATE_AND_TIME value to DATE value.")
	static DateValue DT_TO_DATE(final DateAndTimeValue value) {
		return DateValue.toDateValue(value);
	}

	@Comment("Converts DATE_AND_TIME value to LTIME_OF_DAY value.")
	static LTimeOfDayValue DT_TO_LTOD(final DateAndTimeValue value) {
		return LTimeOfDayValue.toLTimeOfDayValue(value);
	}

	@Comment("Converts DATE_AND_TIME value to TIME_OF_DAY value.")
	static TimeOfDayValue DT_TO_TOD(final DateAndTimeValue value) {
		return TimeOfDayValue.toTimeOfDayValue(value);
	}

	@Comment("Converts LTIME_OF_DAY value to TIME_OF_DAY value.")
	static TimeOfDayValue LTOD_TO_TOD(final LTimeOfDayValue value) {
		return TimeOfDayValue.toTimeOfDayValue(value);
	}

	@Comment("Converts TIME_OF_DAY value to LTIME_OF_DAY value.")
	static LTimeOfDayValue TOD_TO_LTOD(final TimeOfDayValue value) {
		return LTimeOfDayValue.toLTimeOfDayValue(value);
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in second and returns it as LINT value.")
	static LIntValue TIME_IN_S_TO_LINT(final AnyDurationValue value) {
		return LIntValue.toLIntValue(value.toDuration().toSeconds());
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in millisecond and returns it as LINT value.")
	static LIntValue TIME_IN_MS_TO_LINT(final AnyDurationValue value) {
		return LIntValue.toLIntValue(value.toDuration().toMillis());
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in microsecond and returns it as LINT value.")
	static LIntValue TIME_IN_US_TO_LINT(final AnyDurationValue value) {
		return LIntValue.toLIntValue(value.toDuration().toNanos() / 1000);
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in nanosecond and returns it as LINT value.")
	static LIntValue TIME_IN_NS_TO_LINT(final AnyDurationValue value) {
		return LIntValue.toLIntValue(value.toDuration().toNanos());
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in second and returns it as ULINT value.")
	static ULIntValue TIME_IN_S_TO_ULINT(final AnyDurationValue value) {
		return ULIntValue.toULIntValue(value.toDuration().toSeconds());
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in millisecond and returns it as ULINT value.")
	static ULIntValue TIME_IN_MS_TO_ULINT(final AnyDurationValue value) {
		return ULIntValue.toULIntValue(value.toDuration().toMillis());
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in microsecond and returns it as ULINT value.")
	static ULIntValue TIME_IN_US_TO_ULINT(final AnyDurationValue value) {
		return ULIntValue.toULIntValue(value.toDuration().toNanos() / 1000);
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in nanosecond and returns it as ULINT value.")
	static ULIntValue TIME_IN_NS_TO_ULINT(final AnyDurationValue value) {
		return ULIntValue.toULIntValue(value.toDuration().toNanos());
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in second and returns it as LREAL value.")
	static LRealValue TIME_IN_S_TO_LREAL(final AnyDurationValue value) {
		return LRealValue.toLRealValue(value.toDuration().toNanos() / 1000000000.0);
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in millisecond and returns it as LREAL value.")
	static LRealValue TIME_IN_MS_TO_LREAL(final AnyDurationValue value) {
		return LRealValue.toLRealValue(value.toDuration().toNanos() / 1000000.0);
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in microsecond and returns it as LREAL value.")
	static LRealValue TIME_IN_US_TO_LREAL(final AnyDurationValue value) {
		return LRealValue.toLRealValue(value.toDuration().toNanos() / 1000.0);
	}

	@OnlySupportedBy("4diac FORTE")
	@Comment("Converts DURATION value in nanosecond and returns it as LREAL value.")
	static LRealValue TIME_IN_NS_TO_LREAL(final AnyDurationValue value) {
		return LRealValue.toLRealValue(value.toDuration().toNanos());
	}

	/* ANY_CHARS conversions */
	@Comment("Converts WString value to String value.")
	static StringValue WSTRING_TO_STRING(final WStringValue value) {
		return StringValue.toStringValue(value);
	}

	@Comment("Converts WString value to WChar value.")
	static WCharValue WSTRING_TO_WCHAR(final WStringValue value) {
		return WCharValue.toWCharValue(value);
	}

	@Comment("Converts String value to WString value.")
	static WStringValue STRING_TO_WSTRING(final StringValue value) {
		return WStringValue.toWStringValue(value);
	}

	@Comment("Converts String value to Char value.")
	static CharValue STRING_TO_CHAR(final StringValue value) {
		return CharValue.toCharValue(value);
	}

	@Comment("Converts WChar value to WString value.")
	static WStringValue WCHAR_TO_WSTRING(final WCharValue value) {
		return WStringValue.toWStringValue(value);
	}

	@Comment("Converts WChar value to Char value.")
	static CharValue WCHAR_TO_CHAR(final WCharValue value) {
		return CharValue.toCharValue(value);
	}

	@Comment("Converts Char value to String value.")
	static StringValue CHAR_TO_STRING(final CharValue value) {
		return StringValue.toStringValue(value);
	}

	@Comment("Converts Char to WChar value.")
	static WCharValue CHAR_TO_WCHAR(final CharValue value) {
		return WCharValue.toWCharValue(value);
	}

	@SuppressWarnings("unchecked")
	private static <T extends AnyRealValue> T apply(final T value, final DoubleUnaryOperator operator) {
		if (value instanceof RealValue) {
			return (T) RealValue.toRealValue((float) operator.applyAsDouble(value.doubleValue()));
		}
		return (T) LRealValue.toLRealValue(operator.applyAsDouble(value.doubleValue()));
	}

	@SuppressWarnings("unchecked")
	private static <T extends AnyRealValue> T apply(final T value1, final T value2,
			final DoubleBinaryOperator operator) {
		if (value1 instanceof RealValue) {
			return (T) RealValue
					.toRealValue((float) operator.applyAsDouble(value1.doubleValue(), value2.doubleValue()));
		}
		return (T) LRealValue.toLRealValue(operator.applyAsDouble(value1.doubleValue(), value2.doubleValue()));
	}

	private static StringValue apply(final StringValue value, final UnaryOperator<String> operator) {
		return StringValue.toStringValue(operator.apply(value.stringValue()));
	}

	private static WStringValue apply(final WStringValue value, final UnaryOperator<String> operator) {
		return WStringValue.toWStringValue(operator.apply(value.stringValue()));
	}

	private static StringValue apply(final AnySCharsValue value1, final AnySCharsValue value2,
			final BinaryOperator<String> operator) {
		return StringValue.toStringValue(operator.apply(value1.stringValue(), value2.stringValue()));
	}

	private static WStringValue apply(final AnyWCharsValue value1, final AnyWCharsValue value2,
			final BinaryOperator<String> operator) {
		return WStringValue.toWStringValue(operator.apply(value1.stringValue(), value2.stringValue()));
	}
}
