import numpy as np
from sklearn.svm import SVC

print ('reading data...')

train = np.genfromtxt('../data/MNIST/train.csv', delimiter=',')
test = np.genfromtxt('../data/MNIST/test.csv', delimiter=',')

print ('training model...')

# complexity is quadratic (?), so much more than 10'000 samples might not be possible

y = train[:10000, 0]
X = train[:10000, 1:]

y_test = test[:, 0]
X_test = test[:, 1:]

svm = SVC(verbose=True, kernel='rbf')
svm.fit(X, y)

print ('predicting...')

pred = svm.predict(X_test)

print (pred)
print (y_test)

acc = ((y_test == pred).sum() + 0.0) / len(pred)

print ('done.')

print ('Accuracy: %3f ' % acc)


# Results:
#
# kernel=linear, C=1.0, gamma=1/#features -> Acc=0.913806
# kernel=rbf   , C=1.0, gamma=1/#features -> Acc= <still running>
#