pub enum Twist {
    Object {
        name: String,
        children: Vec<Box<dyn Twistable>>,
    },
    Array {
        name: String,
        children: Vec<Box<dyn Twistable>>,
    },
    Attr {
        name: String,
        value: String,
    },
    Value {
        name: String,
        value: Box<dyn Twistable>,
    },
    Leaf(String),
    Opt(Option<Box<dyn Twistable>>),
}

impl Twist {
    pub fn obj(name: String, children: Vec<Box<dyn Twistable>>) -> Twist {
        Twist::Object {
            name: name,
            children: children,
        }
    }

    pub fn arr(name: String, children: Vec<Box<dyn Twistable>>) -> Twist {
        Twist::Array {
            name: name,
            children: children,
        }
    }

    pub fn attr(name: String, v: String) -> Twist {
        Twist::Attr {
            name: name,
            value: v,
        }
    }

    pub fn val(name: String, value: Box<dyn Twistable>) -> Twist {
        Twist::Value {
            name: name,
            value: value,
        }
    }

    pub fn opt(v: &Option<Box<dyn Twistable>>) -> Twist {
        Twist::Opt(
            v.as_ref()
                .map(|o| -> Box<dyn Twistable> { Box::new(o.twist()) }),
        )
    }

    pub fn leaf(s: String) -> Twist {
        Twist::Leaf(s)
    }

    pub fn bobj(name: String, children: Vec<Box<dyn Twistable>>) -> Box<Twist> {
        Box::new(Twist::Object {
            name: name,
            children: children,
        })
    }

    pub fn barr(name: String, children: Vec<Box<dyn Twistable>>) -> Box<Twist> {
        Box::new(Twist::Array {
            name: name,
            children: children,
        })
    }

    pub fn battr(name: String, v: &str) -> Box<Twist> {
        Box::new(Twist::Attr {
            name: name,
            value: v.to_owned(),
        })
    }

    pub fn bval(name: String, value: Box<dyn Twistable>) -> Box<Twist> {
        Box::new(Twist::Value {
            name: name,
            value: value,
        })
    }

    pub fn bopt(v: &Option<Box<dyn Twistable>>) -> Box<Twist> {
        Box::new(Self::opt(v))
    }

    pub fn bleaf(s: String) -> Box<Twist> {
        Box::new(Twist::Leaf(s))
    }

    pub fn indent(levels: usize) -> String {
        "  ".repeat(levels)
    }

    pub fn render(&self, depth: usize) -> String {
        match self {
            Twist::Object { name, children } => {
                let mut result = Twist::indent(depth) + "object " + name + ":\n";
                for c in children {
                    result += &c.twist().render(depth + 1)
                }
                result
            }
            Twist::Array { name, children } => {
                let mut result = Twist::indent(depth) + "array " + name + ":\n";
                for c in children {
                    result += &c.twist().render(depth + 1)
                }
                result
            }
            Twist::Attr { name, value } => Twist::indent(depth) + name + "=" + value + "\n",
            Twist::Value { name, value } => {
                let result = Twist::indent(depth) + name + ":\n";
                result + &value.twist().render(depth + 1)
            }
            Twist::Leaf(v) => Twist::indent(depth) + v + "\n",
            Twist::Opt(o) => match o {
                Some(o) => o.twist().render(depth),
                None => "".to_string(),
            },
        }
    }

    pub fn twist_optvec(name: String, v: &Option<Vec<impl Twistable>>) -> Box<dyn Twistable> {
        Twist::bopt(
            &v.as_ref()
                .map(|it| -> Box<dyn Twistable> { Twist::barr(name, Twist::twist_vec(&it)) }),
        )
    }

    pub fn twist_optbvec(name: String, v: &Option<Vec<Box<impl Twistable>>>) -> Box<dyn Twistable> {
        Twist::bopt(
            &v.as_ref()
                .map(|it| -> Box<dyn Twistable> { Twist::barr(name, Twist::twist_bvec(&it)) }),
        )
    }

    pub fn twist_vec<T: Twistable>(v: &Vec<T>) -> Vec<Box<dyn Twistable>> {
        v.iter()
            .map(|it| -> Box<dyn Twistable> { Box::new(it.twist()) })
            .collect::<Vec<Box<dyn Twistable>>>()
    }

    pub fn twist_bvec<T: Twistable>(v: &Vec<Box<T>>) -> Vec<Box<dyn Twistable>> {
        v.iter()
            .map(|it| -> Box<dyn Twistable> { Box::new(it.twist()) })
            .collect::<Vec<Box<dyn Twistable>>>()
    }
}

impl Clone for Twist {
    fn clone(&self) -> Self {
        match self {
            Self::Object { name, children } => Self::Object {
                name: name.clone(),
                children: children
                    .iter()
                    .map(|c| -> Box<dyn Twistable> { Box::new(c.twist()) })
                    .collect::<Vec<Box<dyn Twistable>>>(),
            },
            Self::Array { name, children } => Self::Array {
                name: name.clone(),
                children: children
                    .iter()
                    .map(|c| -> Box<dyn Twistable> { Box::new(c.twist()) })
                    .collect::<Vec<Box<dyn Twistable>>>(),
            },
            Self::Attr { name, value } => Self::Attr {
                name: name.clone(),
                value: value.clone(),
            },
            Self::Value { name, value } => Self::Value {
                name: name.clone(),
                value: Box::new(value.twist()),
            },
            Self::Leaf(v) => Self::Leaf(v.clone()),
            Self::Opt(v) => match v {
                Some(it) => Self::Opt(Some(Box::new(it.twist()))),
                None => Self::Opt(None),
            },
        }
    }
}

pub trait Twistable {
    fn twist(&self) -> Twist;

    fn btwist(&self) -> Box<Twist> {
        Box::new(self.twist())
    }
}

impl Twistable for Twist {
    fn twist(&self) -> Twist {
        self.to_owned()
    }
}
pub trait Renderable {
    fn render(&self) -> String;
}
