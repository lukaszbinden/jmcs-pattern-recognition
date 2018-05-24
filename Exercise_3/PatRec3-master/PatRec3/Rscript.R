vec <- scan("numbers.txt") + 1;

x <- c(0);
y <- c(1);

for (i in sort(unique(vec)) )  {
	prec <- mean( (1/vec) [vec <= i] )	
	reca <- sum ( !! (vec <= i) ) / length(vec)
	x <- c(x, reca)
	y <- c(y, prec)
}

x <- c(x, 1)
y <- c(y, 0)

plot(x, y, xlim=c(0,1), ylim=c(0,1), type="l", col="darkred", lwd=2, xlab="Recall", ylab="Precision", main="Recall-Precision Curve")

