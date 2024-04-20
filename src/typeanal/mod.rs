use std::collections::HashMap;

use crate::ast::*;

/*
* The idea here is that during type analysis, we
* create an instantiation for each static use of
* a definition. Those are the only places where
* a new type binding could be introduced. (But note
* that a new instantiation can happen in an expression
* or action, so it's not just top-level declarations
* that we need to worry about.)
*
* When we find an instantiation, we create an instantiated
* object corresponding to it, and we create bindings for
* its typevars based on the parameters supplied for it,
* plus any bindings that they, in turn, might have inherited.
*
* Each instantion, thus, has a type context - the
* scope in which it was instantiated.
*
* So, for example, image we have a quark type
* T[Q, R]. Inside of one of its behaviors, it creates
* a boson Cons[X] as a Cons[Q]. We instantiate T as
* T[Int, String].
*
* What happens is:
* 1. When we instantiate T, we create new instantiated
*   type vars for its type params, Q_N, R_N, and bind them
*   to the bare type variables Q and R.
* 2. Then we create all of the things instantiated inside
*   of T, so we instantiate Cons[Q].
* 3. Initially, the instantiation of Cons would create its
*   own instantiated type var, X_O, and bind it to X.
* 4. Then we'd start to bind from the bottom up - so first,
*   we'd do the type binding of X_O, and bind it to
*   Q_N. That means that every binding to X_O will be replaced
*   with a binding to Q_N. So now the X in our instantiated
*   Cons[X] is bound to Q_N, and X_0 is gone.
* 5. Moving upwards, we'd know that in our instantiation of
*   T, Q_N should be bound to the concrete type Int,
*   and R_N should be bound to the concrete type String.
*   So we go through, replacing every reference to Q_N
*   with a reference to the concrete type Int. Now our instantiated
*   tree has no uses of an unbound type variable.
*
* What makes this tricky, even with that stuff worked out, is
* rust ownership. Who owns:
* - the AST nodes?
* - the instantiated type vars?
* - the instantiated definition nodes?
* - the type variable bindings? - I think this is the context, which maintains
*     the full set of type variable to type mappings.
*
* If the bindings wind up in the context, then everything else except
* for the AST nodes probably also should be owned by the context. But
* the original AST nodes clearly shouldn't be part of the context.
*
* I think that means that we want some kind of object representing the
* compiler state, which owns both the ASTs and the TypeAnalysisContext.
*/

#[derive(Clone, PartialEq, Hash, Debug)]
pub enum InstantiatedType {
    ConcreteType(Type),
    UnboundType(InstantiatedTypeVar),
}

pub struct TypeAnalysisContext<'a> {
    type_var_indices: HashMap<TypeVar, u32>,
    instantiated_type_vars: Vec<InstantiatedTypeVar>,
    bindings: HashMap<&'a InstantiatedTypeVar, InstantiatedType>,
}

/// Each time a parameter type is instantiated, we need
/// to create a new, unique identifier for its type.
/// This is just a little utility to help us generate those
/// unique instantiated type variables.
///

impl<'a> TypeAnalysisContext<'a> {
    pub fn new() -> TypeAnalysisContext<'a> {
        TypeAnalysisContext {
            type_var_indices: HashMap::new(),
            instantiated_type_vars: Vec::new(),
            bindings: HashMap::new(),
        }
    }

    pub fn instantiate_type_var(&'a mut self, t: &TypeVar) -> InstantiatedTypeVar {
        let idx = self.type_var_indices.get_mut(t);
        let name = match idx {
            Some(idx) => {
                let varname = format!("{}__{}", t.s, *idx + 1);
                *idx = 1;
                varname
            }
            None => {
                self.type_var_indices.insert(t.clone(), 1);
                format!("{}__{}", t.s, 1)
            }
        };
        let result = InstantiatedTypeVar {
            original_name: t.clone(),
            instantiated_name: name,
        };
        self.instantiated_type_vars.push(result.clone());
        result
    }
}

#[derive(Hash, PartialEq, Debug, Clone)]
pub struct InstantiatedTypeVar {
    original_name: TypeVar,
    instantiated_name: Symbol,
}

trait Instantiated {
    fn get_uninstantiated_type_vars(&self, context: &mut TypeAnalysisContext) -> Vec<TypeVar>;
    fn get_unbound_type_vars(&self, context: &mut TypeAnalysisContext) -> Vec<InstantiatedTypeVar>;
    fn bind_type_vars(&mut self, new_bindings: Vec<(InstantiatedTypeVar, InstantiatedTypeVar)>);
}

struct InstantiatedDef<'a> {
    pub base_node: &'a Definition,
    pub instantiated_typevars: Vec<&'a InstantiatedTypeVar>,
}
