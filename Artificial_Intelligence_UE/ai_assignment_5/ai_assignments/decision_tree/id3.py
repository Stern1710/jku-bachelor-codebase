import numpy as np


class ID3():
    def __init__(self):
        self.root = None

    def fit(self, X, y):
        self.root = DecisionTreeNode().split(X, y)
        return self

    def __str__(self):
        return str(self.root)


def entropy(labels):
    """returns the same as scipy.stats.entropy([positive, negative], base=2)"""
    n = len(labels)
    if n == 0:
        return 0.0
    positive = sum(labels) / n
    negative = 1 - positive
    if positive == 0 or negative == 0:
        return 0.0
    return -positive * np.log2(positive) - negative * np.log2(negative)


class DecisionTreeNode():
    def __init__(self):
        self.label = None
        self.split_point = None
        self.split_feature = None
        self.left_child = None
        self.right_child = None

    def get_all_possible_split_points(self, features, labels):
        nr_samples, nr_features = features.shape
        split_points = []  # add tuples using: split_points.append((f_idx, split_at))
        for f_idx in range(nr_features):
            # sort by feature feat
            idx_sort = features[:, f_idx].argsort()
            features = features[idx_sort, :]
            labels = labels[idx_sort]
            # TODO: check for consecutive samples whether the labels and features are different

            last_feature, last_label = features[0][f_idx], labels[0]  # Load row 0 for comparison outside of for
            for (feat, lbl) in zip(features[1:, f_idx], labels[1:]):  # Start with row 1 and compare to the last one
                if feat != last_feature and lbl != last_label:  # If feature and label doesn match split
                    split_points.append((f_idx, (last_feature + feat) / 2))  # Add split point

                last_feature = feat  # Assign feat to last_feature and lbl to last_label and do next iteration
                last_label = lbl

        return split_points

    def get_optimal_split_point(self, features, labels):
        split_feature, split_point = None, None
        possible_split_points = self.get_all_possible_split_points(features, labels)

        current_best_ig = -np.Inf

        # loop over all possible splitting points that you computed and return the best one
        for (f_idx, split_at) in possible_split_points:
            # TODO: compute information gain for splitting points and store the best one
            # For all possible features, calculate where the optimal split point (highest information gain) is
            ig = self.get_information_gain(features[:, f_idx], labels, split_at)
            if ig > current_best_ig:  # If yes, set the according values
                current_best_ig = ig
                split_feature = f_idx
                split_point = split_at

        return split_feature, split_point

    def get_information_gain(self, x, y, split_point):
        data_left = []  # Create arrays for the two data items on the left and right of the split point
        data_right = []

        for (ft, lbl) in zip(x, y):  # Iterate over all our values for that one feature
            if ft <= split_point:  # If the value is smaller equal to the split point, append to the left data array
                data_left.append(lbl)
            else:  # Otherwise to the right data array
                data_right.append(lbl)
        # Calculate information gain value with the weighted entropy values
        return entropy(y) - len(data_left)/len(y) * entropy(data_left) - len(data_right)/len(y) * entropy(data_right)

    def split(self, X, y):

        #  np.all checks if the values in 'y' are all equal (therefore the same as y[0])
        if not np.all(y == y[0]):  # Create the left and right arrays for the according values
            x_left = []
            y_left = []
            x_right = []
            y_right = []

            self.split_feature, self.split_point = self.get_optimal_split_point(X, y) # Calc split pont

            for (x, lbl) in zip(X, y):  # Iterate over all features and according labels
                if x[self.split_feature] <= self.split_point:  # Add to left if feature of row is <= than the split
                    x_left.append(x)
                    y_left.append(lbl)
                else:  # Add to the right if greater than the split value
                    x_right.append(x)
                    y_right.append(lbl)

            # Create nparrays when passing as an argument so that the shape field is available again
            self.left_child = DecisionTreeNode().split(np.array(x_left), np.array(y_left))  # Create left child
            self.right_child = DecisionTreeNode().split(np.array(x_right), np.array(y_right))  # Create right child
        else:
            self.label = y[0]  # If all labels are equal, write the label to the node

        return self  # Return the currently worked on node

    def __str__(self):
        if self.label is not None: return "(" + str(self.label) + ")"

        str_value = str(self.split_feature) + ":" + str(self.split_point) + "|"
        str_value = str_value + str(self.left_child) + str(self.right_child)
        return str_value
