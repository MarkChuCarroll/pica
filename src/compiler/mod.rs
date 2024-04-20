use std::collections::HashMap;
use std::error::Error;
use std::fmt::Display;
use std::fs::read_to_string;
use std::io;

use crate::ast;
use crate::grammar;
use crate::lexer::{LexicalError, Location, Scanner, Token};
use crate::typeanal::*;
mod tests;

type ParseError = lalrpop_util::ParseError<Location, Token, LexicalError>;

#[derive(Debug)]
pub enum CompilationError {
    Lex(LexicalError),
    InvalidToken(usize, usize, ParseError),
    UnsupportedToken(usize, usize, Token, ParseError),
    Unexpected(usize, usize, Token, Vec<String>, ParseError),
    EOF(usize, usize, ParseError),
    IO(io::Error),
    Descriptive(String),
}

impl Error for CompilationError {
    fn source(&self) -> Option<&(dyn std::error::Error + 'static)> {
        match *self {
            CompilationError::Lex(ref l) => Some(l),
            CompilationError::InvalidToken(_, _, ref e) => Some(e),
            CompilationError::UnsupportedToken(_, _, _, ref e) => Some(e),
            CompilationError::Unexpected(_, _, _, _, ref e) => Some(e),
            CompilationError::EOF(_, _, ref e) => Some(e),
            CompilationError::IO(ref e) => Some(e),
            CompilationError::Descriptive(_) => None,
        }
    }
}

impl From<LexicalError> for CompilationError {
    fn from(value: LexicalError) -> Self {
        CompilationError::Lex(value)
    }
}

impl From<lalrpop_util::ParseError<Location, Token, LexicalError>> for CompilationError {
    fn from(value: lalrpop_util::ParseError<Location, Token, LexicalError>) -> Self {
        match &value {
            lalrpop_util::ParseError::InvalidToken { location } => {
                Self::InvalidToken(location.line, location.column, value)
            }
            lalrpop_util::ParseError::UnrecognizedEof { location, .. } => {
                Self::EOF(location.line, location.column, value)
            }
            lalrpop_util::ParseError::UnrecognizedToken {
                token: (loc, token, _),
                expected,
            } => Self::Unexpected(loc.line, loc.column, token.clone(), expected.clone(), value),
            lalrpop_util::ParseError::ExtraToken { token: (loc, t, _) } => {
                Self::UnsupportedToken(loc.line, loc.column, t.clone(), value)
            }
            lalrpop_util::ParseError::User { error } => Self::Lex(error.clone()),
        }
    }
}

impl From<io::Error> for CompilationError {
    fn from(value: io::Error) -> Self {
        CompilationError::IO(value)
    }
}

impl Display for CompilationError {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match self {
            CompilationError::Lex(le) => le.fmt(f),
            CompilationError::InvalidToken(l, c, _) => {
                write!(
                    f,
                    "Error @({line}, {column}): Invalid token",
                    line = l,
                    column = c
                )
            }
            CompilationError::UnsupportedToken(l, c, t, _) => write!(
                f,
                "Error @({line}, {column}): Unsupported token {t}",
                line = l,
                column = c,
                t = t
            ),
            CompilationError::Unexpected(l, c, t, expected, _) => write!(
                f,
                "Error @({line}, {column}): Found {t} while expecting one of {e}",
                line = l,
                column = c,
                t = t,
                e = expected.join(", ")
            ),
            CompilationError::EOF(l, c, _) => {
                write!(
                    f,
                    "Error @({line}, {column}): Unexpected EOF",
                    line = l,
                    column = c
                )
            }
            CompilationError::IO(e) => e.fmt(f),
            CompilationError::Descriptive(s) => write!(f, "{}", s),
        }
    }
}

///
/// The container for all of the parts of the compiler.
/// This is the data owner for all of the data that persists
/// throughout compilation and code generation.
///
pub struct Compiler<'a> {
    pub context: TypeAnalysisContext<'a>,
    pub parser: grammar::HadronParser,
    pub modules: HashMap<String, ast::HadronDef>,
}

impl<'a> Compiler<'a> {
    pub fn new() -> Compiler<'a> {
        Compiler {
            context: TypeAnalysisContext::new(),
            parser: grammar::HadronParser::new(),
            modules: HashMap::new(),
        }
    }

    pub fn parse_file(&mut self, path: &str) -> Result<(), CompilationError> {
        let buffer = read_to_string(path)?;
        let scanner = Scanner::new(&buffer);
        let module = self.parser.parse(&buffer, path, scanner)?;
        self.modules.insert(path.to_string(), module);
        Ok(())
    }
}
