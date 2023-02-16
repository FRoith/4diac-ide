/**
 * Copyright (c) 2022 - 2023 Martin Erich Jobst
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
package org.eclipse.fordiac.ide.export.forte_ng.util

import org.eclipse.fordiac.ide.model.data.AnyElementaryType
import org.eclipse.fordiac.ide.model.data.AnyStringType
import org.eclipse.fordiac.ide.model.data.ArrayType
import org.eclipse.fordiac.ide.model.data.DataType
import org.eclipse.fordiac.ide.model.data.DateAndTimeType
import org.eclipse.fordiac.ide.model.data.DateType
import org.eclipse.fordiac.ide.model.data.LdateType
import org.eclipse.fordiac.ide.model.data.LdtType
import org.eclipse.fordiac.ide.model.data.LtimeType
import org.eclipse.fordiac.ide.model.data.LtodType
import org.eclipse.fordiac.ide.model.data.StructuredType
import org.eclipse.fordiac.ide.model.data.TimeOfDayType
import org.eclipse.fordiac.ide.model.data.TimeType
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration

final class ForteNgExportUtil {
	private new() {
	}

	def static CharSequence generateTypeDefaultValue(DataType type) {
		switch (type) {
			AnyStringType: '''«type.generateTypeName»("")'''
			AnyElementaryType: '''«type.generateTypeName»(0)'''
			ArrayType: '''«type.generateTypeName»()'''
			StructuredType: '''«type.generateTypeName»()'''
			default:
				"0"
		}
	}

	def static CharSequence generateTypeName(VarDeclaration variable) //
	'''«IF variable.array»CIEC_ARRAY_COMMON<«ENDIF»«variable.type.generateTypeName»«IF variable.array»>«ENDIF»'''

	def static CharSequence generateTypeName(DataType type) '''CIEC_«type.generateTypeNamePlain»'''

	def static String generateTypeNamePlain(DataType type) {
		switch (type) {
			TimeType: "TIME"
			LtimeType: "LTIME"
			DateType: "DATE"
			LdateType: "LDATE"
			TimeOfDayType: "TIME_OF_DAY"
			LtodType: "LTIME_OF_DAY"
			DateAndTimeType: "DATE_AND_TIME"
			LdtType: "LDATE_AND_TIME"
			ArrayType: "ARRAY"
			default: type.name
		}
	}
}
