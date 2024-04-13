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

module:
   '==Headers'
   'hadron' '=' id=ident ';'
   ('requires' '=' '[' reqs=idList ']' ';')?
   ( metaTag ';' )*
   '--'
   '==Bosons'
   boson+
   '==Quarks'
   quark+
   '==Instructions'
   body
;

boson:
   'boson'
   'name' '=' name=ident';'
   ( bosonOption )+
   '--'
;

bosonOption:
  'option'
  name=ID '(' fieldTypes=idList ')' ';'
;


quark:
   'quark'
   'name' '=' name=ident ';'
   'channels' '=' '[' chs=typedIdList ']' ';'
   'fields' '=' '[' fs=typedIdList ']' ';'
   'entryPoint' '=' l=loc ';'
   '--'
;

typedIdList:
   typedId ( ',' typedId )*
;

typedId:
   '(' k=ID ',' v=ident ')'
;

metaTag:
   ident '=' metaValue
;

metaValue:
    ident  # metaId
|  LIT_STRING # metaStr
|  LIT_INT # metaInt
|  '[' metaValueList ']' # metaList
;

metaValueList:
   metaValue (',' metaValue)*
;

idList:
  ident (',' ident)*
;

body:
   (labeledInstruction ';')+;

labeledInstruction:
    (ID '=')? instruction
;

instruction:
   bget  # ibget
|  bnew  # ibnew
|  brIf  # ibrif
|  jmp   # ijmp
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
|  sel # iSel
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
    'bGet' tgt=reg ',' src=reg ',' fld=LIT_INT
;


bnew:
   'bNew' r=reg ',' type=ident (',' args=regList)?

;


bset :
   'bSet' tgt=reg ',' typeName=ident ',' field=LIT_INT ',' value=reg

;

cnew:
  'cNew' tgt=reg ',' l=loc

;


convert:
   'iToF' tgt=reg ',' src=reg  # iToF
| 'iToS' tgt=reg ',' src=reg # iToS
| 'fToI' tgt=reg ',' src=reg # fToI
| 'fToS' tgt=reg ',' src=reg # fToS
| 'sToI' tgt=reg ',' src=reg # sToI
| 'sToF' tgt=reg ',' src=reg # sToF
;

eq:
   'eq' tgt=reg ',' r1=reg ',' r2=reg
;

binop:
  'iPlus'  tgt=reg ',' r1=reg ',' r2=reg  # iPlus
| 'iMinus'  tgt=reg ',' r1=reg ',' r2=reg  # iMinus
| 'iTimes'  tgt=reg ',' r1=reg ',' r2=reg  # iTimes
| 'iDiv'  tgt=reg ',' r1=reg ',' r2=reg  # iDiv
| 'iMod'  tgt=reg ',' r1=reg ',' r2=reg  # iMod
| 'iGt'  tgt=reg ',' r1=reg ',' r2=reg   # iGt
| 'iGe'  tgt=reg ',' r1=reg ',' r2=reg  # iGe
| 'iLt'  tgt=reg ',' r1=reg ',' r2=reg   # iLt
| 'iLe'  tgt=reg ',' r1=reg ',' r2=reg   # iLe
| 'fPlus'  tgt=reg ',' r1=reg ',' r2=reg  # fPlus
| 'fMinus'  tgt=reg ',' r1=reg ',' r2=reg  # fMinus
| 'fTimes'  tgt=reg ',' r1=reg ',' r2=reg  # fTimes
| 'fDiv'  tgt=reg ',' r1=reg ',' r2=reg  # fDiv
| 'fGt'  tgt=reg ',' r1=reg ',' r2=reg   # fGt
| 'fGe'  tgt=reg ',' r1=reg ',' r2=reg   # fGe
| 'fLt'  tgt=reg ',' r1=reg ',' r2=reg   # fLt
| 'fLe'  tgt=reg ',' r1=reg ',' r2=reg   # fLe
| 'sCat'  tgt=reg ',' r1=reg ',' r2=reg  # sCat
| 'sGt'  tgt=reg ',' r1=reg ',' r2=reg  # sGt
| 'sGe' tgt=reg ',' r1=reg ',' r2=reg   # sGe
| 'sLt'  tgt=reg ',' r1=reg ',' r2=reg   # sLt
| 'sLe'  tgt=reg ',' r1=reg ',' r2=reg   # sLe
| 'sIdx' tgt=reg ',' r1=reg ',' r2=reg   # sIdx
| 'and'  tgt=reg ',' r1=reg ',' r2=reg   # and
| 'or' tgt=reg ',' r1=reg ',' r2=reg   # or
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

jmp:
   'jmp' loc
;

qgetc:
   'qGetC' tgt=reg ',' q=reg ',' qt=ident ',' name=ID
   ;

qgets:
   'qGetS' tgt=reg ',' q=reg ',' qt=ident ',' name=ID
;

qsets:
   'qSetS' tgt=reg ',' qt=ident ',' name=ID ',' value=reg
;

qnew :
   'qNew' tgt=reg ',' qt=ident  (',' regList)?
;

rnew:
   'rNew' r=reg
;

send :
   'send' ch=reg ',' msg=reg ',' c=reg
;

sel:
  'sel' regList
;

recv:
   'recv' target=reg ',' ch=reg ',' c=reg
;

sLen:
   'sLen' tgt=reg ',' src=reg
;

spawn:
   'spawn' r=regList
;

stop:
   'stop'
;

ident:
   ID ('::' ID)*
;

ID : [a-zA-Z_]+ [a-zA-Z0-9_]*
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