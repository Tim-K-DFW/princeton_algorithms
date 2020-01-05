# princeton_algorithms
solutions to [Princeton Algorithms course](https://www.coursera.org/learn/algorithms-part1/) on Coursera

(at first most of it was wrestling with Java, spending hours on stuff that's trivial in Python and R, but it's getting much more interesting)

## Part I
##### Week 1
- 101.25% (bonus for memory thrift)
- did a cool recursion and, IMHO, a good domain model

##### Week 2
- 99.92%
- 1 memory test failing, will fix when have time

##### Week 3
- 100%
- superfast treatment of output segment array in `Fast...` version, under 2 sec even for n=4096 in last tests
- ... thanks to custom inner class that implements comparable for line segments, thus allowing sorting and deduplication in O(NlogN)
- ... but there is a way to make it even faster by being smarter about duplicated segments (deduplicate at discovery time, not at the end?). Maybe will revisit.

##### Week 4
- 100%
- method names in `Board` are funny because initial implementation used 2d array but I had to convert it to 1d to pass memory test (which only mattered for smallest boards (2..4)). So it's a hybrid: storage is 1d but most things that require spacial reasoning are first converted to 2d
- creation of neighbors and twins turned out clean, IMO... both use same swapping method
- Java is getting easier to deal with, custom comparables helped a lot with `Node`, for sure.