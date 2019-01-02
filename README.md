# ConnectFourAI
Computer plays connect four using min-max algorithm. A genetic algorithm is implemented to improve decisions.  

In the min-max algorithm, the moves where the computer has more opprotunities to win are prioritized; however, the depth at which the win occurs is not taken into account for. In my programm, an exponential function was used to weigh the value of the win--wins that occured in earlier depths were worth more than wins later on. To determine the values for the constants of this exponential function, a genetic algorithm was implemented. The computers played against each other and the most fit had their constant values taken.
