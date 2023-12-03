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
    : header
    | primary
    | scope
    | map
    | services
    | annotate
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
    : qualifiedName '=' sqlType Identifier? ';'
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

