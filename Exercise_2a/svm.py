import numpy as np
from sklearn.svm import SVC


train = np.genfromtxt('../data/MNIST/train_med.csv', delimiter=',')
test = np.genfromtxt('../data/MNIST/test.csv', delimiter=',')

# complexity is quadratic (?), so much more than 10'000 (we tried 60'000) samples might not be possible
y = train[:60000, 0]
X = train[:60000, 1:] / 255.

y_test = test[:, 0]
X_test = test[:, 1:] / 255.

accuracies = np.zeros((6, 6))

for i in range(0, 6):
    for j in range(0, 6):
        svm = SVC(verbose=False, kernel='linear', cache_size=4000, gamma=2**(j-5), C=10**(i+5))
        svm.fit(X, y)
        pred = svm.predict(X_test)
        accuracies[i,j] = ((y_test == pred).sum() + 0.0) / len(pred)
        print ('Gamma={0}, C={1}, Acc={2}'.format(2**(j-5), 10**(i+5), accuracies[i,j]))

print accuracies

