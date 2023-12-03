grammar Spp;

import Base;

program
    : servicedomain compilationUnits
    ;

servicedomain
    : 'domain' domainname ';'
    ;

domainname
    : Identifier
    ;

compilationUnits
    : (compilationUnit)*
    ;

compilationUnit
    : entity
    | enumeration
    | role
    | actas
    | atomicservice
    | scenario
    ;
enumeration
    : 'Enum' Identifier '{' enumBody '}'
    ;

enumBody
    : enumBodyIdentifier (',' enumBodyIdentifier)*
    ;

enumBodyIdentifier
    : Identifier
    | Identifier '(' StringLiteral ')'
    ;

entity
    : ('Entity' | 'Reference' ) Identifier entityBody
    ;

entityBody
    :   '{' entityBodyDeclaration* '}'
    ;

entityBodyDeclaration
    : fieldDeclaration
    ;

fieldDeclaration
    : type variableDeclaratorId ';'
    | entitytype variableDeclaratorId ';'
    ;

variableDeclaratorId
    :   Identifier ('[' ']')*
    ;

type
    :   primitiveType ('[' ']')*
    ;

role
    : ('Role' | 'Contract' ) Identifier roleBody
    ;

roleBody
    : entityBody
    ;

actas
    : 'take' Identifier 'as' Identifier mapBody
    ;

mapBody
    : '{' mapDeclaration* '}'
    ;

mapDeclaration
    : mapfield
    | takeall ';'
    | mapsame ';'
    ;

mapfield
    : primitiveType Identifier '=' Identifier ';'
    ;

takeall
    : 'takeall'
    ;

mapsame
    : 'mapsame'
    ;

scenario
    :  'Scenario' autosort? serviceDefinition '{' scenarioDeclaration '}'
    ;

autosort
    : 'serial'
    | 'parallel'
    ;

atomicservice
    : 'atomic' servicetype returnList? serviceDefinition ';'
    ;

returnList
    : '<' returnType (',' returnType)* '>'
    ;

returnType
    : parameterDeclaration
    ;
serviceDefinition
    : Identifier '(' parameterDeclarations')'
    ;

servicetype
    : 'query'
    | 'check'
    | 'calculate'
    | 'update'
    ;

entitytype
    : Identifier  ('[' ']')*
    ;

parameterDeclarations
    : parameterDeclaration (',' parameterDeclaration)*
    ;

parameterDeclaration
    : type Identifier
    | roletype Identifier
    | roletype
    ;

roletype
    : Identifier ('[' ']')*
    ;
scenarioDeclaration
    : executegroup transactionStatements?
    ;
executegroup
    : atomiccall* (serialgroup* | parallelgroup*)
    ;
serialgroup
    : 'serial' '{' atomiccall* parallelgroup* '}'
    ;
parallelgroup
    : 'parallel' '{' atomiccall* serialgroup* '}'
    ;

transactionStatements
    : 'transaction' '{' atomiccall* '}'
    ;

atomiccall
    : Identifier '(' parameters ')' ';'
    ;

parameters
    :
    | parameter (',' parameter)*
    ;

parameter
    : qualifiedName
    ;
