/* Copyright 2022, Mark C. Chu-Carroll
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
grammar PicaGrammar;

@header { package org.goodmath.pica.parser; }

hadron:
  ( useDef )*
  ( definition )*
;

useDef:
   'use' ident ( '::' '{' ID ( ',' ID)* '}')?
;

definition:
  quarkDef  # quarkDefChoice
| flavorDef # flavorDefChoice
| bosonDef # bosonDefChoice
;

flavorDef:
   'flavor' (typeParamBlock)? ID ('composes'  composes=typeList)? 'is'
      flavorMessage+
   'end' ( '@flavor' )?
;

flavorMessage:
   ID '(' typedIdList ')'
;

quarkDef:
   'quark' (typeParamBlock)? ID (  argSpec )?
   ('composes' composes=constructorList)?
   'is'
      ( messageDef
      | slotDef
      )*
   'do'
      action*
   'end' ('@quark')?
 ;

constructorList:
   constructor ( ',' constructor )*
;

constructor:
   type ( '(' exprList ')' )?
;

messageDef:
   'message' ID '(' typedIdList ')' 'do' action+ 'end' ('@message')?
;


slotDef:
   'slot' ID ':' type '=' expr
;

idList:
   ID (',' ID)*
;

typeParamBlock:
   '[' typeParamSpec (',' typeParamSpec)* ']'
;

typeParamSpec:
   ID ( '<' '(' typeList ')' )?
;

typeList:
   type ( ',' type )*
;

argSpec:
    '(' ( arg ( ',' arg )*  )?')'
;

arg:
    ID ':' type
;

type:
    ( typeArgBlock )? ident   # namedType
;

typeArgBlock:
   '[' type ( ',' type )* ']'
;

bosonDef:
   'boson' (typeParamBlock)? ID 'is' bosonBody 'end' ( '@boson' )?
;


bosonBody:
   bosonOption (',' bosonOption)*
;

bosonOption:
   ID '(' typeList ')' # tupleBosonOption
| ID '{' typedIdList  '}' # structBosonOption
;

typedIdList:
   typedId (',' typedId )*
;

typedId:
   ID ':' type
;

action:
  'seq' '{' action (',' action)+ '}'  # sequenceAction
| 'par' '{' action (',' action)+ '}'  # parAction
| '(' action ')'      # parenAction
| lvalue '=' expr     # assignAction
| expr '<-' ID ( '(' exprList ')' )?  # sendAction
| 'var' ID ':' type  '=' expr         # vardefStmt// variable definition.
| matchAction   # matchActionStmt
| 'if' '(' cond=expr  ')'  t=action  'else'  f=action    # ifAction
| 'while' '(' expr ')'  action                # whileAction
| 'repeat' action  # loopAction
| 'for' ID 'in' expr 'do' action  'end' ('@action')?         # forAction
| 'exit'  # exitAction
;

matchAction:
   'match' e=expr 'as'
   onClause+
   ('else' elseClause=action)?
   'end' ( '@match' )?
;


onClause:
   'on' pattern 'do' action
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


lvalue:
  ident  # simpleLvalue
| lvalue '.' v=(ID | LIT_INT) # dotLvalue
;

expr:
  // start a process.
    'spawn' type '(' (exprList)? ')'  # runExpr

  | // Sync call: if you have a message whose last parameter is of type "[T]Result", then you can call it
    // as an expression, and the current continuation will be converted to an object of type [T]result.
    expr '<<' ID '(' exprList? ')' # syncCallExpr
  | l=expr op=('and' | 'or' ) r=expr  # logicExpr
  | l=expr op=('==' | '!=' | '>' | '>=' | '<' | '<=') r=expr  # compareExpr
  | l=expr op=('+' | '-'  )  r=expr  # addExpr
  | l=expr op=('*' | '/' | '%') r=expr  # multExpr
  | op=('not' | '-' ) expr  # negateExpr
  | '(' expr ')' # parenExpr
  | ident ('(' exprList ')')? # bosonTupleExpr
  | ident '{' keyValueList '}' # bosonStructExpr
  | LIT_STRING # litStrExpr
  | LIT_INT  # litIntExpr
  | LIT_FLOAT # litFloatExpr
  | LIT_CHAR # litCharExpr
  | type '[' exprList ']'  # listExpr
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

ID : [a-zA-Z_]+ [a-zA-Z0-9_]*
;

LIT_SYMBOL:
    '#' [a-zA-Z_]+ [a-zA-Z0-9_]*
;

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