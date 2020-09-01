# alphaRTS

alphaRTS is a Java framework for AI research in real-time strategy game enviorment.

![game screenshot](https://github.com/adamxuuuu/rts-ai-framework/blob/master/screenshots/mid.png)

## Installation

Currently the only way to run this framework is to clone the repositroy to your local system and run it under a Java enviorment (preferably a IDE).

Put the libraries in the lib folder inside the project structure.


## Usage

To run a pre-defined game, locate the <Start> class in the root of the 'src' directory.

Select a example configuration in the main method:

```java
enum PlayerType {DONOTHING,HUMAN,RANDOM,RANDOM_BIASED,MC,SIMPLE,MCTS,}

public static void main(String[] args) {
    Game g = init(new PlayerType[]{PlayerType.HUMAN, PlayerType.RANDOM});
    ...
    runGame(g);
}
```

and simply watch the game play.

### Using the API

To create your own bot, please make a package and put in the player directory.
The main class of your bot must extend the <Agent> class or a subclass of it. Implement the following method:

```java
/**
 * Function requests an action from the agent, given current game state observation.
 *
 * @param gs - current game state.
 * @return - action to play in this game state.
 */
public abstract PlayerAction act(GameState gs);

public abstract Agent copy();
```

Add the type to the <PlayerType> enum in the <Start> class with its constructor (this will be imporved).

### Creating unit

It is very simple to define your own unit in alphaRTS, simple write a JSON file with following format:

```json
{
  "type": 0,
  "name": "light",
  "speed": 2,
  "range": 2,
  "cost": 5,
  "maxHp": 10,
  "attack": 2,
  "buildTime": 1,
  "rateOfFire": 1,
  "spriteKey": [
    "scifiUnit_01.png",
    "scifiUnit_13.png",
    "scifiUnit_25.png",
    "scifiUnit_37.png"
  ]
}
```

Spritekey can be found in the 'kenny_rtsci-fi' directory or you can use your own sprite sheet. (Don't forget to change things in the 'sprite' folder)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.


## License
[GPL-3.0 License](https://choosealicense.com/licenses/gpl-3.0/)
