from scipy.sparse import dok_matrix
import numpy as np
import csv

# CONSTS
INDEX_LABEL = 0
START_INDEX_IMG = 1

def load_data(file):
    """

    :return: image labels, images raw data
    """
    imgs = []
    labels = []
    data = np.loadtxt(file, delimiter=',')
    for i in range(0, len(data)):
        labels.append(data[i][INDEX_LABEL])
        # sparse_vec = dok_matrix((length, 1), dtype=np.int)
        img_raw = data[i][START_INDEX_IMG:len(data[i])]
        imgs.append(img_raw)
    return labels, imgs


def knn(train_imgs, train_labels, test_imgs, test_labels, k, distance_metric):
    """

    :return: accuracy for k
    """
    assert(len(train_imgs) == len(train_labels))
    assert(len(test_imgs) == len(test_labels))
    assert(k > 0)

    correct_digits = 0

    for i in range(0, len(test_imgs)):
        test_img = test_imgs[i]
        test_label = test_labels[i]
        predicted_label = knn_predict(distance_metric, test_img, train_imgs, train_labels, k)
        print("predicted label: ", predicted_label, "actual label: ", test_label)
        if predicted_label == test_label:
            correct_digits = correct_digits + 1
    return correct_digits / len(test_labels)


def knn_predict(metric, test_img, train_imgs, train_labels, k):
    """
     the kNN algorithm goes along these lines:
        Q = []
        for x,y in training data:
            if Q.length < k or dist(x,x’) < max distance in Q:
            add dist(x,x’) and y to Q
             if Q.length > k: remove max distance in Q
        return the y that shows up most in Q

    :return: label prediction according to kNN classifier
    """
    k_nearest_neighbors = {}

    for j in range(0, len(train_imgs)):
        train_img = train_imgs[j]
        distance = metric(train_img, test_img)
        num_neighbors = len(k_nearest_neighbors)
        if num_neighbors < k or distance < max(k_nearest_neighbors):
            if distance not in k_nearest_neighbors:
                k_nearest_neighbors[distance] = []
            k_nearest_neighbors[distance].append(train_labels[j])
        if num_neighbors > k:
            del k_nearest_neighbors[max(k_nearest_neighbors)]

    #print('k nearest neighbors:')
    #print(k_nearest_neighbors)

    return extract_most_frequent_label(k_nearest_neighbors)


def extract_most_frequent_label(k_nearest_neighbors):
    """

    :return: label prediction according to kNN classifier
    """
    labels = {}
    for key in k_nearest_neighbors:
        labels_of_key = k_nearest_neighbors[key]
        for label in labels_of_key:
            if label not in labels:
                labels[label] = 1
            else:
                labels[label] = labels[label] + 1

    max_cnt = 0
    for key, value in labels.items():
        if value > max_cnt:
            predicted_label = key
            max_cnt = value

    return predicted_label


def euclidean(train_img, test_img):
    """
    Euclidean distance
    :return:
    """
    return np.sqrt(sum((train_img - test_img) ** 2))


def manhattan(train_img, test_img):
    """
    Manhattan distance
    :return:
    """
    return sum(abs(train_img - test_img))


if __name__ == "__main__":
    print("exercise_1a -->")
    train_labels, train_imgs = load_data("train.csv")
    test_labels, test_imgs = load_data("test_1.csv")

    for k in [1, 3, 5, 10, 15]:
        acc_k1_euclidean = knn(train_imgs, train_labels, test_imgs, test_labels, k, euclidean)
        print("accuracy [k=", k, ", euclidean] = ", acc_k1_euclidean)
        acc_k1_manhattan = knn(train_imgs, train_labels, test_imgs, test_labels, k, manhattan)
        print("accuracy [k=", k, ", manhattan] = ", acc_k1_euclidean)

    print("exercise_1a <--")
