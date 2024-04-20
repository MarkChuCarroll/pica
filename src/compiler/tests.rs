use crate::ast::HadronDef;
use crate::compiler::Compiler;
use crate::twist::Twistable;

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
    let expected_parse_tree = "object hadron:
  name=./test_data/test.pica
  array uses:
    array IO:
      InputStream
  array defs:
    object BosonDef:
      id=ScannerOutput
      array options:
        object StructOptionDecl:
          name=Token
          array values:
            object value_param:
              id=type
              value_type:
                object NamedType:
                  base_type_id=String
            object value_param:
              id=content
              value_type:
                object NamedType:
                  base_type_id=String
            object value_param:
              id=line
              value_type:
                object NamedType:
                  base_type_id=Int
        object TupleOptionDecl:
          id=EndOfStream
          array value_types:
            object NamedType:
              base_type_id=Unit
        object StructOptionDecl:
          name=ScanError
          array values:
            object value_param:
              id=message
              value_type:
                object NamedType:
                  base_type_id=String
            object value_param:
              id=line
              value_type:
                object NamedType:
                  base_type_id=Int
    object QuarkDef:
      name=Scanner
      array value_params:
        object value_param:
          id=in
          value_type:
            object NamedType:
              base_type_id=InputStream
      array provides:
      array slots:
        object slot_decl:
          id=currentToken
          value_type:
            object NamedType:
              base_type_id=String
      array behaviors:
        object behavior_decl:
          id=main
          array params:
          array actions:
            object RecvAction:
              source:
                object ChannelExpr:
                  VariableExpr=in
                  name=chars
              array clauses:
                object ReceiveClause:
                  pattern:
                    object TuplePattern:
                      c
                  array actions:
                    object AssignAction:
                      target=currentToken
                      value:
                        object OperatorExpr:
                          operator=Plus
                          array arguments:
                            VariableExpr=currentToken
                            VariableExpr=c
                    object CondAction:
                      array cond_clauses:
                        object clause:
                          cond:
                            object OperatorExpr:
                              operator=Equals
                              array arguments:
                                VariableExpr=c
                                VariableExpr=end_of_token
                          array actions:
                            object SendAction:
                              target:
                                VariableExpr=output
                              value:
                                object BosonStructExpr:
                                  struct_type=Token
                                  array value_args:
                                    type:
                                      StringLit=\"t\"
                                    content:
                                      VariableExpr=currentToken
                                    line:
                                      IntLit=27
                            object AssignAction:
                              target=currentToken
                              value:
                                StringLit=\"\"
                object ReceiveClause:
                  pattern:
                    object TuplePattern:
                      u
                  array actions:
                    object SendAction:
                      target:
                        VariableExpr=output
                      value:
                        object BosonStructExpr:
                          struct_type=Token
                          array value_args:
                            type:
                              StringLit=\"t\"
                            content:
                              VariableExpr=currentToken
                            line:
                              VariableExpr=line_number
                    object SendAction:
                      target:
                        VariableExpr=output
                      value:
                        object BosonTupleExpr:
                          tuple_type=EndOfStream
                          array value_args:
                            VariableExpr=Unit
                    ExitAction
      array channels:
        object channel_decl:
          id=output
          channel_type:
            object ChannelType:
              dir=Both
              value_type:
                object NamedType:
                  base_type_id=ScannerOutput
      object initial_beh:
        behavior=main
        array args:\n"
        .to_string();
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
