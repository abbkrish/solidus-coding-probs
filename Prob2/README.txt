Three approaches to solve this problem-

1) Brute Force: For each element iterate through the rest of the list and see if there is another element which sums to the target.
The time complexity for this is O(n^2) which is terrible.
Space: O(1) additional space (not considering the original input)

2) Hashtables: Store every element in a hashtable. For each price x, check if target-x exists in the hashtable. The runtime for this is O(n)
but if there are too many rows, this is not a feasible option.
Space: O(n) additional space
Time complexity: O(n)

3) The best tradeoff: Sort the array. Then use two pointers one from the start and other at the end. Depending on if the value is higher or lower
than the target, adjust the pointers respectively.
Space: O(1) additional space
Time complexity: O(nlog(n))


*****BONUS Question*****
To find 3 items:
1) For each element 'x' in the array, check if the are two other items in the array which sum upto target - 'x' using the previous subroutine.
The runtime is O(n^2).
