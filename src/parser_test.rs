use crate::grammar::BosonDefParser;
use crate::lexer::Scanner;
use crate::twist::*;

#[test]
fn test_parse_boson() {
    let funstr = "
    boson [`a]Cons is
      Pair(`a, [`a]Cons)
    | Nil()
    end@boson
    ";

    let parsed = BosonDefParser::new().parse(&funstr, "foo", Scanner::new(funstr));
    let parsed_str = parsed.unwrap().twist().render(0);

    let expected = Twist::obj(
        "BosonDef".to_string(),
        vec![
            Twist::battr("id".to_string(), "Cons"),
            Twist::barr(
                "type_params".to_string(),
                vec![Twist::bobj(
                    "type_param".to_string(),
                    vec![Twist::battr("typevar".to_string(), "`a")],
                )],
            ),
            Twist::barr(
                "options".to_string(),
                vec![
                    Twist::bobj(
                        "TupleOptionDecl".to_string(),
                        vec![
                            Twist::battr("id".to_string(), "Pair"),
                            Twist::barr(
                                "value_types".to_string(),
                                vec![
                                    Twist::battr("TypeVar".to_string(), "`a"),
                                    Twist::bobj(
                                        "NamedType".to_string(),
                                        vec![
                                            Twist::battr("base_type_id".to_string(), "Cons"),
                                            Twist::barr(
                                                "type_arguments".to_string(),
                                                vec![Twist::battr("TypeVar".to_string(), "`a")],
                                            ),
                                        ],
                                    ),
                                ],
                            ),
                        ],
                    ),
                    Twist::bobj(
                        "TupleOptionDecl".to_string(),
                        vec![
                            Twist::battr("id".to_string(), "Nil"),
                            Twist::barr("value_types".to_string(), Vec::new()),
                        ],
                    ),
                ],
            ),
        ],
    );

    println!("EXPECTED\n{}", expected.render(0));
    println!(
        "=======================================\nACTUAL\n{}",
        parsed_str
    );
    assert_eq!(expected.render(0), parsed_str)
}
