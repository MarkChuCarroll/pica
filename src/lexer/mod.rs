use std::{collections::HashMap, str::CharIndices};
use unicode_categories::UnicodeCategories;
mod linecol;
mod tests;

#[derive(Clone, Debug)]
pub struct LexicalError {
    pub msg: String,
    pub pos: usize,
    pub line: usize,
    pub column: usize,
}

#[derive(Clone, Debug, PartialEq)]
pub enum Token {
    Comma,
    Bang,
    Bar,
    Colon,
    ColonColon,
    ColonEq,
    Div,
    Dot,
    Equal,
    EqualEqual,
    GE,
    GT,
    LE,
    LT,
    Minus,
    Mod,
    NotEqual,
    Plus,
    Times,

    LParen,
    RParen,
    LBrack,
    RBrack,
    LCurly,
    RCurly,
    LCH, // <<
    RCH, // >>

    AtBehavior,
    AtBoson,
    AtCond,
    AtFlavor,
    AtFor,
    AtQuark,
    AtWhile,

    Adopt,
    And,
    Behavior,
    Boson,
    Both,
    Chan,
    Cond,
    Create,
    Do,
    End,
    Else,
    Exit,
    Flavor,
    For,
    Hadron,
    In,
    Is,
    Not,
    On,
    Or,
    Out,
    Par,
    Provides,
    Quark,
    Rec,
    Select,
    Send,
    Seq,
    Slot,
    Then,
    Use,
    Var,
    While,

    Symbol(String),
    TypeVar(String),
    StrLit(String),
    IntLit(i64),
    FloatLit(f64),
    CharLit(char),
}

pub struct Scanner<'input> {
    text: &'input str,
    chars: std::iter::Peekable<CharIndices<'input>>,
    index: linecol::LineNumberIndex<'input>,
    current_char: Option<(usize, char)>,
    next_char: Option<(usize, char)>,
    reserved: HashMap<String, Token>,
}

impl<'input> Scanner<'input> {
    pub fn new(text: &'input str) -> Scanner<'input> {
        let mut result = Scanner {
            text: text,
            chars: text.char_indices().peekable(),
            index: linecol::LineNumberIndex::new(text),
            current_char: None,
            next_char: None,
            reserved: HashMap::from([
                ("adopt".to_string(), Token::Adopt),
                ("and".to_string(), Token::And),
                ("behavior".to_string(), Token::Behavior),
                ("boson".to_string(), Token::Boson),
                ("Both".to_string(), Token::Both),
                ("chan".to_string(), Token::Chan),
                ("cond".to_string(), Token::Cond),
                ("create".to_string(), Token::Create),
                ("do".to_string(), Token::Do),
                ("end".to_string(), Token::End),
                ("exit".to_string(), Token::Exit),
                ("flavor".to_string(), Token::Flavor),
                ("for".to_string(), Token::For),
                ("hadron".to_string(), Token::Hadron),
                ("In".to_string(), Token::In),
                ("is".to_string(), Token::Is),
                ("not".to_string(), Token::Not),
                ("on".to_string(), Token::On),
                ("or".to_string(), Token::Or),
                ("Out".to_string(), Token::Out),
                ("par".to_string(), Token::Par),
                ("provides".to_string(), Token::Provides),
                ("quark".to_string(), Token::Quark),
                ("rec".to_string(), Token::Rec),
                ("select".to_string(), Token::Select),
                ("send".to_string(), Token::Send),
                ("seq".to_string(), Token::Seq),
                ("slot".to_string(), Token::Slot),
                ("then".to_string(), Token::Then),
                ("use".to_string(), Token::Use),
                ("var".to_string(), Token::Var),
                ("while".to_string(), Token::While),
                ("@behavior".to_string(), Token::AtBehavior),
                ("@boson".to_string(), Token::AtBoson),
                ("@cond".to_string(), Token::AtCond),
                ("@flavor".to_string(), Token::AtFlavor),
                ("@for".to_string(), Token::AtFor),
                ("@quark".to_string(), Token::AtQuark),
                ("@while".to_string(), Token::AtWhile),
            ]),
        };
        result.advance();
        result
    }

    pub fn line_and_col(&self, pos: usize) -> (usize, usize) {
        self.index.line_and_col_of(pos).unwrap()
    }

    fn advance(&mut self) {
        self.current_char = self.chars.next();
        self.next_char = match self.chars.peek() {
            Some(u) => Some(*u),
            None => None,
        }
    }

    fn current_char_is(&self, pred: fn(c: char) -> bool) -> bool {
        match self.current_char {
            Some((_, c)) => pred(c),
            None => false,
        }
    }

    fn current_pos(&self) -> Option<usize> {
        self.current_char.map(|(p, _)| p)
    }
}

pub type ScannerResult<'input> = Result<(usize, Token, usize), LexicalError>;

trait LexicalCategories {
    fn is_id_char(&self) -> bool;
    fn is_id_start_char(&self) -> bool;
}

impl LexicalCategories for char {
    fn is_id_char(&self) -> bool {
        self.is_alphabetic()
            || self.is_numeric()
            || self.is_punctuation_connector()
            || self.is_symbol_math()
    }

    fn is_id_start_char(&self) -> bool {
        self.is_alphabetic() || self.is_punctuation_connector() || self.is_symbol_math()
    }
}

impl<'input> Iterator for Scanner<'input> {
    type Item = ScannerResult<'input>;

    fn next(&mut self) -> Option<Self::Item> {
        return self.scan_token();
    }
}

/// This impl block contains the meat of the scanner.
///
/// It's a relatively straightforward scanner. The easiest way to think
/// of it is that it's an FSM, where each state in the FSM is
/// a scan function, and we return either a token or an error when
/// we reach a final state.
///
/// So, for example, the scanning process for a float literal:
/// - Enter the "scan_number" state. Any numeric character stays in
///    "scan_number". A "." switches to "scan_float"; any non-numeric
///    is a terminal which returns an integer token.
/// - In the scan_float state, you consume the ".", and then again
///   stay in the state for any numeric character. If you see an "e"
///   (for exponent), then you switch to "scan_float_exponent".
/// - Etc.
///
/// Each time that you switch states in the above explanation, you
/// just call the new state function in the scanner code.
impl<'input> Scanner<'input> {
    fn lexical_error(&self, msg: &str, loc: usize) -> LexicalError {
        let (line, col) = self.line_and_col(loc);
        LexicalError {
            msg: msg.to_string(),
            line: line,
            column: col,
            pos: loc,
        }
    }

    pub fn scan_token(&mut self) -> Option<ScannerResult<'input>> {
        loop {
            match self.current_char {
                // Skip WS
                Some((_, ' ')) | Some((_, '\n')) | Some((_, '\t')) => {
                    self.advance();
                    continue;
                }
                // the unambiguous single char tokens
                Some((idx, '{')) => {
                    self.advance();
                    return Some(Ok((idx, Token::LCurly, idx + 1)));
                }
                Some((idx, '}')) => {
                    self.advance();
                    return Some(Ok((idx, Token::RCurly, idx + 1)));
                }
                Some((idx, '(')) => {
                    self.advance();
                    return Some(Ok((idx, Token::LParen, idx + 1)));
                }
                Some((idx, ')')) => {
                    self.advance();
                    return Some(Ok((idx, Token::RParen, idx + 1)));
                }
                Some((idx, '[')) => {
                    self.advance();
                    return Some(Ok((idx, Token::LBrack, idx + 1)));
                }
                Some((idx, ']')) => {
                    self.advance();
                    return Some(Ok((idx, Token::RBrack, idx + 1)));
                }
                Some((idx, ',')) => {
                    self.advance();
                    return Some(Ok((idx, Token::Comma, idx + 1)));
                }
                Some((idx, '%')) => {
                    self.advance();
                    return Some(Ok((idx, Token::Mod, idx + 1)));
                }
                Some((idx, '+')) => {
                    self.advance();
                    return Some(Ok((idx, Token::Plus, idx + 1)));
                }
                Some((idx, '*')) => {
                    self.advance();
                    return Some(Ok((idx, Token::Times, idx + 1)));
                }
                Some((idx, '.')) => {
                    self.advance();
                    return Some(Ok((idx, Token::Dot, idx + 1)));
                }
                Some((idx, '|')) => {
                    self.advance();
                    return Some(Ok((idx, Token::Bar, idx + 1)));
                }
                Some((idx, '!')) => {
                    self.advance();
                    match self.current_char {
                        Some((_, '=')) => {
                            self.advance();
                            return Some(Ok((idx, Token::NotEqual, idx + 2)));
                        }
                        _ => return Some(Ok((idx, Token::Bang, idx + 1))),
                    }
                }
                Some((idx, '=')) => {
                    self.advance();
                    match self.current_char {
                        Some((_, '=')) => {
                            self.advance();
                            return Some(Ok((idx, Token::EqualEqual, idx + 2)));
                        }
                        _ => return Some(Ok((idx, Token::Equal, idx + 1))),
                    }
                }
                Some((idx, '>')) => {
                    self.advance();
                    match self.current_char {
                        Some((_, '>')) => {
                            self.advance();
                            return Some(Ok((idx, Token::RCH, idx + 2)));
                        }
                        Some((_, '=')) => {
                            self.advance();
                            return Some(Ok((idx, Token::GE, idx + 2)));
                        }
                        _ => return Some(Ok((idx, Token::GT, idx + 1))),
                    }
                }
                Some((idx, '<')) => {
                    self.advance();
                    match self.current_char {
                        Some((_, '<')) => {
                            self.advance();
                            return Some(Ok((idx, Token::LCH, idx + 2)));
                        }
                        Some((_, '=')) => {
                            self.advance();
                            return Some(Ok((idx, Token::LE, idx + 2)));
                        }
                        _ => return Some(Ok((idx, Token::LT, idx + 1))),
                    }
                }
                Some((idx, '/')) => {
                    self.advance();
                    match self.current_char {
                        Some((_, '*')) => match self.scan_past_comment(idx) {
                            Ok(_) => continue,
                            Err(e) => return Some(Err(e)),
                        },
                        Some((_, '/')) => {
                            self.scan_past_newline();
                            continue;
                        }
                        _ => return Some(Ok((idx, Token::Div, idx + 1))),
                    }
                }
                Some((idx, ':')) => {
                    self.advance();
                    match self.current_char {
                        Some((_, ':')) => {
                            self.advance();
                            return Some(Ok((idx, Token::ColonColon, idx + 2)));
                        }
                        _ => return Some(Ok((idx, Token::Colon, idx + 1))),
                    }
                }
                Some((idx, '@')) => return self.scan_at_ident(idx),
                Some((idx, '\'')) => {
                    // char literal
                    return Some(self.scan_char_literal(idx));
                }
                Some((idx, '`')) => return self.scan_typevar(idx),
                Some((idx, '"')) => return self.scan_string(idx),
                Some((idx, c)) => {
                    if c == '-' {
                        // If it's a minus, and the next character is a digit,
                        // then send to number.
                        match self.next_char {
                            Some((_, c)) if c.is_number() => return self.scan_number(idx),
                            _ => {
                                self.advance();
                                return Some(Ok((idx, Token::Minus, idx + 1)));
                            }
                        }
                    }
                    if c.is_id_start_char() {
                        return self.scan_id(idx);
                    } else if c.is_number() || c == '-' {
                        return self.scan_number(idx);
                    } else {
                        // error: skip past the error character, and then return the error.
                        self.advance();
                        return Some(Err(
                            self.lexical_error(&format!("Invalid token char: {}", c), idx)
                        ));
                    }
                }
                None => return None,
            }
        }
    }

    fn scan_id(&mut self, start: usize) -> Option<ScannerResult> {
        self.advance();
        loop {
            match self.current_char {
                Some((_, c)) if c.is_id_char() => self.advance(),
                Some((idx, _)) => {
                    return Some(Ok(self.id_or_reserved(
                        start,
                        idx,
                        self.text[start..idx].to_string(),
                    )))
                }
                None => {
                    return Some(Ok(self.id_or_reserved(
                        start,
                        self.text.len(),
                        self.text[start..self.text.len()].to_string(),
                    )))
                }
            }
        }
    }

    fn id_or_reserved(&self, start: usize, end: usize, name: String) -> (usize, Token, usize) {
        if self.reserved.contains_key(&name) {
            return (start, self.reserved.get(&name).unwrap().clone(), end);
        } else {
            return (start, Token::Symbol(name), end);
        }
    }

    fn scan_past_comment(&mut self, start: usize) -> Result<(), LexicalError> {
        self.advance();
        loop {
            match self.current_char {
                Some((_, '*')) => {
                    self.advance();
                    match self.current_char {
                        Some((_, '/')) => {
                            self.advance();
                            return Ok(());
                        }
                        _ => continue,
                    }
                }
                Some(_) => {
                    self.advance();
                }
                None => return Err(self.lexical_error("Unterminated comment", start)),
            }
        }
    }

    /// Scan a numeric literal.
    fn scan_number(&mut self, start: usize) -> Option<ScannerResult<'input>> {
        let mut count = 0;
        if let Some((_, c)) = self.current_char {
            if c == '-' {
                self.advance();
            }
        }
        loop {
            if let Some((i, c)) = self.current_char {
                count = count + 1;
                if c.is_ascii_digit() {
                    self.advance();
                    continue;
                } else if c == '.' {
                    self.advance();
                    return self.scan_float(start);
                } else {
                    return Some(Ok((
                        start,
                        Token::IntLit(self.text[start..i].parse::<i64>().unwrap()),
                        i,
                    )));
                }
            } else {
                return Some(Ok((
                    start,
                    Token::IntLit(self.text[start..(start + count)].parse::<i64>().unwrap()),
                    start + count,
                )));
            }
        }
    }

    /// Scan the fractional part of a floating point literal.
    /// This state is only entered from scan_number, and returns a token
    /// containing everything matched by both scan_number and this state.
    fn scan_float(&mut self, start: usize) -> Option<ScannerResult<'input>> {
        loop {
            if let Some((i, c)) = self.current_char {
                if c.is_ascii_digit() {
                    self.advance();
                    continue;
                } else if c == 'e' {
                    self.advance();
                    return self.scan_float_exponent(start);
                } else {
                    return Some(Ok((
                        start,
                        Token::FloatLit(self.text[start..i].parse::<f64>().unwrap()),
                        i,
                    )));
                }
            }
        }
    }

    /// Scan the exponent part of a floating point literal.
    /// This state is only entered from scan_float, and returns a token
    /// containing everything matched by scan_number, scan_float, and this state.
    fn scan_float_exponent(&mut self, start: usize) -> Option<ScannerResult<'input>> {
        if let Some((_, c)) = self.current_char {
            if c == '-' {
                self.advance();
            }
        }
        loop {
            if let Some((i, c)) = self.current_char {
                if c.is_ascii_digit() {
                    self.advance();
                    continue;
                } else {
                    return Some(Ok((
                        start,
                        Token::FloatLit(self.text[start..i].parse::<f64>().unwrap()),
                        i,
                    )));
                }
            } else {
                return Some(Ok((
                    start,
                    Token::FloatLit(self.text[start..].parse::<f64>().unwrap()),
                    self.text.len(),
                )));
            }
        }
    }

    /// Scan a string literal.
    fn scan_string(&mut self, start: usize) -> Option<ScannerResult<'input>> {
        self.advance();
        loop {
            if let Some((i, c)) = self.current_char {
                match c {
                    '"' => {
                        self.advance();
                        return Some(Ok((
                            start,
                            Token::StrLit(self.text[start + 1..i].to_string()),
                            i + 1,
                        )));
                    }
                    '\\' => {
                        self.advance();
                        match self.scan_string_escape() {
                            Err(e) => return Some(Err(e)),
                            Ok(_) => continue,
                        }
                    }
                    _ => self.advance(),
                }
            }
        }
    }

    fn scan_string_escape(&mut self) -> Result<char, LexicalError> {
        if let Some((pos, c)) = self.current_char {
            match c {
                '\\' => return Ok('\\'),
                'n' => return Ok('\n'),
                'r' => return Ok('\r'),
                '0' => return Ok('\0'),
                't' => return Ok('\t'),
                '"' => return Ok('"'),
                'x' => {
                    self.advance();
                    // scan two hex digits
                    let digits = self.swallow(2, 2, |q: char| q.is_ascii_hexdigit())?;
                    return Ok(char::from_u32(u32::from_str_radix(&digits, 16).unwrap()).unwrap());
                }
                'u' => {
                    self.advance();
                    self.swallow_char('{')?;
                    let digits = self.swallow(1, 6, |c| c.is_ascii_hexdigit())?;
                    self.swallow_char('}')?;
                    return Ok(char::from_u32(u32::from_str_radix(&digits, 16).unwrap()).unwrap());
                }
                other => {
                    return Err(
                        self.lexical_error(&format!("Invalid escape sequence '\\{}'", other), pos)
                    )
                }
            }
        } else {
            return Err(self.lexical_error("Unterminated escape sequence", self.text.len()));
        }
    }

    /// Convenience function for scanning past a group of characters,
    /// adding them to the current token.
    ///
    /// Args:
    /// - min: the minimum number of characters to match.
    /// - max: the maximum number of characters to match.
    /// - pred: a function that returns true if a character is one
    ///    that should be matched.
    fn swallow(
        &mut self,
        min: usize,
        max: usize,
        pred: fn(c: char) -> bool,
    ) -> Result<String, LexicalError> {
        let mut result = String::new();
        for i in 0..max {
            if let Some((pos, c)) = self.current_char {
                if pred(c) {
                    result.push(c);
                    self.advance()
                } else {
                    if i >= min {
                        return Ok(result);
                    } else {
                        return Err(self.lexical_error(
                            &format!("Invalid token: Expected at least {} chars", min),
                            pos,
                        ));
                    }
                }
            } else {
                if i >= min {
                    return Ok(result);
                } else {
                    return Err(self.lexical_error(
                        &format!("Expected at least {} characters", min),
                        self.text.len(),
                    ));
                }
            }
        }
        return Ok(result);
    }

    /// Similar to [swallow], but it only consumes a single, specific
    /// character.
    fn swallow_char(&mut self, c: char) -> Result<(), LexicalError> {
        if let Some((pos, q)) = self.current_char {
            if q == c {
                self.advance();
                return Ok(());
            } else {
                return Err(self.lexical_error(&format!("Expected '{}', but saw '{}'", c, q), pos));
            }
        } else {
            return Err(
                self.lexical_error(&format!("Expected character, but saw EOF"), self.text.len())
            );
        }
    }

    fn scan_char_escape(&mut self, start: usize) -> ScannerResult {
        let c = self.scan_string_escape()?;
        match self.current_char {
            Some((end, '\'')) => return Ok((start, Token::CharLit(c), end)),
            _ => return Err(self.lexical_error("Unterminated char literal", start)),
        }
    }

    fn scan_char_literal(&mut self, start: usize) -> ScannerResult {
        self.advance();
        // After the "'", we should see either a single character,
        // or an escape code, followed by a single quote.
        if let Some((_, c)) = self.current_char {
            match c {
                '\\' => return self.scan_char_escape(start),
                _ => {
                    self.advance();
                    match self.current_char {
                        Some((end, '\'')) => {
                            self.advance();
                            return Ok((start, Token::CharLit(c), end));
                        }
                        Some((i, _)) => {
                            return Err(self.lexical_error("Invalid character literal", i))
                        }
                        _ => {
                            return Err(
                                self.lexical_error("Invalid character literal", self.text.len())
                            )
                        }
                    }
                }
            }
        } else {
            return Err(self.lexical_error("Invalid character literal", start));
        }
    }

    fn scan_past_newline(&mut self) {
        while self.current_char.is_some() && !self.current_char_is(|c| c == '\n') {
            self.advance()
        }
        if self.current_char_is(|c| c == '\n') {
            self.advance()
        }
    }

    fn scan_at_ident(
        &mut self,
        start: usize,
    ) -> Option<Result<(usize, Token, usize), LexicalError>> {
        self.advance();
        let mut count = 1;
        while self.current_char_is(|c| c.is_alphabetic()) {
            self.advance();
            count += 1
        }
        let content = self.text[start..start + count].to_string();
        let maybe_token = self.reserved.get(&content);
        match maybe_token {
            Some(tok) => Some(Ok((start, tok.clone(), start + count))),
            None => Some(Err(
                self.lexical_error(&format!("Invalid token {}", content), start)
            )),
        }
    }

    fn scan_typevar(&mut self, start: usize) -> Option<ScannerResult> {
        // A typevar is a backwards quote followed by id characters
        self.advance();
        let mut count = 1;
        while self.current_char_is(|c| c.is_id_char()) {
            self.advance();
            count += 1
        }
        let name = self.text[start..start + count].to_string();
        return Some(Ok((start, Token::TypeVar(name), start + count)));
    }
}
