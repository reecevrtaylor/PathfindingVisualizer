# Pathfinding Visualizer
<div align="center">
  <h2>Capstone Project</h2>
  <p>Reece Taylor</p>
</div>

### Contents
1. [About The Project](#about-the-project)
2. [Getting Started](#getting-started)
    * [Prerequisites](#prerequisites)
    * [Installation](#installation)
3. [Technical Documentation](#technical-documentation)
4. [Examples](#examples)
5. [Roadmap](#roadmap)
6. [License](#license)

## About The Project 🎓
The Pathfinding Visualizer is an educational tool designed for learners to explore and understand pathfinding algorithms, visualizing their operations in real-time.

Originally, I embarked on this project to enhance my comprehension of algorithms—a cornerstone of computer science. By developing this visualization tool, I could witness algorithmic behaviors firsthand. The overarching goal was not only to complete the project but to mature as a software developer. I believe this endeavor, combined with the knowledge I've gained through research and hands-on experience, has greatly enriched my skills.

## Getting Started ✅

### Prerequisites ⚠️
* **JDK 17 or later**
    - [Windows](https://www.oracle.com/java/technologies/downloads/#jdk18-windows)
    - [macOS](https://www.oracle.com/java/technologies/downloads/#jdk18-mac)
    - [Linux](https://www.oracle.com/java/technologies/downloads/#jdk18-linux)
* **Maven 3.8.x or later (recommended)**
    - [Download](https://maven.apache.org/download.cgi)
    - [Installation Guide](https://maven.apache.org/install.html)

### Installation 🖥️
To utilize the Pathfinding Visualizer, you can execute the pre-compiled JAR file or build the project from source.

**Using Pre-compiled JAR:**
1. Clone the repository.
   ```sh
   git clone [REPO_URL]
   ```
2. Navigate to the `ImprovedVisualizer_jar` directory.
3. Execute the `run.bat` file.

**Using Maven (recommended):**
1. Clone the repository.
   ```sh
   git clone [REPO_URL]
   ```
2. Change directory to `ImprovedVisualizer`.
   ```sh
   cd ImprovedVisualizer
   ```
3. Build the project with Maven.
   ```sh
   mvn install
   ```
4. Navigate to the output folder containing `ImprovedVisualizer.jar`.
5. Run the JAR file.
   ```sh
   java -jar ImprovedVisualizer.jar
   ```

<div align="center">
  <video width="520" height="340" controls>
    <source src="Resources/maven-help.mp4" type="video/mp4">
  </video>
</div>

## Technical Documentation 📝
This section delves into the technical specifics of the project, from its architecture to algorithmic implementations.

### Project Configuration
The Pathfinding Visualizer adopts an object-oriented design, comprising nine integral classes.

<div align="center">
  <img src="Resources/project_classes.png" alt="Project Classes" width="400"/>
</div>

The structured approach facilitates easy augmentation with additional features, classes, heuristics, or algorithms. The synergy between classes ensures a seamless user experience and workflow, a pivotal element in this project's success.

### Features Overview
* **Nodes**: Represented by each tile on the grid.
* **Start Node**: The algorithm's initiation point.
* **End Node**: The algorithm's termination point.
* **Walls**: Barriers the algorithm will bypass.
* **Open Node**: Nodes currently under algorithmic evaluation.
* **Closed Node**: Nodes previously evaluated by the algorithm.
* **Path**: The finalized route determined by the algorithm.

### Algorithms and Heuristics
This project incorporates various pathfinding algorithms and heuristics, including:
* A-Star Search (A*)
* Breadth-First Search (BFS)
* Depth-First Search (DFS)

Heuristics utilized are:
* Manhattan Distance
* Euclidean Distance
* Diagonal Distance
* Dijkstra (no heuristic)
* Chebyshev Distance
* Random Walk Iteration

For an in-depth exploration of each feature, algorithm, and heuristic, please consult the provided [examples](#examples).

## Examples 📸
Presented below are visual demonstrations showcasing how various algorithms and heuristics navigate identical mazes.

### A* Examples
- **A* Manhattan Distance**:
  ![A* Manhattan](Resources/program_astar_manhattan.png)

- **A* Euclidean Distance**:
  ![A* Euclidean](Resources/program_astar_euclidean.png)

- **A* Diagonal Distance**:
  ![A* Diagonal](Resources/program_astar_diagonal.png)

- **A* Dijkstra**:
  ![A* Dijkstra](Resources/program_astar_dijkstra.png)

### BFS Examples
- **BFS with Walls**:
  ![BFS Walls](Resources/program_bfs_walls.png)

- **BFS without Walls**:
  ![BFS No Walls](Resources/program_bfs_nowalls.png)

## Roadmap 🗺️
Continuous enhancement is at the heart of this project. I aim to integrate additional features, bolstering the user experience further.

**Planned Additions**:
- Ability to alter grid dimensions.
- Finalizing the DFS algorithm.

**Known Issues**:
- Dragging the start node leaves a trail, mimicking wall behavior. This minor glitch doesn't hamper functionality and is rectified upon pressing any button.

## License 🎫
This project is licensed under the [GNU General Public License v3.0](LICENSE).