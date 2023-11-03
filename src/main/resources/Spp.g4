
grammar Spp;

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
    : object
    | role
    | actas
    | atomicservice
    | scenario
    ;

object
    : ('Object' | 'Reference') Identifier objectBody
    ;

objectBody
    :   '{' objectBodyDeclaration* '}'
    ;

objectBodyDeclaration
    : fieldDeclaration
    ;

fieldDeclaration
    : type variableDeclaratorId ';'
    | objecttype variableDeclaratorId ';'
    ;

variableDeclaratorId
    :   Identifier ('[' ']')*
    ;

type
    :   primitiveType ('[' ']')*
    ;

role
    : 'Role' Identifier roleBody
    ;

roleBody
    : objectBody
    ;

actas
    : 'act' Identifier 'as' Identifier mapBody
    ;

mapBody
    : '{' mapDeclaration* '}'
    ;

mapDeclaration
    : Identifier '=' Identifier ';'
    | takeall ';'
    | mapsame ';'
    ;

takeall
    : 'takeall'
    ;

mapsame
    : 'mapname'
    ;

scenario
    :  'Scenario' serviceDefinition '{' scenarioDeclaration* '}'
    ;

atomicservice
    : 'atomic' servicetype serviceDefinition ';'
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

objecttype
    : Identifier
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
    : Identifier
    ;
scenarioDeclaration
    : atomiccall ';'
    | transactionStatements
    ;

transactionStatements
    : 'transaction' '{' scenarioDeclaration+ '}'
    ;

atomiccall
    : Identifier '(' parameters ')'
    ;

parameters
    :
    | parameter (',' parameter)*
    ;

parameter
    : qualifiedname
    ;
qualifiedname
    : Identifier ('.' Identifier)*
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