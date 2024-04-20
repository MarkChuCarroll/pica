use crate::twist::{Renderable, Twist, Twistable};

pub type Symbol = String;

impl Renderable for Symbol {
    fn render(&self) -> String {
        self.to_string()
    }
}

trait Instantiable {}

pub type Identifier = Vec<Symbol>;
impl Renderable for Identifier {
    fn render(&self) -> String {
        self.join("::")
    }
}

#[derive(Clone, Eq, PartialEq, Hash, Debug)]
pub struct TypeVar {
    pub s: Symbol,
}

impl Renderable for TypeVar {
    fn render(&self) -> String {
        self.s.to_owned()
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct HadronDef {
    pub id: Symbol,
    pub use_decls: Vec<UseDecl>,
    pub defs: Vec<Definition>,
}

impl Twistable for HadronDef {
    fn twist(&self) -> Twist {
        Twist::obj(
            "hadron".to_string(),
            vec![
                Twist::battr("name".to_string(), &self.id.render()),
                Twist::barr("uses".to_string(), Twist::twist_vec(&self.use_decls)),
                Twist::barr("defs".to_string(), Twist::twist_vec(&self.defs)),
            ],
        )
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct UseDecl {
    pub module_name: Identifier,
    pub names: Option<Vec<Symbol>>,
}

impl Twistable for UseDecl {
    fn twist(&self) -> Twist {
        Twist::arr(
            self.module_name.render(),
            self.names
                .iter()
                .map(|it| -> Box<dyn Twistable> { Box::new(Twist::leaf(it.render())) })
                .collect::<Vec<Box<dyn Twistable>>>(),
        )
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub enum Definition {
    Q(QuarkDef),
    B(BosonDef),
    F(FlavorDef),
}

impl Twistable for Definition {
    fn twist(&self) -> Twist {
        match self {
            Self::Q(q) => q.twist(),
            Self::B(b) => b.twist(),
            Self::F(f) => f.twist(),
        }
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct QuarkDef {
    pub id: Symbol,
    pub type_params: Option<Vec<TypeParamSpec>>,
    pub value_params: Vec<ValueParamSpec>,
    pub provides: Vec<Type>,
    pub slots: Vec<SlotDecl>,
    pub behaviors: Vec<BehaviorDecl>,
    pub channels: Vec<ChannelDecl>,
    pub initial_beh: (Symbol, Vec<Expr>),
}

impl Twistable for QuarkDef {
    fn twist(&self) -> Twist {
        Twist::obj(
            "QuarkDef".to_string(),
            vec![
                Twist::battr("name".to_string(), &self.id.render()),
                Twist::twist_optvec("type_params".to_string(), &self.type_params),
                Twist::barr(
                    "value_params".to_string(),
                    Twist::twist_vec(&self.value_params),
                ),
                Twist::barr("provides".to_string(), Twist::twist_vec(&self.provides)),
                Box::new(Twist::arr(
                    "slots".to_string(),
                    Twist::twist_vec(&self.slots),
                )),
                Box::new(Twist::arr(
                    "behaviors".to_string(),
                    Twist::twist_vec(&self.behaviors),
                )),
                Box::new(Twist::arr(
                    "channels".to_string(),
                    Twist::twist_vec(&self.channels),
                )),
                Box::new(Twist::obj(
                    "initial_beh".to_string(),
                    vec![
                        Twist::battr("behavior".to_string(), &self.initial_beh.0.render()),
                        Twist::barr("args".to_string(), Twist::twist_vec(&self.initial_beh.1)),
                    ],
                )),
            ],
        )
    }
}

#[derive(Clone, Eq, PartialEq, Debug, Hash)]
pub enum Direction {
    In,
    Out,
    Both,
}

impl Renderable for Direction {
    fn render(&self) -> String {
        match self {
            Direction::In => "In".to_string(),
            Direction::Out => "Out".to_string(),
            Direction::Both => "Both".to_string(),
        }
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct ChannelDecl {
    pub id: Symbol,
    pub channel_type: ChannelType,
}

impl Twistable for ChannelDecl {
    fn twist(&self) -> Twist {
        Twist::obj(
            "channel_decl".to_string(),
            vec![
                Twist::battr("id".to_string(), &self.id),
                Twist::bval("channel_type".to_string(), self.channel_type.btwist()),
            ],
        )
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct TypeParamSpec {
    pub type_var: TypeVar,
    pub constraint: Option<Type>,
}

impl Twistable for TypeParamSpec {
    fn twist(&self) -> Twist {
        Twist::obj(
            "type_param".to_string(),
            vec![
                Twist::battr("typevar".to_string(), &self.type_var.render()),
                Twist::bopt(
                    &(self
                        .constraint
                        .as_ref()
                        .map(|t| -> Box<dyn Twistable> { Box::new(t.twist()) })),
                ),
            ],
        )
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct ValueParamSpec {
    pub id: Symbol,
    pub value_type: Type,
}

impl Twistable for ValueParamSpec {
    fn twist(&self) -> Twist {
        Twist::obj(
            "value_param".to_string(),
            vec![
                Twist::battr("id".to_string(), &self.id),
                Twist::bval("value_type".to_string(), self.value_type.btwist()),
            ],
        )
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct SlotDecl {
    pub id: Symbol,
    pub value_type: Type,
    pub init_value: Expr,
}

impl Twistable for SlotDecl {
    fn twist(&self) -> Twist {
        Twist::obj(
            "slot_decl".to_string(),
            vec![
                Twist::battr("id".to_string(), &self.id),
                Twist::bval("value_type".to_string(), self.value_type.btwist()),
            ],
        )
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct BehaviorDecl {
    pub id: Symbol,
    pub params: Vec<ValueParamSpec>,
    pub actions: Vec<Box<Action>>,
}

impl Twistable for BehaviorDecl {
    fn twist(&self) -> Twist {
        Twist::obj(
            "behavior_decl".to_string(),
            vec![
                Twist::battr("id".to_string(), &self.id),
                Twist::barr("params".to_string(), Twist::twist_vec(&self.params)),
                Twist::barr(
                    "actions".to_string(),
                    self.actions
                        .iter()
                        .map(|act| -> Box<dyn Twistable> { act.btwist() })
                        .collect::<Vec<Box<dyn Twistable>>>(),
                ),
            ],
        )
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct FlavorDef {
    pub id: Symbol,
    pub type_params: Option<Vec<TypeParamSpec>>,
    pub composes: Option<Vec<Type>>,
    pub channels: Vec<ChannelDecl>,
}

impl Twistable for FlavorDef {
    fn twist(&self) -> Twist {
        Twist::obj(
            "FlavorDef".to_string(),
            vec![
                Twist::battr("id".to_string(), &self.id),
                Twist::bopt(&self.type_params.as_ref().map(|it| -> Box<dyn Twistable> {
                    Twist::barr("type_params".to_string(), Twist::twist_vec(&it))
                })),
                Twist::bopt(&self.composes.as_ref().map(|it| -> Box<dyn Twistable> {
                    Twist::barr("composes".to_string(), Twist::twist_vec(&it))
                })),
                Twist::barr("channels".to_string(), Twist::twist_vec(&self.channels)),
            ],
        )
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct BosonDef {
    pub id: Symbol,
    pub type_params: Option<Vec<TypeParamSpec>>,
    pub options: Vec<BosonOptionDecl>,
}

impl Twistable for BosonDef {
    fn twist(&self) -> Twist {
        Twist::obj(
            "BosonDef".to_string(),
            vec![
                Twist::battr("id".to_string(), &self.id),
                Twist::twist_optvec("type_params".to_string(), &self.type_params),
                Twist::barr("options".to_string(), Twist::twist_vec(&self.options)),
            ],
        )
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub enum BosonOptionDecl {
    TupleOptionDecl {
        id: Symbol,
        value_types: Vec<Type>,
    },
    StructOptionDecl {
        id: Symbol,
        field_types: Vec<ValueParamSpec>,
    },
}
impl Twistable for BosonOptionDecl {
    fn twist(&self) -> Twist {
        match self {
            Self::TupleOptionDecl { id, value_types } => Twist::obj(
                "TupleOptionDecl".to_string(),
                vec![
                    Twist::battr("id".to_string(), id),
                    Twist::barr("value_types".to_string(), Twist::twist_vec(value_types)),
                ],
            ),

            Self::StructOptionDecl { id, field_types } => Twist::obj(
                "StructOptionDecl".to_string(),
                vec![
                    Twist::battr("name".to_string(), id),
                    Twist::barr("values".to_string(), Twist::twist_vec(field_types)),
                ],
            ),
        }
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct ChannelType {
    pub value_type: Type,
    pub direction: Direction,
}

impl Twistable for ChannelType {
    fn twist(&self) -> Twist {
        Twist::obj(
            "ChannelType".to_string(),
            vec![
                Twist::battr("dir".to_string(), &self.direction.render()),
                Twist::bval("value_type".to_string(), self.value_type.btwist()),
            ],
        )
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub enum Action {
    Par(Vec<Box<Action>>),
    Seq(Vec<Box<Action>>),
    Select(Vec<Box<Action>>),
    Send {
        target: Expr,
        value: Expr,
    },
    Recv {
        source: Expr,
        clauses: Vec<ReceiveClause>,
        else_clause: Option<Vec<Box<Action>>>,
    },
    Assign {
        target: Symbol,
        value: Expr,
    },
    VarDecl {
        id: Symbol,
        var_type: Type,
        init_value: Expr,
    },
    CondAction {
        clauses: Vec<(Expr, Vec<Box<Action>>)>,
        else_clause: Option<Vec<Box<Action>>>,
    },
    WhileAction {
        cond: Expr,
        body: Vec<Box<Action>>,
    },
    ForAction {
        id: Symbol,
        range: Expr,
        body: Vec<Box<Action>>,
    },
    AdoptAction {
        id: Symbol,
        arguments: Vec<Expr>,
    },
    ExitAction,
}

impl Twistable for Action {
    fn twist(&self) -> Twist {
        match self {
            Action::Par(acts) => Twist::obj("ParExpr".to_string(), Twist::twist_bvec(acts)),
            Action::Seq(acts) => Twist::obj("SeqExpr".to_string(), Twist::twist_bvec(acts)),
            Action::Select(acts) => Twist::obj("SelectExpr".to_string(), Twist::twist_bvec(acts)),
            Action::Send { target, value } => Twist::obj(
                "SendAction".to_string(),
                vec![
                    Twist::bval("target".to_string(), target.btwist()),
                    Twist::bval("value".to_string(), value.btwist()),
                ],
            ),
            Action::Recv {
                source,
                clauses,
                else_clause,
            } => Twist::obj(
                "RecvAction".to_string(),
                vec![
                    Twist::bval("source".to_string(), source.btwist()),
                    Twist::barr("clauses".to_string(), Twist::twist_vec(clauses)),
                    Twist::twist_optbvec("else".to_string(), else_clause),
                ],
            ),
            Action::Assign { target, value } => Twist::obj(
                "AssignAction".to_string(),
                vec![
                    Twist::battr("target".to_string(), &target.render()),
                    Twist::bval("value".to_string(), value.btwist()),
                ],
            ),
            Action::VarDecl {
                id,
                var_type,
                init_value,
            } => Twist::obj(
                "VarDeclAction".to_string(),
                vec![
                    Twist::battr("id".to_string(), &id.render()),
                    Twist::bval("type".to_string(), var_type.btwist()),
                    Twist::bval("init_value".to_string(), init_value.btwist()),
                ],
            ),
            Action::CondAction {
                clauses,
                else_clause,
            } => Twist::obj(
                "CondAction".to_string(),
                vec![
                    Twist::barr(
                        "cond_clauses".to_string(),
                        clauses
                            .iter()
                            .map(|c| -> Box<dyn Twistable> {
                                match c {
                                    (condition, actions) => Twist::bobj(
                                        "clause".to_string(),
                                        vec![
                                            Twist::bval("cond".to_string(), condition.btwist()),
                                            Twist::barr(
                                                "actions".to_string(),
                                                Twist::twist_bvec(actions),
                                            ),
                                        ],
                                    ),
                                }
                            })
                            .collect::<Vec<Box<dyn Twistable>>>(),
                    ),
                    Twist::twist_optbvec("else_clause".to_string(), else_clause),
                ],
            ),
            Action::WhileAction { cond, body } => Twist::obj(
                "WhileAction".to_string(),
                vec![
                    Twist::bval("cond".to_string(), cond.btwist()),
                    Twist::barr("body".to_string(), Twist::twist_bvec(body)),
                ],
            ),
            Action::ForAction { id, range, body } => Twist::obj(
                "ForAction".to_string(),
                vec![
                    Twist::battr("id".to_string(), &id.render()),
                    Twist::bval("range".to_string(), range.btwist()),
                    Twist::barr("body".to_string(), Twist::twist_bvec(body)),
                ],
            ),
            Action::AdoptAction { id, arguments } => Twist::obj(
                "AdoptAction".to_string(),
                vec![
                    Twist::battr("name".to_string(), &id.render()),
                    Twist::barr("args".to_string(), Twist::twist_vec(arguments)),
                ],
            ),
            Action::ExitAction => Twist::leaf("ExitAction".to_string()),
        }
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub enum Type {
    Named {
        base_type_id: Identifier,
        type_arguments: Option<Vec<Box<Type>>>,
    },
    ChannelType(Box<Type>, Direction),
    Var(TypeVar),
}

impl Twistable for Type {
    fn twist(&self) -> Twist {
        match self {
            Self::Named {
                base_type_id,
                type_arguments,
            } => Twist::obj(
                "NamedType".to_string(),
                vec![
                    Twist::battr("base_type_id".to_string(), &base_type_id.render()),
                    Twist::twist_optbvec("type_arguments".to_string(), type_arguments),
                ],
            ),
            Self::ChannelType(val_type, dir) => Twist::obj(
                "ChannelType".to_string(),
                vec![
                    val_type.btwist(),
                    Twist::battr("dir".to_string(), &dir.render()),
                ],
            ),
            Self::Var(t) => Twist::attr("TypeVar".to_string(), t.render()),
        }
    }
}

#[derive(Clone, Eq, PartialEq, Debug, Hash)]
pub enum Operator {
    OpPlus,
    OpMinus,
    OpTimes,
    OpDiv,
    OpMod,
    OpGt,
    OpLt,
    OpGe,
    OpLe,
    OpEq,
    OpNotEq,
    OpNot,
    OpAnd,
    OpOr,
}

impl Renderable for Operator {
    fn render(&self) -> String {
        match self {
            Operator::OpPlus => "Plus",
            Operator::OpMinus => "Minus",
            Operator::OpTimes => "Times",
            Operator::OpDiv => "Div",
            Operator::OpMod => "Mod",
            Operator::OpGt => "GreaterThan",
            Operator::OpLt => "LessThan",
            Operator::OpGe => "GreaterEqual",
            Operator::OpLe => "LessEqual",
            Operator::OpEq => "Equals",
            Operator::OpNotEq => "NotEqual",
            Operator::OpNot => "Not",
            Operator::OpAnd => "And",
            Operator::OpOr => "Or",
        }
        .to_string()
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct ReceiveClause {
    pub pattern: MessagePattern,
    pub actions: Vec<Box<Action>>,
}
impl Twistable for ReceiveClause {
    fn twist(&self) -> Twist {
        Twist::obj(
            "ReceiveClause".to_string(),
            vec![
                Twist::bval("pattern".to_string(), self.pattern.btwist()),
                Twist::barr("actions".to_string(), Twist::twist_bvec(&self.actions)),
            ],
        )
    }
}

#[derive(Clone, Eq, PartialEq, Debug, Hash)]
pub enum MessagePattern {
    TuplePattern(Vec<Symbol>),
    StructPattern(Vec<(Symbol, Symbol)>),
}
impl Twistable for MessagePattern {
    fn twist(&self) -> Twist {
        match self {
            MessagePattern::TuplePattern(syms) => Twist::obj(
                "TuplePattern".to_string(),
                syms.iter()
                    .map(|s| -> Box<dyn Twistable> { Twist::bleaf(s.render()) })
                    .collect::<Vec<Box<dyn Twistable>>>(),
            ),
            MessagePattern::StructPattern(pairs) => Twist::obj(
                "StructPattern".to_string(),
                pairs
                    .iter()
                    .map(|(k, v)| -> Box<dyn Twistable> { Twist::battr(k.to_string(), v) })
                    .collect::<Vec<Box<dyn Twistable>>>(),
            ),
        }
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub enum Expr {
    VariableExpr(Symbol),
    OperatorExpr {
        op: Operator,
        args: Vec<Box<Expr>>,
    },
    CreateQuarkExpr {
        quark_type: Type,
        args: Vec<Box<Expr>>,
    },
    BosonTupleExpr {
        tuple_type: Identifier,
        type_args: Option<Vec<Box<Type>>>,
        args: Option<Vec<Box<Expr>>>,
    },
    BosonStructExpr {
        boson_type: Identifier,
        type_args: Option<Vec<Box<Type>>>,
        args: Vec<(String, Box<Expr>)>,
    },
    LitStringExpr(String),
    LitIntExpr(i64),
    LitFloatExpr(String),
    LitCharExpr(char),
    //    ListLitExpr(Vec<Box<Expr>>),
    ChannelExpr(Box<Expr>, Symbol),
}

impl Twistable for Expr {
    fn twist(&self) -> Twist {
        match self {
            Expr::VariableExpr(v) => Twist::attr("VariableExpr".to_string(), v.render()),
            Expr::OperatorExpr { op, args } => Twist::obj(
                "OperatorExpr".to_string(),
                vec![
                    Twist::battr("operator".to_string(), &op.render()),
                    Twist::barr("arguments".to_string(), Twist::twist_bvec(args)),
                ],
            ),
            Expr::CreateQuarkExpr { quark_type, args } => Twist::obj(
                "CreateQuarkExpr".to_string(),
                vec![
                    Twist::bval("quark_type".to_string(), quark_type.btwist()),
                    Twist::barr("arguments".to_string(), Twist::twist_bvec(args)),
                ],
            ),
            Expr::BosonTupleExpr {
                tuple_type,
                type_args,
                args,
            } => Twist::obj(
                "BosonTupleExpr".to_string(),
                vec![
                    Twist::battr("tuple_type".to_string(), &tuple_type.render()),
                    Twist::twist_optbvec("type_args".to_string(), type_args),
                    Twist::twist_optbvec("value_args".to_string(), args),
                ],
            ),
            Expr::BosonStructExpr {
                boson_type,
                type_args,
                args,
            } => Twist::obj(
                "BosonStructExpr".to_string(),
                vec![
                    Twist::battr("struct_type".to_string(), &boson_type.render()),
                    Twist::twist_optbvec("type_args".to_string(), type_args),
                    Twist::barr(
                        "value_args".to_string(),
                        args.iter()
                            .map(|(k, v)| -> Box<dyn Twistable> {
                                Twist::bval(k.to_string(), v.btwist())
                            })
                            .collect::<Vec<Box<dyn Twistable>>>(),
                    ),
                ],
            ),
            Expr::LitStringExpr(s) => Twist::attr("StringLit".to_string(), format!("\"{}\"", s)),
            Expr::LitIntExpr(i) => Twist::attr("IntLit".to_string(), i.to_string()),
            Expr::LitFloatExpr(f) => Twist::attr("FloatLit".to_string(), f.to_string()),
            Expr::LitCharExpr(c) => Twist::attr("CharLit".to_string(), format!("'{}'", c)),
            Expr::ChannelExpr(owner, name) => Twist::obj(
                "ChannelExpr".to_string(),
                vec![owner.btwist(), Twist::battr("name".to_string(), name)],
            ),
        }
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub struct LValue(pub Identifier);

impl Twistable for LValue {
    fn twist(&self) -> Twist {
        Twist::attr("LValue".to_string(), self.0.render())
    }
}

#[derive(Clone, PartialEq, Debug, Hash)]
pub enum TempBosonParam {
    T(Vec<Type>),
    S(Vec<ValueParamSpec>),
}
