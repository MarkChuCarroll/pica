grammar PicaGrammar;

@header { package org.goodmath.pica.parser; }

module:
  ( useDef )*
  ( definition )*
;


useDef:
   'use' ident ( '::' '{' ID ( ',' ID)* '}')?
;

definition:
  quarkDef
| bosonDef
| funDef
;

quarkDef:
   'quark' (typeParamBlock)? ID  argSpec
       ('composes' composes=typeList)?
       ('channels'
          (channelDef ';')+)?
       ('state'
          ( slotDef ';' )+)?
       ('actions'
          ( actionDef ';' )+)?
       'end'
 ;

slotDef:
   'var' ID ':' type '=' expr
;

channelDef:
    'chan' ID ':' type // must be a gluon type
;

actionDef:
    '?' ID 'do'
      onClause+
    'end'
;

onClause:
   'on' pattern '=>' action
;

pattern:
   ID ( tuplePattern | structPattern )
;

tuplePattern:
    '(' idList ')'
;

structPattern:
   '{' keyedPattern ( ',' keyedPattern )* '}'
;

keyedPattern:
   k=ID ':' v=ID
;

idList:
   ID (',' ID)*
;

typeParamBlock:
   '[' typeParamSpec (',' typeParamSpec)* ']'
;

typeParamSpec:
   ID ( '<' type )?
;

typeList:
   type ( ',' type )*
;

funDef: // TODO
   'fun' ID (typeParamBlock)? argSpec ':' type 'do' action 'end'
;

argSpec:
    '(' ( arg ( ',' arg )*  )?')'
;

arg:
    ID ':' type
;

type:
   ( typeArgBlock )? ident   # namedType
|  'fun' '(' typeList? ')' '->' type  # funType
;

typeArgBlock:
   '[' type ( ',' type )* ']'
;

/*
 Example:

 boson [T]List is
   Cons(T, [T]List) or Nil
 end
*/
bosonDef:
   'boson' (typeParamBlock)? ID 'is' bosonBody 'end'
;


bosonBody:
   bosonOption ('or' bosonOption)*
;

bosonOption:
   ident '(' typeList ')' # tupleBosonOption
| ident '{' typedIdList  '}' # structBosonOption

;

typedIdList:
   typedId (',' typedId )*
;

typedId:
   ID ':' type
;

action:
  action ('|' action)+   # choiceAction
| action (';' action)+   # sequenceAction
| action ('&' action)+   # parAction
| '(' action ')'      # parenAction
| lvalue '=' expr     # assignAction
| '!' ident  '(' expr ')' # sendAction
| 'var' ID ':' type  '=' expr         # vardefStmt// variable definition.
| 'if' cond=expr 'then' t=action 'else' f=action 'end'  # ifAction
| 'while' expr 'do' action 'end'                  # whileAction
| 'for' ID 'in' expr 'do' action  'end'           # forAction
| 'return' expr                       # returnAction
| expr                                # exprAction
;

lvalue:
  ident  # simpleLvalue
| lvalue '.' v=(ID | LIT_INT) # dotLvalue
;

expr:
  // start a process; returns the process ID, which can be used for sending
  // messages to the process.
  '^' type '(' (exprList)? ')'  # runExpr
  | l=expr op=('and' | 'or' ) r=expr  # logicExpr
  | l=expr op=('==' | '!=' | '>' | '>=' | '<' | '<=') r=expr  # compareExpr
  | l=expr op=('+' | '-'  )  r=expr  # addExpr
  | l=expr op=('*' | '/' | '%') r=expr  # multExpr
  | op=('not' | '-' ) expr  # negateExpr
  | '?' ident # receiveExpr
  | expr '(' exprList? ')' # funCallExpr
  | '(' expr ')' # parenExpr
  | ident '(' exprList ')' # bosonTupleExpr
  | ident '{' keyValueList '}' # bosonStructExpr
  | LIT_STRING # litStrExpr
  | LIT_INT  # litIntExpr
  | LIT_FLOAT # litFloatExpr
  | LIT_CHAR # litCharExpr
  | '[' exprList ']'  # listExpr
  | lvalue # lvalueExpr
;

keyValueList:
   keyValuePair (',' keyValuePair)*
;

keyValuePair:
   ID ':' expr
;

exprList:
   expr (',' expr)*
;

ident:
   (ID '::')* ID
;

ID : [a-zA-Z_]+ [-a-zA-Z0-9_]*
   | [-+!@$%^&*/;?']+
;

LIT_SYMBOL:
    '#' ([a-zA-Z_]+ [-a-zA-Z0-9_]*
           | [-+!@$%^&*/;?']+);

LIT_STRING :  '"' (ESC | ~["\\])* '"' ;

fragment ESC :   '\\' (["\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;

LIT_CHAR: '\''  (ESC | ~['\\]) '\'' ;

LIT_INT : ('-')?[0-9]+ ;

LIT_FLOAT
    :   '-'? INT '.' INT EXP?   // 1.35, 1.35E-9, 0.3, -4.5
    |   '-'? INT EXP            // 1e10 -3e4
    |   '-'? INT                // -3, 45
    ;

fragment INT :   '0' | [1-9] [0-9]* ; // no leading zeros
fragment EXP :   [Ee] [+\-]? INT ;

// Comments and whitespace
COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~'\n'* '\n' -> channel(HIDDEN) ;
WS : [ \t\n\r]+ -> channel(HIDDEN) ;