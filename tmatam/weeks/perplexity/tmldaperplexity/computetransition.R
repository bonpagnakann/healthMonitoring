#!/usr/bin/env Rscript	library(tm)
library(tm)
library(gdata)
library("corpcor")
#data/perplexity/tmldaperplexity/train
trainfile = read.csv(file = "../../data/perplexity/tmldaperplexity/train", sep = ',',header =  FALSE)
testfile = read.csv(file = "../../data/perplexity/tmldaperplexity/test", sep = ',',header =  FALSE)
dmtrain = as.matrix(trainfile)
dmtest = as.matrix(testfile)
T = pseudoinverse(t(dmtrain) %*% dmtrain)%*%t(dmtrain)%*%dmtest
write.csv(T,file='../../data/perplexity/tmldaperplexity/transitionlda')
