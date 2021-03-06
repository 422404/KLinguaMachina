package org.linguamachina.klinguamachina.parser.ast.tranformations

import org.linguamachina.klinguamachina.parser.ast.nodes.StatementNode
import org.linguamachina.klinguamachina.parser.ast.nodes.impl.*
import org.linguamachina.klinguamachina.parser.ast.BaseASTNodeVisitor
import org.linguamachina.klinguamachina.util.getSelectorFromKeywords

class DesugarClassDeclarations: BaseASTNodeVisitor<Unit> {
    private val classDeclarationSelectors = listOf(
        "subclass:instanceVars:",
        "subclass:classVars:",
        "subclass:instanceVars:classVars:",
        "subclass:classVars:instanceVars:"
    )

    override fun defaultValue() = Unit

    override fun visit(node: BlockLiteralNode) {
        node.statements = node.statements.map {
            if (it is StatementExprNode)
                desugar(it)
            else
                it
        }
        super.visit(node)
    }

    override fun visit(node: RootNode) {
        node.statements = node.statements.map {
            if (it is StatementExprNode)
                desugar(it)
            else
                it
        }
        super.visit(node)
    }

    private fun desugar(node: StatementExprNode): StatementNode {
        var result: StatementNode = node

        if (node.expression is MessageSendingNode
                && node.expression.selectors is MessageSelectorNode) {
            val selector = node.expression.selectors
            val selectorString = getSelectorFromKeywords(
                    selector.keywords, selector.values.isNotEmpty())
            if (classDeclarationSelectors.contains(selectorString)) {
                selector.values[0].let {
                    if (it is SymbolLiteralNode) {
                        result = VarDeclarationNode(
                            node.position,
                            it.value,
                            node.expression
                        )
                    }
                }
            }
        }

        return result
    }
}
