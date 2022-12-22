ll1000_JAVA_ef = c(3.257,4.212,4.156,3.584,4.028,4.218,4.017,3.327,3.593,3.175,2.925,4.189,3.56,4.492,3.041,3.65,4.122,3.65,4.368,3.435,3.785,3.317,3.787,3.932,3.347,4.317,4.322,3.728,3.457,3.257)
lle1000_JAVA_ef = c(5.033,5.114,4.953,4.953,5.616,4.597,4.597,5.33,5.33,5.222,6.099,6.099,5.349,4.926,4.31,4.31,4.448,4.46,3.813,4.777,4.626,4.803,5.015,5.397,4.9,4.029,5.506,3.825,4.553,5.408)
par(mfrow=c(1,2))
barplot(ll1000_JAVA_ef, main=paste('localLogin-1000 avg:' ,mean(ll1000_JAVA_ef)))
barplot(lle1000_JAVA_ef, main=paste('localLoginEnc-1000 avg:' ,mean(lle1000_JAVA_ef)))
ll1000_JAVA_ef_Data = c(mean(ll1000_JAVA_ef),sd(ll1000_JAVA_ef))
lle1000_JAVA_ef_Data = c(mean(lle1000_JAVA_ef),sd(lle1000_JAVA_ef))
ll1000_JAVA_ef_Data
lle1000_JAVA_ef_Data
