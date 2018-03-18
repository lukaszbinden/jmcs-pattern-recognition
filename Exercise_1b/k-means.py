import sys
import csv
from datetime import datetime
import random
import numpy as np
import scipy.spatial
import math
from itertools import combinations

# CONSTS
MAX_ITERATIONS = 15

TYPE_FIXED_NUMBER_OF_ITERATIONS = 99
TYPE_RANDOM_CHOICE = 100
METHOD_C_INDEX = 500
METHOD_DUNN_INDEX = 501

# CONFIGURATION OF PROGRAM
TERMINATION_CRITERIA = TYPE_FIXED_NUMBER_OF_ITERATIONS
ALGORITHM_INITIAL_CLUSTERS = TYPE_RANDOM_CHOICE


def load_data(filename):
    with open(filename, 'r') as f:
        reader = csv.reader(f)
        data = list(reader)
    matrix = np.array(data, dtype = int)
    # separate labels from samples
    samples = matrix[:,1:]
    labels = matrix[:,0]
    return labels, samples


def print_indent(text, indent, indent_char='\t'):
    print('{indent}{text}'.format(indent=indent*indent_char, text=text))
    sys.stdout.flush()


def k_means(train_set, k):
    """
    :return: clustering [C_1,...,C_k]
    """
    assert(k > 0)

    k_cluster_centers = choose_cluster_centers(train_set, k, ALGORITHM_INITIAL_CLUSTERS)
    k_clusters = {}
    termination_dict = {}

    while True:
        dist = scipy.spatial.distance.cdist(train_set, k_cluster_centers)  # uses euclidean
        # for each xi, assign it to nearest center
        cluster_ids = np.argmin(dist, axis=1)
        for i in range(0, k):  # for each cluster
            xi_indices = np.where(cluster_ids == i)[0]
            cluster_i = train_set[xi_indices]
            k_clusters[i] = xi_indices  # cluster_i
            # recompute cluster center
            k_cluster_centers[i] = np.mean(np.array(cluster_i), axis=0)

        if terminate(termination_dict, TERMINATION_CRITERIA):
            break

    assert(len(k_clusters) == k)
    result = []
    for i in k_clusters:
        result.append(k_clusters[i])
    return result


def terminate(termination_dict, criteria):
    if criteria == TYPE_FIXED_NUMBER_OF_ITERATIONS:
        if 'cnt' not in termination_dict:
            termination_dict['cnt'] = 0
        termination_dict['cnt'] = termination_dict['cnt'] + 1
        if termination_dict['cnt'] >= MAX_ITERATIONS:
            return True

    return False


def validate(train_set, clusters, k, validation_dict, method):
    if method == METHOD_C_INDEX:
        gamma = 0
        alpha = 0
        distances = []
        pdist_square = get_pdist_square(train_set, validation_dict)
        for i in range(0, len(train_set) - 2):
            for j in range(i+1, len(train_set) - 1):
                distances.append(pdist_square[i][j])
                if in_same_cluster(clusters, i, j):
                    gamma = gamma + pdist_square[i][j]
                    alpha = alpha + 1
        distances = np.array(distances)
        idx = np.argpartition(distances, alpha)
        min_dist = sum(distances[idx[:alpha]])
        idx = np.argpartition(distances, -alpha)
        max_dist = sum(distances[idx[-alpha:]])
        c_index = (gamma - min_dist) / (max_dist - min_dist)
        print_indent('C-Index for k={k_val}: {c_val}'.format(k_val=k, c_val=c_index), indent=1)

    elif method == METHOD_DUNN_INDEX:
        pdist_square = get_pdist_square(train_set, validation_dict)

        inter_cluster_distances = []
        for pair in combinations(clusters, 2):  # all possible pairs of clusters
            cluster_i = pair[0]
            cluster_j = pair[1]
            inter_cluster_distances.append(dunn_cluster_distance(cluster_i, cluster_j, pdist_square))

        diameters = []
        for cluster in clusters:
            diameters.append(dunn_cluster_diameter(pdist_square, cluster))
        delta_max = max(diameters)

        dunn_index = min(inter_cluster_distances) / delta_max
        print_indent('Dunn-Index for k={k_val}: {d_val}'.format(k_val=k, d_val=dunn_index), indent=1)

    else:
        print("invalid method specified.")


def in_same_cluster(clusters, i, j):
    for xi_indices in clusters:
        if i in xi_indices and j in xi_indices:
            return True
    return False


def get_pdist_square(train_set, validation_dict):
    if 'pdist_square_key' not in validation_dict:
        pdist = scipy.spatial.distance.pdist(train_set)
        pdist_square = scipy.spatial.distance.squareform(pdist)
        validation_dict['pdist_square_key'] = pdist_square
    else:
        pdist_square = validation_dict['pdist_square_key']
    return pdist_square


def dunn_cluster_distance(cluster1, cluster2, pdist_square):
    min_distance = math.inf
    for i in cluster1:
        for j in cluster2:
            dist = pdist_square[i][j]
            if dist < min_distance:
                min_distance = dist
    assert(min_distance != math.inf)
    return min_distance


def dunn_cluster_diameter(pdist_square, cluster):
    diameter = 0
    for pair in combinations(cluster, 2):  # all possible pairs x,y e C
        dist = pdist_square[pair[0]][pair[1]]
        if dist > diameter:
            diameter = dist
    return diameter


def choose_cluster_centers(train_set, k, algorithm):
    if algorithm == TYPE_RANDOM_CHOICE:
        # random choice of k elements of train_set
        indices = random.sample(range(0, len(train_set) - 1), k)
        centers = train_set[indices]
    else:
        print('no algorithm defined')
    assert(len(centers) == k)
    return centers


def main():
    print("exercise_1b -->")
    _, train_imgs = load_data("../data/MNIST/train_large.csv")

    print("\ttraining set size..: ", len(train_imgs))

    start_total = datetime.now()
    validation_dict = {}
    for k in [5, 7, 9, 10, 12, 15]:  # [3]
        start_k = datetime.now()
        clusters = k_means(train_imgs, k)
        # validate(train_imgs, clusters, k, validation_dict, METHOD_C_INDEX)
        validate(train_imgs, clusters, k, validation_dict, METHOD_DUNN_INDEX)
        end_k = datetime.now()
        print_indent('Runtime for k={k_key}: {duration}'.format(k_key=k, duration=end_k-start_k), indent=1)

    end = datetime.now()
    print_indent('Total runtime: {duration}'.format(duration=end-start_total), indent=1)

    print("exercise_1b <--")


if __name__ == "__main__":
    main()
