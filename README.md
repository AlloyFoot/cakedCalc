# cakedCalc

A command-line calculator in Java. Type a math expression, or plain-English phrasing, and get the answer.

```
> 2 + 3 * 4
14
> what is 12 plus 8 times 2
28
> what is the square root of 16?
4
> (2 + 3) * 4
20
```

## Features

- Operators: `+  -  *  /  %  ^` with correct precedence
- Parentheses and unary minus: `-(2 + 3)`, `3 * -2`
- Functions: `sqrt`, `abs`, `sin`, `cos`, `tan`, `log`, `ln`, `round`, `asin`, `acos`, `atan`, `exp`
- Constants: `pi`, `e`
- Plain-English input: `plus`, `minus`, `times`, `divided by`, `mod`, `squared`, `cubed`, `to the power of`
- Function phrases: `square root of 16`, `absolute value of -5`, `sine of 30`
- Readable error messages instead of crashes
- Implicit multiplication: `2pi`, `2(3 + 4)`, `(1 + 2)(3 + 4)`
- `ans` reuses the last result: `6 * 7` then `ans + 1`
- Implicit multiplication: `2pi`, `2(3 + 4)`, `(1 + 2)(3 + 4)`

## Requirements

- Java 17 or newer (developed on Java 21)

## Build and run

```
javac src/CakedCalc.java
java -cp src CakedCalc
```

Type `quit` to exit.

## How it works

cakedCalc is a recursive descent parser. Input is first normalized (English words become symbols), then parsed by a chain of methods where each one handles a tighter-binding level of the grammar:

```
expression := term (('+' | '-') term)*
term       := unary (('*' | '/' | '%') unary)*
unary      := ('-' | '+') unary | power
power      := primary ('^' unary)?
primary    := number | '(' expression ')' | name | name '(' expression ')'
```

## Known limits

- Trig functions use **degrees**, not radians.
- Numbers must be digits. Spelled-out numbers like "twelve" are not supported.
- Function phrases grab one number or one parenthesized group: `square root of 16 plus 9` means `sqrt(16) + 9`. Use parentheses for `sqrt(16 + 9)`.
- Floating-point rounding applies, so `sin(30)` prints `0.49999999999999994`.
- Implicit multiplication is evaluated left to right, so `10 / 2pi` means `(10 / 2) * pi`. Use parentheses for `10 / (2pi)`.
- Scientific notation like `2e3` is not supported.
- `ans` is only remembered during the current session.
- Implicit multiplication is evaluated left to right, so `10 / 2pi` means `(10 / 2) * pi`. Use parentheses for `10 / (2pi)`.
- Scientific notation like `2e3` is not supported.

## Author

Made by Ved Patel for Hack Club.