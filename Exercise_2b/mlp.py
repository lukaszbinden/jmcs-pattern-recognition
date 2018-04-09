#
#
# inital version taken from
# https://raw.githubusercontent.com/andrewliao11/dni.pytorch/master/mlp.py
#
import torch
import torch.nn as nn
import torchvision.datasets as dsets
import torchvision.transforms as transforms
from torch.autograd import Variable
import math
from datetime import datetime
import matplotlib.pyplot as plt
import numpy as np


print('mlp.py -->')

# Hyper Parameters
input_size = 784
num_classes = 10
batch_size = 100

# MNIST Dataset
train_dataset = dsets.MNIST(root='../data',
                            train=True,
                            transform=transforms.ToTensor(),
                            download=True)

test_dataset = dsets.MNIST(root='../data',
                           train=False,
                           transform=transforms.ToTensor())

# Data Loader (Input Pipeline)
train_loader = torch.utils.data.DataLoader(dataset=train_dataset,
                                           batch_size=batch_size,
                                           shuffle=True)

test_loader = torch.utils.data.DataLoader(dataset=test_dataset,
                                          batch_size=batch_size,
                                          shuffle=False)

# Neural Network Model (1 hidden layer)
class Net(nn.Module):
    def __init__(self, input_size, hidden_size, num_classes):
        super(Net, self).__init__()
        self.fc1 = nn.Linear(input_size, hidden_size)
        self.relu = nn.ReLU()
        self.fc2 = nn.Linear(hidden_size, num_classes)

    def forward(self, x):
        out = self.fc1(x)
        out = self.relu(out)
        out = self.fc2(out)
        return out


def create_plot(epochs, training, validation):
    width = 0.4
    array_epoches = np.arange(len(training))
    string_epoches = [str(num) for num in epochs]

    # we just want the axis part
    _, ax = plt.subplots()

    # plot 1
    training_plot = ax.bar(array_epoches, training, width, color='red', bottom=0)
    # plot 2
    validation_plot = ax.bar(array_epoches + width, validation, width, color='blue', bottom=0)
    # title
    ax.set_title('Error rate on training vs. test set wrt to training epochs')
    # so bars are next to each other
    ax.set_xticks(array_epoches + width / 2)
    # assigns epochs as tick rates, so our bars are over the tickrates
    ax.set_xticklabels((string_epoches))
    plt.xlabel('#Epochs')
    plt.ylabel('Error rate in %')
    ax.set_yticks(np.arange(0, 10, 1))
    ax.set_ylim([0, 10])
    ax.legend((training_plot[0], validation_plot[0]), ('Training set', 'Test set'))
    plt.savefig('train_vs_test_epoch.png')
    plt.show()


print('MNIST training set size:...%d' % (len(train_dataset)))
print('MNIST test set size:.......%d' % (len(test_dataset)))


# number of epochs value set
EPOCH_values = [5, 10, 20]
# hidden size value set
H_values = [16, 32, 64, 128, 256]
# learning rate value set
LR_values = [1, 0.5, 0.1, 0.01]
# training error rate
training_error = []
# validation error rate
test_error = []

best_accuracy = -math.inf
best_num_epochs = -math.inf
best_hidden_size = -math.inf
best_learning_rate = -math.inf

for num_epochs in EPOCH_values:
    best_train_error_rate_epoch = math.inf
    best_test_error_rate_epoch = math.inf
    for hidden_size in H_values:
        for learning_rate in LR_values:
            tstart = datetime.now()
            net = Net(input_size, hidden_size, num_classes)

            # Loss and Optimizer
            criterion = nn.CrossEntropyLoss()
            optimizer = torch.optim.Adam(net.parameters(), lr=learning_rate)

            # Train the Model
            for epoch in range(num_epochs):
                for i, (images, labels) in enumerate(train_loader):
                    # Convert torch tensor to Variable
                    images = Variable(images.view(-1, 28*28))
                    labels = Variable(labels)

                    # Forward + Backward + Optimize
                    optimizer.zero_grad()  # zero the gradient buffer
                    outputs = net(images)
                    loss = criterion(outputs, labels)
                    loss.backward()
                    optimizer.step()

                    #if (i+1) % 100 == 0:
                    #    print ('Epoch [%d/%d], Step [%d/%d], Loss: %.4f'
                    #           %(epoch+1, num_epochs, i+1, len(train_dataset)//batch_size, loss.data[0]))

            # collect error rate on training set
            correct = 0
            total = 0
            for images, labels in train_loader:
                images = Variable(images.view(-1, 28*28))
                outputs = net(images)
                _, predicted = torch.max(outputs.data, 1)
                total += labels.size(0)
                correct += (predicted.cpu() == labels).sum()

            train_accuracy = (correct / total)
            train_error_rate = (1-train_accuracy)
            if train_error_rate < best_train_error_rate_epoch:
                best_train_error_rate_epoch = train_error_rate

            # collect error rate on test set
            correct = 0
            total = 0
            for images, labels in test_loader:
                images = Variable(images.view(-1, 28*28))
                outputs = net(images)
                _, predicted = torch.max(outputs.data, 1)
                total += labels.size(0)
                correct += (predicted.cpu() == labels).sum()

            test_accuracy = correct / total
            test_error_rate = (1-test_accuracy)
            if test_error_rate < best_test_error_rate_epoch:
                best_test_error_rate_epoch = test_error_rate

            tend = datetime.now()
            print('Current NN: num_epochs=%d, hidden_size=%d, learning_rate=%f | %s | accuracy train set: %d %% | accuracy test set: %d %%'
                  % (num_epochs, hidden_size, learning_rate, str((tend-tstart)), 100 * train_accuracy, 100 * test_accuracy))

            if test_accuracy > best_accuracy:
                best_accuracy = test_accuracy
                best_num_epochs = num_epochs
                best_hidden_size = hidden_size
                best_learning_rate = learning_rate

    # only take the best error rate each per epoch
    training_error.append(100 * best_train_error_rate_epoch)
    test_error.append(100 * best_test_error_rate_epoch)


print('Best test accuracy found: %f' % best_accuracy)
print('NN best parameters: num_epochs=%d, hidden_size=%d, learning_rate=%f' % (num_epochs, hidden_size, learning_rate))

print("EPOCH_values: ", EPOCH_values)
print("training_error: ", training_error)
print("test_error: ", test_error)
create_plot(EPOCH_values, training_error, test_error)
print('mlp.py <--')
