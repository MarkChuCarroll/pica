use lalrpop_util::lalrpop_mod;
mod ast;
mod lexer;
mod twist;

lalrpop_mod!(grammar);

fn main() {
    println!("Hello, world!");
}
