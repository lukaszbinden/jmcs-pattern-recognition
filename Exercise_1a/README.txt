Pattern Recognition SS 2018 - Exercise 1a
=========================================
Zbinden Lukas


File knn.py
------------
1. implementation: numpy array, single threaded
The implementation was first done with numpy array. After an execution of more than 12 hours with the following (smaller) set sizes I aborted it:

exercise_1a -->
training set size..:  26999
test set size......:  4032
accuracy [k= 1 , euclidean] =  0.9630456349206349
accuracy [k= 1 , manhattan] =  0.9563492063492064
accuracy [k= 3 , euclidean] =  0.9632936507936508
accuracy [k= 3 , manhattan] =  0.9548611111111112
accuracy [k= 5 , euclidean] =  0.9590773809523809
accuracy [k= 5 , manhattan] =  0.9538690476190477


2. implementation: dok_matrix array (sparse vector), single threaded
I changed the array implementation from numpy to dok_matrix and started to execute it, but before first results came out, I aborted again and decided to make it multithreaded.


3. implementation: dok_matrix array, multi-threaded
Here are the results after executing the third implementation using 4 threads on a MacBook Pro (with 4 cores):

