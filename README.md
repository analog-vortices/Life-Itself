# Life Itself
A simple framework for visualizing the interactions between and development of, cellular cultures. 

![Demo](https://github.com/analog-vortices/Life-Itself/blob/master/demo.gif)

## Requirements
 - JDK 8+
 - Gradle 4.8+

## Building
`./gradlew build`

## Running
`./gradlew run`

## Usage
#### Websocket:
A websocket will be opened on `0.0.0.0:9999` when the application is started, allowing you to remotely control the simulation.

| Command | Action |
| :--- | :--- |
| `set x y` | Set the cell on the board at `(x, y)` |
| `unset x y` | Unset the cell on the board at `(x, y)` |
| `boardClear` | Clear the board with unset cells |
| `boardRandomize` | Randomize the board |
| `pause` | Pause the simulation |
| `play` | Play the simulation |
| `nextMode` | Switch the simulation type |
| `increaseSpeed` | Increase the speed of the simulation |
| `decreaseSpeed` | Decrease the speed of the simulation |
| `setColour r g b` | Set the cell colour to `(r, g, b)` |
| `clear` | Reset the cell colour to `(0, 0, 0)` |

#### Keyboard Controls:
 - `[SPACE]` Pause / resume the simulation
 - `r` Generate a random board
 - `R` Enable rainbow drawing
 - `c` Clear the board
 - `-` Slow down the simulation speed
 - `=` Increase the simulation speed
 - `m` Switch the ruleset
 - `g` Toggle the background grid
 
#### Mouse Controls:
 - `LEFT` Set a cell
 - `RIGHT` Delete a cell

## License
This project is licensed under the [M.I.T. License](https://github.com/analog-vortices/Life-Itself/blob/master/LICENSE)
