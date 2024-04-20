use crate::{compiler::Compiler, twist::Twistable};
use std::fs::read_to_string;

#[test]
fn test_compiler() {
    let mut compiler = Compiler::new();
    let path = "./test_data/test.pica";
    assert!(compiler.modules.is_empty());
    let parse_result = compiler.parse_file(path);
    if parse_result.is_err() {
        print!("{:?}\n", parse_result);
    }
    assert!(!compiler.modules.is_empty());
    assert!(compiler.modules.contains_key("./test_data/test.pica"));
    let expected_parse_tree = read_to_string("./test_data/expected_parse_tree.twist").unwrap();
    assert_eq!(
        expected_parse_tree,
        compiler
            .modules
            .get("./test_data/test.pica")
            .unwrap()
            .twist()
            .render(0)
    )
}
