import numpy as np
from sklearn.svm import SVC

print('svm_evaluation -->')

train = np.genfromtxt('../data/MNIST/train.csv', delimiter=',')
test = np.genfromtxt('../data/MNIST/mnist_test.csv', delimiter=',')

y = train[:60000, 0]
X = train[:60000, 1:] / 255.

X_test = test / 255.


c_i = 14
gamma_j = 0

svm = SVC(verbose=False, kernel='linear', 
		  cache_size=4000, gamma=2**(gamma_j-5), C=10**(c_i+5))
print('fit -->')		  
svm.fit(X, y)
print('fit <--')

print('predict -->')
pred = svm.predict(X_test)
print('predict <--')

f = open('mninst_predictions.csv', 'w')
for test_id in range(0, len(pred)):
	f.write('{:d}, {:d}\n'.format(test_id+1, int(pred[test_id])))
f.close()

print('svm_evaluation <--')