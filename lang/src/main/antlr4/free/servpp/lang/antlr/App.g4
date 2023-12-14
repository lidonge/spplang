grammar App;
import JavaExpr;

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
    : annotateDefine* annotatableRule
    | annotate
    ;

annotatableRule
    : header
    | primary
    | scope
    | map
    | services
    | tables
    ;

tables
    : 'tables' Identifier '{' annotateTableDefine* '}'
    ;

annotateTableDefine
    : annotateDefine* tableDefine
    ;

tableDefine
    : 'table' Identifier  '(' identifyList ')' '{' columnDefine* keyDefine*'}'
    ;

keyDefine
    : primaryKeyDefine
    | foreignKeyDefine
    ;

foreignKeyDefine
    : 'foreignkey' '(' qualifiedItemList ')' foreignKeyReferences? ';'
    ;

foreignKeyReferences
    :'references' Identifier
    ;

primaryKeyDefine
    : 'primarykey' '(' qualifiedItemList ')' ';'
    ;

qualifiedItemList
    : qualifiedItem (',' qualifiedItem)*
    ;

qualifiedItem
    : qualifiedName
    ;

identifyList
    : identifyItem (',' identifyItem )*
    ;

identifyItem
    : Identifier
    ;

columnDefine
    : columnModifier+ columnType columnName ';'
    ;

columnName
    : Identifier
    ;

columnModifier
    : 'primarykey' #columnPrimaryKey
    | 'null' #columnNullalbe
    | 'notnull' #columnNotnull
    ;

columnType
    : columnTypePrimitiveType  columnTypePrecisionScale?
    ;

columnTypePrecisionScale
    : '(' columnTypePrecision (',' columnTypeScale )?')'
    ;

columnTypePrecision
    : IntegerLiteral
    ;

columnTypeScale
    : IntegerLiteral
    ;

columnTypePrimitiveType
    : primitiveType
    ;

annotate
    : annotateDefine+ '{' annotateBody* '}'
    ;

annotateDefine
    : '@' Identifier annotateParameterDefine?
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
    : qualifiedName
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
    : Identifier '=' sqlType Identifier? ';'
    ;

sqlType
    : sqlTypeNotnull? sqlTypePrimitiveType sqlTypeLength?
    ;

sqlTypePrimitiveType
    : primitiveType
    ;

sqlTypeNotnull
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
    : 'local' scopeParameter? '{' scopeItem* '}'
    ;

remoteBody
    : 'remote' scopeParameter? '{' scopeItem* '}'
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

