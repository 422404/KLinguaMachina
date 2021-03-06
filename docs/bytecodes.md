# Byte codes

## Data access

### Read data
**get_global** <*global variable index*> : . -> value

**get_module** <*module variable index*> : . -> value

**get_instance** <*instance variable index*> : . -> value

**get_class** <*class variable index*> : . -> value

**get_local** <*local variable index*> : . -> value

### Write data
**set_global** <*global variable index*> : value -> . 

**set_module** <*module variable index*> : value -> . 

**set_instance** <*instance variable index*> : value -> . 

**set_class** <*class variable index*> : value -> . 

**set_local** <*local variable index*> : value -> . 

### Refs
**create_ref** <*local variable index*> : . -> ref

**get_ref** <*local variable index*> : . -> value

**set_ref** <*local variable index*> : value -> .

## Message sending
**send** <*selector index*> : receiver, arg1, arg2, ..., argN -> result

**compile** <*AST node index*> : receiver -> .

**bind_primitive** <*selector index*> <*primitive name index*> : receiver -> .

**and** : receiver, value -> result

**or** : receiver, value -> result

**add** : receiver, value -> result

**minus** : receiver, value -> result

**mul** : receiver, value -> result

**div** : receiver, value -> result

**mod** : receiver, value -> result

**eq** : receiver, value -> result

**neq** : receiver, value -> result

**lower** : receiver, value -> result

**lower_eq** : receiver, value -> result

**greater** : receiver, value -> result

**greater_eq** : receiver, value -> result

**pos** : receiver -> result

**neg** : receiver -> result

**not** : receiver -> result

## Literal values
**array** <*items count*> : item1, item2, ..., itemN -> array

**self** : . -> self

**context** : . -> thisContext

**true** : . -> true

**false** : . -> false

**nil** : . -> nil

**const** <*constant index*> : . -> value

**const_int** <*immediate_integer*> : . -> value

## Blocks
**closure** <*copied variables count*> <*args count*> <*length*> : copiedValue1, ..., copiedValueN -> closure

**return** : value -> .
- Push the *value* on the caller stack

**non_local_return** : value -> .
- Return from the current method and push the *value* on the 
  method's caller stack

## Stack manipulation
**pop** : value -> .

**dup** : value -> value, value

**swap** : value1, value2 -> value2, value1
