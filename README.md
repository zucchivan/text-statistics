# text-statistics

A small project based on a challege over processing text statistics

## Installation and Setup

Clone the project and run it with the Spring Boot Maven plugin by executing:

```
$ mvn spring-boot:run
```
## Usage

*/process* GET endpoint is used for obtaining text processing data.

The following body shows how to obtain the 30 most frequent words, the 20 longest, along with lines and words count for Homer's The Odyssey.

```
{
    "url": "https://www.gutenberg.org/files/1727/1727-0.txt",
    "numberOfFrequentWords": 30,
    "numberOfLongestWords": 20
}
```
