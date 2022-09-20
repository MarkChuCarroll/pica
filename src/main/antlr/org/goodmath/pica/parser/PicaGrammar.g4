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
          channelDef+)?
       ('state'
          slotDef+)?
       'action' action
   'endquark'
 ;

slotDef:
   'slot' ID ':' type '=' expr
;

channelDef:
    'chan' ID ':' type // must be a gluon type
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
   'fun' ID (typeParamBlock)? argSpec ':' type 'do' action 'endfun'
;

argSpec:
    '(' ( arg ( ',' arg )*  )?')'
;

arg:
    ID ':' type
;

type:
   'chan' type # channelType
|   ( typeArgBlock )? ident   # namedType
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
   'boson' (typeParamBlock)? ID 'is' bosonBody 'endboson'
;


bosonBody:
   bosonOption ('or' bosonOption)*
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
  action ('|' action)+   # choiceAction
| action (';' action)+   # sequenceAction
| action ('&' action)+   # parAction
| '(' action ')'      # parenAction
| lvalue '=' expr     # assignAction
| '!' ident  '(' expr ')' # sendAction
| '?' ID 'do'
        onClause+
      'end'  # receiveAction
| 'var' ID ':' type  '=' expr         # vardefStmt// variable definition.
| 'if' cond=expr 'then' t=action 'else' f=action 'endif'  # ifAction
| 'while' expr 'do' action 'end'                  # whileAction
| 'repeat' action 'end' # loopAction
| 'for' ID 'in' expr 'do' action  'end'           # forAction
| 'return' expr                       # returnAction
| expr                                # exprAction
| 'exit'  # exitAction
;

onClause:
   'on' pattern 'do' action 'end'
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
  // start a process; returns the process ID, which can be used for sending
  // messages to the process.
    'run' type '(' (exprList)? ')'  # runExpr
  | 'newchan' type  # newChanExpr
  | l=expr op=('and' | 'or' ) r=expr  # logicExpr
  | l=expr op=('==' | '!=' | '>' | '>=' | '<' | '<=') r=expr  # compareExpr
  | l=expr op=('+' | '-'  )  r=expr  # addExpr
  | l=expr op=('*' | '/' | '%') r=expr  # multExpr
  | op=('not' | '-' ) expr  # negateExpr
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