use crate::lexer::*;

#[cfg(test)]
fn assert_token_is<'input>(result: Option<ScannerResult<'input>>, expected: Token) {
    assert!(result.is_some());
    let (_, t, _) = result.unwrap().unwrap();
    assert_eq!(expected, t)
}

#[test]
fn test_scanner() {
    let mut lex = Scanner::new("foo bar/baz + 23\nbehavior\n|end m @flavor=3.134- ==32.43e13");

    assert_token_is(lex.scan_token(), Token::Symbol("foo".to_string()));
    assert_token_is(lex.scan_token(), Token::Symbol("bar".to_string()));
    assert_token_is(lex.scan_token(), Token::Div);
    assert_token_is(lex.scan_token(), Token::Symbol("baz".to_string()));
    assert_token_is(lex.scan_token(), Token::Plus);
    assert_token_is(lex.scan_token(), Token::IntLit(23));
    assert_token_is(lex.scan_token(), Token::Behavior);
    assert_token_is(lex.scan_token(), Token::Bar);
    assert_token_is(lex.scan_token(), Token::End);
    assert_token_is(lex.scan_token(), Token::Symbol("m".to_string()));
    assert_token_is(lex.scan_token(), Token::AtFlavor);
    assert_token_is(lex.scan_token(), Token::Equal);
    assert_token_is(lex.scan_token(), Token::FloatLit("3.134".to_string()));
    assert_token_is(lex.scan_token(), Token::Minus);
    assert_token_is(lex.scan_token(), Token::EqualEqual);
    assert_token_is(lex.scan_token(), Token::FloatLit("32.43e13".to_string()));
}

#[test]
fn test_scanner_more() {
    let mut lex = Scanner::new("!= << < <= >< >= >> { } [ ] ( ) and or not");
    assert_token_is(lex.scan_token(), Token::NotEqual);
    assert_token_is(lex.scan_token(), Token::LCH);
    assert_token_is(lex.scan_token(), Token::LT);
    assert_token_is(lex.scan_token(), Token::LE);
    assert_token_is(lex.scan_token(), Token::GT);
    assert_token_is(lex.scan_token(), Token::LT);
    assert_token_is(lex.scan_token(), Token::GE);
    assert_token_is(lex.scan_token(), Token::RCH);
    assert_token_is(lex.scan_token(), Token::LCurly);
    assert_token_is(lex.scan_token(), Token::RCurly);
    assert_token_is(lex.scan_token(), Token::LBrack);
    assert_token_is(lex.scan_token(), Token::RBrack);
    assert_token_is(lex.scan_token(), Token::LParen);
    assert_token_is(lex.scan_token(), Token::RParen);
    assert_token_is(lex.scan_token(), Token::And);
    assert_token_is(lex.scan_token(), Token::Or);
    assert_token_is(lex.scan_token(), Token::Not);
}

#[test]
fn test_scan_string() {
    let mut lex = Scanner::new("\"abcd\" \"abc\ndef\"");
    assert_token_is(lex.scan_token(), Token::StrLit("abcd".to_string()));
    assert_token_is(lex.scan_token(), Token::StrLit("abc\ndef".to_string()));
}
