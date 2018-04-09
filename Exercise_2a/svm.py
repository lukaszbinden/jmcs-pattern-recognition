import numpy as np
from sklearn.svm import SVC

print ('reading data...')

train = np.genfromtxt('../data/MNIST/train.csv', delimiter=',')
test = np.genfromtxt('../data/MNIST/test.csv', delimiter=',')

print ('training model...')

y = train[:, 0]
X = train[:, 1:]

y_test = test[:10, 0]
X_test = test[:10, 1:]

svm = SVC(verbose=True, kernel='linear')
svm.fit(X, y)

print ('predicting...')

pred = svm.predict(X_test)

#print (pred)
#print (y_test)

acc = (y_test == pred).sum() / len(pred)

print ('done.')

print ('Accuracy: %3f ' % acc)
