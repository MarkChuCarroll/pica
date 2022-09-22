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
grammar QuarkPlasmaAssembly;

@header { package org.goodmath.pica.vm; }

file:
   (labeledInstruction ';')+;

labeledInstruction:
    (ID '=')? instruction
;

instruction:
   bget  # ibget
|  bnew  # ibnew
|  brIf  # ibrif
|  bset  # ibset
|  cnew  # icnew
|  convert # iconvert
|  eq    # ieq
|  binop  # ibinop
|  qgetc  # iqgetc
|  qgets  # iqgets
|  qsets  # iqsets
|  qnew  # iqnew
|  rnew # irnew
|  recv # irecv
|  send # isend
|  spawn # ispawn
|  stop # istop
|  iNeg # iineg
|  fNeg # ifneg
|  fInv   #ifinv
|  sLen  # islen
|  not # inot
;

reg:
   '(' reg ')' # indirectReg
|  '$' i=LIT_INT   # localReg
|  '@' # quarkReg
|  name=ID   # namedReg
|  i=LIT_INT  # intLitReg
|  s=LIT_STRING # strLitReg
;

loc:
   id=ID  # namedCodeLoc
|  '$' i=LIT_INT  # indexedCodeLoc
|  i=LIT_INT  # relativeCodeLoc
;

regList:
   reg ( ',' reg )*
;

brIf:
   'brIf' r=reg ',' l=loc
;


bget:
    'bGet' tgt=reg ',' src=reg ',' fld=ID
;


bnew:
   'bNew' r=reg ',' type=ID (',' args=regList)?

;


bset :
   'bSet' tgt=reg ',' t=ID ',' f=ID ',' val=reg

;

cnew:
  'cNew' tgt=reg ',' l=loc

;


convert:
   op=('iToF' | 'iToS' | 'fToI' | 'fToS' | 'sToI' | 'sToF') tgt=reg ',' src=reg

;

eq:
   'eq' tgt=reg ',' r1=reg ',' r2=reg
;

binop:
   op=('iPlus' | 'iMinus' | 'iTimes' | 'iDiv' | 'iMod' | 'iGt' | 'iGe' | 'iLt' | 'iLe' |
    'fPlus' | 'fMinus' | 'fTimes' | 'fDiv' | 'fGt' | 'fGe' | 'fLt' | 'fLe' |
    'sCat' | 'sGt' | 'sGe' | 'sLt' | 'sLe' | 'sIdx'| 'and' | 'or')
     tgt=reg ',' r1=reg ',' r2=reg


;

iNeg:
   'iNeg' tgt=reg ',' src=reg

;

not:
   'not' tgt=reg ',' src=reg
;


fNeg:
   'fNeg' tgt=reg ',' src=reg
;

fInv:
   'fInv' tgt=reg ',' src=reg
;

qgetc:
   'qGetC' tgt=reg ',' q=reg ',' qt=ID ',' name=ID
   ;

qgets:
   'qGetS' tgt=reg ',' q=reg ',' qt=ID ',' name=ID
;

qsets:
   'qSetS' tgt=reg ',' qt=ID ',' name=ID ',' val=reg
;

qnew :
   'qNew' tgt=reg ',' qt=ID  (',' regList)?
;

rnew:
   'rNew' r=reg
;

send :
   'send' ch=reg ',' msg=reg ',' c=reg
;

recv:
   'recv' target=reg ',' ch=reg ',' c=reg
;

sLen:
   'sLen' tgt=reg ',' src=reg
;

spawn:
   'spawn' r=reg
;

stop:
   'stop'
;

ID : [a-zA-Z_]+ [a-zA-Z0-9_:]*
;

LIT_STRING :  '"' (ESC | ~["\\])* '"' ;

fragment ESC :   '\\' (["\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;

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