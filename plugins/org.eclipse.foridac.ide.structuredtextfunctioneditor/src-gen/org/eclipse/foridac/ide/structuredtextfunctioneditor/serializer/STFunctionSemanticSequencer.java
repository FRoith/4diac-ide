/*
 * generated by Xtext 2.25.0
 */
package org.eclipse.foridac.ide.structuredtextfunctioneditor.serializer;

import com.google.inject.Inject;
import java.util.Set;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.ArrayInitElement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.ArrayInitializerExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.BOOL_LITERAL;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.Code;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.DATE_AND_TIME_LITERAL;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.DATE_LITERAL;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.MultibitPartialAccess;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.NUMERIC_LITERAL;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STAddSubExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STAndExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STAssignmentStatement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STBoolLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STCaseCases;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STCaseStatement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STComparisonExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STContinue;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STCorePackage;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STDateAndTimeLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STDateLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STElseIfPart;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STElsePart;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STEqualityExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STExit;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STForStatement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STIfStatment;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STMemberSelection;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STMulDivModExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STNumericLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STOrExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STPowerExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STRING_LITERAL;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STRepeatStatement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STReturn;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STSignumExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STStatements;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STStringLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STSubrangeExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STSymbol;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STTimeLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STTimeOfDayLiteral;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STWhileStatement;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STXorExpression;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.TIME_LITERAL;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.TIME_OF_DAY_LITERAL;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.VarDeclaration;
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.VarDeclarationBlock;
import org.eclipse.fordiac.ide.structuredtextcore.serializer.STCoreSemanticSequencer;
import org.eclipse.foridac.ide.structuredtextfunctioneditor.sTFunction.FunctionDefinition;
import org.eclipse.foridac.ide.structuredtextfunctioneditor.sTFunction.STFunction;
import org.eclipse.foridac.ide.structuredtextfunctioneditor.sTFunction.STFunctionPackage;
import org.eclipse.foridac.ide.structuredtextfunctioneditor.services.STFunctionGrammarAccess;
import org.eclipse.xtext.Action;
import org.eclipse.xtext.Parameter;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.serializer.ISerializationContext;

@SuppressWarnings("all")
public class STFunctionSemanticSequencer extends STCoreSemanticSequencer {

	@Inject
	private STFunctionGrammarAccess grammarAccess;
	
	@Override
	public void sequence(ISerializationContext context, EObject semanticObject) {
		EPackage epackage = semanticObject.eClass().getEPackage();
		ParserRule rule = context.getParserRule();
		Action action = context.getAssignedAction();
		Set<Parameter> parameters = context.getEnabledBooleanParameters();
		if (epackage == STCorePackage.eINSTANCE)
			switch (semanticObject.eClass().getClassifierID()) {
			case STCorePackage.ARRAY_INIT_ELEMENT:
				sequence_ArrayInitElement(context, (ArrayInitElement) semanticObject); 
				return; 
			case STCorePackage.ARRAY_INITIALIZER_EXPRESSION:
				sequence_ArrayInitializerExpression(context, (ArrayInitializerExpression) semanticObject); 
				return; 
			case STCorePackage.BOOL_LITERAL:
				sequence_BOOL_LITERAL(context, (BOOL_LITERAL) semanticObject); 
				return; 
			case STCorePackage.CODE:
				sequence_Code(context, (Code) semanticObject); 
				return; 
			case STCorePackage.DATE_AND_TIME_LITERAL:
				sequence_DATE_AND_TIME_LITERAL(context, (DATE_AND_TIME_LITERAL) semanticObject); 
				return; 
			case STCorePackage.DATE_LITERAL:
				sequence_DATE_LITERAL(context, (DATE_LITERAL) semanticObject); 
				return; 
			case STCorePackage.MULTIBIT_PARTIAL_ACCESS:
				sequence_MultibitPartialAccess(context, (MultibitPartialAccess) semanticObject); 
				return; 
			case STCorePackage.NUMERIC_LITERAL:
				sequence_NUMERIC_LITERAL(context, (NUMERIC_LITERAL) semanticObject); 
				return; 
			case STCorePackage.ST_ADD_SUB_EXPRESSION:
				sequence_STAddSubExpression(context, (STAddSubExpression) semanticObject); 
				return; 
			case STCorePackage.ST_AND_EXPRESSION:
				sequence_STAndExpression(context, (STAndExpression) semanticObject); 
				return; 
			case STCorePackage.ST_ASSIGNMENT_STATEMENT:
				sequence_STAssignmentStatement(context, (STAssignmentStatement) semanticObject); 
				return; 
			case STCorePackage.ST_BOOL_LITERAL:
				sequence_STLiteralExpressions(context, (STBoolLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_CASE_CASES:
				sequence_STCaseCases(context, (STCaseCases) semanticObject); 
				return; 
			case STCorePackage.ST_CASE_STATEMENT:
				sequence_STCaseStatement(context, (STCaseStatement) semanticObject); 
				return; 
			case STCorePackage.ST_COMPARISON_EXPRESSION:
				sequence_STComparisonExpression(context, (STComparisonExpression) semanticObject); 
				return; 
			case STCorePackage.ST_CONTINUE:
				sequence_STStatement(context, (STContinue) semanticObject); 
				return; 
			case STCorePackage.ST_DATE_AND_TIME_LITERAL:
				sequence_STLiteralExpressions(context, (STDateAndTimeLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_DATE_LITERAL:
				sequence_STLiteralExpressions(context, (STDateLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_ELSE_IF_PART:
				sequence_STElseIfPart(context, (STElseIfPart) semanticObject); 
				return; 
			case STCorePackage.ST_ELSE_PART:
				sequence_STElsePart(context, (STElsePart) semanticObject); 
				return; 
			case STCorePackage.ST_EQUALITY_EXPRESSION:
				sequence_STEqualityExpression(context, (STEqualityExpression) semanticObject); 
				return; 
			case STCorePackage.ST_EXIT:
				sequence_STStatement(context, (STExit) semanticObject); 
				return; 
			case STCorePackage.ST_FOR_STATEMENT:
				sequence_STForStatement(context, (STForStatement) semanticObject); 
				return; 
			case STCorePackage.ST_IF_STATMENT:
				sequence_STIfStatment(context, (STIfStatment) semanticObject); 
				return; 
			case STCorePackage.ST_MEMBER_SELECTION:
				sequence_STSelectionExpression(context, (STMemberSelection) semanticObject); 
				return; 
			case STCorePackage.ST_MUL_DIV_MOD_EXPRESSION:
				sequence_STMulDivModExpression(context, (STMulDivModExpression) semanticObject); 
				return; 
			case STCorePackage.ST_NUMERIC_LITERAL:
				sequence_STLiteralExpressions(context, (STNumericLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_OR_EXPRESSION:
				sequence_STOrExpression(context, (STOrExpression) semanticObject); 
				return; 
			case STCorePackage.ST_POWER_EXPRESSION:
				sequence_STPowerExpression(context, (STPowerExpression) semanticObject); 
				return; 
			case STCorePackage.STRING_LITERAL:
				sequence_STRING_LITERAL(context, (STRING_LITERAL) semanticObject); 
				return; 
			case STCorePackage.ST_REPEAT_STATEMENT:
				sequence_STRepeatStatement(context, (STRepeatStatement) semanticObject); 
				return; 
			case STCorePackage.ST_RETURN:
				sequence_STStatement(context, (STReturn) semanticObject); 
				return; 
			case STCorePackage.ST_SIGNUM_EXPRESSION:
				sequence_STSignumExpression(context, (STSignumExpression) semanticObject); 
				return; 
			case STCorePackage.ST_STATEMENTS:
				sequence_STStatement(context, (STStatements) semanticObject); 
				return; 
			case STCorePackage.ST_STRING_LITERAL:
				sequence_STLiteralExpressions(context, (STStringLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_SUBRANGE_EXPRESSION:
				sequence_STSubrangeExpression(context, (STSubrangeExpression) semanticObject); 
				return; 
			case STCorePackage.ST_SYMBOL:
				sequence_STAtomicExpression(context, (STSymbol) semanticObject); 
				return; 
			case STCorePackage.ST_TIME_LITERAL:
				sequence_STLiteralExpressions(context, (STTimeLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_TIME_OF_DAY_LITERAL:
				sequence_STLiteralExpressions(context, (STTimeOfDayLiteral) semanticObject); 
				return; 
			case STCorePackage.ST_WHILE_STATEMENT:
				sequence_STWhileStatement(context, (STWhileStatement) semanticObject); 
				return; 
			case STCorePackage.ST_XOR_EXPRESSION:
				sequence_STXorExpression(context, (STXorExpression) semanticObject); 
				return; 
			case STCorePackage.TIME_LITERAL:
				sequence_TIME_LITERAL(context, (TIME_LITERAL) semanticObject); 
				return; 
			case STCorePackage.TIME_OF_DAY_LITERAL:
				sequence_TIME_OF_DAY_LITERAL(context, (TIME_OF_DAY_LITERAL) semanticObject); 
				return; 
			case STCorePackage.VAR_DECLARATION:
				sequence_VarDeclaration(context, (VarDeclaration) semanticObject); 
				return; 
			case STCorePackage.VAR_DECLARATION_BLOCK:
				if (rule == grammarAccess.getVarDeclarationBlockRule()) {
					sequence_VarDeclarationBlock(context, (VarDeclarationBlock) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getVarInputDeclarationBlockRule()) {
					sequence_VarInputDeclarationBlock(context, (VarDeclarationBlock) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getVarOutputDeclarationBlockRule()) {
					sequence_VarOutputDeclarationBlock(context, (VarDeclarationBlock) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getVarTempDeclarationBlockRule()) {
					sequence_VarTempDeclarationBlock(context, (VarDeclarationBlock) semanticObject); 
					return; 
				}
				else break;
			}
		else if (epackage == STFunctionPackage.eINSTANCE)
			switch (semanticObject.eClass().getClassifierID()) {
			case STFunctionPackage.FUNCTION_DEFINITION:
				sequence_FunctionDefinition(context, (FunctionDefinition) semanticObject); 
				return; 
			case STFunctionPackage.ST_FUNCTION:
				sequence_STFunction(context, (STFunction) semanticObject); 
				return; 
			}
		if (errorAcceptor != null)
			errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Contexts:
	 *     FunctionDefinition returns FunctionDefinition
	 *
	 * Constraint:
	 *     (
	 *         name=ID 
	 *         returnType=[DataType|ID]? 
	 *         (
	 *             varDeclarations+=VarDeclaration | 
	 *             varTempDeclarations+=VarTempDeclarationBlock | 
	 *             varInpuDeclarations+=VarInputDeclarationBlock | 
	 *             varOutputDeclarations+=VarOutputDeclarationBlock
	 *         )* 
	 *         code+=STStatement*
	 *     )
	 */
	protected void sequence_FunctionDefinition(ISerializationContext context, FunctionDefinition semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     STFunction returns STFunction
	 *
	 * Constraint:
	 *     functions+=FunctionDefinition+
	 */
	protected void sequence_STFunction(ISerializationContext context, STFunction semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
}
