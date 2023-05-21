
# Wikipedia Game Path Finder 

The Wikipedia Game Path Finder project is a robust web scraper that navigates from one Wikipedia page to another by following embedded article links similar to the popular "Wikipedia Game".

The project is divided into four primary classes: `Main`, `UrlChecker`, `LinkList`, and `Scraper`. 
* `Main`  functions as the orchestrator of the entire process, delegating tasks and invoking methods from the other classes. 
* `UrlChecker` validates URLs and establishes a connection to a webpage. 
* `LinkList` handles a list of URLs, while 
* `Scraper` is responsible for web scraping and retrieving a list of URLs from a given webpage.

## Getting Started

Follow the instructions below to set up this project locally.

### Prerequisites

- Java 8 or higher
- jsoup 1.14.1 or later

### Installation

1. Clone the repository

```
git clone https://github.com/soradotwav/path-finder.git
```

2. Navigate to the root directory of the project in your terminal

```
cd path-finder
```

3. Compile the Java files

```
javac net/soradotwav/*.java
```

4. Execute the program with Java

```
java net.soradotwav.Main
```

## Usage

This project identifies a path between a starting URL and an ending URL by scraping Wikipedia articles and following the hyperlinks in each page. Both the starting and ending URLs must be Wikipedia articles. The default starting URL is "https://en.wikipedia.org/wiki/MissingNo.", and the ending URL is "https://en.wikipedia.org/wiki/The_Legend_of_Zelda:_Tears_of_the_Kingdom".

You can alter the starting and ending URLs by modifying the `startUrl` and `endUrl` variables in the `main` method of the `Main` class. Recompile and rerun the program to reflect these changes.

The final output of the program includes the path from the starting article to the ending article and the total number of websites evaluated during the search process. The path is displayed as a sequence of article names, connected by arrows representing the navigational flow.

## Work In Progress

The project is currently being enhanced to incorporate natural language processing (NLP) techniques to evaluate the relevance of each link to the destination URL. By employing NLP, we hope to improve the efficiency of our path-finding algorithm by prioritizing the most related links. In addition, a graphical user interface (GUI) is under development to provide a more intuitive and user-friendly experience.

## Acknowledgments

The `UrlChecker` and `Scraper` classes were adapted and modified from another project found at https://github.com/soradotwav/mcs.

## Contributions

Contributions, issues, and feature requests are welcome. Feel free to check the [issues page](https://github.com/soradotwav/path-finder/issues) if you want to contribute.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.