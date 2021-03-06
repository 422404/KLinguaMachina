package org.linguamachina.klinguamachina.interpreter.block.impl

import org.linguamachina.klinguamachina.generation.IndexedSet
import org.linguamachina.klinguamachina.generation.bytecode.BytecodeEmitter
import org.linguamachina.klinguamachina.generation.bytecode.BytecodeStorage
import org.linguamachina.klinguamachina.generation.bytecode.Bytecode
import org.linguamachina.klinguamachina.generation.bytecode.Bytecode.*
import org.linguamachina.klinguamachina.generation.impl.BaseIndexedSet
import org.linguamachina.klinguamachina.interpreter.block.Block
import org.linguamachina.klinguamachina.interpreter.block.BlockLiteralValue

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
class CompiledBlock(
    arity: Int
): Block(arity), BytecodeEmitter, BytecodeStorage {
    val constants: IndexedSet<BlockLiteralValue> = BaseIndexedSet()
    val variables: IndexedSet<String> = BaseIndexedSet()
    override val bytecodes = mutableListOf<UInt>()
    var hasModuleReturn = false
        private set

    private fun appendBytecode(bytecode: Bytecode) {
        bytecodes += bytecode.ordinal.toUInt()
    }

    private fun appendImmediate(immediateValue: Int) {
        bytecodes += immediateValue.toUInt()
    }

    fun addModuleReturn() {
        if (!hasModuleReturn) {
            hasModuleReturn = true
            emitNil()
            emitReturn()
        }
    }

    fun addNonLocalModuleReturn() {
        if (!hasModuleReturn) {
            hasModuleReturn = true
            emitNil()
            emitNonLocalReturn()
        }
    }

    fun removeModuleReturns() {
        if (hasModuleReturn && bytecodes.isNotEmpty()) {
            hasModuleReturn = false
            // Remove the 'return' or 'non_local_return' bytecode
            bytecodes.removeLast()
            // Remove the 'nil' bytecode
            bytecodes.removeLast()
        }
    }

    override fun emitGetGlobal(variableIndex: Int) {
        appendBytecode(GET_GLOBAL)
        appendImmediate(variableIndex)
    }

    override fun emitGetModule(variableIndex: Int) {
        appendBytecode(GET_MODULE)
        appendImmediate(variableIndex)
    }

    override fun emitGetInstance(variableIndex: Int) {
        appendBytecode(GET_INSTANCE)
        appendImmediate(variableIndex)
    }

    override fun emitGetClass(variableIndex: Int) {
        appendBytecode(GET_CLASS)
        appendImmediate(variableIndex)
    }

    override fun emitGetLocal(variableIndex: Int) {
        appendBytecode(GET_LOCAL)
        appendImmediate(variableIndex)
    }

    override fun emitSetGlobal(variableIndex: Int) {
        appendBytecode(SET_GLOBAL)
        appendImmediate(variableIndex)
    }

    override fun emitSetModule(variableIndex: Int) {
        appendBytecode(SET_MODULE)
        appendImmediate(variableIndex)
    }

    override fun emitSetInstance(variableIndex: Int) {
        appendBytecode(SET_INSTANCE)
        appendImmediate(variableIndex)
    }

    override fun emitSetClass(variableIndex: Int) {
        appendBytecode(SET_CLASS)
        appendImmediate(variableIndex)
    }

    override fun emitSetLocal(variableIndex: Int) {
        appendBytecode(SET_LOCAL)
        appendImmediate(variableIndex)
    }

    override fun emitCreateRef(variableIndex: Int) {
        appendBytecode(CREATE_REF)
        appendImmediate(variableIndex)
    }

    override fun emitGetRef(variableIndex: Int) {
        appendBytecode(GET_REF)
        appendImmediate(variableIndex)
    }

    override fun emitSetRef(variableIndex: Int) {
        appendBytecode(SET_REF)
        appendImmediate(variableIndex)
    }

    override fun emitSend(selectorIndex: Int) {
        appendBytecode(SEND)
        appendImmediate(selectorIndex)
    }

    override fun emitCompile(astNodeIndex: Int) {
        appendBytecode(COMPILE)
        appendImmediate(astNodeIndex)
    }

    override fun emitBindPrimitive(selectorIndex: Int, primitiveNameIndex: Int) {
        appendBytecode(BIND_PRIMITIVE)
        appendImmediate(selectorIndex)
        appendImmediate(primitiveNameIndex)
    }

    override fun emitAnd() {
        appendBytecode(AND)
    }

    override fun emitOr() {
        appendBytecode(OR)
    }

    override fun emitAdd() {
        appendBytecode(ADD)
    }

    override fun emitMinus() {
        appendBytecode(MINUS)
    }

    override fun emitMul() {
        appendBytecode(MUL)
    }

    override fun emitDiv() {
        appendBytecode(DIV)
    }

    override fun emitMod() {
        appendBytecode(MOD)
    }

    override fun emitEq() {
        appendBytecode(EQ)
    }

    override fun emitNeq() {
        appendBytecode(NEQ)
    }

    override fun emitLower() {
        appendBytecode(LOWER)
    }

    override fun emitLowerEq() {
        appendBytecode(LOWER_EQ)
    }

    override fun emitGreater() {
        appendBytecode(GREATER)
    }

    override fun emitGreaterEq() {
        appendBytecode(GREATER_EQ)
    }

    override fun emitPos() {
        appendBytecode(POS)
    }

    override fun emitNeg() {
        appendBytecode(NEG)
    }

    override fun emitNot() {
        appendBytecode(NOT)
    }

    override fun emitArray(itemCount: Int) {
        appendBytecode(ARRAY)
        appendImmediate(itemCount)
    }

    override fun emitSelf() {
        appendBytecode(SELF)
    }

    override fun emitContext() {
        appendBytecode(CONTEXT)
    }

    override fun emitTrue() {
        appendBytecode(TRUE)
    }

    override fun emitFalse() {
        appendBytecode(FALSE)
    }

    override fun emitNil() {
        appendBytecode(NIL)
    }

    override fun emitConst(constIndex: Int) {
        appendBytecode(CONST)
        appendImmediate(constIndex)
    }

    override fun emitConstInt(immediateValue: Int) {
        appendBytecode(CONST_INT)
        appendImmediate(immediateValue)
    }

    override fun emitClosure(copiedVariableCount: Int, argsCount: Int,
                             closureBodyEmitter: () -> Unit) {
        appendBytecode(CLOSURE)
        appendImmediate(copiedVariableCount)
        appendImmediate(argsCount)
        val lengthBackpatchIndex = bytecodes.size
        appendImmediate(0) // Stub
        closureBodyEmitter()
        bytecodes[lengthBackpatchIndex] = (bytecodes.size - (lengthBackpatchIndex + 1)).toUInt()
    }

    override fun emitReturn() {
        appendBytecode(RETURN)
    }

    override fun emitNonLocalReturn() {
        appendBytecode(NON_LOCAL_RETURN)
    }

    override fun emitPop() {
        appendBytecode(POP)
    }

    override fun emitDup() {
        appendBytecode(DUP)
    }

    override fun emitSwap() {
        appendBytecode(SWAP)
    }

    override fun toString(): String {
        var out = "code:\n"
        var bytecodeIndex = 0
        var immediateArgIndex = 0

        while (bytecodeIndex < bytecodes.size) {
            val bytecode = Bytecode.values()[bytecodes[bytecodeIndex].toInt()]
            out += "$bytecodeIndex: ${bytecode.name.toLowerCase()}"
            bytecodeIndex++
            immediateArgIndex = 0
            while (immediateArgIndex < bytecode.immediateArgsCount) {
                out += " ${bytecodes[bytecodeIndex + immediateArgIndex]}"
                immediateArgIndex++
            }
            bytecodeIndex += bytecode.immediateArgsCount
            out += "\n"
        }

        out += "\nliterals:\n"
        for (entry in constants.entries) {
            out += "${entry.value}: ${entry.key}\n"
        }

        out += "\nvariables:\n"
        for (entry in variables.entries) {
            out += "${entry.value}: ${entry.key}\n"
        }

        return out
    }
}
