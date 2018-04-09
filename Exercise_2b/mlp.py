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


print('MNIST training set size:...%d' % (len(train_dataset)))
print('MNIST test set size:.......%d' % (len(test_dataset)))

#training_set_size = 50000
#validation_set_size = 10000

# number of epochs value set
EPOCH_values = [5, 10, 20]
# hidden size value set
H_values = [16, 32, 64, 128, 256, 512]
# learning rate value set
LR_values = [1, 0.5, 0.1, 0.01, 0.001]

best_accuracy = -math.inf
best_num_epochs = -math.inf
best_hidden_size = -math.inf
best_learning_rate = -math.inf

for num_epochs in EPOCH_values:
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

            # # Test the Model on training_set and validation_set
            # correct_train = 0
            # total_train = 0
            # correct_val = 0
            # total_val = 0
            # for i, (images, labels) in enumerate(train_loader):
            #     if i < training_set_size:
            #         images = Variable(images.view(-1, 28*28))
            #         outputs = net(images)
            #         _, predicted = torch.max(outputs.data, 1)
            #         total += labels.size(0)
            #         correct += (predicted.cpu() == labels).sum()
            #     else:


            # Finally, test the Model on test set
            correct = 0
            total = 0
            for images, labels in test_loader:
                images = Variable(images.view(-1, 28*28))
                outputs = net(images)
                _, predicted = torch.max(outputs.data, 1)
                total += labels.size(0)
                correct += (predicted.cpu() == labels).sum()

            current_accuracy = correct / total
            tend = datetime.now()
            print('Current NN: num_epochs=%d, hidden_size=%d, learning_rate=%f | %s | accuracy on test set: %d %%' % (num_epochs, hidden_size, learning_rate, str((tend-tstart)), 100 * current_accuracy))

            #print('Accuracy on test set: %d %%' % (100 * current_accuracy))


            if current_accuracy > best_accuracy:
                best_accuracy = current_accuracy
                best_num_epochs = num_epochs
                best_hidden_size = hidden_size
                best_learning_rate = learning_rate


print('Best test accuracy found: %f' % best_accuracy)
print('NN best parameters: num_epochs=%d, hidden_size=%d, learning_rate=%f' % (num_epochs, hidden_size, learning_rate))
