from scipy.sparse import dok_matrix
import numpy as np
import threading
import multiprocessing

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
        img_sp_mat = dok_matrix([img_raw], dtype=np.int)
        imgs.append(img_sp_mat)
    return labels, imgs


def knn(train_imgs, train_labels, test_imgs, test_labels, k, distance_metric):
    """
    apply the kNN alogrithm to each image in the test_imgs source and calculate the
    accuracy of the kNN model that it achieves based on the given training data in
    train_imgs and train_labels.
    :return: accuracy for k
    """
    assert(len(train_imgs) == len(train_labels))
    assert(len(test_imgs) == len(test_labels))
    assert(k > 0)

    correct_digits = 0
    for i in range(0, len(test_imgs)):
        test_img = test_imgs[i]
        predicted_label = knn_predict(distance_metric, test_img, train_imgs, train_labels, k)
        if i % 50 == 0:
            print(threading.get_ident(), ": knn_predict:", (i + 1))
        if predicted_label == test_labels[i]:
            correct_digits = correct_digits + 1
    return correct_digits / len(test_labels)  # calculate accuracy


def knn_predict(metric, test_img, train_imgs, train_labels, k):
    """
     implementation of the kNN algorithm
    :return: label prediction according to kNN classifier
    """
    k_nearest_neighbors = {}
    max_distance = -1
    num_neighbors = 0
    for j in range(0, len(train_imgs)):
        train_img = train_imgs[j]
        distance = metric(train_img, test_img)
        if num_neighbors < k or distance < max_distance:
            if distance not in k_nearest_neighbors:
                k_nearest_neighbors[distance] = []
            k_nearest_neighbors[distance].append(train_labels[j])
            max_distance = max(k_nearest_neighbors)
            num_neighbors = num_neighbors + 1
        if num_neighbors > k:
            del k_nearest_neighbors[max_distance]
            max_distance = max(k_nearest_neighbors)
            num_neighbors = num_neighbors - 1

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
    for label, cnt in labels.items():
        if cnt > max_cnt:
            predicted_label = label
            max_cnt = cnt

    return predicted_label


def euclidean(train_img, test_img):
    """
    Euclidean distance
    :return:
    """
    # omit sqrt operation, it is equivalent without
    return (train_img - test_img).power(2).sum()


def manhattan(train_img, test_img):
    """
    Manhattan distance
    :return:
    """
    return abs(train_img - test_img).sum()


class KnnThread(threading.Thread):
    def __init__(self, *args):
        threading.Thread.__init__(self)
        self._args = args

    def run(self):
        # print('running', threading.get_ident(), '-->')
        accuracy = knn(self._args[0], self._args[1], self._args[2], self._args[3], self._args[4], self._args[5])
        print("accuracy [k=", self._args[4], ",", self._args[6], "] =", accuracy)
        # print('done', threading.get_ident(), '<--')


if __name__ == "__main__":
    print("exercise_1a -->")
    train_labels, train_imgs = load_data("train_small.csv")
    test_labels, test_imgs = load_data("test_small.csv")

    print("training set size..: ", len(train_labels))
    print("test set size......: ", len(test_labels))

    threads = []
    cpus = multiprocessing.cpu_count()
    for k in [1, 3]:  # , 3, 5, 10, 15]:
        t1 = KnnThread(train_imgs, train_labels, test_imgs, test_labels, k, euclidean, "euclidean")
        t1.start()
        threads.append(t1)
        t2 = KnnThread(train_imgs, train_labels, test_imgs, test_labels, k, manhattan, "manhattan")
        t2.start()
        threads.append(t2)
        if len(threads) >= cpus:
            threads[0].join()
            threads[1].join()
            del threads[0:2]

    [t.join() for t in threads]

    print("exercise_1a <--")
