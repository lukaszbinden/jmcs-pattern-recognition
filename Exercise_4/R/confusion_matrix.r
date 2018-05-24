library(caret)

setwd("C:/Users/lukaszbinden/git/jmcs-pattern-recognition/Exercise_4")

data = read.csv("predictions.csv", header = TRUE)

predictions = data[2-3]
result = table(predictions, dnn=c("Prediction", "Ground truth"))
result
confM = confusionMatrix(t, dnn=c("Prediction", "Ground truth"))
confM

# plot(confM)
fourfoldplot(confM$table)

