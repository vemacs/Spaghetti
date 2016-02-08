Spaghetti
==

Spaghetti is a Finite State Machine (FSM) for Minecraft minigames. It is named Spaghetti because FSM also stands for Flying Spaghetti Monster (praise him), and the code is bad.

Overview
--

* Spaghetti does have global state (no, it's not Hitler), as only one state can be active at once.
* States are essentially plugins, and are isolated from each other. They are automatically cleaned up when changing to the next state.
* To promote isolation (and discourage memory leaks), a MemoryConfiguration can be passed during state transitions to the next state. (Think of this as Android's Bundles).
* A Transition object (use the builder) is required for a state change. You can specify player resets (Spaghetti), world loads and unloads (WorldHandler handles this cleanly), and player teleportation. Game-specific transition data should be handled by passing through a MemoryConfiguration.
* There is an optional `cleanup()` method for AbstractGameStates that should be overridden if you use any non-Bukkit resources that require cleanup (disguises, holograms, etc...)
* Spaghetti aims to be minimal (and adaptable to any environment). For that reason, no default state implementations (other than the null state) are provided, you have to write your own.
* Spaghetti States deal with classic Bukkit concepts, such as tasks, listeners, and commands. All can be registered/unregistered dynamically, and are cleaned up on state transitions.
