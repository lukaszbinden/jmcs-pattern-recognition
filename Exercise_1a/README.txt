Pattern Recognition SS 2018 - Exercise 1a
=========================================
Zbinden Lukas
https://github.com/lukaszbinden/jmcs-pattern-recognition


1) File knn_numpyarr.py
-----------------------
-> implementation: numpy array, single threaded
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


2) File knn_dokmatrix.py
------------------------
-> implementation: dok_matrix array (sparse vector), single threaded
I changed the array implementation from numpy to dok_matrix and started to execute it, but before first results came out, I aborted again and decided to make it multithreaded.


3) File knn_final.py
-----------------------
-> implementation: dok_matrix array, multi-threaded
Here are the results after executing the third implementation using 4 threads on a MacBook Pro (with 4 cores) and even smaller set sizes (taking about 2.5 hours):
Note that the euclidean appears to be the better distance metric.

lzmac:Series_01 lukaszbinden$ nice -n 10 python knn.py 
exercise_1a -->
training set size..:  1935
test set size......:  500
123145414230016 : knn_predict: 1
123145424740352 : knn_predict: 1
123145429995520 : knn_predict: 1
123145419485184 : knn_predict: 1
123145414230016 : knn_predict: 51
123145424740352 : knn_predict: 51
123145429995520 : knn_predict: 51
123145419485184 : knn_predict: 51
123145414230016 : knn_predict: 101
123145424740352 : knn_predict: 101
123145429995520 : knn_predict: 101
123145419485184 : knn_predict: 101
123145414230016 : knn_predict: 151
123145424740352 : knn_predict: 151
123145429995520 : knn_predict: 151
123145419485184 : knn_predict: 151
123145414230016 : knn_predict: 201
123145424740352 : knn_predict: 201
123145429995520 : knn_predict: 201
123145419485184 : knn_predict: 201
123145414230016 : knn_predict: 251
123145424740352 : knn_predict: 251
123145429995520 : knn_predict: 251
123145419485184 : knn_predict: 251
123145414230016 : knn_predict: 301
123145424740352 : knn_predict: 301
123145419485184 : knn_predict: 301
123145429995520 : knn_predict: 301
123145414230016 : knn_predict: 351
123145424740352 : knn_predict: 351
123145419485184 : knn_predict: 351
123145429995520 : knn_predict: 351
123145414230016 : knn_predict: 401
123145424740352 : knn_predict: 401
123145419485184 : knn_predict: 401
123145429995520 : knn_predict: 401
123145414230016 : knn_predict: 451
123145424740352 : knn_predict: 451
123145419485184 : knn_predict: 451
123145429995520 : knn_predict: 451
accuracy [k= 1 , euclidean ] = 0.906
accuracy [k= 3 , euclidean ] = 0.888
accuracy [k= 1 , manhattan ] = 0.896
accuracy [k= 3 , manhattan ] = 0.866
123145419485184 : knn_predict: 1
123145414230016 : knn_predict: 1
123145424740352 : knn_predict: 1
123145429995520 : knn_predict: 1
123145414230016 : knn_predict: 51
123145424740352 : knn_predict: 51
123145419485184 : knn_predict: 51
123145429995520 : knn_predict: 51
123145414230016 : knn_predict: 101
123145424740352 : knn_predict: 101
123145419485184 : knn_predict: 101
123145429995520 : knn_predict: 101
123145414230016 : knn_predict: 151
123145419485184 : knn_predict: 151
123145424740352 : knn_predict: 151
123145429995520 : knn_predict: 151
123145414230016 : knn_predict: 201
123145419485184 : knn_predict: 201
123145424740352 : knn_predict: 201
123145429995520 : knn_predict: 201
123145414230016 : knn_predict: 251
123145419485184 : knn_predict: 251
123145424740352 : knn_predict: 251
123145429995520 : knn_predict: 251
123145414230016 : knn_predict: 301
123145419485184 : knn_predict: 301
123145424740352 : knn_predict: 301
123145429995520 : knn_predict: 301
123145414230016 : knn_predict: 351
123145419485184 : knn_predict: 351
123145424740352 : knn_predict: 351
123145429995520 : knn_predict: 351
123145414230016 : knn_predict: 401
123145419485184 : knn_predict: 401
123145424740352 : knn_predict: 401
123145429995520 : knn_predict: 401
123145414230016 : knn_predict: 451
123145419485184 : knn_predict: 451
123145424740352 : knn_predict: 451
123145429995520 : knn_predict: 451
accuracy [k= 5 , euclidean ] = 0.888
accuracy [k= 5 , manhattan ] = 0.872
123145419485184 : knn_predict: 1
123145414230016 : knn_predict: 1
accuracy [k= 10 , euclidean ] = 0.876
accuracy [k= 10 , manhattan ] = 0.856
123145419485184 : knn_predict: 51
123145414230016 : knn_predict: 51
123145419485184 : knn_predict: 101
123145414230016 : knn_predict: 101
123145419485184 : knn_predict: 151
123145414230016 : knn_predict: 151
123145419485184 : knn_predict: 201
123145419485184 : knn_predict: 251
123145414230016 : knn_predict: 201
123145419485184 : knn_predict: 301
123145414230016 : knn_predict: 251
123145419485184 : knn_predict: 351
123145414230016 : knn_predict: 301
123145419485184 : knn_predict: 401
123145419485184 : knn_predict: 451
123145414230016 : knn_predict: 351
accuracy [k= 15 , manhattan ] = 0.826
123145414230016 : knn_predict: 401
123145414230016 : knn_predict: 451
accuracy [k= 15 , euclidean ] = 0.86
exercise_1a <--
