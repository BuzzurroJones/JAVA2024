# To run go to Game.java

# Idea
The core idea is to observe how different initial conditions and environmental factors, such as food availability and volatility, impact the overall system. Observing which type of behaviour is thriving under different conditions, finding population balance. By adjusting initailization parameters, the simulation explores the emergence of social vs. greedy behaviors, changes in player's characteristics, competition and cooperation simulate evolutionary processes as behaviors adapt in response to environmental pressures.

# Structure
  ## Evolution
  The evolution simulation is driven by the mechanisms of reproduction and natural selection, mirroring fundamental evolution processes. Players on the board reproduce, generating offspring with inherited traits that vary within predetermined range. The environment, including food availability and competition, imposes pressure that influence player's survival and reproduction.

### Reproduction Process

Reproduction occurs when a player consume entire unit of food without sharing. The `reproduce` method creates a new player, or offspring, in a neighboring cell of the parent. This offspring inherits the parent's traits, such as vision and speed with variation to enforce evolution. The social behavior of the offspring is directly inherited from the parent, maintaining the parent's social or greedy nature.

### Natural Selection

Natural selection in this simulation is implemented through competition and survival challenges. Players must find food to survive, leading to conflicts when multiple players encounter the same food source. The conflict resolution process determines which players get to eat and which are marked for death, mimicking competitive exclusion. Players that fail to secure food are removed from the game, simulating death from starvation. This selective pressure ensures that only the fittest players—those who are better at finding food and avoiding conflicts—survive and reproduce.
Players with advantageous traits are more likely to survive and reproduce, leading to a gradual adaptation of the population to the environment. This dynamic system reflects natural evolutionary processes, where environmental pressures and genetic variation shape the development of species.
#### Social Behavior Dynamics
In the simulation, social players share food, ensuring mutual survival but no reproduction, maintaining their numbers without increasing them. When a social player meets a greedy player, the greedy player consumes all the food, reproduces, and the social player dies. If two greedy players compete for food, both die due to conflict, limiting their population. This dynamic creates a balance between social and greedy behaviors, with cooperation and competition fluctuating based on environmental pressures and resources.
  ## Player & Board initiation
  The initialization process in the simulation involves setting up the board and populating it with players. The Board class is responsible for creating a grid structure with a specified number of rows and columns. Each cell in this grid is instantiated as a Cell object and stored in a two-dimensional array. During the board's construction, each cell is linked to its neighbors, establishing a network that facilitates player movement and interaction. 
  
  For each player, a random cell is selected, and a new Human object is created with defined attributes (e.g., vision of 3, speed of 5). The player's social nature is decided randomly. This player is then added to the players' list and placed on the corresponding cell. This setup ensures a diverse and unpredictable initial state, setting the stage for the game's dynamics as players begin interacting with their environment and each other.
  
  ## Gamelogic
  #### The simulation's structure is centered around managing a dynamic ecosystem on a board where human players interact with each other and their environment. The primary components of this structure include the players, the board, the food spawn dynamics.

At the begginig we initiate: the board, the number of initial players, amount of food and volatility. These parameters determine the dynamics of the simulation. The players are instantiated and placed on the board, each with specific attributes like speed, vision, and type of behaviour. The game progresses in turns, with each turn consisting of multiple sub-turns where players decide and execute their movements, and determine following actions.

#### Each turn follows a structured sequence:

* Resetting Player States: Players' speeds are reset, and the game averages for speed and vision are updated.
* Spawning Food: Food is randomly placed on the board, ensuring that players have resources to compete for.
* Player Movements: Players pick and execute their moves across multiple sub-turns.
* Resolving Conflicts: If players land on the same cell with food, conflicts are resolved based on their social or greedy nature.
* Managing Hunger and Death: Players who fail to find food are marked for death, and this is finalized by removing them from the game.
* Reproduction: Players who meet the criteria reproduce.
  
This cyclical process continues, with the game state evolving based on player interactions, resource availability, and player attributes. During execution the set of statistics is tracked, it contains the number of social and greedy players and  an average speed and vision, providing insights into the simulation's dynamics. The structure of the simulation in a self-sustaining and adaptive system where players must navigate survival and competition.
  ## User Interface

The purpose of the UI is to provide a clear and intuitive way to visualize the dynamics of the system. By displaying real-time statistics such as the number of social and greedy players, average speed, and average vision, the UI allows users to easily monitor the key metrics that indicate the behavior of the population. The graphical representation of these statistics, along with the board visualization, helps users understand players' interactions and trends over time.

Additionally, the UI enables interaction with the simulation by allowing users to pause and resume updates, providing ability to analyze specific moment in detail. The historical data graphs enable users to track changes and patterns over extended periods, making it easier to see the impact of different initial conditions and environmental changes on the population's evolution. 
# Observations
  * Equilibrium at 65% Food: Without considering volatility, when the food covers 65% of the board, there exists an equilibrium state. This suggests a balance in the population dynamics where neither social nor greedy behaviors dominate.
  ![Alt text](images/equilibrium.png?raw=true "Equilibrium")
  * Impact of Food Percentage: Increasing the food percentage favors greedy players, while decreasing it favors social players. This indicates that food availability directly influences the prevalence of different behaviors within the population.
  
  Graph when food is around 30% of board
  ![Alt text](images/scarcity.png?raw=true "Scarcity")
  Graph when food is around 90% of board
  ![Alt text](images/abundance.png?raw=true "Abundance")
  * Effect of Volatility: Starting from the equilibrium case and testing different values of volatility reveals that the change in population is much sharper. Higher volatility rates tend to favor social players, suggesting that unpredictable environmental changes can amplify the advantage of social behaviors in securing resources.
  ![Alt text](images/volatility.png?raw=true "Volatility")
  * Stabilization of Speed: Despite being limited to 15, speed always stabilizes before reaching its limit. It tends to lower values when the food rate is high and higher values when it is low or volatility is high. This suggests that speed adapts to environmental conditions, with players adjusting their speed based on resource availability and environmental stability.