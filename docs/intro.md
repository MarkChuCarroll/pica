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
make up the atoms around us are formed of collections of quarks. In Pica, a quark is
some defined thing that can run a variety of snippets of parallel code. It
does small bits of local computation, and then sends messages to other quarks.

Those messages are called bosons. In physics, Bosons are subatomic particles
with mediate interactions between quarks. All of the fundamental forces that
make protons and neutrons exist as particles, and stick together to form
the matter that we see around us, are mediated by bosons.

So in Pica, we've implement quarks which interact with each other by
sending and receiving bosons.

The underlying system that runs Pica programs is the
Hadron virtual machine. (Hadrons are the family of particles
with mass that make up matter.)

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
  using the state, send messages, and recieve messages.

## Boson Basics



## Example

This is a fragment of what a lexical scanner could look like
in Pica. We've got an input stream, which is a quark that
sends a boson containing characters to a channel as it reads them, and sends
an end boson when it reaches the end.

The scanner is a quark that receives characters from the input
stream, and assembles them into a token. When it reads a complete
token, it sends it as a boson on its output channel.

```
boson ScannerOutput
    Token{type: String, content: String, line: Int}
or  EndOfStream(Unit)
or  ScanError{message: String, line: Int}
end

quark Scanner(in: InputStream)
  state
     slot input: InputStream = in
     slot currentToken: String = ""
  channels
     chan output: ScannerOutput

  action
    repeat
        ?input(c)
            on More(c) do
                currentToken += c;
                if ... then
                    !output(Token{type: "t", content: currentToken, line: ...});
                    currentToken = ""
                end
            end
            on End(u) do
                !output(Token{type: "t", content: currentToken, line: ...});
                !output(EndOfStream(Unit);
                exit
            end
        end
    end
end
```