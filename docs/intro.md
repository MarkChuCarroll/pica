# What is Pica?

All of the programming languages that we use, day to day, were designed
primarily for sequential programming. Most of them now include some kind
of threading library, but that's almost always an afterthought - it might
be a really good threading system, but even so, the bulk of the language is
designed for sequential programming.

Modern languages are all about either objects or functions. In both cases,
the basic model that programmer understand is fundamentally sequential.

And yet, modern computers aren't really sequential anymore. We've gotten to
the point where the way that we improve speed isn't by making faster
processors, it's by making _more_ processors. But our programs aren't
really built to take advantage of that.

Pica is an attempt to explore what it would be like to program,
from the ground up, in a system that's massively multi-threaded,
where the fundamental operations are all built around the idea
of parallel processing and message passing.

## Some Goofy Terminology

The fundamental constructs in Pica are new. In the pi calculus, which
Pica is loosely based on, they're called processes. But processes
in the pi calculus aren't what most computer scientists or engineers
understand when they hear the word process. So I didn't want to call
them processes. Similarly, the other normal terms - objects, threads,
fibers, actors - they're all strongly loaded with a semantic model,
which isn't the same as what Pica provides. So I decided to just
go for something new, which sounds cool, and which makes sense to me.

So - the fundamental unit of computation is called a _quark_, after the
fundamental building block of physical matter. All of the particles that
make up the atoms around us are formed of collections of quarkSpecs. In Pica, a quark is
some defined thing that can run a variety of snippets of parallel code. It
does small bits of local computation, and then sends messages to other quarkSpecs.

Those messages are called bosonSpecs. In physics, Bosons are subatomic particles
with mediate interactions between quarkSpecs. All of the fundamental forces that
make protons and neutrons exist as particles, and stick together to form
the matter that we see around us, are mediated by bosonSpecs.

So in Pica, we've implement quarkSpecs which interact with each other by
sending and receiving bosonSpecs.

The underlying system that runs Pica programs is the
QGP VM, which stands for "Quark Gluon Plasma", a recently observed
state of matter where quarkSpecs and gluons interact freely.

## Quark Basics

Quarks are the fundamental unit of computation in Pica. You can think of them as
the pica equivalent of a function: an abstract computation encapsulated in a unit.

A quark consists of three parts:

* State: a collection of typed variables that can be retrieved or modified
  by actions.
* Channels: a collection of _named_ communication ports. Each channel
  has a specified _boson_ type that can be sent and received on the
  channel. A quark that gets access to a channel can both send _and_ receive
  messages on that channel. There's no way to build a one-way channel.
* Actions: a quark specifies its behavior using an action. An action consists
  of a collection of parallel sub-actions, which can perform local computation
  using the state, send messages, and receive messages.

## Boson Basics

Bosons are just simple data types - essentially nothing more than named tuples.
Unlike an object-oriented language, there are virtually no behaviors associated with bosonSpecs. They're just
completely passive data. A boson type is very similar to an algebraic
type in a language like OCaml. The type has a name, which is used in
type declarations; and it has a list of named type constructors. Each
type constructor has a different list of arguments, which can be either
positional ("tuple bosonSpecs") or named ("struct bosonSpecs").

For convenience, Pica provides a list boson, which is basically just
a lisp-style cons list with added syntax. It's basically:

```
boson [T]List is
   Cons(T, [T]List)
or Nil(Unit)
end
```

You can write a list using a list expression, which is automatically
parsed and transformed into cons format.

```
[1, 2, 3]: [Int]List == Cons(1, Cons(2, Cons(3, Nil(unit))))
```


## Example

This is a fragment of what a lexical scanner could look like
in Pica. We've got an input stream, which is a quark that
sends a boson containing characters to a channel as it reads them, and sends
an end boson when it reaches the end.

The scanner is a quark that receives characters from the input
stream, and assembles them into a token. When it reads a complete
token, it sends it as a boson on its output channel.

```
boson ScannerOutput is
    Token{type: String, content: String, line: Int}
|  EndOfStream(Unit)
|  ScanError{message: String, line: Int}
end

boson InputStreamEvent is
  More(Char)
| End()
end

quark InputStream(path: String)
  channels
    chan chars: InputStreamEvent
  ...
end


quark Scanner(in: InputStream) is
     chan output: ScannerOutput
     slot input: InputStream = in.chars
     slot currentToken: String = ""
     do
        rec input do
            on More(c) do
                currentToken += c;
                if ... then
                    send output(Token{type: "t", content: currentToken, line: ...});
                    currentToken = ""
                end
            end
            on End(u) do
                send output(Token{type: "t", content: currentToken, line: ...});
                send output(EndOfStream(Unit);
                exit
            end
        end
    end
end
```


## Quarks

A quark is the fundamental construct of Pica. It's based on an action
from the synchronous &pi;-calculus. It's basically a lightweight thread
that communicates with other quarks by passing messages. The only way that
a quark can do anything is by sending or receiving messages.

A quark declaration includes:
* a set of _slots_ which contain the quark's state;
* a list of parameters, which are used to initialize the quark's slots;
* a set of named _channels_ which are used to send and receive messages.
  The quark's behaviors may create other channels, but they'll only be
  accessible to other quarks if they're sent via a message; the channels 
  from the quark declaration are publicly visible.
* a set of _behaviors_ which define how the quark communicates with other
  quarks in the program. A behavior can take a set of parameters, and it
  defines a set of _actions_ which the quark will execute.
* A _body_, which is just the default behavior that the quark runs when it's 
  created.

### Actions

