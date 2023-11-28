grammar App;

program
    : serviceDomain ruleBlock*
    ;
serviceDomain
    : 'domain' domainName ';'
    ;

domainName
    : Identifier
    ;

ruleBlock
    : header
    | primary
    | scope
    | map
    | services
    | annotate
    ;

annotate
    : '@' Identifier annotateParameterDefine? '{' annotateBody* '}'
    ;

annotateParameterDefine
    : '(' (annotateParameter (',' annotateParameter)*)? ')'
    ;

annotateParameter
    : Identifier '=' StringLiteral
    ;

annotateBody
    : annotateBodyIdentifier ';'
    | annotate
    ;

annotateBodyIdentifier
    : Identifier
    ;
header
    : 'header' Identifier ('extends' extendsHeaders)? '{' (headerFieldDefine ';')* '}'
    ;

extendsHeaders
    : Identifier (',' Identifier)*
    ;
primary
    : 'primary' '{' (primaryQualified ';')* '}'
    ;

primaryQualified
    : qualified
    ;
scope
    : 'scope' '{' scopeDefine '}'
    ;

map
    : 'map' Identifier mapParameters? '{' mapItem* '}'
    ;

mapParameters
    : '(' mapParameterIdentifier (',' mapParameterIdentifier)* ')'
    ;

mapParameterIdentifier
    : Identifier
    ;
mapItem
    : qualified '=' sqlType Identifier? ';'
    ;

sqlType
    : notnull? sqlTypePrimitiveType sqlTypeLength?
    ;

sqlTypePrimitiveType
    : primitiveType
    ;
notnull
    : 'notnull'
    ;

sqlTypeLength
    : '(' precision (',' scale)?')'
    ;

precision
    : IntegerLiteral
    ;

scale
    : IntegerLiteral
    ;

qualified
    : Identifier ('.' Identifier)*
    ;

headerFieldDefine
    : headerFieldModifier* headerFieldDefinePrimitiveType headerFieldDefineIdentifier defaultValue? override?
    ;

headerFieldDefinePrimitiveType
    : primitiveType
    ;

headerFieldDefineIdentifier
    : Identifier
    ;

headerFieldModifier
    : 'notnull' # HeaderFieldModifierNotNull
    | 'unique' # HeaderFieldModifierUnique
    ;

defaultValue
    : '(' 'Default' '=' defaultValueliteral ')'
    ;

defaultValueliteral
    : literal
    ;
override
    : '=' Identifier
    ;
scopeDefine
    : localBody* remoteBody*
    ;

localBody
    : 'local' scopeParameter '{' scopeItem* '}'
    ;

remoteBody
    : 'remote' scopeParameter '{' scopeItem* '}'
    ;
scopeParameter
    : '(' inParameter ',' outParameter ')'
    ;

inParameter
    : 'in' Identifier
    ;

outParameter
    : 'out' Identifier
    ;

scopeItem
    : scopeModifier* scopeItemIdentifier ';'
    ;

scopeItemIdentifier
    : Identifier
    ;

scopeModifier
    : 'asycn' # asycnModifier
    | transactionModifier # transModifier
    ;

transactionModifier
    : 'tcc'
    | 'saga'
    | 'db'
    ;

services
    : 'services' '{' service* '}'
    ;

service
    : Identifier '{' serviceBody '}'
    ;

serviceBody
    : expressionStatement *
    ;

expressionStatement
    : expression ';'
    ;

expression
    :   primaryExpr # primExpr
    |   '(' expression ')' # parenthesesExpr
    |   expression '[' expression ']' # arrayExpr
    |   expression ('++' | '--') # rightIncDecExpr
    |   ('+'|'-'|'++'|'--') expression # leftIncDecAndSignExpr
    |   ('~'|'!') expression # bitAndLogicNotExpr
    |   expression ('*'|'/'|'%') expression # devMultModeExpr
    |   expression ('+'|'-') expression # plusMinusExpr
    |   expression ('<' '<' | '>' '>' '>' | '>' '>') expression # shiftExpr
    |   expression ('<=' | '>=' | '>' | '<') expression # logicGreatLessExpr
    |   expression ('==' | '!=') expression # logicEqualsExpr
    |   expression '&' expression # bitAndExpr
    |   expression '^' expression # bitXorExpr
    |   expression '|' expression # bitOrExpr
    |   expression '&&' expression # logicAndExpr
    |   expression '||' expression # logicOrExpr
    |   expression '?' expression ':' expression # questionExpr
    |   <assoc=right> expression
        (   '='
        |   '+='
        |   '-='
        |   '*='
        |   '/='
        |   '&='
        |   '|='
        |   '^='
        |   '>>='
        |   '>>>='
        |   '<<='
        |   '%='
        )
        expression # assignExpr
    ;

primaryExpr
    :   primaryExprLiteral
    |   primaryExprQualified
    ;

primaryExprQualified
    : qualified
    ;

primaryExprLiteral
    : literal
    ;

literal
    :   IntegerLiteral
    |   FloatingPointLiteral
    |   CharacterLiteral
    |   StringLiteral
    |   BooleanLiteral
    |   'null'
    ;

// §3.10.1 Integer Literals

IntegerLiteral
    :   DecimalIntegerLiteral
    |   HexIntegerLiteral
    |   OctalIntegerLiteral
    |   BinaryIntegerLiteral
    ;

fragment
DecimalIntegerLiteral
    :   DecimalNumeral IntegerTypeSuffix?
    ;

fragment
HexIntegerLiteral
    :   HexNumeral IntegerTypeSuffix?
    ;

fragment
OctalIntegerLiteral
    :   OctalNumeral IntegerTypeSuffix?
    ;

fragment
BinaryIntegerLiteral
    :   BinaryNumeral IntegerTypeSuffix?
    ;

fragment
IntegerTypeSuffix
    :   [lL]
    ;

fragment
DecimalNumeral
    :   '0'
    |   NonZeroDigit (Digits? | Underscores Digits)
    ;

fragment
Digits
    :   Digit (DigitOrUnderscore* Digit)?
    ;

fragment
Digit
    :   '0'
    |   NonZeroDigit
    ;

fragment
NonZeroDigit
    :   [1-9]
    ;

fragment
DigitOrUnderscore
    :   Digit
    |   '_'
    ;

fragment
Underscores
    :   '_'+
    ;

fragment
HexNumeral
    :   '0' [xX] HexDigits
    ;

fragment
HexDigits
    :   HexDigit (HexDigitOrUnderscore* HexDigit)?
    ;

fragment
HexDigit
    :   [0-9a-fA-F]
    ;

fragment
HexDigitOrUnderscore
    :   HexDigit
    |   '_'
    ;

fragment
OctalNumeral
    :   '0' Underscores? OctalDigits
    ;

fragment
OctalDigits
    :   OctalDigit (OctalDigitOrUnderscore* OctalDigit)?
    ;

fragment
OctalDigit
    :   [0-7]
    ;

fragment
OctalDigitOrUnderscore
    :   OctalDigit
    |   '_'
    ;

fragment
BinaryNumeral
    :   '0' [bB] BinaryDigits
    ;

fragment
BinaryDigits
    :   BinaryDigit (BinaryDigitOrUnderscore* BinaryDigit)?
    ;

fragment
BinaryDigit
    :   [01]
    ;

fragment
BinaryDigitOrUnderscore
    :   BinaryDigit
    |   '_'
    ;

// §3.10.2 Floating-Point Literals

FloatingPointLiteral
    :   DecimalFloatingPointLiteral
    |   HexadecimalFloatingPointLiteral
    ;

fragment
DecimalFloatingPointLiteral
    :   Digits '.' Digits? ExponentPart? FloatTypeSuffix?
    |   '.' Digits ExponentPart? FloatTypeSuffix?
    |   Digits ExponentPart FloatTypeSuffix?
    |   Digits FloatTypeSuffix
    ;

fragment
ExponentPart
    :   ExponentIndicator SignedInteger
    ;

fragment
ExponentIndicator
    :   [eE]
    ;

fragment
SignedInteger
    :   Sign? Digits
    ;

fragment
Sign
    :   [+-]
    ;

fragment
FloatTypeSuffix
    :   [fFdD]
    ;

fragment
HexadecimalFloatingPointLiteral
    :   HexSignificand BinaryExponent FloatTypeSuffix?
    ;

fragment
HexSignificand
    :   HexNumeral '.'?
    |   '0' [xX] HexDigits? '.' HexDigits
    ;

fragment
BinaryExponent
    :   BinaryExponentIndicator SignedInteger
    ;

fragment
BinaryExponentIndicator
    :   [pP]
    ;

// §3.10.3 Boolean Literals

BooleanLiteral
    :   'true'
    |   'false'
    ;

// §3.10.4 Character Literals

CharacterLiteral
    :   '\'' SingleCharacter '\''
    |   '\'' EscapeSequence '\''
    ;

fragment
SingleCharacter
    :   ~['\\]
    ;

// §3.10.5 String Literals

StringLiteral
    :   '"' StringCharacters? '"'
    ;

fragment
StringCharacters
    :   StringCharacter+
    ;

fragment
StringCharacter
    :   ~["\\]
    |   EscapeSequence
    ;

// §3.10.6 Escape Sequences for Character and String Literals

fragment
EscapeSequence
    :   '\\' [btnfr"'\\]
    |   OctalEscape
    |   UnicodeEscape
    ;

fragment
OctalEscape
    :   '\\' OctalDigit
    |   '\\' OctalDigit OctalDigit
    |   '\\' ZeroToThree OctalDigit OctalDigit
    ;

fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
ZeroToThree
    :   [0-3]
    ;

// §3.10.7 The Null Literal

NullLiteral
    :   'null'
    ;

primitiveType
    :   'boolean'
    |   'char'
    |   'String'
    |   'short'
    |   'int'
    |   'long'
    |   'float'
    |   'double'
    |	'date'
    ;

Identifier
    :   Letter (Letter|JavaIDDigit)*
    ;

/**copy from Java
 */
fragment
Letter
    :  '\u0024' |
       '\u0041'..'\u005a' |
       '\u005f' |
       '\u0061'..'\u007a' |
       '\u00c0'..'\u00d6' |
       '\u00d8'..'\u00f6' |
       '\u00f8'..'\u00ff' |
       '\u0100'..'\u1fff' |
       '\u3040'..'\u318f' |
       '\u3300'..'\u337f' |
       '\u3400'..'\u3d2d' |
       '\u4e00'..'\u9fff' |
       '\uf900'..'\ufaff'
    ;

fragment
JavaIDDigit
    :  '\u0030'..'\u0039' |
       '\u0660'..'\u0669' |
       '\u06f0'..'\u06f9' |
       '\u0966'..'\u096f' |
       '\u09e6'..'\u09ef' |
       '\u0a66'..'\u0a6f' |
       '\u0ae6'..'\u0aef' |
       '\u0b66'..'\u0b6f' |
       '\u0be7'..'\u0bef' |
       '\u0c66'..'\u0c6f' |
       '\u0ce6'..'\u0cef' |
       '\u0d66'..'\u0d6f' |
       '\u0e50'..'\u0e59' |
       '\u0ed0'..'\u0ed9' |
       '\u1040'..'\u1049'
   ;

COMMENT
    :   '/*' .*? '*/'    -> channel(HIDDEN) // match anything between /* and */
    ;
WS  :   [ \r\t\u000C\n]+ -> channel(HIDDEN)
    ;

LINE_COMMENT
    : '//' ~[\r\n]* '\r'? '\n' -> channel(HIDDEN)
    ;