mod ast;
mod lexer;
mod parser;

mod twist;
use lalrpop_util::lalrpop_mod;
lalrpop_mod!(pub grammar);

#[cfg(test)]
mod parser_test;

fn main() {
    println!("Hello, world!");
}
