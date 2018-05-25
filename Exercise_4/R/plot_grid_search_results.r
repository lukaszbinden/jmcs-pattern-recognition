setwd("C:/Users/lukaszbinden/git/jmcs-pattern-recognition/Exercise_4")

data = read.csv("stats-214950.290.csv", header = TRUE)

result=lm(data$acc~data$k+data$c_n+data$c_e)
summary(result)

plot(data$acc ~ data$k, main="Accuracy explained by k", ylab="Accuracy %", xlab="k", col="blue", pch=20)

plot(data$acc ~ data$c_n, main="Accuracy explained by c_n", ylab="Accuracy %", xlab="c_n", col="blue", pch=20)

plot(data$acc ~ data$c_e, main="Accuracy explained by c_e", ylab="Accuracy %", xlab="c_e", col="blue", pch=20)

# plot(data$acc ~ data$k+data$c_n+data$c_e, main="Accuracy explained by k,c_n+c_e", ylab="Accuracy %", xlab="k+c_n+c_e", col="blue", pch=20)