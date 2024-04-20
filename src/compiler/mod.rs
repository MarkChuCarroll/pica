use std::collections::HashMap;
use std::error::Error;
use std::fmt::Display;
use std::fs::read_to_string;

use crate::ast;
use crate::grammar;
use crate::lexer;
use crate::typeanal::*;
mod tests;

#[derive(Debug)]
struct CompilationError {
    cause: Option<Box<dyn Error>>,
    message: String,
}

impl CompilationError {
    fn new(msg: &str, cause: Option<Box<dyn Error>>) -> Box<dyn Error> {
        Box::new(CompilationError {
            cause,
            message: msg.to_string(),
        })
    }
}

impl Display for CompilationError {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        match &self.cause {
            Some(e) => write!(f, "{}, caused by {}", self.message, e),
            _ => write!(f, "{}", self.message),
        }
    }
}

impl Error for CompilationError {}

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

    pub fn parse_file(&mut self, path: &str) -> Result<(), Box<dyn Error>> {
        let buffer = read_to_string(path)?;
        let scanner = lexer::Scanner::new(&buffer);
        let module = self.parser.parse(&buffer, path, scanner);
        match module {
            Ok(m) => {
                self.modules.insert(path.to_string(), m);
                Ok(())
            }
            Err(e) => {
                match e {
                    lalrpop_util::ParseError::UnrecognizedToken { token, expected } => {
                        match token {
                            (loc, t, _) => {
                                print!(
                                    "Parse error on {} at ({},{}): {:?}",
                                    t, loc.line, loc.column, expected
                                );
                            }
                        }
                    }
                    _ => (),
                }
                Err(CompilationError::new("ParseError", None))
            }
        }
    }
}
