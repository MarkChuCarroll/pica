mod ast;
mod compiler;
mod lexer;
mod parser;
mod typeanal;

mod twist;
use lalrpop_util::lalrpop_mod;
lalrpop_mod!(pub grammar);

#[cfg(test)]
mod parser_test;

fn main() {
    println!("Hello, world!");
}
