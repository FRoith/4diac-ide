/*******************************************************************************
 * Copyright (c) 2022 Primetals Technologies Austria GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Ulzii Jargalsaikhan - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.structuredtextfunctioneditor.formatting2

import com.google.inject.Inject
import org.eclipse.fordiac.ide.structuredtextcore.formatting2.STCoreFormatter
import org.eclipse.fordiac.ide.structuredtextfunctioneditor.services.STFunctionGrammarAccess
import org.eclipse.fordiac.ide.structuredtextfunctioneditor.stfunction.STFunction
import org.eclipse.fordiac.ide.structuredtextfunctioneditor.stfunction.STFunctionPackage
import org.eclipse.fordiac.ide.structuredtextfunctioneditor.stfunction.STFunctionSource
import org.eclipse.xtext.formatting2.IFormattableDocument
import org.eclipse.fordiac.ide.structuredtextcore.formatting2.KeywordCaseTextReplacer
import org.eclipse.xtext.Keyword
import org.eclipse.xtext.RuleCall

class STFunctionFormatter extends STCoreFormatter {

	@Inject extension STFunctionGrammarAccess

	def dispatch void format(STFunctionSource stFunctionSource, extension IFormattableDocument document) {
		stFunctionSource.allSemanticRegions.filter [
			switch (element : grammarElement) {
				Keyword case element.value.matches("[_a-zA-Z]+"): true
				RuleCall case element.rule == boolLiteralRule: true
				RuleCall case element.rule == STNumericLiteralTypeRule: true
				RuleCall case element.rule == STDateLiteralTypeRule: true
				RuleCall case element.rule == STTimeLiteralTypeRule: true
				RuleCall case element.rule == orOperatorRule : true
				RuleCall case element.rule == xorOperatorRule : true
				default: false
			}
		].forEach [
			document.addReplacer(new KeywordCaseTextReplacer(document, it))
		]
		stFunctionSource.allRegionsFor.keywords(STPrimaryExpressionAccess.leftParenthesisKeyword_0_0).forEach[append[noSpace]]
		stFunctionSource.allRegionsFor.keywords(STPrimaryExpressionAccess.rightParenthesisKeyword_0_2).forEach[prepend[noSpace]]
		for (stFunction : stFunctionSource.functions) {
			stFunction.format
		}
	}

	def dispatch void format(STFunction stFunction, extension IFormattableDocument document) {
		stFunction.regionFor.keyword("FUNCTION").prepend[noIndentation].append[oneSpace]

		if (stFunction.returnType !== null) {
			// We have a return type
			stFunction.regionFor.keyword(":").surround[oneSpace]
			stFunction.regionFor.feature(STFunctionPackage.Literals.ST_FUNCTION__RETURN_TYPE).prepend[oneSpace].append [
				newLine
			]
		} else {
			stFunction.regionFor.assignment(STFunctionAccess.nameAssignment_2).append[newLine]
		}

		stFunction.varDeclarations.forEach[format]

		stFunction.code.forEach[format]

		stFunction.regionFor.keyword("END_FUNCTION").prepend[noIndentation]

		stFunction.regionFor.keyword("END_FUNCTION").append[newLine]
	}

}
